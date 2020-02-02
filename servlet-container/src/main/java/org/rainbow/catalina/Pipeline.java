/**
 * 
 */
package org.rainbow.catalina;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author biya-bi
 *
 */
public interface Pipeline {
	Valve getBasic();

	void setBasic(Valve valve);

	void addValve(Valve valve);

	Valve[] getValves();

	void removeValve(Valve valve);

	void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;;
}