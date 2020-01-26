/**
 * 
 */
package org.rainbow.catalina.core;

import org.rainbow.catalina.Container;
import org.rainbow.catalina.Lifecycle;
import org.rainbow.catalina.LifecycleEvent;
import org.rainbow.catalina.LifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author biya-bi
 *
 */
public class SimpleContextLifecycleListener implements LifecycleListener {
	private static final Logger logger = LoggerFactory.getLogger(SimpleContextLifecycleListener.class);

	@Override
	public void lifecycleEvent(LifecycleEvent event) {
		String name = ((Container) event.getSource()).getName();

		if (Lifecycle.START_EVENT.equals(event.getType())) {
			logger.info(String.format("Starting context '%s'", name));
		} else if (Lifecycle.STOP_EVENT.equals(event.getType())) {
			logger.info(String.format("Stopping context '%s'", name));
		}
	}
}
