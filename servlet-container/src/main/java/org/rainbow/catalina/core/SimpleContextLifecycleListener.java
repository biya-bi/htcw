/**
 * 
 */
package org.rainbow.catalina.core;

import org.rainbow.catalina.Container;
import org.rainbow.catalina.Lifecycle;
import org.rainbow.catalina.LifecycleEvent;
import org.rainbow.catalina.LifecycleListener;
import org.rainbow.catalina.Logger;

/**
 * @author biya-bi
 *
 */
public class SimpleContextLifecycleListener implements LifecycleListener {
	@Override
	public void lifecycleEvent(LifecycleEvent event) {
		if (!(event.getSource() instanceof Container)) {
			throw new IllegalStateException(String.format("A '%s' can only listen to events fired from a '%s'",
					this.getClass(), Container.class));
		}

		Container container = (Container) event.getSource();

		Logger logger = container.getLogger();

		if (logger == null) {
			return;
		}

		String name = container.getName();

		if (Lifecycle.START_EVENT.equals(event.getType())) {
			logger.log(String.format("Starting context '%s'", name));
		} else if (Lifecycle.STOP_EVENT.equals(event.getType())) {
			logger.log(String.format("Stopping context '%s'", name));
		}
	}
}
