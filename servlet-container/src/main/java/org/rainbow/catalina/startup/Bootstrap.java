package org.rainbow.catalina.startup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.rainbow.catalina.Context;
import org.rainbow.catalina.Lifecycle;
import org.rainbow.catalina.LifecycleException;
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

	public static void main(String[] args) throws LifecycleException, IOException {
		List<Context> contexts = getContexts();

		for (Context context : contexts) {
			if (context instanceof Lifecycle) {
				((Lifecycle) context).start();
			}
		}

		HttpConnector connector = new HttpConnector(getPort(args));

		final Context context = contexts.get(0);
		connector.setContainer(context);

		connector.start();

		// Make the application wait until we press a key.
		System.in.read();

		((Lifecycle) context).stop();
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
