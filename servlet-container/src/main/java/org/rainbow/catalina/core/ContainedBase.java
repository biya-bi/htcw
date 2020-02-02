/**
 * 
 */
package org.rainbow.catalina.core;

import org.rainbow.catalina.Contained;
import org.rainbow.catalina.Container;

/**
 * @author biya-bi
 *
 */
public class ContainedBase implements Contained {

	private Container container;

	public ContainedBase() {
		super();
	}

	@Override
	public Container getContainer() {
		return container;
	}

	@Override
	public void setContainer(Container container) {
		this.container = container;
	}

}