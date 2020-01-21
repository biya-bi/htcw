/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author biya-bi
 *
 */
public class ClientIPLoggerValve implements Valve, Contained {

	protected Container container;

	public void invoke(HttpServletRequest request, HttpServletResponse response, ValveContext valveContext)
			throws IOException, ServletException {

		// Pass this request on to the next valve in our pipeline
		valveContext.invokeNext(request, response);
		System.out.println("Client IP Logger Valve");
		System.out.println(request.getRemoteAddr());
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