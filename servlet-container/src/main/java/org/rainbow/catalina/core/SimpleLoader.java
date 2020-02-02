/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import org.rainbow.catalina.Container;
import org.rainbow.catalina.Globals;
import org.rainbow.catalina.Lifecycle;
import org.rainbow.catalina.LifecycleException;
import org.rainbow.catalina.LifecycleListener;
import org.rainbow.catalina.Loader;
import org.rainbow.catalina.Logger;
import org.rainbow.catalina.util.LifecycleSupport;

/**
 * @author biya-bi
 *
 */
public class SimpleLoader implements Loader, Lifecycle {
	private URLClassLoader classLoader;
	private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);
	private Container container;

	public SimpleLoader() {
	}

	@Override
	public ClassLoader getClassLoader() {
		if (classLoader == null) {
			try {
				URL[] urls = new URL[1];
				URLStreamHandler streamHandler = null;
				File classPath = new File(getPath());
				String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
				urls[0] = new URL(null, repository, streamHandler);
				classLoader = new URLClassLoader(urls);
			} catch (IOException e) {
				Logger logger = container.getLogger();
				if (logger != null)
					logger.log("An I/O error has occurred", e);
			}
		}
		return classLoader;
	}

	protected String getPath() {
		return Globals.WEB_APPS;
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
		log("Starting simple loader");
	}

	@Override
	public synchronized void stop() throws LifecycleException {
		log("Stopping simple loader");
	}

	@Override
	public Container getContainer() {
		return container;
	}

	@Override
	public void setContainer(Container container) {
		this.container = container;
	}

	private void log(String message) {
		Logger logger = container.getLogger();

		if (logger != null)
			logger.log(message);
	}
}
