/**
 * 
 */
package org.rainbow.catalina.core;

import org.rainbow.catalina.Globals;

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
		return Globals.WEB_APPS + contextPath;
	}
}
