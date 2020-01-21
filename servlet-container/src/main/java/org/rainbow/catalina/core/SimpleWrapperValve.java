/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rainbow.catalina.connector.http.HttpRequest;
import org.rainbow.catalina.connector.http.HttpResponse;
import org.rainbow.catalina.processors.ServletProcessor;
import org.rainbow.catalina.processors.StaticResourceProcessor;

/**
 * @author biya-bi
 *
 */
public class SimpleWrapperValve implements Valve, Contained {

	private Container container;

	@Override
	public void invoke(HttpServletRequest request, HttpServletResponse response, ValveContext context)
			throws IOException, ServletException {
		final Wrapper wrapper = (Wrapper) getContainer();

		final String uri = request.getRequestURI();
		
		if (uri.startsWith("/servlet/")) {
			final String servletName = getServletName(uri);
			
			wrapper.setServletClass(servletName);

			// Allocate a servlet instance to process this request
			Servlet servlet = wrapper.allocate();
			ServletProcessor processor = new ServletProcessor();
			processor.process(servlet, request, response);
		} else {
			StaticResourceProcessor processor = new StaticResourceProcessor();
			processor.process((HttpRequest) request, (HttpResponse) response);
		}
	}

	private String getServletName(String uri) {
		// The assumption here is that the servlet name is everything after the
		// /servlet/ prefix.
		String prefix = "/servlet/";
		int index = uri.indexOf(prefix);
		String name = uri.substring(index + prefix.length());
		String slash = "/";
		while (name.lastIndexOf(slash) == name.length() - 1) {
			name = name.substring(0, name.length() - 1);
		}
		return name.replace(slash, ".");
	}

	@Override
	public Container getContainer() {
		return container;
	}

	@Override
	public void setContainer(Container container) {
		this.container = container;
	}
}
