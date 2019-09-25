package com.wl.util.exceptions;

public class ApplicationException extends RuntimeException {
	
	private String message;
	/*
	 *  
	 */
	private static final long serialVersionUID = -6493721670569610775L;
	public ApplicationException(String message) {
		super(message);
		this.message = message;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	

}
