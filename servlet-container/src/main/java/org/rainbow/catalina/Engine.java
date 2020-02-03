/**
 * 
 */
package org.rainbow.catalina;

import javax.servlet.http.HttpServletRequest;

/**
 * @author biya-bi
 *
 */
public interface Engine extends Container {
	String getDefaultHost();

	void setDefaultHost(String defaultHost);

	Host map(HttpServletRequest request, boolean update);

	Service getService();

	void setService(Service service);
}
