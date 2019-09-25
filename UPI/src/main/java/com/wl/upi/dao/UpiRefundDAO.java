package com.wl.upi.dao;

/**
 * @author kunal.surana
 *
 */
public interface UpiRefundDAO {
	
	
	/**
	 * inserts in table upi_refund
	 * @param args args should be in  following order:
tr_id       
mobile_number
refund_amount
refund_id    
refund_reason
txn_ref_no   
txn_amount   
bankcode     
merch_id     
TID          
customer_vpa 
from_entity  
created      
updated      
	 */
	public void saveRefundTransaction(Object[] args);
	
	public boolean checkRefundExist(String trId,String bankCode);
	
	double getSumForRefundedAmount(String rrn,String bankCode);
}
