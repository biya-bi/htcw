/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import org.rainbow.catalina.Lifecycle;
import org.rainbow.catalina.LifecycleException;
import org.rainbow.catalina.LifecycleListener;
import org.rainbow.catalina.Loader;
import org.rainbow.catalina.connector.http.Constants;
import org.rainbow.catalina.util.LifecycleSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author biya-bi
 *
 */
public class SimpleLoader implements Loader, Lifecycle {
	private static final Logger logger = LoggerFactory.getLogger(SimpleLoader.class);

	private URLClassLoader classLoader;
	private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);

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
				logger.error("An I/O error has occurred", e);
			}
		}
		return classLoader;
	}

	protected String getPath() {
		return Constants.WEB_APPS;
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
		logger.info("Starting simple loader");
	}

	@Override
	public void stop() throws LifecycleException {
		logger.info("Stopping simple loader");
	}
}
