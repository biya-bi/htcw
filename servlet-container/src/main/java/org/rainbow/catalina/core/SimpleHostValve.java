/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rainbow.catalina.Context;
import org.rainbow.catalina.Host;
import org.rainbow.catalina.Manager;
import org.rainbow.catalina.Session;
import org.rainbow.catalina.Valve;
import org.rainbow.catalina.ValveContext;
import org.rainbow.catalina.util.StringManager;

/**
 * @author biya-bi
 *
 */
public class SimpleHostValve extends ContainedBase implements Valve {
	private StringManager sm = StringManager.getManager(Constants.PACKAGE);

	@Override
	public void invoke(HttpServletRequest request, HttpServletResponse response, ValveContext valveContext)
			throws IOException, ServletException {
		// Select the Context to be used for this Request
		Host host = (Host) getContainer();
		Context context = (Context) host.map(request.getRequestURI());
		if (context == null) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					sm.getString("simpleHost.contextNotFound"));
			return;
		}
		// Bind the context CL to the current thread
		Thread.currentThread().setContextClassLoader(context.getLoader().getClassLoader());
		// Update the session last access time for our session (if any)
		String sessionId = request.getRequestedSessionId();
		if (sessionId != null) {
			Manager manager = context.getManager();
			if (manager != null) {
				Session session = manager.findSession(sessionId);
				if ((session != null) && session.isValid())
					session.access();
			}
		}
		// Ask this Context to process this request
		context.invoke(request, response);
	}

}
