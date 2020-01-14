package org.rainbow.catalina.loaders;

import org.rainbow.catalina.connector.http.Constants;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

public class LibraryLoader {
    /**
     * Loads all the libraries in the lib directory on the container working directory.
     */
    public void loadAll() throws NoSuchMethodException, MalformedURLException, InvocationTargetException, IllegalAccessException {
        // Get the ClassLoader class
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Class<?> clazz = classLoader.getClass();

        // Get the protected addURL method from the parent URLClassLoader class
        Method method = clazz.getSuperclass().getDeclaredMethod("addURL", new Class[]{URL.class});

        // Run projected addURL method to add JAR to classpath
        method.setAccessible(true);

        File[] jars = getJars();

        for (int i = 0; i < jars.length; i++) {
            method.invoke(classLoader, new Object[]{jars[i].toURI().toURL()});
        }
    }

    private File[] getJars() {
        return new File(Constants.LIB_DIR).listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));
    }
}
