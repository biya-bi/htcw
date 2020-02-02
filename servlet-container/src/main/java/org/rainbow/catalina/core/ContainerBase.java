/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rainbow.catalina.Container;
import org.rainbow.catalina.Lifecycle;
import org.rainbow.catalina.LifecycleException;
import org.rainbow.catalina.LifecycleListener;
import org.rainbow.catalina.Loader;
import org.rainbow.catalina.Logger;
import org.rainbow.catalina.Pipeline;
import org.rainbow.catalina.Valve;
import org.rainbow.catalina.util.LifecycleSupport;
import org.rainbow.catalina.util.StringManager;

/**
 * @author biya-bi
 *
 */
public abstract class ContainerBase implements Container, Lifecycle, Pipeline {

	private Loader loader;
	private Container parent;
	private String name;
	private Logger logger;
	private Map<String, Container> children = new HashMap<>();
	protected volatile boolean started;
	
	protected LifecycleSupport lifecycleSupport = new LifecycleSupport(this);
	protected Pipeline pipeline = new SimplePipeline(this);
	protected static StringManager sm = StringManager.getManager(Constants.PACKAGE);
	
	@Override
	public Loader getLoader() {
		if (loader != null)
			return loader;
		if (parent != null)
			return parent.getLoader();
		return null;
	}

	@Override
	public void setLoader(Loader loader) {
		if (loader.getContainer() != this)
			loader.setContainer(this);
		this.loader = loader;
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
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void addChild(Container child) {
		if (child.getParent() != this)
			child.setParent((Container) this);
		synchronized (children) {
			children.put(child.getName(), child);
		}
	}

	@Override
	public Container findChild(String name) {
		if (name == null)
			return null;
		synchronized (children) {
			return (Container) children.get(name);
		}
	}

	@Override
	public Container[] findChildren() {
		synchronized (children) {
			Container[] results = new Container[children.size()];
			return (Container[]) children.values().toArray(results);
		}
	}

	@Override
	public Logger getLogger() {
		if (logger != null)
			return logger;
		if (parent != null)
			return parent.getLogger();
		return null;
	}

	@Override
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	protected void log(String message) {
		Logger logger = getLogger();
		if (logger != null) {
			logger.log(message);
		}
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
	public void addValve(Valve valve) {
		pipeline.addValve(valve);
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
	public void start() throws LifecycleException {
	}

	@Override
	public void stop() throws LifecycleException {
	}

	@Override
	public void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		pipeline.invoke(request, response);
	}

}
