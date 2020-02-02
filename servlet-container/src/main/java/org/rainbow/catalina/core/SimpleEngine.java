/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rainbow.catalina.Container;
import org.rainbow.catalina.Engine;
import org.rainbow.catalina.Host;
import org.rainbow.catalina.Lifecycle;
import org.rainbow.catalina.LifecycleException;
import org.rainbow.catalina.LifecycleListener;
import org.rainbow.catalina.util.LifecycleSupport;
import org.rainbow.catalina.util.StringManager;

/**
 * @author biya-bi
 *
 */
public class SimpleEngine extends ContainerBase implements Engine, Lifecycle {
	private SimplePipeline pipeline = new SimplePipeline(this);
	private StringManager sm = StringManager.getManager(Constants.PACKAGE);
	private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);
	private volatile boolean started;
	private String defaultHost;

	public SimpleEngine() {
		pipeline.setBasic(new SimpleEngineValve());
	}

	@Override
	public void addChild(Container child) {
		if (!(child instanceof Host))
			throw new IllegalArgumentException(sm.getString("simpleEngine.cannotAddChild"));
		super.addChild(child);
	}

	@Override
	public void setParent(Container parent) {
		throw new IllegalStateException(sm.getString("simpleEngine.cannotSetParent"));
	}

	@Override
	public void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		pipeline.invoke(request, response);
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
		if (started)
			throw new LifecycleException(sm.getString("simpleEngine.alreadyStarted", getName()));

		try {
			started = true;

			// Notify our interested LifecycleListeners
			lifecycleSupport.fireLifecycleEvent(BEFORE_START_EVENT, null);

			// Notify our interested LifecycleListeners
			lifecycleSupport.fireLifecycleEvent(START_EVENT, null);

			// Start our child containers, if any
			Container children[] = findChildren();
			for (int i = 0; i < children.length; i++) {
				if (children[i] instanceof Lifecycle)
					((Lifecycle) children[i]).start();
			}

			// Start the Valves in our pipeline (including the basic),
			// if any
			if (pipeline instanceof Lifecycle)
				((Lifecycle) pipeline).start();

			// Notify our interested LifecycleListeners
			lifecycleSupport.fireLifecycleEvent(AFTER_START_EVENT, null);
		} catch (Throwable t) {
			stop();
			throw t;
		}
	}

	@Override
	public synchronized void stop() throws LifecycleException {
		if (!started)
			throw new LifecycleException(sm.getString("simpleEngine.notStarted", getName()));

		started = false;

		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(BEFORE_STOP_EVENT, null);
		lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);

		// Stop the Valves in our pipeline (including the basic), if any
		if (pipeline instanceof Lifecycle) {
			((Lifecycle) pipeline).stop();
		}

		// Stop our child containers, if any
		Container children[] = findChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof Lifecycle)
				((Lifecycle) children[i]).stop();
		}

		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(AFTER_STOP_EVENT, null);
	}

	@Override
	public String getDefaultHost() {
		return defaultHost;
	}

	@Override
	public void setDefaultHost(String defaultHost) {
		this.defaultHost = defaultHost;
	}

	@Override
	public Host map(HttpServletRequest request, boolean update) {
		Host host = (Host) findChild(request.getServerName());
		if (host != null)
			return host;
		return (Host) findChild(getDefaultHost());
	}

}
