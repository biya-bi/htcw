/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rainbow.catalina.Engine;
import org.rainbow.catalina.Host;
import org.rainbow.catalina.Valve;
import org.rainbow.catalina.ValveContext;
import org.rainbow.catalina.util.StringManager;

/**
 * @author biya-bi
 *
 */
public class SimpleEngineValve extends ContainedBase implements Valve {
	private StringManager sm = StringManager.getManager(Constants.PACKAGE);

	@Override
	public void invoke(HttpServletRequest request, HttpServletResponse response, ValveContext context)
			throws IOException, ServletException {
		// Validate that any HTTP/1.1 request included a host header
		if ("HTTP/1.1".equals(request.getProtocol()) && (request.getServerName() == null)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, sm.getString("simpleEngine.noHostHeader"));
			return;
		}
		// Select the Host to be used for this Request
		Engine engine = (SimpleEngine) getContainer();
		Host host = (Host) engine.map(request, true);
		if (host == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					sm.getString("simpleEngine.noHost", request.getRequestURI()));
			return;
		}
		// Ask this host to process this request
		host.invoke(request, response);
	}
}
