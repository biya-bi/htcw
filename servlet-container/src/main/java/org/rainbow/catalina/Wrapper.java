/**
 * 
 */
package org.rainbow.catalina;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

/**
 * @author biya-bi
 *
 */
public interface Wrapper extends Container {

	Servlet allocate() throws ServletException;

	void deallocate(Servlet servlet) throws ServletException;

	void load() throws ServletException;

	void unload() throws ServletException;

	String getName();

	void setName(String name);

	String getServletClass();

	void setServletClass(String servletClass);

}
