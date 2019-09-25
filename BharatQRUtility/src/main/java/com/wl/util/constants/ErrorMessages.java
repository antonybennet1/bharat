package com.wl.util.constants;

public enum ErrorMessages {
	JSON_FORMAT_ERROR("Invalid Json Format"),
	INVALID_AMOUNT("Invalid Amount"), 
	PROGRAM_NOT_FOUND("Program Type not found"), 
	Invalid_PRG_TYPE("Invalid Program Type"), 
	BHARATQR_UNSUPPORTED("Bharat QR not supported"), 
	UPI_UNSUPPORTED("UPI not supported"), 
	BHARATQR_UPI_UNSUPPORTED("Bharat QR + UPI not supported"),
	STRING_LENGTH_EXCEEDED("Invalid Length"),
	SERVER_ERROR("Server Internal Error"), 
	INVALID_PSP("Invalid Bank PSP"),
	TXN_QR_DETAIL_NOT_FOUND("QR Details not found"),
	CHECKSUM_NOT_MATCH("Checksum for the request is not valid"), 
	ORG_TXN_NOT_FOUND("Transaction not found"), 
	REFUND_EXCEEDED("Refund Amount exceeds Transaction amount"),
	REFUND_ALREADY("Refund has already completed for this transaction"),
	A0("Transaction not found"),
	A1("PAN not found"),
	A2("Invalid PAN"),
	A3("Invalid RRN"),
	MISSING_MANDATORY_FIELDS("Missing Mandatory Fields"), 
	REFUND_UNSUPPORTED("Refund not supported"), 
	SMS_UNSUPPORTED("SMS vendor not configured for bank"), 
	MERCHANT_MAPPING_NOT_FOUND("Merchant Mapping not found"), 
	TXN_ALREADY_DONE("Transaction for qr code already done"),
	DUPLICATE_TXN("Duplicate Transaction"),
	MERCHANT_VPA_NOT_FOUND("Merchant VPA not found"),
	REFUNDID_LENGTH("RefundID is less then 4 charaters"),
	TRANSACTION_ID_EMPTY("Transaction id is empty"),
	DATE_PARSING_ERROR("INVALID DATE FORMAT"),
	INPUT_NOT_VALID("Input Params is not valid"),
	ADDITIONAL_EMPTY_KLIF("Additional data is empty");
	
	
	private String value;
	private ErrorMessages(String value) {
		this.value = value;
	}
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return value;
	}
}