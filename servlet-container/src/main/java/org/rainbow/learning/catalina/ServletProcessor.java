package org.rainbow.learning.catalina;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class ServletProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ServletProcessor.class);

    public void process(Request request, Response response) {
        try {
            Class servletClass = loadServlet(request.getUri());
            Servlet servlet = (Servlet) servletClass.newInstance();
            servlet.service((ServletRequest) request, (ServletResponse) response);
        } catch (IllegalAccessException | InstantiationException | ServletException | IOException | ClassNotFoundException e) {
            logger.error("An expected error occurred while processing a request.", e);
        }
    }

    private Class loadServlet(String uri) throws IOException, ClassNotFoundException {
        String servletName = getServletName(uri);

        logger.info("Loading the '{}' servlet.", servletName);

        URL[] urls = new URL[1];
        URLStreamHandler streamHandler = null;
        File classPath = new File(Constants.WEB_ROOT);
        String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
        urls[0] = new URL(null, repository, streamHandler);

        URLClassLoader loader = new URLClassLoader(urls);

        Class servletClass = loader.loadClass(servletName);

        logger.info("The '{}' servlet was loaded successfully.", servletName);

        return servletClass;
    }

    private String getServletName(String uri) {
        // The assumption here is that the servlet name is everything after the /servlet/ prefix.
        String prefix = "/servlet/";
        int index = uri.indexOf(prefix);
        String name = uri.substring(index + prefix.length());
        String slash = "/";
        while (name.lastIndexOf(slash) == name.length() - 1) {
            name = name.substring(0, name.length() - 1);
        }
        return name.replace(slash, ".");
    }
}
