package com.wl.upi.service;

import javax.servlet.http.HttpServletRequest;

/**
 * PSPTransactionService is the interface exposed for implementation of each and every bank PSP callback API. For each Bank separate implementation has to be present.
 * PSPServiceFactory will then generate instance of this implementation based on PSP name 
 * @author kunal.surana
 * @see PSPServiceFactory
 */
public interface PSPTransactionService {

	/**
	 * Function must be implemented based on Bank PSP integration document or WL specified document for callback service.  
	 * @param fromEntity this will name of bank psp as present in WL URL
	 * @param req this is {@link HttpServletRequest}
	 * @return returns the hashmap object which will be be convert to json using jackson. 
	 */
	public Object upiTransactionCallback(String fromEntity, HttpServletRequest req);
}
