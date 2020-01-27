/**
 * 
 */
package org.rainbow.catalina;

/**
 * @author biya-bi
 *
 */
public interface Loader {
	ClassLoader getClassLoader();

	Container getContainer();

	void setContainer(Container container);
}
