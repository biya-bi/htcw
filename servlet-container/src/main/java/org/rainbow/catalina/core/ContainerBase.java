/**
 * 
 */
package org.rainbow.catalina.core;

import java.util.HashMap;
import java.util.Map;

import org.rainbow.catalina.Container;
import org.rainbow.catalina.Loader;
import org.rainbow.catalina.Logger;

/**
 * @author biya-bi
 *
 */
public abstract class ContainerBase implements Container {

	private Loader loader;
	private Container parent;
	private String name;
	private Logger logger;
	private Map<String, Container> children = new HashMap<>();

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

}
