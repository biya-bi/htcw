package org.rainbow.catalina.core;

import java.awt.event.ContainerListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.directory.DirContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.rainbow.catalina.Container;
import org.rainbow.catalina.Context;
import org.rainbow.catalina.Globals;
import org.rainbow.catalina.Lifecycle;
import org.rainbow.catalina.LifecycleException;
import org.rainbow.catalina.Loader;
import org.rainbow.catalina.Manager;
import org.rainbow.catalina.Mapper;
import org.rainbow.catalina.Wrapper;
import org.rainbow.catalina.config.WebXml;
import org.rainbow.catalina.config.Servlet;
import org.rainbow.catalina.config.ServletMapping;
import org.rainbow.catalina.config.XmlProcessor;
import org.rainbow.catalina.deploy.ApplicationParameter;
import org.rainbow.catalina.deploy.ContextEjb;
import org.rainbow.catalina.deploy.ContextEnvironment;
import org.rainbow.catalina.deploy.ContextLocalEjb;
import org.rainbow.catalina.deploy.ContextResource;
import org.rainbow.catalina.deploy.ContextResourceLink;
import org.rainbow.catalina.deploy.ErrorPage;
import org.rainbow.catalina.deploy.FilterDef;
import org.rainbow.catalina.deploy.FilterMap;
import org.rainbow.catalina.deploy.LoginConfig;
import org.rainbow.catalina.deploy.NamingResources;
import org.rainbow.catalina.deploy.SecurityConstraint;
import org.rainbow.catalina.util.CharsetMapper;

public class SimpleContext extends ContainerBase implements Context {
	private Map<String, String> servletMappings = new HashMap<>();
	private Mapper mapper;
	private Map<String, Mapper> mappers = new HashMap<>();
	private Manager manager;
	private String path;

	public SimpleContext() {
		pipeline.setBasic(new SimpleContextValve());
	}

	public Object[] getApplicationListeners() {
		return null;
	}

	public void setApplicationListeners(Object listeners[]) {
	}

	public boolean getAvailable() {
		return false;
	}

	public void setAvailable(boolean flag) {
	}

	public CharsetMapper getCharsetMapper() {
		return null;
	}

	public void setCharsetMapper(CharsetMapper mapper) {
	}

	public boolean getConfigured() {
		return false;
	}

	public void setConfigured(boolean configured) {
	}

	public boolean getCookies() {
		return false;
	}

	public void setCookies(boolean cookies) {
	}

	public boolean getCrossContext() {
		return false;
	}

	public void setCrossContext(boolean crossContext) {
	}

	public String getDisplayName() {
		return null;
	}

	public void setDisplayName(String displayName) {
	}

	public boolean getDistributable() {
		return false;
	}

	public void setDistributable(boolean distributable) {
	}

	public String getDocBase() {
		return null;
	}

	public void setDocBase(String docBase) {
	}

	public LoginConfig getLoginConfig() {
		return null;
	}

	public void setLoginConfig(LoginConfig config) {
	}

	public NamingResources getNamingResources() {
		return null;
	}

