/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rainbow.catalina.Container;
import org.rainbow.catalina.Context;
import org.rainbow.catalina.Globals;
import org.rainbow.catalina.Host;
import org.rainbow.catalina.Lifecycle;
import org.rainbow.catalina.LifecycleException;
import org.rainbow.catalina.LifecycleListener;
import org.rainbow.catalina.Loader;
import org.rainbow.catalina.Logger;
import org.rainbow.catalina.Mapper;
import org.rainbow.catalina.Pipeline;
import org.rainbow.catalina.Valve;
import org.rainbow.catalina.util.LifecycleSupport;
import org.rainbow.catalina.util.StringManager;

/**
 * @author biya-bi
 *
 */
public class SimpleHost extends ContainerBase implements Host, Lifecycle {

	private Pipeline pipeline = new SimplePipeline(this);
	private StringManager sm = StringManager.getManager(Constants.PACKAGE);
	private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);
	private volatile boolean started;

	public SimpleHost() {
		pipeline.setBasic(new SimpleHostValve());
	}

	@Override
	public void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		pipeline.invoke(request, response);
	}

	@Override
	public Context map(String uri) {
		if (uri == null)
			return null;
		// Match on the longest possible context path prefix
		log(sm.getString("simpleHost.tryingLongestContextPath"));
		Context context = null;
		String mapUri = uri;
		while (true) {
			context = (Context) findChild(mapUri);
			if (context != null)
				break;
			int slash = mapUri.lastIndexOf('/');
			if (slash < 0)
				break;
			mapUri = mapUri.substring(0, slash);
		}
		// If no Context matches, select the default Context
		if (context == null) {
			log(sm.getString("simpleHost.tryingDefaultContext"));
			context = (Context) findChild("");
		}
		// Complain if no Context has been selected
		if (context == null) {
			log(sm.getString("simpleHost.mappingError", uri, getName()));
			return null;
		}
		// Return the mapped Context (if any)
		log(sm.getString("simpleHost.mappedToContext", context.getPath()));
		return context;
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
	public synchronized void start() throws LifecycleException {
		if (started)
			throw new LifecycleException(String.format(sm.getString("simpleHost.alreadyStarted"), getName()));
		try {
			started = true;

			// Notify our interested LifecycleListeners
			lifecycleSupport.fireLifecycleEvent(BEFORE_START_EVENT, null);

			// Notify our interested LifecycleListeners
			lifecycleSupport.fireLifecycleEvent(START_EVENT, null);

			Logger logger = getLogger();
			if (logger instanceof Lifecycle) {
				((Lifecycle) logger).start();
			}

			addContexts();

			// Start our subordinate components, if any
			Loader loader = getLoader();
			if ((loader != null) && (loader instanceof Lifecycle))
				((Lifecycle) loader).start();
			// Start our child containers, if any
			Container children[] = findChildren();
			for (int i = 0; i < children.length; i++) {
				if (children[i] instanceof Lifecycle)
					((Lifecycle) children[i]).start();
			}
			// Start the Valves in our pipeline (including the basic),
			// if any
			if (pipeline instanceof Lifecycle)
				((Lifecycle) pipeline).start();

			// Notify our interested LifecycleListeners
			lifecycleSupport.fireLifecycleEvent(AFTER_START_EVENT, null);
		} catch (Throwable t) {
			stop();
			throw t;
		}

	}

	@Override
	public synchronized void stop() throws LifecycleException {
		if (!started)
			throw new LifecycleException(sm.getString("simpleHost.notStarted", getName()));

		started = false;

		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(BEFORE_STOP_EVENT, null);
		lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);

		// Stop the Valves in our pipeline (including the basic), if any
		if (pipeline instanceof Lifecycle) {
			((Lifecycle) pipeline).stop();
		}

		// Stop our child containers, if any
		Container children[] = findChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof Lifecycle)
				((Lifecycle) children[i]).stop();
		}

		Loader loader = getLoader();
		if ((loader != null) && (loader instanceof Lifecycle)) {
			((Lifecycle) loader).stop();
		}

		Logger logger = getLogger();
		if (logger instanceof Lifecycle) {
			((Lifecycle) logger).stop();
		}

		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(AFTER_STOP_EVENT, null);
	}

	private void addContexts() {
		File webapps = new File(Globals.WEB_APPS);
		if (webapps.isDirectory()) {
			for (File webapp : webapps.listFiles()) {
				if (webapp.isDirectory()) {
					String contextName = webapp.getName();
					SimpleContext context = new SimpleContext();
					final String path = "/" + contextName;
					context.setName(path);
					context.setPath(path);
					context.addLifecycleListener(new SimpleContextLifecycleListener());

					Mapper mapper = new SimpleContextMapper();
					mapper.setProtocol("HTTP/1.1");
					context.addMapper(mapper);

					Valve valve1 = new HeaderLoggerValve();
					Valve valve2 = new ClientIPLoggerValve();

					((Pipeline) context).addValve(valve1);
					((Pipeline) context).addValve(valve2);

					context.setLogger(getLogger());

					addChild(context);
				}
			}
		}
	}

	@Override
	public void addChild(Container child) {
		if (!(child instanceof Context))
			throw new IllegalArgumentException(sm.getString("simpleHost.cannotAddChild"));
		super.addChild(child);
	}
}
