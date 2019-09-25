package com.wl.bharatqr.model;

import java.util.Map;

public class AmexFields {

	private String type;
	private String primary_account_number;
	private String full_pan;
	private String expiry_date;
	private String processing_code;
	private String transaction_amount;
	private String tip_amount;
	private String transaction_time;
	private String system_trace_number;
	private String transaction_time_local;
	private String reconciliation_date;
	private String acquirer_reference_data;
	private String retrieval_reference_number;
	private String acquirer_id;
	private String approval_code;
	private String action_code;
	private String card_acceptor_id;
	private String card_acceptor_id_type;
//	private Enum card_acceptor_id_type;
	private String card_acceptor_name;
	private String currency_code;
	private String pos_dc;
	private Map<String,String> additional_data;
	private String authorization;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrimary_account_number() {
		return primary_account_number;
	}
	public void setPrimary_account_number(String primary_account_number) {
		this.primary_account_number = primary_account_number;
	}
	public String getFull_pan() {
		return full_pan;
	}
	public void setFull_pan(String full_pan) {
		this.full_pan = full_pan;
	}
	public String getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(String expiry_date) {
		this.expiry_date = expiry_date;
	}
	public String getProcessing_code() {
		return processing_code;
	}
	public void setProcessing_code(String processing_code) {
		this.processing_code = processing_code;
	}
	public String getTransaction_amount() {
		return transaction_amount;
	}
	public void setTransaction_amount(String transaction_amount) {
		this.transaction_amount = transaction_amount;
	}
	public String getTip_amount() {
		return tip_amount;
	}
	public void setTip_amount(String tip_amount) {
		this.tip_amount = tip_amount;
	}
	public String getTransaction_time() {
		return transaction_time;
	}
	public void setTransaction_time(String transaction_time) {
		this.transaction_time = transaction_time;
	}
	public String getSystem_trace_number() {
		return system_trace_number;
	}
	public void setSystem_trace_number(String system_trace_number) {
		this.system_trace_number = system_trace_number;
	}
	public String getTransaction_time_local() {
		return transaction_time_local;
	}
	public void setTransaction_time_local(String transaction_time_local) {
		this.transaction_time_local = transaction_time_local;
	}
	public String getReconciliation_date() {
		return reconciliation_date;
	}
	public void setReconciliation_date(String reconciliation_date) {
		this.reconciliation_date = reconciliation_date;
	}
	public String getAcquirer_reference_data() {
		return acquirer_reference_data;
	}
	public void setAcquirer_reference_data(String acquirer_reference_data) {
		this.acquirer_reference_data = acquirer_reference_data;
	}
	public String getRetrieval_reference_number() {
		return retrieval_reference_number;
	}
	public void setRetrieval_reference_number(String retrieval_reference_number) {
		this.retrieval_reference_number = retrieval_reference_number;
	}
	public String getAcquirer_id() {
		return acquirer_id;
	}
	public void setAcquirer_id(String acquirer_id) {
		this.acquirer_id = acquirer_id;
	}
	public String getApproval_code() {
		return approval_code;
	}
	public void setApproval_code(String approval_code) {
		this.approval_code = approval_code;
	}
	public String getAction_code() {
		return action_code;
	}
	public void setAction_code(String action_code) {
		this.action_code = action_code;
	}
	public String getCard_acceptor_id() {
		return card_acceptor_id;
	}
	public void setCard_acceptor_id(String card_acceptor_id) {
		this.card_acceptor_id = card_acceptor_id;
	}
	/*public Enum getCard_acceptor_id_type() {
		return card_acceptor_id_type;
	}
	public void setCard_acceptor_id_type(Enum card_acceptor_id_type) {
		this.card_acceptor_id_type = card_acceptor_id_type;
	}*/
	
	public String getCard_acceptor_name() {
		return card_acceptor_name;
	}
	public String getCard_acceptor_id_type() {
		return card_acceptor_id_type;
	}
	public void setCard_acceptor_id_type(String card_acceptor_id_type) {
		this.card_acceptor_id_type = card_acceptor_id_type;
	}
	public void setCard_acceptor_name(String card_acceptor_name) {
		this.card_acceptor_name = card_acceptor_name;
	}
	public String getCurrency_code() {
		return currency_code;
	}
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
	public String getPos_dc() {
		return pos_dc;
	}
	public void setPos_dc(String pos_dc) {
		this.pos_dc = pos_dc;
	}
	public Map<String, String> getAdditional_data() {
		return additional_data;
	}
	public void setAdditional_data(Map<String, String> additional_data) {
		this.additional_data = additional_data;
	}
	public String getAuthorization() {
		return authorization;
	}
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
	
	
	
}
