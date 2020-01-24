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
import org.rainbow.catalina.Loader;
import org.rainbow.catalina.Pipeline;
import org.rainbow.catalina.Valve;
import org.rainbow.catalina.Wrapper;

/**
 * @author biya-bi
 *
 */
public class SimpleWrapper implements Wrapper, Pipeline {

	// The servlet instance
	private Servlet instance;
	private String servletClass;
	private Loader loader;
	private String name;
	private Pipeline pipeline = new SimplePipeline(this);
	protected Container parent;
	private Container container;

	public SimpleWrapper() {
		pipeline.setBasic(new SimpleWrapperValve());
	}

	@Override
	public Container getParent() {
		return parent;
	}

	@Override
	public void setParent(Container parent) {
		this.parent = parent;

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
	public Loader getLoader() {
		if (loader != null)
			return loader;
		if (parent != null)
			return parent.getLoader();
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
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
	public void setLoader(Loader loader) {
		this.loader = loader;
	}

	@Override
	public String getServletClass() {
		return servletClass;
	}

	@Override
	public void setServletClass(String servletClass) {
		this.servletClass = servletClass;
	}

}
