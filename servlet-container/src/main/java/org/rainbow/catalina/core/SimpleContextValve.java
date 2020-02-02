package org.rainbow.catalina.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rainbow.catalina.Context;
import org.rainbow.catalina.Valve;
import org.rainbow.catalina.ValveContext;
import org.rainbow.catalina.Wrapper;

public class SimpleContextValve extends ContainedBase implements Valve {

	@Override
	public void invoke(HttpServletRequest request, HttpServletResponse response, ValveContext valveContext)
			throws IOException, ServletException {
		// Disallow any direct access to resources under WEB-INF or META-INF

		String contextPath = request.getContextPath();
		String requestURI = request.getRequestURI();
		String relativeURI = requestURI.substring(contextPath.length()).toUpperCase();

		Context context = (Context) getContainer();
		// Select the Wrapper to be used for this Request
		Wrapper wrapper = null;
		try {
			wrapper = (Wrapper) context.map(request, true);
		} catch (IllegalArgumentException e) {
			badRequest(requestURI, response);
			return;
		}
		if (wrapper == null) {
			notFound(requestURI, response);
			return;
		}

		// Ask this Wrapper to process this Request
		wrapper.invoke(request, response);
	}

	public String getInfo() {
		return null;
	}

	private void badRequest(String requestURI, HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, requestURI);
	}

	private void notFound(String requestURI, HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND, requestURI);
	}
}
