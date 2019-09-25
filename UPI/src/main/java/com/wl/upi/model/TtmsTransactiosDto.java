package com.wl.upi.model;

public class TtmsTransactiosDto {

	private String class_name;
	private String function;
	private String 	mpan;
	
	private String customer_pan;
	private String customer_name;
	private String txn_currency;
	
	private String auth_code;
	private String ref_no;
	private String primary_id;
	private String secondary_id;
	private String settlement_amount;
	
	private String time_stamp;
	private String transaction_type;
	private String bank_code;
	private String additional_data;
	private String batch_number;
	private String scheme_transaction_id;
	
	
	
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getMpan() {
		return mpan;
	}
	public void setMpan(String mpan) {
		this.mpan = mpan;
	}
	public String getCustomer_pan() {
		return customer_pan;
	}
	public void setCustomer_pan(String customer_pan) {
		this.customer_pan = customer_pan;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getTxn_currency() {
		return txn_currency;
	}
	public void setTxn_currency(String txn_currency) {
		this.txn_currency = txn_currency;
	}
	public String getAuth_code() {
		return auth_code;
	}
	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}
	public String getRef_no() {
		return ref_no;
	}
	public void setRef_no(String ref_no) {
		this.ref_no = ref_no;
	}
	public String getPrimary_id() {
		return primary_id;
	}
	public void setPrimary_id(String primary_id) {
		this.primary_id = primary_id;
	}
	public String getSecondary_id() {
		return secondary_id;
	}
	public void setSecondary_id(String secondary_id) {
		this.secondary_id = secondary_id;
	}
	public String getSettlement_amount() {
		return settlement_amount;
	}
	public void setSettlement_amount(String settlement_amount) {
		this.settlement_amount = settlement_amount;
	}
	public String getTime_stamp() {
		return time_stamp;
	}
	public void setTime_stamp(String time_stamp) {
		this.time_stamp = time_stamp;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	public String getBank_code() {
		return bank_code;
	}
	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}
	public String getAdditional_data() {
		return additional_data;
	}
	public void setAdditional_data(String additional_data) {
		this.additional_data = additional_data;
	}
	public String getBatch_number() {
		return batch_number;
	}
	public void setBatch_number(String batch_number) {
		this.batch_number = batch_number;
	}
	public String getScheme_transaction_id() {
		return scheme_transaction_id;
	}
	public void setScheme_transaction_id(String scheme_transaction_id) {
		this.scheme_transaction_id = scheme_transaction_id;
	}
	
	@Override
	public String toString() {
		return "TtmsTransactiosDto [class_name=" + class_name + ", function=" + function + ", mpan=" + mpan
				+ ", customer_pan=" + customer_pan + ", customer_name=" + customer_name + ", txn_currency="
				+ txn_currency + ", auth_code=" + auth_code + ", ref_no=" + ref_no + ", primary_id=" + primary_id
				+ ", secondary_id=" + secondary_id + ", settlement_amount=" + settlement_amount + ", time_stamp="
				+ time_stamp + ", transaction_type=" + transaction_type + ", bank_code=" + bank_code
				+ ", additional_data=" + additional_data + ", batch_number=" + batch_number + ", scheme_transaction_id="
				+ scheme_transaction_id + "]";
	}
}
