package com.wl.upi.model;

public class CbiRefundResponse {

	private String responseCode;
	private String responseStatus;
	private String txnId;
	private String txnRefundId;
	
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getTxnRefundId() {
		return txnRefundId;
	}
	public void setTxnRefundId(String txnRefundId) {
		this.txnRefundId = txnRefundId;
	}
	@Override
	public String toString() {
		return "CbiRefundResponse [responseCode=" + responseCode + ", responseStatus=" + responseStatus + ", txnId="
				+ txnId + ", txnRefundId=" + txnRefundId + "]";
	}
	public CbiRefundResponse(String responseCode, String responseStatus, String txnId, String txnRefundId) {
		super();
		this.responseCode = responseCode;
		this.responseStatus = responseStatus;
		this.txnId = txnId;
		this.txnRefundId = txnRefundId;
	}
	
	
public String showStatus(String responseCode){
		
		//CbiRefundResponse cbiRefRes = null;
		 if(responseCode.equals("00")){
			 return  "00" + "Success";
		 }
		 else{
			   return "01" + "Failure";
		 }
		
	}
	
	
	
}
