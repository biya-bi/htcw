package org.rainbow.catalina;

/**
 * 
 */
import java.util.EventObject;

/**
 * @author biya-bi
 *
 */
public final class LifecycleEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8031758888587442378L;

	// ----------------------------------------------------- Instance Variables

	/**
	 * The event data associated with this event.
	 */
	private final Object data;

	/**
	 * The Lifecycle on which this event occurred.
	 */
	private final Lifecycle lifecycle;

	/**
	 * The event type this instance represents.
	 */
	private final String type;
	// ----------------------------------------------------------- Constructors

	/**
	 * Construct a new LifecycleEvent with the specified parameters.
	 *
	 * @param lifecycle Component on which this event occurred
	 * @param type      Event type (required)
	 */
	public LifecycleEvent(Lifecycle lifecycle, String type) {
		this(lifecycle, type, null);
	}

	/**
	 * Construct a new LifecycleEvent with the specified parameters.
	 *
	 * @param lifecycle Component on which this event occurred
	 * @param type      Event type (required)
	 * @param data      Event data (if any)
	 */
	public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {
		super(lifecycle);
		this.lifecycle = lifecycle;
		this.type = type;
		this.data = data;
	}

	// ------------------------------------------------------------- Properties

	/**
	 * Return the event data of this event.
	 */
	public Object getData() {
		return this.data;
	}

	/**
	 * Return the Lifecycle on which this event occurred.
	 */
	public Lifecycle getLifecycle() {
		return this.lifecycle;
	}

	/**
	 * Return the event type of this event.
	 */
	public String getType() {
		return this.type;
	}

}