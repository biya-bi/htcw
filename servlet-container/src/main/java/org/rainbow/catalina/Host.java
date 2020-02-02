/**
 * 
 */
package org.rainbow.catalina;

/**
 * @author biya-bi
 *
 */
public interface Host extends Container {
	Context map(String uri);
}
