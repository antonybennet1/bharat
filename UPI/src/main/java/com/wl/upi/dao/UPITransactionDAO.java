package com.wl.upi.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wl.upi.model.AggregatorRequest;
import com.wl.upi.model.AggregatorTransDTO;
import com.wl.upi.model.IPGTransactionDTO;
import com.wl.upi.model.MrlDTO;
import com.wl.upi.model.TCHRequest;
import com.wl.upi.model.TxnDTO;
import com.wl.upi.model.UPITransactionDTO;

public interface UPITransactionDAO {

	/**
	 * Following method insert the record in upi_transaction table
	 * @param dataArr data to be passed if following sequence
	 * merch_id, merch_vpa, qr_code_type, txn_id, tr_id, txn_ref_no, txn_date,amount, bankcode, customer_vpa, 
	 * response_code , response_message, from_entity, tid, gateway_trans_id,additional_info1,additional_info2, 
	 * additional_info3, auth_code
	 */
	public void saveUpiTxn(Object[] dataArr);
	
	/**
	 * Get UPI QR and merchant and transaction details based on trId.  This function used in individual PSP implementation
	 * @param trId this is tr of UPI specs
	 * @return the database result
	 */
	public Map<String, Object> getTxnQRDetails(String trId);
	/**
	 * 
	 * @param tchRequest
	 * txnid, bankCode, fromEntity
	 * @return
	 */
	public UPITransactionDTO checkUpiTxn(Object[]  args);
	public int cancelQR(TCHRequest tchRequest);
	public int updateQRStatus(String tr_id, String status);
	public List<UPITransactionDTO>  getQRDetailsForRefund(String bankCode);
	public int updateRefundStatus(String tr_id, String status);
	public UPITransactionDTO checkBharatQRTxn(TCHRequest tchRequest);
	public AggregatorTransDTO checkAggregatorTxn(AggregatorRequest aggregatorRequest);
	
	/**
	 * Get the details of UPI txn based  on trId. This function is used for push notification and aggregator notification
	 * @param trId trId of UPI txn
	 * @return instance of {@link TxnDTO}
	 */
	//public TxnDTO getUpiTxnDetails(String trId);
	
	/**
	 * Get the details of UPI txn based on rrn and tid
	 * @param tid terminal id of upi txn
	 * @param rrn RRN of UPI transaction
	 * @return
	 */
	public TxnDTO getUpiTxnDetails(String tid,String rrn);
	
	/**
	 * Update SettlementFlag (txn_settled_flag) in upi_transaction table
	 * @param flag N flag if settlement is n
	 * @param trId trId of UPI txn
	 * @return
	 */
	public int updateTxnSettlementFlag(String flag, String trId);

	public TxnDTO getUpiTxnDetails(String trId, boolean with_vpa,String rrn);

	Map<String, Object> getTxnQRDetailsFromVpa(String vpa);

	public boolean checkBharatQRTxnExists(String ref_no, String auth_code);
	
	public boolean checkUPITxnExists(String rrn, String trid);

	public int getMerchantVpaExist(String creditVpa);
	
	/*This method will store the transaction which is not succeessfull due to some exception occures
	 * It will store the rrn , jsonRequest, formEntity , exception
	 * */
	public void saveTransactionException(Object[] arr);
	
	public TxnDTO getUpiRefundTxnDetails(String tid, String rrn);
	List<TxnDTO>  getTransactionBtw(Date toDate,Date fromDate,String bankCode);
	
	
	public void saveMRLTxn(Object[] dataArr);

	public MrlDTO getDataForMRLUPI(String refNo,int prgmType, String batchNumber);

	public int sendDataToMRLDB(MrlDTO mrl) throws Exception;
	
	public int updateMrlSettlementFlag(String rrn);

	public MrlDTO getDataForMrlBQR(String refNo,String mpan,int prgmType,String batchNumber,String transactionCurrencyCode);
	
	//public int sendDataToMRLDBRefund(MrlDTO mrl) throws Exception;
	
	//public MrlDTO getDataForMrlRefund(String refNo,String mpan,int prgmType,String batchNumber,String transactionCurrencyCode);
	
	public Map<String, Double> checkRefundIdExist(String rrn, String trid);
	
}
