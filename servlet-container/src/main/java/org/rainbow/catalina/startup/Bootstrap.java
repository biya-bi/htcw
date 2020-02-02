package org.rainbow.catalina.startup;

import java.io.IOException;

import org.rainbow.catalina.Globals;
import org.rainbow.catalina.LifecycleException;
import org.rainbow.catalina.Logger;
import org.rainbow.catalina.connector.http.HttpConnector;
import org.rainbow.catalina.core.SimpleEngine;
import org.rainbow.catalina.core.SimpleHost;
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

		SimpleHost host = new SimpleHost();
		host.setName(defaultHost);
		host.setLogger(logger);

		SimpleEngine engine = new SimpleEngine();
		engine.setName("Simple engine");
		engine.setDefaultHost(defaultHost);
		engine.addChild(host);

		HttpConnector connector = new HttpConnector(getPort(args, logger));
		connector.setContainer(engine);

		engine.start();
		connector.start();

		// Make the application wait until we press a key.
		System.in.read();

		engine.stop();
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
