package com.wl.bharatqr.model;

public class AmexRefundFields {
	private String retrieval_reference;
	private String merchant_id;
	private MerchantAddress	merchant_address;
	private String currency_code;
	private String amount;
	private String merchant_country_code;
	private String merchant_category_code;
	private String nonce;
	private String account_data;
	private String local_timestamp;
	private String acquirer_country_code;
	private String acquirer_id;
	private String recon_date;
	
	public String getRetrieval_reference() {
		return retrieval_reference;
	}
	public void setRetrieval_reference(String retrieval_reference) {
		this.retrieval_reference = retrieval_reference;
	}
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}
	public MerchantAddress getMerchant_address() {
		return merchant_address;
	}
	public void setMerchant_address(MerchantAddress merchant_address) {
		this.merchant_address = merchant_address;
	}
	public String getCurrency_code() {
		return currency_code;
	}
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getMerchant_country_code() {
		return merchant_country_code;
	}
	public void setMerchant_country_code(String merchant_country_code) {
		this.merchant_country_code = merchant_country_code;
	}
	public String getMerchant_category_code() {
		return merchant_category_code;
	}
	public void setMerchant_category_code(String merchant_category_code) {
		this.merchant_category_code = merchant_category_code;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getAccount_data() {
		return account_data;
	}
	public void setAccount_data(String account_data) {
		this.account_data = account_data;
	}
	public String getLocal_timestamp() {
		return local_timestamp;
	}
	public void setLocal_timestamp(String local_timestamp) {
		this.local_timestamp = local_timestamp;
	}
	public String getAcquirer_country_code() {
		return acquirer_country_code;
	}
	public void setAcquirer_country_code(String acquirer_country_code) {
		this.acquirer_country_code = acquirer_country_code;
	}
	public String getAcquirer_id() {
		return acquirer_id;
	}
	public void setAcquirer_id(String acquirer_id) {
		this.acquirer_id = acquirer_id;
	}
	public String getRecon_date() {
		return recon_date;
	}
	public void setRecon_date(String recon_date) {
		this.recon_date = recon_date;
	}
	
	

}
