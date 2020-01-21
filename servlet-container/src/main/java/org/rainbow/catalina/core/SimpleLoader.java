/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import org.rainbow.catalina.connector.http.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author biya-bi
 *
 */
public class SimpleLoader implements Loader {
	private static final Logger logger = LoggerFactory.getLogger(SimpleLoader.class);

	private URLClassLoader classLoader;

	public SimpleLoader() {
		try {
			URL[] urls = new URL[1];
			URLStreamHandler streamHandler = null;
			File classPath = new File(Constants.WEB_ROOT);
			String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
			urls[0] = new URL(null, repository, streamHandler);
			classLoader = new URLClassLoader(urls);
		} catch (IOException e) {
			logger.error("An I/O error has occurred", e);
		}
	}

	@Override
	public ClassLoader getClassLoader() {
		return classLoader;
	}

}
