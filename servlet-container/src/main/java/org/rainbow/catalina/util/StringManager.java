package org.rainbow.catalina.util;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

public class StringManager {
    private static final Hashtable<String, StringManager> managers = new Hashtable<>();
    private final ResourceBundle bundle;

    private StringManager(String packageName) {
        String bundleName = packageName + ".LocalStrings";
        bundle = ResourceBundle.getBundle(bundleName);
    }

    public static synchronized StringManager getManager(String packageName) {
        StringManager manager = managers.get(packageName);
        if (manager == null) {
            manager = new StringManager(packageName);
            managers.put(packageName, manager);
        }
        return manager;
    }

    /**
     * Get a string from the underlying resource bundle.
     *
     * @param key
     */
    public String getString(String key) {
        Objects.requireNonNull(key, "key is null");

        String str = null;

        try {
            str = bundle.getString(key);
        } catch (MissingResourceException mre) {
            str = "Cannot find message associated with key '" + key + "'";
        }

        return str;
    }

    /**
     * Get a string from the underlying resource bundle and format
     * it with the given set of arguments.
     *
     * @param key
     * @param args
     */
    public String getString(String key, Object... args) {
        String iString = null;
        String value = getString(key);

        // This check for the runtime exception is some pre 1.1.6
        // VMs don't do an automatic toString() on the passed in
        // objects and barf out.

        try {
            // Ensure the arguments are not null so pre 1.2 VMs don't barf.
            Object nonNullArgs[] = args;
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) {
                    if (nonNullArgs == args)
                        nonNullArgs = (Object[]) args.clone();
                    nonNullArgs[i] = "null";
                }
            }

            iString = MessageFormat.format(value, nonNullArgs);
        } catch (IllegalArgumentException iae) {
            StringBuffer buf = new StringBuffer();
            buf.append(value);
            for (int i = 0; i < args.length; i++) {
                buf.append(" args[" + i + "]=" + args[i]);
            }
            iString = buf.toString();
        }
        return iString;
    }

    /**
     * Get a string from the underlying resource bundle and format it
     * with the given object argument. This argument can of course be
     * a String object.
     *
     * @param key
     * @param arg
     */
    public String getString(String key, Object arg) {
        Object[] args = new Object[]{arg};
        return getString(key, args);
    }

}
