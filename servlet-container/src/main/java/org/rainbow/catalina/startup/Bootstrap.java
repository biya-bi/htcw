package org.rainbow.catalina.startup;

import org.rainbow.catalina.connector.http.HttpConnector;
import org.rainbow.catalina.loaders.LibraryLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Bootstrap {
	static {
		try {
			new LibraryLoader().loadAll();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	public static void main(String[] args) {
		HttpConnector connector = new HttpConnector(getPort(args));
		connector.start();
	}

	private static int getPort(String[] args) {
		if (args.length >= 2) {
			for (int i = 0; i < args.length; i++) {
				if ("--port".equals(args[i])) {
					String port = args[i + 1];
					try {
						return Integer.valueOf(port);
					} catch (NumberFormatException e) {
						logger.error(String.format("'%s' is not a invalid port number.", port), e);
						System.exit(1);
					}
				}
			}
		}
		return 8080;
	}
}
