/**
 * 
 */
package org.rainbow.catalina;

import org.rainbow.catalina.Container;

/**
 * @author biya-bi
 *
 */
public interface Connector {

	Container getContainer();

	void setContainer(Container container);

	Service getService();

	void setService(Service service);

	void initialize() throws LifecycleException;

}