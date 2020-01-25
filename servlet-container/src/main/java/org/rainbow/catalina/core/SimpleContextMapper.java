package org.rainbow.catalina.core;

import javax.servlet.http.HttpServletRequest;

import org.rainbow.catalina.Container;
import org.rainbow.catalina.Mapper;
import org.rainbow.catalina.Wrapper;

public class SimpleContextMapper implements Mapper {

	/**
	 * The Container with which this Mapper is associated.
	 */
	private SimpleContext context = null;

	public Container getContainer() {
		return (context);
	}

	public void setContainer(Container container) {
		if (!(container instanceof SimpleContext))
			throw new IllegalArgumentException("Illegal type of container");
		context = (SimpleContext) container;
	}

	public String getProtocol() {
		return null;
	}

	public void setProtocol(String protocol) {
	}

	/**
	 * Return the child Container that should be used to process this Request, based
	 * upon its characteristics. If no such child Container can be identified,
	 * return <code>null</code> instead.
	 *
	 * @param request Request being processed
	 * @param update  Update the Request to reflect the mapping selection?
	 *
	 * @exception IllegalArgumentException if the relative portion of the path
	 *                                     cannot be URL decoded
	 */
	public Container map(HttpServletRequest request, boolean update) {
		// Identify the context-relative URI to be mapped
		String contextPath = request.getContextPath();
		String requestURI = request.getRequestURI();
		String relativeURI = requestURI.substring(contextPath.length());
		// Apply the standard request URI mapping rules from the specification
		Wrapper wrapper = null;
		String name = context.findServletMapping(relativeURI);
		if (name != null)
			wrapper = (Wrapper) context.findChild(name);
		return wrapper;
	}
}