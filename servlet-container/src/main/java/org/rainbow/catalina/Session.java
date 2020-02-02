/**
 * 
 */
package org.rainbow.catalina;

import javax.servlet.http.HttpSession;

/**
 * @author biya-bi
 *
 */
public interface Session extends HttpSession {
	boolean isValid();

	void access();
}
