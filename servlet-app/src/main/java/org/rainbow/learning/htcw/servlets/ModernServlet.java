package org.rainbow.learning.htcw.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ModernServlet implements Servlet {
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		System.out.println("Running the init method");
	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse)
			throws ServletException, IOException {
		PrintWriter writer = servletResponse.getWriter();

		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

		List<String> headers = new ArrayList<>();

		int contentLength = 0;

		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement().toString();
			String headerValue = httpServletRequest.getHeader(headerName);
			final String header = headerName + ":" + headerValue + "<br>";
			contentLength += header.length();
			headers.add(header);
		}

		// We add 2 to account for the CRLF (\r\n) that will be printed in the stream
		// after the header.
		contentLength += 2;

		writer.println("HTTP/1.1 200 OK");
		writer.println("Content-Type: text/html");
		writer.println(String.format("Content-Length: %s", contentLength));

		writer.println();

		for (String header : headers) {
			writer.println(header);
		}
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void destroy() {
		System.out.println("Destroying the " + this.getClass().getName() + " servlet.");
	}
}
