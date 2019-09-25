package com.wl.recon.util;

public interface QueryConstants {
	
	String GET_FILE_DETAILS="select u.TID, u.merch_id, u.txn_ref_no, u.gateway_trans_id, u.txn_date, u.amount, d.merchant_category_code, u.response_code from upi_transaction u inner join detail d on d.TID=u.TID where u.bank_code=? and txn_date between ? and ?";
}
