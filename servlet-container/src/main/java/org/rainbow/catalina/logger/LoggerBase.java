/**
 * 
 */
package org.rainbow.catalina.logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.CharArrayWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import org.rainbow.catalina.Container;
import org.rainbow.catalina.LifecycleException;
import org.rainbow.catalina.Logger;

/**
 * @author biya-bi
 *
 */
public abstract class LoggerBase implements Logger {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The Container with which this Logger has been associated.
	 */
	protected Container container = null;

	/**
	 * The debugging detail level for this component.
	 */
	protected int debug = 0;

	/**
	 * The descriptive information about this implementation.
	 */
	protected static final String info = "org.rainbow.catalina.logger.LoggerBase/1.0";

	/**
	 * The property change support for this component.
	 */
	protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	/**
	 * The verbosity level for above which log messages may be filtered.
	 */
	protected int verbosity = ERROR;

	// ------------------------------------------------------------- Properties

	/**
	 * Return the Container with which this Logger has been associated.
	 */
	@Override
	public Container getContainer() {
		return container;
	}

	/**
	 * Set the Container with which this Logger has been associated.
	 *
	 * @param container The associated Container
	 */
	@Override
	public void setContainer(Container container) {
		Container oldContainer = this.container;
		this.container = container;
		propertyChangeSupport.firePropertyChange("container", oldContainer, this.container);
	}

	/**
	 * Return the debugging detail level for this component.
	 */
	public int getDebug() {
		return debug;
	}

	/**
	 * Set the debugging detail level for this component.
	 *
	 * @param debug The new debugging detail level
	 */
	public void setDebug(int debug) {
		this.debug = debug;
	}

	/**
	 * Return descriptive information about this Logger implementation and the
	 * corresponding version number, in the format
	 * <code>&lt;description&gt;/&lt;version&gt;</code>.
	 */
	@Override
	public String getInfo() {
		return info;
	}

	/**
	 * Return the verbosity level of this logger. Messages logged with a higher
	 * verbosity than this level will be silently ignored.
	 */
	@Override
	public int getVerbosity() {
		return verbosity;
	}

	/**
	 * Set the verbosity level of this logger. Messages logged with a higher
	 * verbosity than this level will be silently ignored.
	 *
	 * @param verbosity The new verbosity level
	 */
	@Override
	public void setVerbosity(int verbosity) {
		this.verbosity = verbosity;
	}

	/**
	 * Set the verbosity level of this logger. Messages logged with a higher
	 * verbosity than this level will be silently ignored.
	 *
	 * @param verbosityLevel The new verbosity level, as a string
	 */
	public void setVerbosityLevel(String verbosity) {
		if ("FATAL".equalsIgnoreCase(verbosity))
			this.verbosity = FATAL;
		else if ("ERROR".equalsIgnoreCase(verbosity))
			this.verbosity = ERROR;
		else if ("WARNING".equalsIgnoreCase(verbosity))
			this.verbosity = WARNING;
		else if ("INFORMATION".equalsIgnoreCase(verbosity))
			this.verbosity = INFORMATION;
		else if ("DEBUG".equalsIgnoreCase(verbosity))
			this.verbosity = DEBUG;
	}

	// --------------------------------------------------------- Public Methods

	/**
	 * Add a property change listener to this component.
	 *
	 * @param listener The listener to add
	 */
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	/**
	 * Writes the specified message to a servlet log file, usually an event log. The
	 * name and type of the servlet log is specific to the servlet container. This
	 * message will be logged unconditionally.
	 *
	 * @param message A <code>String</code> specifying the message to be written to
	 *                the log file
	 */
	@Override
	public abstract void log(String msg);

	/**
	 * Writes the specified exception, and message, to a servlet log file. The
	 * implementation of this method should call <code>log(msg, exception)</code>
	 * instead. This method is deprecated in the ServletContext interface, but not
	 * deprecated here to avoid many useless compiler warnings. This message will be
	 * logged unconditionally.
	 *
	 * @param exception An <code>Exception</code> to be reported
	 * @param msg       The associated message string
	 */
	@Override
	public void log(Exception exception, String msg) {
		log(msg, exception);
	}

	/**
	 * Writes an explanatory message and a stack trace for a given
	 * <code>Throwable</code> exception to the servlet log file. The name and type
	 * of the servlet log file is specific to the servlet container, usually an
	 * event log. This message will be logged unconditionally.
	 *
	 * @param msg       A <code>String</code> that describes the error or exception
	 * @param throwable The <code>Throwable</code> error or exception
	 */
	@Override
	public void log(String msg, Throwable throwable) {
		CharArrayWriter buf = new CharArrayWriter();
		PrintWriter writer = new PrintWriter(buf);
		writer.println(msg);
		if (throwable != null)
			throwable.printStackTrace(writer);
		Throwable rootCause = null;
		if (throwable instanceof LifecycleException)
			rootCause = ((LifecycleException) throwable).getCause();
		else if (throwable instanceof ServletException)
			rootCause = ((ServletException) throwable).getRootCause();
		if (rootCause != null) {
			writer.println("----- Root Cause -----");
			rootCause.printStackTrace(writer);
		}
		log(buf.toString());
	}

	/**
	 * Writes the specified message to the servlet log file, usually an event log,
	 * if the logger is set to a verbosity level equal to or higher than the
	 * specified value for this message.
	 *
	 * @param message   A <code>String</code> specifying the message to be written
	 *                  to the log file
	 * @param verbosity Verbosity level of this message
	 */
	@Override
	public void log(String message, int verbosity) {
		if (this.verbosity >= verbosity)
			log(message);
	}

	/**
	 * Writes the specified message and exception to the servlet log file, usually
	 * an event log, if the logger is set to a verbosity level equal to or higher
	 * than the specified value for this message.
	 *
	 * @param message   A <code>String</code> that describes the error or exception
	 * @param throwable The <code>Throwable</code> error or exception
	 * @param verbosity Verbosity level of this message
	 */
	@Override
	public void log(String message, Throwable throwable, int verbosity) {
		if (this.verbosity >= verbosity)
			log(message, throwable);
	}

	/**
	 * Remove a property change listener from this component.
	 *
	 * @param listener The listener to remove
	 */
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

}
