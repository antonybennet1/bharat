package com.wl.upi.dao;

import com.wl.upi.model.BharatQRTransDTO;
import com.wl.upi.model.MMSTransDTO;
import com.wl.upi.model.TxnDTO;

public interface BharatQRTransactionDao {
	
	public TxnDTO checkBharatQRTrans(String txnId,Integer id);
	public TxnDTO checkBharatQRTrans(String rrn,String authCode,String tid);
	public void saveBharatQRRefundTxn(Object[] dataArr);
	public void updateRefundedAmount(int id, double amount);
	public void saveBharatQRTxn(BharatQRTransDTO bharatQRTxn);
	
	public int updateBQRMMSFlag(String RRN,String Flag);
	public int updateBQRMRLFlag(String RRN,String Flag);
	public int updateUPIMRLFlag(String RRN,String Flag);
	
	public int insertIpgRequest(String jsonRequest);
	
	public void saveMMSTxn(MMSTransDTO bharatQRTxn);
	
	public boolean getRefundId(String rrn,String authCode,String tid,String refundId);
	
	public Long getUPITransactionCount();
	public Long getSchemeTransactionCount();
	
	
}
