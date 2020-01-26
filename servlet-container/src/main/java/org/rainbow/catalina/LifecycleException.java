/**
 * 
 */
package org.rainbow.catalina;

/**
 * @author biya-bi
 *
 */
public final class LifecycleException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7018141478184306863L;
	// ------------------------------------------------------------ Constructors

	/**
	 * Construct a new LifecycleException with no other information.
	 */
	public LifecycleException() {
		super(null, null);
	}

	/**
	 * Construct a new LifecycleException for the specified message.
	 *
	 * @param message Message describing this exception
	 */
	public LifecycleException(String message) {
		super(message, null);
	}

	/**
	 * Construct a new LifecycleException for the specified throwable.
	 *
	 * @param throwable Throwable that caused this exception
	 */
	public LifecycleException(Throwable throwable) {
		super(null, throwable);
	}

	/**
	 * Construct a new LifecycleException for the specified message and throwable.
	 *
	 * @param message   Message describing this exception
	 * @param throwable Throwable that caused this exception
	 */
	public LifecycleException(String message, Throwable throwable) {
		super(message, throwable);
	}

	// ---------------------------------------------------------- Public Methods

	/**
	 * Return a formatted string that describes this exception.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("LifecycleException:  ");
		String message = getMessage();
		Throwable cause = getCause();
		if (message != null) {
			sb.append(message);
			if (cause != null) {
				sb.append(":  ");
			}
		}
		if (cause != null) {
			sb.append(cause.toString());
		}
		return (sb.toString());
	}

}