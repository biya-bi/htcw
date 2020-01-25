package org.rainbow.catalina;

import javax.servlet.http.HttpServletRequest;

public interface Mapper {
	Container getContainer();

	void setContainer(Container container);

	String getProtocol();

	void setProtocol(String protocol);

	Container map(HttpServletRequest request, boolean update);
}
