/**
 * 
 */
package org.rainbow.catalina.config;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author biya-bi
 *
 */
@XmlRootElement(name = "web-app")
public class DeploymentDescriptor {
	private List<ServletMapping> servletMappings;
	private List<Servlet> servlets;

	@XmlElement(name = "servlet-mapping")
	public List<ServletMapping> getServletMappings() {
		return servletMappings;
	}

	public void setServletMappings(List<ServletMapping> servletMappings) {
		this.servletMappings = servletMappings;
	}

	@XmlElement(name = "servlet")
	public List<Servlet> getServlets() {
		return servlets;
	}

	public void setServlets(List<Servlet> servlets) {
		this.servlets = servlets;
	}
}
