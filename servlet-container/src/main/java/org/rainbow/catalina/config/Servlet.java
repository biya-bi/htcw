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
@XmlRootElement(name = "servlet")
public class Servlet {
	private String servletName;
	private String servletClass;
	private List<InitParam> initParams;

	public Servlet() {
	}

	public Servlet(String servletName, String servletClass) {
		this.servletName = servletName;
		this.servletClass = servletClass;
	}

	@XmlElement(name = "servlet-name")
	public String getServletName() {
		return servletName;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	@XmlElement(name = "servlet-class")
	public String getServletClass() {
		return servletClass;
	}

	public void setServletClass(String servletClass) {
		this.servletClass = servletClass;
	}

	@XmlElement(name = "init-param")
	public List<InitParam> getInitParams() {
		return initParams;
	}

	public void setInitParams(List<InitParam> initParams) {
		this.initParams = initParams;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((servletClass == null) ? 0 : servletClass.hashCode());
		result = prime * result + ((servletName == null) ? 0 : servletName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Servlet other = (Servlet) obj;
		if (servletClass == null) {
			if (other.servletClass != null)
				return false;
		} else if (!servletClass.equals(other.servletClass))
			return false;
		if (servletName == null) {
			if (other.servletName != null)
				return false;
		} else if (!servletName.equals(other.servletName))
			return false;
		return true;
	}

}
