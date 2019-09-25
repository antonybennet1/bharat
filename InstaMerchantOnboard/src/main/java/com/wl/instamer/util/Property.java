package com.wl.instamer.util;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Property {

	private static final Logger logger = LoggerFactory.getLogger(Property.class);
	private static String axisBankMandateFields;
	private static String kotakBankMandateFields;

	static {
		load();
	}

	public static void load() {	

		try {
			Properties ep = new Properties();
			InputStream in = Property.class.getResourceAsStream("/application.properties");
			ep.load(in);
			axisBankMandateFields = ep.getProperty("axisBankMandateFields");
			kotakBankMandateFields = ep.getProperty("kotakBankMandateFields");	
			logger.debug("Property file loaded");
		}
		catch (Exception e) {
			logger.debug("Couldn't access properties file:/application.properties from classpath"+e);
		}
	}

	public static String getAxisBankMandateFields() {
		return axisBankMandateFields;
	}

	public static String getKotakBankMandateFields() {
		return kotakBankMandateFields;
	}

}
