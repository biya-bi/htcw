/**
 * 
 */
package org.rainbow.catalina.logger;

/**
 * @author biya-bi
 *
 */
public final class SystemOutLogger extends LoggerBase {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The descriptive information about this implementation.
	 */
	protected static final String info = "org.apache.catalina.logger.SystemOutLogger/1.0";

	// --------------------------------------------------------- Public Methods

	/**
	 * Writes the specified message to a servlet log file, usually an event log. The
	 * name and type of the servlet log is specific to the servlet container.
	 *
	 * @param msg A <code>String</code> specifying the message to be written to the
	 *            log file
	 */
	@Override
	public void log(String msg) {
		System.out.println(msg);
	}

}
