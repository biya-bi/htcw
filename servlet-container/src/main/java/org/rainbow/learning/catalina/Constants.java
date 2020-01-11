package org.rainbow.learning.catalina;

import java.io.File;

public class Constants {
    private static final String WORKING_DIR = System.getProperty("user.home") + File.separator + "learning" + File.separator + "tomcat";

    public static final String WEB_ROOT = WORKING_DIR + File.separator + "webroot";
    public static final String LIB_DIR = WORKING_DIR + File.separator + "lib";
}
