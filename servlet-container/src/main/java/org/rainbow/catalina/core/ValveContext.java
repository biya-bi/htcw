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
public interface ValveContext {
	void invokeNext(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException;
}
