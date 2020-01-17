/**
 * 
 */
package org.rainbow.catalina.core;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.management.ObjectName;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.AccessLog;
import org.apache.catalina.Cluster;
import org.apache.catalina.Container;
import org.apache.catalina.ContainerListener;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Pipeline;
import org.apache.catalina.Realm;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.juli.logging.Log;
import org.rainbow.catalina.connector.http.HttpRequest;
import org.rainbow.catalina.connector.http.HttpResponse;
import org.rainbow.catalina.processors.ServletProcessor;
import org.rainbow.catalina.processors.StaticResourceProcessor;

/**
 * @author biya-bi
 *
 */
public class SimpleContainer implements Container {

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public LifecycleListener[] findLifecycleListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() throws LifecycleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() throws LifecycleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() throws LifecycleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() throws LifecycleException {
		// TODO Auto-generated method stub

	}

	@Override
	public LifecycleState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Log getLogger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLogName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectName getObjectName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMBeanKeyProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pipeline getPipeline() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cluster getCluster() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCluster(Cluster cluster) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getBackgroundProcessorDelay() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBackgroundProcessorDelay(int delay) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public Container getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(Container container) {
		// TODO Auto-generated method stub

	}

	@Override
	public ClassLoader getParentClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParentClassLoader(ClassLoader parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public Realm getRealm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRealm(Realm realm) {
		// TODO Auto-generated method stub

	}

	@Override
	public void backgroundProcess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addChild(Container child) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addContainerListener(ContainerListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Container findChild(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Container[] findChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContainerListener[] findContainerListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeChild(Container child) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeContainerListener(ContainerListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireContainerEvent(String type, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logAccess(Request request, Response response, long time, boolean useDefault) {
		// TODO Auto-generated method stub

	}

	@Override
	public AccessLog getAccessLog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStartStopThreads() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setStartStopThreads(int startStopThreads) {
		// TODO Auto-generated method stub

	}

	@Override
	public File getCatalinaBase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getCatalinaHome() {
		// TODO Auto-generated method stub
		return null;
	}

	public void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (request.getRequestURI().startsWith("/servlet/")) {
			ServletProcessor processor = new ServletProcessor();
			processor.process((HttpRequest) request, (HttpResponse) response);
		} else {
			StaticResourceProcessor processor = new StaticResourceProcessor();
			processor.process((HttpRequest) request, (HttpResponse) response);
		}
	}
}
