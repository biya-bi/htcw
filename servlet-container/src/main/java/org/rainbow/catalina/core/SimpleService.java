/**
 * 
 */
package org.rainbow.catalina.core;

import java.beans.PropertyChangeSupport;
import java.util.Objects;

import org.rainbow.catalina.Connector;
import org.rainbow.catalina.Container;
import org.rainbow.catalina.Engine;
import org.rainbow.catalina.Lifecycle;
import org.rainbow.catalina.LifecycleException;
import org.rainbow.catalina.LifecycleListener;
import org.rainbow.catalina.Server;
import org.rainbow.catalina.Service;
import org.rainbow.catalina.util.LifecycleSupport;
import org.rainbow.catalina.util.StringManager;

/**
 * @author biya-bi
 *
 */
public class SimpleService implements Service, Lifecycle {
	private static StringManager sm = StringManager.getManager(Constants.PACKAGE);

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);
	private String name;
	private Container container;
	private boolean started;
	private Connector[] connectors = new Connector[0];
	private boolean initialized;
	private Server server;

	public SimpleService() {
	}

	public SimpleService(String name) {
		this.name = name;
	}

	@Override
	public Container getContainer() {
		return container;
	}

	@Override
	public void setContainer(Container container) {
		Container oldContainer = this.container;
		if ((oldContainer != null) && (oldContainer instanceof Engine))
			((Engine) oldContainer).setService(null);
		this.container = container;
		if ((this.container != null) && (this.container instanceof Engine))
			((Engine) this.container).setService(this);
		if (started && (this.container != null) && (this.container instanceof Lifecycle)) {
			try {
				((Lifecycle) this.container).start();
			} catch (LifecycleException e) {
				; // TODO Auto-generated method stub
			}
		}
		synchronized (connectors) {
			for (int i = 0; i < connectors.length; i++)
				connectors[i].setContainer(this.container);
		}
		if (started && (oldContainer != null) && (oldContainer instanceof Lifecycle)) {
			try {
				((Lifecycle) oldContainer).stop();
			} catch (LifecycleException e) {
				;
			}
		}
		// Report this property change to interested listeners
		propertyChangeSupport.firePropertyChange("container", oldContainer, this.container);
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Server getServer() {
		return server;
	}

	@Override
	public void setServer(Server server) {
		this.server = server;
	}

	@Override
	public void addConnector(Connector connector) {
		Objects.requireNonNull(connector, sm.getString("simpleService.connectorArgumentCannotBeNull"));

		synchronized (connectors) {
			connector.setContainer(this.container);
			connector.setService(this);
			Connector[] temp = new Connector[connectors.length + 1];
			System.arraycopy(connectors, 0, temp, 0, connectors.length);
			temp[connectors.length] = connector;
			connectors = temp;
			if (initialized) {
				try {
					connector.initialize();
				} catch (LifecycleException e) {
					e.printStackTrace(System.err);
				}
			}
			if (started && (connector instanceof Lifecycle)) {
				try {
					((Lifecycle) connector).start();
				} catch (LifecycleException e) {
					;
				}
			} // Report this property change to interested listeners
			propertyChangeSupport.firePropertyChange("connector", null, connector);
		}
	}

	@Override
	public Connector[] findConnectors() {
		return connectors;
	}

	@Override
	public void removeConnector(Connector connector) {
		synchronized (connectors) {
			int j = -1;
			for (int i = 0; i < connectors.length; i++) {
				if (connector == connectors[i]) {
					j = i;
					break;
				}
			}
			if (j < 0)
				return;
			if (started && (connectors[j] instanceof Lifecycle)) {
				try {
					((Lifecycle) connectors[j]).stop();
				} catch (LifecycleException e) {
					;
				}
			}
			connectors[j].setContainer(null);
			connector.setService(null);
			int k = 0;
			Connector[] temp = new Connector[connectors.length - 1];
			for (int i = 0; i < connectors.length; i++) {
				if (i != j)
					temp[k++] = connectors[i];
			}
			connectors = temp;
			// Report this property change to interested listeners
			propertyChangeSupport.firePropertyChange("connector", connector, null);
		}

	}

	@Override
	public void initialize() throws LifecycleException {
		if (initialized)
			throw new LifecycleException(sm.getString("simpleService.alreadyInitialized"));
		initialized = true;
		// Initialize our defined Connectors
		synchronized (connectors) {
			for (int i = 0; i < connectors.length; i++) {
				connectors[i].initialize();
			}
		}
	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		lifecycleSupport.addLifecycleListener(listener);
	}

	@Override
	public LifecycleListener[] findLifecycleListeners() {
		return lifecycleSupport.findLifecycleListeners();
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		lifecycleSupport.removeLifecycleListener(listener);
	}

	@Override
	public synchronized void start() throws LifecycleException {
		// Validate and update our current component state
		if (started) {
			throw new LifecycleException(sm.getString("simpleService.alreadyStarted"));
		}
		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(BEFORE_START_EVENT, null);
		lifecycleSupport.fireLifecycleEvent(START_EVENT, null);

		started = true;

		// Start our defined Container first
		if (container != null) {
			if (container instanceof Lifecycle) {
				((Lifecycle) container).start();
			}
		}
		// Start our defined Connectors second
		for (int i = 0; i < connectors.length; i++) {
			if (connectors[i] instanceof Lifecycle)
				((Lifecycle) connectors[i]).start();
		}
		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(AFTER_START_EVENT, null);
	}

	@Override
	public synchronized void stop() throws LifecycleException {
		// Validate and update our current component state
		if (!started) {
			throw new LifecycleException(sm.getString("simpleService.notStarted"));
		}
		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(BEFORE_STOP_EVENT, null);
		lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);
		started = false;
		// Stop our defined Connectors first
		synchronized (connectors) {
			for (int i = 0; i < connectors.length; i++) {
				if (connectors[i] instanceof Lifecycle)
					((Lifecycle) connectors[i]).stop();
			}
		}
		// Stop our defined Container second
		if (container != null) {
			synchronized (container) {
				if (container instanceof Lifecycle) {
					((Lifecycle) container).stop();
				}
			}
		}
		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(AFTER_STOP_EVENT, null);
	}

}
