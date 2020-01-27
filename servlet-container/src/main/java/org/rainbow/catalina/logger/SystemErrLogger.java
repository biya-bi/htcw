/**
 * 
 */
package org.rainbow.catalina.logger;

/**
 * @author biya-bi
 *
 */
public class SystemErrLogger extends LoggerBase {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The descriptive information about this implementation.
	 */
	protected static final String info = "org.rainbow.catalina.logger.SystemErrLogger/1.0";

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
		System.err.println(msg);
	}

}
