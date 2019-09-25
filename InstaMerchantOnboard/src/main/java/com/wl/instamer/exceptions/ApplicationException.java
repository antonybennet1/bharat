package com.wl.instamer.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2846038015828453965L;
	private static final Logger logger = LoggerFactory.getLogger(ApplicationException.class);

	public ApplicationException() {
		// TODO Auto-generated constructor stub
	}

	public ApplicationException(String message) {
		super(message);
		logger.error("Application Exception with message +"+message);
		// TODO Auto-generated constructor stub
	}

	public ApplicationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
		logger.error("Application Exception",cause);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
		logger.error("Application Exception with message +"+message,cause);
	}

	public ApplicationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		logger.error("Application Exception with message +"+message,cause);
		// TODO Auto-generated constructor stub
	}

}
