/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rainbow.catalina.Contained;
import org.rainbow.catalina.Container;
import org.rainbow.catalina.Valve;
import org.rainbow.catalina.ValveContext;
import org.rainbow.catalina.Wrapper;
import org.rainbow.catalina.processors.ServletProcessor;

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

		// Allocate a servlet instance to process this request
		Servlet servlet = wrapper.allocate();
		ServletProcessor processor = new ServletProcessor();
		processor.process(servlet, request, response);
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
