package org.rainbow.catalina.startup;

import java.io.IOException;

import org.rainbow.catalina.Engine;
import org.rainbow.catalina.Globals;
import org.rainbow.catalina.Host;
import org.rainbow.catalina.Lifecycle;
import org.rainbow.catalina.LifecycleException;
import org.rainbow.catalina.Logger;
import org.rainbow.catalina.Server;
import org.rainbow.catalina.Service;
import org.rainbow.catalina.connector.http.HttpConnector;
import org.rainbow.catalina.core.SimpleEngine;
import org.rainbow.catalina.core.SimpleHost;
import org.rainbow.catalina.core.SimpleServer;
import org.rainbow.catalina.core.SimpleService;
import org.rainbow.catalina.loaders.LibraryLoader;
import org.rainbow.catalina.logger.FileLogger;

public final class Bootstrap {
	private static FileLogger logger;

	static {
		try {
			// Create and initialize the logger
			System.setProperty("catalina.base", Globals.WORKING_DIR);
			logger = new FileLogger();
			logger.setTimestamp(true);

			new LibraryLoader().loadAll();
		} catch (Throwable e) {
			logger.log(e.getMessage(), e);
			System.exit(1);
		}
	}

	public static void main(String[] args) throws LifecycleException, IOException {
		String defaultHost = "localhost";

		Host host = new SimpleHost();
		host.setName(defaultHost);
		host.setLogger(logger);

		Engine engine = new SimpleEngine();
		engine.setName("Catalina");
		engine.setDefaultHost(defaultHost);
		engine.addChild(host);

		HttpConnector connector = new HttpConnector(getPort(args, logger));

		Service service = new SimpleService();
		service.setContainer(engine);
		service.addConnector(connector);

		Server server = new SimpleServer();
		server.addService(service);
		server.setPort(8005);
		server.setShutdown("SHUTDOWN");

		server.initialize();
		((Lifecycle) server).start();
		server.await();
	}

	private static int getPort(String[] args, Logger logger) {
		if (args.length >= 2) {
			for (int i = 0; i < args.length; i++) {
				if ("--port".equals(args[i])) {
					String port = args[i + 1];
					try {
						return Integer.valueOf(port);
					} catch (NumberFormatException e) {
						logger.log(String.format("'%s' is not a invalid port number.", port), e);
						System.exit(1);
					}
				}
			}
		}
		return 8080;
	}
}
