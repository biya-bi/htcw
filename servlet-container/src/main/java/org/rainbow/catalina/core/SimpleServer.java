/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

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
public class SimpleServer implements Server, Lifecycle {
	private static StringManager sm = StringManager.getManager(Constants.PACKAGE);

	private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);
	private Service[] services = new Service[0];
	private int port;
	private String shutdown;
	private boolean initialized;
	private boolean started;
	private Random random;

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String getShutdown() {
		return shutdown;
	}

	@Override
	public void setShutdown(String shutdown) {
		this.shutdown = shutdown;
	}

	@Override
	public void await() {
		// Set up a server socket to wait on
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
		} catch (IOException e) {
			System.err.println("SimpleServer.await: create[" + port + "]: " + e);
			e.printStackTrace();
			System.exit(1);
		}
		// Loop waiting for a connection and a valid command
		while (true) {
			// Wait for the next connection
			Socket socket = null;
			InputStream stream = null;
			try {
				socket = serverSocket.accept();
				socket.setSoTimeout(10 * 1000); // Ten seconds
				stream = socket.getInputStream();
			} catch (AccessControlException ace) {
				System.err.println("SimpleServer.accept security exception: " + ace.getMessage());
				continue;
			} catch (IOException e) {
				System.err.println("SimpleServer.await: accept: " + e);
				e.printStackTrace();
				System.exit(1);
			}
			// Read a set of characters from the socket
			StringBuffer command = new StringBuffer();
			int expected = 1024; // Cut off to avoid DoS attack
			while (expected < shutdown.length()) {
				if (random == null)
					random = new Random(System.currentTimeMillis());
				expected += (random.nextInt() % 1024);
			}
			while (expected > 0) {
				int ch = -1;
				try {
					ch = stream.read();
				} catch (IOException e) {
					System.err.println("SimpleServer.await: read: " + e);
					e.printStackTrace();
					ch = -1;
				}
				if (ch < 32) // Control character or EOF terminates loop
					break;
				command.append((char) ch);
				expected--;
			}
			// Close the socket now that we are done with it
			try {
				socket.close();
			} catch (IOException e) {
				;
			}
			// Match against our command string
			boolean match = command.toString().equals(shutdown);
			if (match) {
				try {
					stop();
				} catch (LifecycleException e) {
					e.printStackTrace();
				}
				break;
			} else {
				System.err.println("SimpleServer.await: Invalid command '" + command.toString() + "' received");
			}
		}
		// Close the server socket and return
		try {
			serverSocket.close();
		} catch (IOException e) {
			;
		}

	}

	@Override
	public synchronized void initialize() throws LifecycleException {
		if (initialized)
			throw new LifecycleException(sm.getString("simpleServer.alreadyInitialized"));
		initialized = true;
		// Initialize our defined Services
		for (int i = 0; i < services.length; i++) {
			services[i].initialize();
		}
	}

	@Override
	public void addService(Service service) {
		Objects.requireNonNull(service, sm.getString("simpleServer.serviceArgumentCannotBeNull"));

		Service[] temp = new Service[services.length + 1];

		System.arraycopy(services, 0, temp, 0, services.length);

		temp[services.length] = service;

		services = temp;

		Arrays.sort(services, (s1, s2) -> s1.getName().compareTo(s2.getName()));
	}

	@Override
	public Service findService(String name) {
		Service service = new SimpleService(name);
		int index = Arrays.binarySearch(services, service, (s1, s2) -> s1.getName().compareTo(s2.getName()));
		if (index >= 0)
			return services[index];
		return null;
	}

	@Override
	public Service[] findServices() {
		if (services == null)
			return new Service[0];
		return services.clone();
	}

	@Override
	public void removeService(Service service) {
		if (services == null || services.length == 0)
			return;
		int index = Arrays.binarySearch(services, service, (s1, s2) -> s1.getName().compareTo(s2.getName()));
		if (index < 0)
			return;

		Service[] temp = new Service[services.length - 1];

		int j = 0;

		for (int i = 0; i < services.length; i++) {
			if (i != j) {
				temp[j++] = services[i];
			}
		}
		services = temp;
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
		if (started)
			throw new LifecycleException(sm.getString("simpleServer.alreadyStarted"));
		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(BEFORE_START_EVENT, null);
		lifecycleSupport.fireLifecycleEvent(START_EVENT, null);
		started = true;
		// Start our defined Services
		for (int i = 0; i < services.length; i++) {
			if (services[i] instanceof Lifecycle)
				((Lifecycle) services[i]).start();
		}
		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(AFTER_START_EVENT, null);
	}

	@Override
	public synchronized void stop() throws LifecycleException {
		if (!started)
			throw new LifecycleException(sm.getString("simpleServer.notStarted"));
		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(BEFORE_STOP_EVENT, null);
		lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);
		started = false;
		// Stop our defined Services
		for (int i = 0; i < services.length; i++) {
			if (services[i] instanceof Lifecycle)
				((Lifecycle) services[i]).stop();
		}
		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(AFTER_STOP_EVENT, null);
	}

}
