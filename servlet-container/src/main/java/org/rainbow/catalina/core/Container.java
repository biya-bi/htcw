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
public interface Container {
	void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

	Loader getLoader();

	void setLoader(Loader loader);

	Container getParent();

	void setParent(Container parent);
}
