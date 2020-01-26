/**
 * 
 */
package org.rainbow.catalina.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author biya-bi
 *
 */
@XmlRootElement(name = "servlet-mapping")
public class ServletMapping {
	private String servletName;
	private String urlPattern;

	public ServletMapping() {
	}

	public ServletMapping(String servletName, String urlPattern) {
		this.servletName = servletName;
		this.urlPattern = urlPattern;
	}

	@XmlElement(name = "servlet-name")
	public String getServletName() {
		return servletName;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	@XmlElement(name = "url-pattern")
	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((servletName == null) ? 0 : servletName.hashCode());
		result = prime * result + ((urlPattern == null) ? 0 : urlPattern.hashCode());
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
		ServletMapping other = (ServletMapping) obj;
		if (servletName == null) {
			if (other.servletName != null)
				return false;
		} else if (!servletName.equals(other.servletName))
			return false;
		if (urlPattern == null) {
			if (other.urlPattern != null)
				return false;
		} else if (!urlPattern.equals(other.urlPattern))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServletMapping [servletName=" + servletName + ", urlPattern=" + urlPattern + "]";
	}

}
