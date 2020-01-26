/**
 * 
 */
package org.rainbow.catalina.core;

import org.rainbow.catalina.connector.http.Constants;

/**
 * @author biya-bi
 *
 */
public class SimpleContextLoader extends SimpleLoader {
	private String contextPath;

	public SimpleContextLoader(String contextPath) {
		this.contextPath = contextPath;
	}

	@Override
	protected String getPath() {
		return Constants.WEB_APPS + contextPath;
	}
}
