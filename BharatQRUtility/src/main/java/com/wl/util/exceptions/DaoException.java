package com.wl.util.exceptions;

import org.springframework.dao.DataAccessException;

public class DaoException extends DataAccessException {
	
	private String message;
	/*
	 *  
	 */
	private static final long serialVersionUID = -6493721670569610775L;
	public DaoException(String message) {
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
