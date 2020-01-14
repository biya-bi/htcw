package org.rainbow.catalina.startup;

import org.rainbow.catalina.connector.http.HttpConnector;
import org.rainbow.catalina.loaders.LibraryLoader;

public final class Bootstrap {
    static {
        try {
            new LibraryLoader().loadAll();
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        HttpConnector connector = new HttpConnector();
        connector.start();
    }
}
