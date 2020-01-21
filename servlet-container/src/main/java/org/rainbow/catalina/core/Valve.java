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
public interface Valve {
	void invoke(HttpServletRequest request, HttpServletResponse response, ValveContext context)
			throws IOException, ServletException;
}
