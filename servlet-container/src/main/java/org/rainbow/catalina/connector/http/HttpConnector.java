package org.rainbow.catalina.connector.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.rainbow.catalina.Connector;
import org.rainbow.catalina.Container;
import org.rainbow.catalina.Lifecycle;
import org.rainbow.catalina.LifecycleException;
import org.rainbow.catalina.LifecycleListener;
import org.rainbow.catalina.Logger;
import org.rainbow.catalina.Service;
import org.rainbow.catalina.util.LifecycleSupport;
import org.rainbow.catalina.util.StringManager;

public class HttpConnector implements Runnable, Connector, Lifecycle {
	private boolean stopped;
	private final String scheme = "http";
	private int port;

	private Stack<HttpProcessor> processors = new Stack<>();

	private int minProcessors = 5;
	private int maxProcessors = 20;
	private int curProcessors;

	private StringManager sm = StringManager.getManager(Constants.PACKAGE);

	private Container container;

	private ExecutorService executor = Executors.newCachedThreadPool();
	private Service service;
	private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);
	private ServerSocket serverSocket;

	public HttpConnector(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		createProcessors();

		try {
			serverSocket = new ServerSocket(port, maxProcessors, InetAddress.getByName("127.0.0.1"));
		} catch (IOException e) {
			log(sm.getString("ioError"), e);
			System.exit(1);
		}

		while (!stopped) {
			final Socket socket;

			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				log(sm.getString("ioError"), e);
				continue;
			}

			HttpProcessor processor = createProcessor();

			if (processor == null) {
				log(sm.getString("httpConnector.noProcessor"));
				try {
					socket.close();
				} catch (IOException e) {
					log(sm.getString("ioError"), e);
				}
			} else {
				executor.execute(() -> {
					processor.start();
					processor.assign(socket);
				});
			}
		}
	}

	public String getScheme() {
		return scheme;
	}

	private void createProcessors() {
		while (curProcessors < minProcessors) {
			if ((maxProcessors > 0) && (curProcessors >= maxProcessors))
				break;
			HttpProcessor processor = newProcessor();
			recycle(processor);
		}
	}

	private synchronized HttpProcessor newProcessor() {
		HttpProcessor processor = new HttpProcessor(this);
		curProcessors++;
		return processor;
	}

	synchronized void recycle(HttpProcessor processor) {
		processors.push(processor);
	}

	private synchronized HttpProcessor createProcessor() {
		if (!processors.isEmpty()) {
			return processors.pop();
		}
		if (maxProcessors < 0 || curProcessors < maxProcessors) {
			return newProcessor();
		}
		return null;
	}

	@Override
	public Container getContainer() {
		return container;
	}

	@Override
	public void setContainer(Container container) {
		this.container = container;
	}

	private void log(String message) {
		log(message, null);
	}

	private void log(String message, Throwable t) {
		Logger logger = container.getLogger();

		if (logger != null)
			logger.log(message, t);
	}

	@Override
	public Service getService() {
		return service;
	}

	@Override
	public void setService(Service service) {
		this.service = service;
	}

	@Override
	public void initialize() throws LifecycleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		lifecycleSupport.addLifecycleListener(listener);
	}

	@Override
	public LifecycleListener[] findLifecycleListeners() {
		return lifecycleSupport.findLifecycleListeners();
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		lifecycleSupport.removeLifecycleListener(listener);
	}

	@Override
	public void start() throws LifecycleException {
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void stop() throws LifecycleException {
		stopped = true;

		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				log(sm.getString("ioError"), e);
				System.exit(1);
			}
		}
	}
}
