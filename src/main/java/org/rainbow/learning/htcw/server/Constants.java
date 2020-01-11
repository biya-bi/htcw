package org.rainbow.learning.htcw.server;

import java.io.File;

public class Constants {
    private static final String WORKING_DIR = System.getProperty("user.dir") + File.separator + "target" + File.separator + "tomcat";

    public static final String WEB_ROOT = WORKING_DIR + File.separator + "webroot";
    public static final String LIB_DIR = WORKING_DIR + File.separator + "lib";
}
