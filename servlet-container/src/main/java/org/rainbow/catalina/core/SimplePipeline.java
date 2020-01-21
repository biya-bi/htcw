/**
 * 
 */
package org.rainbow.catalina.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author biya-bi
 *
 */
public class SimplePipeline implements Pipeline {

	// The basic Valve (if any) associated with this Pipeline.
	protected Valve basic = null;
	// The Container with which this Pipeline is associated.
	protected Container container = null;
	// the array of Valves
	protected Valve valves[] = new Valve[0];

	public SimplePipeline(Container container) {
		setContainer(container);
	}

	@Override
	public Valve getBasic() {
		return basic;
	}

	@Override
	public void setBasic(Valve valve) {
		this.basic = valve;
		((Contained) valve).setContainer(container);
	}

	@Override
	public void addValve(Valve valve) {
		if (valve instanceof Contained)
			((Contained) valve).setContainer(this.container);

		synchronized (valves) {
			Valve results[] = new Valve[valves.length + 1];
			System.arraycopy(valves, 0, results, 0, valves.length);
			results[valves.length] = valve;
			valves = results;
		}
	}

	@Override
	public Valve[] getValves() {
		return valves;
	}

	@Override
	public void removeValve(Valve valve) {
		// TODO Auto-generated method stub

	}

	@Override
	public Valve getFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Container getContainer() {
		return this.container;
	}

	@Override
	public void setContainer(Container container) {
		this.container = container;
	}

	// This class is copied from org.apache.catalina.core.StandardPipeline class's
	// StandardPipelineValveContext inner class.
	protected class SimplePipelineValveContext implements ValveContext {

		private int stage = 0;

		public String getInfo() {
			return null;
		}

		public void invokeNext(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			int subscript = stage;
			stage = stage + 1;
			// Invoke the requested Valve for the current request thread
			if (subscript < valves.length) {
				valves[subscript].invoke(request, response, this);
			} else if ((subscript == valves.length) && (basic != null)) {
				basic.invoke(request, response, this);
			} else {
				throw new ServletException("No valve");
			}
		}
	} // end of inner class

	@Override
	public void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// Invoke the first Valve in this pipeline for this request
		(new SimplePipelineValveContext()).invokeNext(request, response);
	}
}