	public void setNamingResources(NamingResources namingResources) {
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPublicId() {
		return null;
	}

	public void setPublicId(String publicId) {
	}

	public boolean getReloadable() {
		return false;
	}

	public void setReloadable(boolean reloadable) {
	}

	public boolean getOverride() {
		return false;
	}

	public void setOverride(boolean override) {
	}

	public boolean getPrivileged() {
		return false;
	}

	public void setPrivileged(boolean privileged) {
	}

	public ServletContext getServletContext() {
		return null;
	}

	public int getSessionTimeout() {
		return 0;
	}

	public void setSessionTimeout(int timeout) {
	}

	public String getWrapperClass() {
		return null;
	}

	public void setWrapperClass(String wrapperClass) {
	}

	public void addApplicationListener(String listener) {
	}

	public void addApplicationParameter(ApplicationParameter parameter) {
	}

	public void addConstraint(SecurityConstraint constraint) {
	}

	public void addEjb(ContextEjb ejb) {
	}

	public void addEnvironment(ContextEnvironment environment) {
	}

	public void addErrorPage(ErrorPage errorPage) {
	}

	public void addFilterDef(FilterDef filterDef) {
	}

	public void addFilterMap(FilterMap filterMap) {
	}

	public void addInstanceListener(String listener) {
	}

	public void addLocalEjb(ContextLocalEjb ejb) {
	}

	public void addMimeMapping(String extension, String mimeType) {
	}

	public void addParameter(String name, String value) {
	}

	public void addResource(ContextResource resource) {
	}

	public void addResourceEnvRef(String name, String type) {
	}

	public void addResourceLink(ContextResourceLink resourceLink) {
	}

	public void addRoleMapping(String role, String link) {
	}

	public void addSecurityRole(String role) {
	}

	public void addServletMapping(String pattern, String name) {
		synchronized (servletMappings) {
			servletMappings.put(pattern, name);
		}
	}

	public void addTaglib(String uri, String location) {
	}

	public void addWelcomeFile(String name) {
	}

	public void addWrapperLifecycle(String listener) {
	}

	public void addWrapperListener(String listener) {
	}

	public Wrapper createWrapper() {
		return null;
	}

	public String[] findApplicationListeners() {
		return null;
	}

	public ApplicationParameter[] findApplicationParameters() {
		return null;
	}

	public SecurityConstraint[] findConstraints() {
		return null;
	}

	public ContextEjb findEjb(String name) {
		return null;
	}

	public ContextEjb[] findEjbs() {
		return null;
	}

	public ContextEnvironment findEnvironment(String name) {
		return null;
	}

	public ContextEnvironment[] findEnvironments() {
		return null;
	}

	public ErrorPage findErrorPage(int errorCode) {
		return null;
	}

	public ErrorPage findErrorPage(String exceptionType) {
		return null;
	}

	public ErrorPage[] findErrorPages() {
		return null;
	}

	public FilterDef findFilterDef(String filterName) {
		return null;
	}

	public FilterDef[] findFilterDefs() {
		return null;
	}

	public FilterMap[] findFilterMaps() {
		return null;
	}

	public String[] findInstanceListeners() {
		return null;
	}

	public ContextLocalEjb findLocalEjb(String name) {
		return null;
	}

	public ContextLocalEjb[] findLocalEjbs() {
		return null;
	}

	public String findMimeMapping(String extension) {
		return null;
	}

	public String[] findMimeMappings() {
		return null;
	}

	public String findParameter(String name) {
		return null;
	}

	public String[] findParameters() {
		return null;
	}

	public ContextResource findResource(String name) {
		return null;
	}

	public String findResourceEnvRef(String name) {
		return null;
	}

	public String[] findResourceEnvRefs() {
		return null;
	}

	public ContextResourceLink findResourceLink(String name) {
		return null;
	}

	public ContextResourceLink[] findResourceLinks() {
		return null;
	}

	public ContextResource[] findResources() {
		return null;
	}

	public String findRoleMapping(String role) {
		return null;
	}

	public boolean findSecurityRole(String role) {
		return false;
	}

	public String[] findSecurityRoles() {
		return null;
	}

	public String findServletMapping(String pattern) {
		synchronized (servletMappings) {
			return ((String) servletMappings.get(pattern));
		}
	}

	public String[] findServletMappings() {
		return null;
	}

	public String findStatusPage(int status) {
		return null;
	}

	public int[] findStatusPages() {
		return null;
	}

	public String findTaglib(String uri) {
		return null;
	}

	public String[] findTaglibs() {
		return null;
	}

	public boolean findWelcomeFile(String name) {
		return false;
	}

	public String[] findWelcomeFiles() {
		return null;
	}

	public String[] findWrapperLifecycles() {
		return null;
	}

	public String[] findWrapperListeners() {
		return null;
	}

	public void reload() {
	}

	public void removeApplicationListener(String listener) {
	}

	public void removeApplicationParameter(String name) {
	}

	public void removeConstraint(SecurityConstraint constraint) {
	}

	public void removeEjb(String name) {
	}

	public void removeEnvironment(String name) {
	}

	public void removeErrorPage(ErrorPage errorPage) {
	}

	public void removeFilterDef(FilterDef filterDef) {
	}

	public void removeFilterMap(FilterMap filterMap) {
	}

	public void removeInstanceListener(String listener) {
	}

	public void removeLocalEjb(String name) {
	}

	public void removeMimeMapping(String extension) {
	}

	public void removeParameter(String name) {
	}

	public void removeResource(String name) {
	}

	public void removeResourceEnvRef(String name) {
	}

	public void removeResourceLink(String name) {
	}

	public void removeRoleMapping(String role) {
	}

	public void removeSecurityRole(String role) {
	}

	public void removeServletMapping(String pattern) {
	}

	public void removeTaglib(String uri) {
	}

	public void removeWelcomeFile(String name) {
	}

	public void removeWrapperLifecycle(String listener) {
	}

	public void removeWrapperListener(String listener) {
	}

	// methods of the Container interface
	public String getInfo() {
		return null;
	}

	public ClassLoader getParentClassLoader() {
		return null;
	}

	public void setParentClassLoader(ClassLoader parent) {
	}

	public DirContext getResources() {
		return null;
	}

	public void setResources(DirContext resources) {
	}

	public void addContainerListener(ContainerListener listener) {
	}

	@Override
	public void addMapper(Mapper mapper) {
		// This method is adopted from addMapper in ContainerBase
		// the first mapper added becomes the default mapper
		mapper.setContainer((Container) this); // May throw IAE
		this.mapper = mapper;
		synchronized (mappers) {
			if (mappers.get(mapper.getProtocol()) != null)
				throw new IllegalArgumentException("addMapper:  Protocol '" + mapper.getProtocol() + "' is not unique");
			mapper.setContainer((Container) this); // May throw IAE
			mappers.put(mapper.getProtocol(), mapper);
			if (mappers.size() == 1)
				this.mapper = mapper;
			else
				this.mapper = null;
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
	}

	public ContainerListener[] findContainerListeners() {
		return null;
	}

	public Mapper findMapper(String protocol) {
		// The default mapper will always be returned, if any,
		// regardless the value of protocol
		if (mapper != null) {
			return mapper;
		} else {
			synchronized (mappers) {
				return ((Mapper) mappers.get(protocol));
			}
		}
	}

	public Mapper[] findMappers() {
		return null;
	}

	@Override
	public Container map(HttpServletRequest request, boolean update) {
		// This method is taken from the map method in
		// org.apache.cataline.core.ContainerBase.
		// The findMapper method always returns the default mapper, if any, regardless
		// the request's protocol
		Mapper mapper = findMapper(request.getProtocol());

		if (mapper == null) {
			return null;
		}

		// Use this Mapper to perform this mapping
		return mapper.map(request, update);
	}

	public void removeChild(Container child) {
	}

	public void removeContainerListener(ContainerListener listener) {
	}

	public void removeMapper(Mapper mapper) {
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
	}

	private void readWebXml(String contextPath) throws JAXBException, IOException {
		XmlProcessor<WebXml> processor = new XmlProcessor<>(WebXml.class);

		String deploymentDescriptorPath = new File(
				Globals.WEB_APPS + contextPath + File.separator + "WEB-INF" + File.separator + "web.xml")
						.getCanonicalPath();

		WebXml webXml = processor.unmarshall(deploymentDescriptorPath);

		if (webXml.getServlets() != null) {
			for (Servlet servlet : webXml.getServlets()) {
				Wrapper wrapper = new SimpleWrapper();

				wrapper.setName(servlet.getServletName());
				wrapper.setServletClass(servlet.getServletClass());

				addChild(wrapper);
			}
		}

		if (webXml.getServletMappings() != null) {
			for (ServletMapping mapping : webXml.getServletMappings()) {
				addServletMapping(mapping.getUrlPattern(), mapping.getServletName());
			}
		}
	}

	@Override
	public synchronized void start() throws LifecycleException {
		if (started)
			throw new LifecycleException("SimpleContext has already started");

		started = true;

		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(BEFORE_START_EVENT, null);

		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(START_EVENT, null);

		final String contextPath = File.separator + getName();

		setLoader(new SimpleContextLoader(contextPath));

		try {
			readWebXml(contextPath);
		} catch (JAXBException | IOException e) {
			throw new LifecycleException(e);
		}

		// Start our subordinate components, if any
		Loader loader = getLoader();
		if ((loader != null) && (loader instanceof Lifecycle))
			((Lifecycle) loader).start();
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
	}

	@Override
	public synchronized void stop() throws LifecycleException {
		if (!started)
			throw new LifecycleException("SimpleContext has not been started");

		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(BEFORE_STOP_EVENT, null);
		lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);

		started = false;

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

		Loader loader = getLoader();
		if ((loader != null) && (loader instanceof Lifecycle)) {
			((Lifecycle) loader).stop();
		}

		// Notify our interested LifecycleListeners
		lifecycleSupport.fireLifecycleEvent(AFTER_STOP_EVENT, null);
	}

	@Override
	public Manager getManager() {
		return manager;
	}

	@Override
	public void setManager(Manager manager) {
		this.manager = manager;
	}
}
