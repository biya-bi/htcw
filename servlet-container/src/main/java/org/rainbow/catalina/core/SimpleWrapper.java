/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rainbow.catalina.Container;
import org.rainbow.catalina.Lifecycle;
import org.rainbow.catalina.LifecycleException;
import org.rainbow.catalina.LifecycleListener;
import org.rainbow.catalina.Loader;
import org.rainbow.catalina.Pipeline;
import org.rainbow.catalina.Valve;
import org.rainbow.catalina.Wrapper;
import org.rainbow.catalina.util.LifecycleSupport;
import org.rainbow.catalina.util.StringManager;

/**
 * @author biya-bi
 *
 */
public class SimpleWrapper extends ContainerBase implements Wrapper, Pipeline, Lifecycle {
	// The servlet instance
	private Servlet instance;
	private String servletClass;
	private Pipeline pipeline = new SimplePipeline(this);
	private Container container;
	private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);
	private volatile boolean started;
	private StringManager sm = StringManager.getManager(Constants.PACKAGE);

	public SimpleWrapper() {
		pipeline.setBasic(new SimpleWrapperValve());
	}

	@Override
	public Servlet allocate() throws ServletException {
		// Load and initialize our instance if necessary
		if (instance == null) {
			try {
				instance = loadServlet();
			} catch (ServletException e) {
				throw e;
			} catch (Throwable e) {
				throw new ServletException("Cannot allocate a servlet instance", e);
			}
		}
		return instance;
	}

	@Override
	public void deallocate(Servlet servlet) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void load() throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void unload() throws ServletException {
		// TODO Auto-generated method stub

	}

	public synchronized void addValve(Valve valve) {
		pipeline.addValve(valve);
	}

	private Servlet loadServlet() throws ServletException {
		if (instance != null)
			return instance;

		Servlet servlet = null;
		String actualClass = servletClass;
		if (actualClass == null || actualClass.trim().length() == 0) {
			throw new ServletException("Servlet class has not been specified");
		}

		actualClass = actualClass.trim();

		Loader loader = getLoader();
		// Acquire an instance of the class loader to be used
		if (loader == null) {
			throw new ServletException("No loader");
		}

		ClassLoader classLoader = loader.getClassLoader();

		// Load the specified servlet class from the appropriate class loader
		Class<?> classClass = null;
		try {
			if (classLoader != null) {
				classClass = classLoader.loadClass(actualClass);
			}
		} catch (ClassNotFoundException e) {
			throw new ServletException(String.format("Servlet class '%s' not found", actualClass));
		}
		// Instantiate and initialize an instance of the servlet class itself
		try {
			servlet = (Servlet) classClass.newInstance();
		} catch (Throwable e) {
			throw new ServletException(String.format("Failed to instantiate servlet '%s'", actualClass), e);
		}

		// Call the initialization method of this servlet
		try {
			servlet.init(null);
		} catch (Throwable t) {
			throw new ServletException(String.format("Failed to initialize servlet '%s'", actualClass), t);
		}

		return servlet;
	}

	@Override
	public Valve getBasic() {
		return pipeline.getBasic();
	}

	@Override
	public void setBasic(Valve valve) {
		pipeline.setBasic(valve);
	}

	@Override
	public Valve[] getValves() {
		return pipeline.getValves();
	}

	@Override
	public void removeValve(Valve valve) {
		pipeline.removeValve(valve);
	}

	@Override
	public Valve getFirst() {
		// TODO Auto-generated method stub
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

	@Override
	public void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		pipeline.invoke(request, response);
	}

	@Override
	public String getServletClass() {
		return servletClass;
	}

	@Override
	public void setServletClass(String servletClass) {
		this.servletClass = servletClass;
	}

	@Override
	public void addChild(Container child) {
		throw new IllegalStateException(sm.getString("simpleWrapper.cannotAddChild"));
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
			throw new LifecycleException(sm.getString("simpleWrapper.alreadyStarted"));

		log(String.format(sm.getString("simpleWrapper.starting"), getName()));

		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(BEFORE_START_EVENT, null);

		started = true;

		// Start our subordinate components, if any
		Loader loader = getLoader();
		if ((loader != null) && (loader instanceof Lifecycle))
			((Lifecycle) loader).start();

		// Start the Valves in our pipeline (including the basic), if any
		if (pipeline instanceof Lifecycle)
			((Lifecycle) pipeline).start();

		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(START_EVENT, null);
		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(AFTER_START_EVENT, null);

	}

	@Override
	public synchronized void stop() throws LifecycleException {
		if (!started)
			throw new LifecycleException(String.format(sm.getString("simpleWrapper.notStarted"), getName()));

		log(String.format(sm.getString("simpleWrapper.stopping"), getName()));

		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(BEFORE_STOP_EVENT, null);

		// Shut down our servlet instance (if it has been initialized)
		if (instance != null) {
			instance.destroy();
			instance = null;
		}

		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);

		started = false;

		// Stop the Valves in our pipeline (including the basic), if any
		if (pipeline instanceof Lifecycle) {
			((Lifecycle) pipeline).stop();
		}

		// Stop our subordinate components, if any
		Loader loader = getLoader();
		if ((loader != null) && (loader instanceof Lifecycle)) {
			((Lifecycle) loader).stop();
		}

		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(AFTER_STOP_EVENT, null);
	}

}
