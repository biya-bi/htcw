package org.rainbow.catalina.processors;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rainbow.catalina.connector.http.HttpRequest;
import org.rainbow.catalina.connector.http.HttpResponse;
import org.rainbow.catalina.connector.http.facades.HttpRequestFacade;
import org.rainbow.catalina.connector.http.facades.HttpResponseFacade;

public class ServletProcessor {
	public void process(Servlet servlet, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpRequestFacade requestFacade = new HttpRequestFacade((HttpRequest)request);
		HttpResponseFacade responseFacade = new HttpResponseFacade((HttpResponse)response);

		servlet.service((ServletRequest) requestFacade, (ServletResponse) responseFacade);

		((HttpResponse) response).finishResponse();
	}
}
