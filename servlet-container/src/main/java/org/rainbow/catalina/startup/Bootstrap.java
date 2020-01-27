package org.rainbow.catalina.startup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.rainbow.catalina.Context;
import org.rainbow.catalina.Lifecycle;
import org.rainbow.catalina.LifecycleException;
import org.rainbow.catalina.Logger;
import org.rainbow.catalina.Mapper;
import org.rainbow.catalina.Pipeline;
import org.rainbow.catalina.Valve;
import org.rainbow.catalina.connector.http.Constants;
import org.rainbow.catalina.connector.http.HttpConnector;
import org.rainbow.catalina.core.ClientIPLoggerValve;
import org.rainbow.catalina.core.HeaderLoggerValve;
import org.rainbow.catalina.core.SimpleContext;
import org.rainbow.catalina.core.SimpleContextLifecycleListener;
import org.rainbow.catalina.core.SimpleContextMapper;
import org.rainbow.catalina.loaders.LibraryLoader;
import org.rainbow.catalina.logger.FileLogger;

public final class Bootstrap {
	private static FileLogger logger;

	static {
		try {
			// Create and initialize the logger
			System.setProperty("catalina.base", Constants.WORKING_DIR);
			logger = new FileLogger();
			logger.setTimestamp(true);

			new LibraryLoader().loadAll();
		} catch (Throwable e) {
			logger.log(e.getMessage(), e);
			System.exit(1);
		}
	}

	public static void main(String[] args) throws LifecycleException, IOException {
		List<Context> contexts = getContexts();

		for (Context context : contexts) {
			// Set the logger
			context.setLogger(logger);

			if (context instanceof Lifecycle) {
				((Lifecycle) context).start();
			}
		}

		HttpConnector connector = new HttpConnector(getPort(args, logger));

		final Context context = contexts.get(0);
		connector.setContainer(context);

		connector.start();

		// Make the application wait until we press a key.
		System.in.read();

		((Lifecycle) context).stop();
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

	private static List<Context> getContexts() throws LifecycleException {
		List<Context> contexts = new ArrayList<>();

		File webapps = new File(Constants.WEB_APPS);
		if (webapps.isDirectory()) {
			for (File webapp : webapps.listFiles()) {
				if (webapp.isDirectory()) {
					String contextName = webapp.getName();
					SimpleContext context = new SimpleContext();
					context.setName(contextName);
					context.addLifecycleListener(new SimpleContextLifecycleListener());

					Mapper mapper = new SimpleContextMapper();
					mapper.setProtocol("HTTP/1.1");
					context.addMapper(mapper);

					Valve valve1 = new HeaderLoggerValve();
					Valve valve2 = new ClientIPLoggerValve();

					((Pipeline) context).addValve(valve1);
					((Pipeline) context).addValve(valve2);

					contexts.add(context);
				}
			}
		}

		return contexts;
	}
}
