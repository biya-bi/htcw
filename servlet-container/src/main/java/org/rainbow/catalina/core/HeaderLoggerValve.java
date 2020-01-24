/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rainbow.catalina.Contained;
import org.rainbow.catalina.Container;
import org.rainbow.catalina.Valve;
import org.rainbow.catalina.ValveContext;

/**
 * @author biya-bi
 *
 */
public class HeaderLoggerValve implements Valve, Contained {

	protected Container container;

	public void invoke(HttpServletRequest request, HttpServletResponse response, ValveContext valveContext)
			throws IOException, ServletException {

		// Pass this request on to the next valve in our pipeline
		valveContext.invokeNext(request, response);

		System.out.println("Header Logger Valve");

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement().toString();
			String headerValue = request.getHeader(headerName);
			System.out.println(headerName + ":" + headerValue);
		}

		System.out.println("------------------------------------");
	}

	public String getInfo() {
		return null;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}
}