/**
 * 
 */
package org.rainbow.catalina;

/**
 * @author biya-bi
 *
 */
public interface Server {
	/**
	 * Return the port number we listen to for shutdown commands.
	 */
	int getPort();

	/**
	 * Set the port number we listen to for shutdown commands.
	 *
	 * @param port The new port number
	 */
	void setPort(int port);

	/**
	 * Return the shutdown command string we are waiting for.
	 */
	String getShutdown();

	/**
	 * Set the shutdown command we are waiting for.
	 *
	 * @param shutdown The new shutdown command
	 */
	void setShutdown(String shutdown);

	/**
	 * Wait until a proper shutdown command is received, then return.
	 */
	void await();

	/**
	 * Invoke a pre-startup initialization. This is used to allow connectors to bind
	 * to restricted ports under Unix operating environments.
	 *
	 * @exception LifecycleException If this server was already initialized.
	 */
	void initialize() throws LifecycleException;

	/**
	 * Add a new Service to the set of defined Services.
	 *
	 * @param service The Service to be added
	 */
	void addService(Service service);

	/**
	 * Return the specified Service (if it exists); otherwise return
	 * <code>null</code>.
	 *
	 * @param name Name of the Service to be returned
	 */
	Service findService(String name);

	/**
	 * Return the set of Services defined within this Server.
	 */
	Service[] findServices();

	/**
	 * Remove the specified Service from the set associated from this Server.
	 *
	 * @param service The Service to be removed
	 */
	void removeService(Service service);
}
