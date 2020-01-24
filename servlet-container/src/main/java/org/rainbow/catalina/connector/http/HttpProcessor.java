package org.rainbow.catalina.connector.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.rainbow.catalina.util.RequestUtil;
import org.rainbow.catalina.util.StringManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpProcessor implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(HttpProcessor.class);

	/**
	 * The HttpConnector with which this processor is associated.
	 */
	private HttpConnector connector = null;
	private HttpRequest request;
	private HttpRequestLine requestLine = new HttpRequestLine();
	private HttpResponse response;

	protected String method = null;
	protected String queryString = null;

	/**
	 * The string manager for this package.
	 */
	protected StringManager sm = StringManager.getManager(Constants.PACKAGE);

	private boolean stopped;

	private boolean available;

	private Socket socket;

	// We use a single thread executor for two reasons:
	// 1. We want only one thread to handle all HTTP requests sent to
	// this processor. Creating a new thread for each HTTP request
	// will be a real waste of resources.
	// 2. We want the HTTP requests to be processed in order, as they come.
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	// This variable indicates whether this processor's executor has been
	// started. In other words, if the execute method has been called on
	// the executor object.
	private volatile boolean running;

	private final Lock lock = new ReentrantLock();

	public HttpProcessor(HttpConnector connector) {
		this.connector = connector;
	}

	private void process(Socket socket) throws IOException, ServletException {
		SocketInputStream inputStream = null;
		OutputStream outputStream = null;

		try {
			inputStream = new SocketInputStream(socket.getInputStream(), 2048);
			outputStream = socket.getOutputStream();

			// Create the HttpRequest object.
			request = new HttpRequest(inputStream);

			// Create the HttpResponse object.
			response = new HttpResponse(outputStream);
			response.setRequest(request);

			response.setHeader("Server", "Rainbow Servlet Container");

			parseRequest(inputStream, outputStream);
			parseHeaders(inputStream);

			this.connector.getContainer().invoke(request, response);
		} finally {
			// Close the socket.
			socket.close();
		}
	}

	/**
	 * This method is the simplified version of the similar method in
	 * org.apache.catalina.connector.http.HttpProcessor. However, this method only
	 * parses some "easy" headers, such as "cookie", "content-length", and
	 * "content-type", and ignore other headers.
	 *
	 * @param input The input stream connected to our socket
	 * @throws IOException      if an input/output error occurs
	 * @throws ServletException if a parsing error occurs
	 */
	private void parseHeaders(SocketInputStream input) throws IOException, ServletException {
		while (true) {
			HttpHeader header = new HttpHeader();

			// Read the next header
			input.readHeader(header);
			if (header.nameEnd == 0) {
				if (header.valueEnd == 0) {
					return;
				} else {
					throw new ServletException(sm.getString("httpProcessor.parseHeaders.colon"));
				}
			}

			String name = new String(header.name, 0, header.nameEnd);
			String value = new String(header.value, 0, header.valueEnd);
			request.addHeader(name, value);
			// do something for some headers, ignore others.
			if (name.equals("cookie")) {
				Cookie cookies[] = RequestUtil.parseCookieHeader(value);
				for (int i = 0; i < cookies.length; i++) {
					if (cookies[i].getName().equals("jsessionid")) {
						// Override anything requested in the URL
						if (!request.isRequestedSessionIdFromCookie()) {
							// Accept only the first session id cookie
							request.setRequestedSessionId(cookies[i].getValue());
							request.setRequestedSessionCookie(true);
							request.setRequestedSessionURL(false);
						}
					}
					request.addCookie(cookies[i]);
				}
			} else if (name.equals("content-length")) {
				int n = -1;
				try {
					n = Integer.parseInt(value);
				} catch (Exception e) {
					throw new ServletException(sm.getString("httpProcessor.parseHeaders.contentLength"));
				}
				request.setContentLength(n);
			} else if (name.equals("content-type")) {
				request.setContentType(value);
			}
		} // end while
	}

	private void parseRequest(SocketInputStream input, OutputStream output) throws IOException, ServletException {

		// Parse the incoming request line
		input.readRequestLine(requestLine);
		String method = new String(requestLine.method, 0, requestLine.methodEnd);
		String uri = null;
		String protocol = new String(requestLine.protocol, 0, requestLine.protocolEnd);

		// Validate the incoming request line
		if (method.length() < 1) {
			throw new ServletException("Missing HTTP request method");
		} else if (requestLine.uriEnd < 1) {
			throw new ServletException("Missing HTTP request URI");
		}
		// Parse any query parameters out of the request URI
		int question = requestLine.indexOf("?");
		if (question >= 0) {
			request.setQueryString(new String(requestLine.uri, question + 1, requestLine.uriEnd - question - 1));
			uri = new String(requestLine.uri, 0, question);
		} else {
			request.setQueryString(null);
			uri = new String(requestLine.uri, 0, requestLine.uriEnd);
		}

		// Checking for an absolute URI (with the HTTP protocol)
		if (!uri.startsWith("/")) {
			int pos = uri.indexOf("://");
			// Parsing out protocol and host name
			if (pos != -1) {
				pos = uri.indexOf('/', pos + 3);
				if (pos == -1) {
					uri = "";
				} else {
					uri = uri.substring(pos);
				}
			}
		}

		// Parse any requested session ID out of the request URI
		String match = ";jsessionid=";
		int semicolon = uri.indexOf(match);
		if (semicolon >= 0) {
			String rest = uri.substring(semicolon + match.length());
			int semicolon2 = rest.indexOf(';');
			if (semicolon2 >= 0) {
				request.setRequestedSessionId(rest.substring(0, semicolon2));
				rest = rest.substring(semicolon2);
			} else {
				request.setRequestedSessionId(rest);
				rest = "";
			}
			request.setRequestedSessionURL(true);
			uri = uri.substring(0, semicolon) + rest;
		} else {
			request.setRequestedSessionId(null);
			request.setRequestedSessionURL(false);
		}

		// Normalize URI (using String operations at the moment)
		String normalizedUri = normalize(uri);

		// Set the corresponding request properties
		((HttpRequest) request).setMethod(method);
		request.setProtocol(protocol);
		if (normalizedUri != null) {
			((HttpRequest) request).setRequestURI(normalizedUri);
		} else {
			((HttpRequest) request).setRequestURI(uri);
		}

		if (normalizedUri == null) {
			throw new ServletException("Invalid URI: " + uri + "'");
		}
	}

	/**
	 * Return a context-relative path, beginning with a "/", that represents the
	 * canonical version of the specified path after ".." and "." elements are
	 * resolved out. If the specified path attempts to go outside the boundaries of
	 * the current context (i.e. too many ".." path elements are present), return
	 * <code>null</code> instead.
	 *
	 * @param path Path to be normalized
	 */
	protected String normalize(String path) {
		if (path == null)
			return null;
		// Create a place for the normalized path
		String normalized = path;

		// Normalize "/%7E" and "/%7e" at the beginning to "/~"
		if (normalized.startsWith("/%7E") || normalized.startsWith("/%7e"))
			normalized = "/~" + normalized.substring(4);

		// Prevent encoding '%', '/', '.' and '\', which are special reserved
		// characters
		if ((normalized.indexOf("%25") >= 0) || (normalized.indexOf("%2F") >= 0) || (normalized.indexOf("%2E") >= 0)
				|| (normalized.indexOf("%5C") >= 0) || (normalized.indexOf("%2f") >= 0)
				|| (normalized.indexOf("%2e") >= 0) || (normalized.indexOf("%5c") >= 0)) {
			return null;
		}

		if (normalized.equals("/."))
			return "/";

		// Normalize the slashes and add leading slash if necessary
		if (normalized.indexOf('\\') >= 0)
			normalized = normalized.replace('\\', '/');
		if (!normalized.startsWith("/"))
			normalized = "/" + normalized;

		// Resolve occurrences of "//" in the normalized path
		while (true) {
			int index = normalized.indexOf("//");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index) + normalized.substring(index + 1);
		}

		// Resolve occurrences of "/./" in the normalized path
		while (true) {
			int index = normalized.indexOf("/./");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index) + normalized.substring(index + 2);
		}

		// Resolve occurrences of "/../" in the normalized path
		while (true) {
			int index = normalized.indexOf("/../");
			if (index < 0)
				break;
			if (index == 0)
				return (null); // Trying to go outside our context
			int index2 = normalized.lastIndexOf('/', index - 1);
			normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
		}

		// Declare occurrences of "/..." (three or more dots) to be invalid
		// (on some Windows platforms this walks the directory tree!!!)
		if (normalized.indexOf("/...") >= 0)
			return (null);

		// Return the normalized path that we have completed
		return (normalized);

	}

	synchronized void assign(Socket socket) {
		// Wait for the processor to get the previous socket
		while (available) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		// Store the newly available Socket and notify our thread
		this.socket = socket;
		available = true;
		notifyAll();
	}

	private synchronized Socket await() {
		// Wait for the Connector to provide a new Socket
		while (!available) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		// Notify the Connector that we have received this Socket
		Socket socket = this.socket;
		available = false;
		notifyAll();
		if (socket != null) {
			logger.debug("The incoming request has been awaited");
		}
		return socket;
	}

	@Override
	public void run() {
		// Process requests until we receive a shutdown signal
		while (!stopped) {
			// We wrap the whole content of this loop inside a
			// try-catch block because we don't want any throwable
			// to interrupt the run method. We want the await to
			// always be waiting for requests.
			try {
				// Wait for the next socket to be assigned
				Socket socket = await();
				if (socket == null)
					continue;
				// Process the request from this socket
				process(socket);
				// Finish up this request
				connector.recycle(this);
			} catch (Throwable t) {
				logger.error(sm.getString("unexpectedError"), t);
			}
		}
	}

	public void start() {
		if (running) {
			// If the executor is already running, we return.
			return;
		}
		// If for any reason, the start method is called from two different
		// threads, A and B, on the current processor, and the executor was
		// not yet running, the below could happen:
		// - Thread A sleeps before acquiring the lock.
		// - Thread B acquires the lock, sets the volatile variable to true,
		// starts the executor, releases the lock and exits the methods.
		// - Thread A wakes up, acquires the lock, sees that the running
		// variable is already true (set by Thread B), and releases the lock
		// immediately. Without the second check on the running variable,
		// Thread A would have called the execute method of the executor, which
		// is not what we want because that method was already called by Thread
		// B.
		if (lock.tryLock()) {
			try {
				if (!running) {
					running = true;
					executor.execute(this);
				}
			} finally {
				lock.unlock();
			}
		}
	}
}
