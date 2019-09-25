package com.wl.util.model;

public class Response {
	
	private String status;
	private String message;
	private Object responseObject;
	//private String responseCode;
	
	private Long upi_transaction_count;
	private Long scheme_transaction_count;
	
	
	public Response() {
	}
	
	public Response(String status, String message, Object responseObject) {
		super();
		this.status = status;
		this.message = message;
		this.responseObject = responseObject;
	}
	
	public Response(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getResponseObject() {
		return responseObject;
	}
	public void setResponseObject(Object responseObject) {
		this.responseObject = responseObject;
	}
	
	
	

	public Long getUpi_transaction_count() {
		return upi_transaction_count;
	}

	public void setUpi_transaction_count(Long upi_transaction_count) {
		this.upi_transaction_count = upi_transaction_count;
	}

	public Long getScheme_transaction_count() {
		return scheme_transaction_count;
	}

	public void setScheme_transaction_count(Long scheme_transaction_count) {
		this.scheme_transaction_count = scheme_transaction_count;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Response [status=" + status + ", message=" + message + ", responseObject=" + responseObject + "]";
	}

	/**
	 * @return the responseCode
	 *//*
	public String getResponseCode() {
		return responseCode;
	}

	*//**
	 * @param responseCode the responseCode to set
	 *//*
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}*/

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	
	
	
	
}
