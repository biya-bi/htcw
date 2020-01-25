package org.rainbow.catalina.startup;

import org.rainbow.catalina.Context;
import org.rainbow.catalina.Loader;
import org.rainbow.catalina.Mapper;
import org.rainbow.catalina.Pipeline;
import org.rainbow.catalina.Valve;
import org.rainbow.catalina.Wrapper;
import org.rainbow.catalina.connector.http.HttpConnector;
import org.rainbow.catalina.core.ClientIPLoggerValve;
import org.rainbow.catalina.core.HeaderLoggerValve;
import org.rainbow.catalina.core.SimpleContext;
import org.rainbow.catalina.core.SimpleContextMapper;
import org.rainbow.catalina.core.SimpleLoader;
import org.rainbow.catalina.core.SimpleWrapper;
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

		Wrapper wrapper1 = new SimpleWrapper();
		wrapper1.setName("Primitive");
		wrapper1.setServletClass("org.rainbow.learning.htcw.servlets.PrimitiveServlet");

		Context context = new SimpleContext();
		context.addChild(wrapper1);

		Valve valve1 = new HeaderLoggerValve();
		Valve valve2 = new ClientIPLoggerValve();

		Mapper mapper = new SimpleContextMapper();
		mapper.setProtocol("HTTP/1.1");
		context.addMapper(mapper);

		((Pipeline) context).addValve(valve1);
		((Pipeline) context).addValve(valve2);

		Loader loader = new SimpleLoader();

		context.setLoader(loader);

		context.addServletMapping("/Primitive", "Primitive");

		connector.setContainer(context);

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
