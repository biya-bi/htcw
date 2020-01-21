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
public interface Pipeline {

	Valve getBasic();

	void setBasic(Valve valve);

	void addValve(Valve valve);

	Valve[] getValves();

	void removeValve(Valve valve);

	Valve getFirst();

	Container getContainer();

	void setContainer(Container container);

	void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;;
}