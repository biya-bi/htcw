package org.rainbow.catalina.connector.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.rainbow.catalina.core.Container;
import org.rainbow.catalina.util.StringManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConnector implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(HttpConnector.class);

	private boolean stopped;
	private final String scheme = "http";
	private int port;

	private Stack<HttpProcessor> processors = new Stack<>();

	protected int minProcessors = 5;
	private int maxProcessors = 20;
	private int curProcessors;

	private StringManager sm = StringManager.getManager(Constants.PACKAGE);

	private Container container;

	private ExecutorService executor = Executors.newCachedThreadPool();

	public HttpConnector(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		createProcessors();

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
		} catch (IOException e) {
			logger.error(sm.getString("ioError"), e);
			System.exit(1);
		}

		while (!stopped) {
			final Socket socket;

			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				logger.error(sm.getString("ioError"), e);
				continue;
			}

			HttpProcessor processor = createProcessor();

			if (processor == null) {
				logger.error(sm.getString("httpConnector.noProcessor"));
				try {
					socket.close();
				} catch (IOException e) {
					logger.error(sm.getString("ioError"), e);
				}
			} else {
				executor.execute(() -> {
					processor.start();
					processor.assign(socket);
				});
			}
		}
	}

	public void start() {
		logger.info("Starting the server.");
		Thread thread = new Thread(this);
		thread.start();
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

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

}
