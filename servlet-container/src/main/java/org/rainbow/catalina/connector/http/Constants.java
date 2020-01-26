package org.rainbow.catalina.connector.http;

import java.io.File;

public class Constants {
	private static final String WORKING_DIR = System.getProperty("user.home") + File.separator + "learning"
			+ File.separator + "tomcat";

	public static final String WEB_APPS = WORKING_DIR + File.separator + "webapps";
	public static final String LIB_DIR = WORKING_DIR + File.separator + "lib";
	public static final String PACKAGE = "org.rainbow.catalina.connector.http";
}
