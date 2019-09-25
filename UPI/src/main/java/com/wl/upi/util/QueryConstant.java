package com.wl.upi.util;

public interface QueryConstant {

	String CHECK_UPI_TXN = "SELECT qrHistory.merch_id , qrHistory.merch_vpa , qrHistory.qr_type , qrHistory.txn_id , qrHistory.bank_code , qrHistory.terminal_id , "
			+ "qrHistory.amount, qrHistory.from_entity, upiTxn.txn_ref_no , upiTxn.customer_vpa, upiTxn.tr_id FROM upi_transaction upiTxn JOIN qr_history qrHistory ON upiTxn.tr_id = qrHistory.tr_id "
			+ "WHERE qrHistory.txn_id = ? AND upiTxn.bank_code=?";

	String SET_STATUS_CANCEL = "UPDATE qr_history SET status= 'C' WHERE merch_id=? "
			+ " AND txn_id=? AND bank_code=? AND from_entity=? AND terminal_id=?";
	
	String GET_TXN_QR_DETAILS = "select `merch_id` , `merch_vpa` , `qr_type` , `txn_id` , `bank_code` , `terminal_id` ,"
			+ " `amount`, `from_entity` from qr_history where tr_id = ?";
	
	String GET_TXN_QR_DETAILS_FROM_VPA = "select `merchant_id` , `upi_vpa` , `bank_code` , `TID` from detail where upi_vpa = ?";
	
	String INSERT_UPI_TXN = "INSERT INTO upi_transaction (merch_id, merch_vpa, qr_code_type, txn_id, tr_id, txn_ref_no, txn_date,"
			+ " amount, bank_code, customer_vpa,  response_code , response_message, from_entity, tid, "
			+ "gateway_trans_id,additional_info1,additional_info2, additional_info3, auth_code, txn_settled_flag, created, updated) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Y',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
	
	String  SET_QR_STATUS = "UPDATE qr_history SET status = ?, updated = CURRENT_TIMESTAMP WHERE tr_id = ?";
	
	String INSERT_UPI_REFUND = "insert into upi_refund(tr_id, mobile_number, refund_amount, refund_id, refund_reason, txn_ref_no, txn_amount,"
			+ " bank_code, merch_id, TID, customer_vpa, from_entity, resp_code, resp_msg , created, updated,reqRefundId,merch_vpa) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,?,?)";
	
	String GET_TXN_FOR_REFUND = "select q.`merch_id` , q.`merch_vpa` , q.`qr_type` , q.`txn_id` , q.`bank_code` , "
			+ " q.`amount`, q.`from_entity` , q.`tr_id` , u.txn_ref_no , u.TID from qr_history q join upi_transaction u on q.tr_id = u.tr_id where status  <>  'S' and refund_status = 'N'  and q.bank_code = ?";
	
	String  SET_REFUND_STATUS = "UPDATE qr_history SET refund_status = ?, updated = CURRENT_TIMESTAMP WHERE tr_id = ?";
	
	String CHECK_UPI_REFUND = "select count(1) from  upi_refund where tr_id = ? and bank_code =?";
	
	String CHECK_BHARATQR_TXN  = "select ref_no , auth_code from transaction_detail t where t.primary_id  like ? or t.secondary_id = ?";
	
	String CHECK_BHARATQR_TXN_EXIST  = "select * from transaction_detail t where t.ref_no = ? and t.auth_code = ?";
	
	String CHECK_UPI_TXN_EXIST  = "select * from upi_transaction t where t.tr_id = ? and t.txn_ref_no = ?";
	
	String CHECK_UPI_CREDIT_VPA_EXIST  = "select * from detail d where d.upi_vpa = ? ";
	
	String CHECK_BHARATQR_AGGRE_TXN="select d.merchant_id as mid, mvisa_merchant_id as mpan , customer_name , t.currency_code , amount , auth_code , ref_no , primary_id , secondary_id , settlement_amount ,date as time_stamp , transaction_type , t.bank_code , customer_pan from transaction_detail t JOIN detail d on t.tid = d.tid where t.primary_id = ? or t.secondary_id = ? order by date desc limit 1";
	
	String CHECK_UPI_AGGRE_TXN="select merch_id as mid , amount , auth_code , txn_ref_no , tr_id as primary_id , amount as settlement_amount , txn_date as time_stamp , '1' as txn_type , bank_code, merch_vpa, customer_vpa, response_code from upi_transaction where tr_id = ? and TID = ? order by created desc limit 1 ";
	
	String AGGR_DETAILS = "select a.aggregator_id , a.name , a.url from aggregator_details a join merchant_aggregator_link m on a.aggregator_id = m.aggregator_id where m.mvisa_merchant_id = ?";

	String DEVICE_DETAILS = "SELECT md.`mobile_number`, md.device_id, md.platform , mm.is_inapp_activated , mm.is_primary FROM `mobile_details_mvisa_id_details_link` mm JOIN mobile_details md ON md.mobile_details_id=mm.mobile_details_id join `mvisa_id_details` t on t.mvisa_id_details_id = mm.mvisa_id_details_id where t.mvisa_id = ?";

	String GET_UPI_TXN_JOIN_QR_HISTORY = "select  u.`merch_id` , u.`merch_vpa` , u.`bank_code` , u.`txn_id`, u.`TID` ,u.`amount` ,  u.`from_entity` , u.`txn_ref_no`, u.`auth_code`, "
			+ "u.`txn_date`, u.`customer_vpa`,  u.`tr_id`, u.`response_code`, u.`response_message`, u.`created` , q.program_type , q.qr_type from upi_transaction u JOIN qr_history q on u.tr_id = q.tr_id where u.tr_id = ? or u.txn_ref_no = ? and (u.response_code = '00' or u.response_code = '000') order by `created` desc limit 1";
	
	/*String GET_UPI_TXN_DETAILS_VPA = "select  u.`merch_id` , u.`merch_vpa` , u.`bank_code` , u.`txn_id`, u.`TID` ,u.`amount` ,  u.`from_entity` , u.`txn_ref_no`, u.`auth_code`, "
			+ "u.`txn_date`, u.`customer_vpa`,  u.`tr_id`, u.`response_code`, u.`response_message`, u.`created`,  '2' as program_type, '2' as qr_type from upi_transaction u where u.tr_id = ? and u.response_code = '00' order by `created` desc limit 1";
*/

	/*String GET_UPI_TXN_BY_RRN_TR_ID = "select  u.`merch_id` , u.`merch_vpa` , u.`bank_code` , u.`txn_id`, u.`TID` ,u.`amount` ,  u.`from_entity` , u.`txn_ref_no`, u.`auth_code`, "
	+ "u.`txn_date`, u.`customer_vpa`,  u.`tr_id`, u.`response_code`, u.`response_message`, u.`created`,  '2' as program_type, '2' as qr_type from upi_transaction u where u.tr_id = ? and u.txn_ref_no = ?  and (u.response_code = '00' or u.response_code = '000') order by `created` desc limit 1";
*/
/**@pranav
* select program_type from detail table with joining TID from upi_transaction table
* */
String GET_UPI_TXN_BY_RRN_TR_ID = "select  d.program_type,u.`merch_id` , u.`merch_vpa` , u.`bank_code` , u.`txn_id`, u.`TID` ,u.`amount` ,  u.`from_entity` , u.`txn_ref_no`, u.`auth_code`, "
	+ "u.`txn_date`, u.`customer_vpa`,  u.`tr_id`, u.`response_code`, u.`response_message`, u.`created`,'2' as qr_type from upi_transaction u inner join detail d on d.TID=u.TID where u.tr_id = ? and u.txn_ref_no = ?  and (u.response_code = '00' or u.response_code = '000') order by `created` desc limit 1";

	
	String INSERT_SETTLEMENT_TXN = "insert into MOB_PAYMENT_TXN (P_ID,P_TXN_CHANNEL , P_MERCHANTID , "
			+ "P_TERMINALID , P_BATCH_NUMBER , P_RETRIVAL_REF_NUMBER , P_REQUEST_TYPE , P_DATE , P_STAN_NUMBER , P_AUTH_ID , "
			+ "P_RESPONSE_CODE , P_INVOICENUMBER , P_BRANCH_CODE , P_REFERENCE_VALUE , P_ORIGINAL_AMOUNT ,  "
			+ "P_CURRENCY_CODE , P_ADDAMOUNT ,  P_TIP_APPROVED , P_EXPIRYDATE , P_CARD_ENTRYMODE ,  P_PROCESSING_CODE , "
			+ "P_BANK_CODE , P_MTI ,P_EXTRA_INFO, P_SETTLEMENT_FLAG , P_CREATED , P_UPDATED, P_MERCHANT_VPA, P_CUST_VPA ,P_FIELD63 ) values (SEQ_MOB_SETTLEMENT_ID.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Y',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,?,?,?)";
	
	String INSERT_SETTLEMENT_TXN_P7 = "insert into MOB_PAYMENT_TXN_P7 (P_ID,P_TXN_CHANNEL , P_MERCHANTID , "
			+ "P_TERMINALID , P_BATCH_NUMBER , P_RETRIVAL_REF_NUMBER , P_REQUEST_TYPE , P_DATE , P_STAN_NUMBER , P_AUTH_ID , "
			+ "P_RESPONSE_CODE , P_INVOICENUMBER , P_BRANCH_CODE , P_REFERENCE_VALUE , P_ORIGINAL_AMOUNT ,  "
			+ "P_CURRENCY_CODE , P_ADDAMOUNT ,  P_TIP_APPROVED , P_EXPIRYDATE , P_CARD_ENTRYMODE ,  P_PROCESSING_CODE , "
			+ "P_BANK_CODE , P_MTI ,P_EXTRA_INFO, P_SETTLEMENT_FLAG , P_CREATED , P_UPDATED, P_MERCHANT_VPA, P_CUST_VPA ,P_FIELD63 ) values (SEQ_MOB_SETTLEMENT_ID.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Y',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,?,?,?)";
	
	
	String  SET_SETTLEMENT_FLAG = "UPDATE upi_transaction SET txn_settled_flag = ?, updated = CURRENT_TIMESTAMP WHERE tr_id = ?";

	String UPDATE_BATCH_NUMBER = "update upi_batch_number set batch_number =  batch_number + 1 , updated = CURRENT_TIMESTAMP";
	
	String GET_BATCH_NUMBER = "select batch_number from upi_batch_number";
	
	String CHECK_BHARATQR_TRANSACTION  = "select id , merchant_id , mvisa_merchant_id,customer_pan,customer_name,currency_code,amount,"
			+ "auth_code,ref_no ,primary_id,secondary_id,settlement_amount,date,transaction_type,bank_code , refunded_amount, tid from transaction_detail t where (t.primary_id  like ? or t.secondary_id like ?)";
	
	/*String INSERT_BHARATQR_REFUND_TRANS="insert into upi_refund(tr_id, mobile_number , refund_amount ,  refund_id , refund_reason , txn_ref_no ,"
			+ " txn_amount , bank_code , merch_id , TID ,  from_entity , qr_code_type , txn_type ,  mpan, customer_pan  , resp_code , resp_msg) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	*/
	String INSERT_BHARATQR_REFUND_TRANS="INSERT INTO refund_transaction (id,mvisa_merchant_id,date,currency_code,amount,transaction_amount,customer_name,customer_pan,"
					+ "refund_status,auth_code,ref_no,primary_id,secondary_id,last_update_on,tid,bank_code,reqRefundId,newRrn)"
					+ " value(?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?,?,?,?)";

	String UPDATE_REFUNDED_AMOUNT = "update transaction_detail set refunded_amount = ? where id = ?";
	
	String GET_MERCHANT_DETAIL_MPAN = "select merchant_id ,  program_type , tid , can_refund,merchant_category_code from detail where TID=(SELECT TID FROM merchant_group WHERE merchant_pan = ?)";
	
	String GET_MERCHANT_DETAIL_TID = "select merchant_id ,  program_type , tid , can_refund , merchant_category_code from detail where TID=?";
	
   // String GET_MERCHANT_DETAIL_TID = "select merchant_id ,  program_type , tid , can_refund , mcc from detail inner join contact_details on detail.id = contact_details.id;
	
	String INSERT_BHARATQR_TXN = "INSERT INTO transaction_detail (mvisa_merchant_id,date,currency_code,amount,customer_name,"
			+ "customer_pan,auth_code,ref_no,primary_id,secondary_id,settlement_amount,transaction_type,TID,bank_code,additional_data, merchant_id) "
			+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	String CHECK_BHARATQR_TRANSACTION_ON_ID  = "select id , merchant_id , mvisa_merchant_id,customer_pan,customer_name,currency_code,amount,"
			+ "auth_code,ref_no ,primary_id,secondary_id,settlement_amount,date,transaction_type,bank_code , refunded_amount , tid from transaction_detail t where t.id = ?";

	String CHECK_BHARATQR_TXN_RRN = "select id , merchant_id , mvisa_merchant_id,customer_pan,customer_name,currency_code,amount,"
			+ "auth_code,ref_no ,primary_id,secondary_id,settlement_amount,date,transaction_type,bank_code , refunded_amount , tid from transaction_detail t where t.ref_no = ? and t.auth_code = ? and t.tid = ?";

	String SAVE_SMS_LOGS = "INSERT INTO sms_logs(feedid, sms_to,sms_text,sms_response_code,sms_response_message,bank_code) VALUES (?,?,?,?,?,?)";
	//added one field gateway_trans_id
	String GET_UPI_TXN_DETAILS_RRN = "select  `merch_id` , `merch_vpa` , `bank_code` , `txn_id`, `TID` ,`amount` ,  `from_entity` , `txn_ref_no`, `auth_code`, "
			+ "`txn_date`, `customer_vpa`,  `tr_id`, `response_code`, `response_message`, `created`,`gateway_trans_id` from upi_transaction where TID = ? and txn_ref_no = ? order by `created` desc limit 1";
	
	String GET_TXN_QR_DETAILS_MERCHANT_ID_VPA = "select TID ,bank_code from detail where merchant_id = ? and upi_vpa = ? ";
	
	String INSERT_TRANSACTION_EXCEPTION="insert into track_transaction(formEntity, rrn, json, exception, txn_type, message, created) values(?,?,?,?,?,?,current_timestamp())";
	
	String GET_REFUND_TXN="select resp_code from upi_refund where TID =? and txn_ref_no=?";
	
	/**@pranav 
	 * Query for recon generation : GET_FILE_DETAILS
	 * */
	String GET_FILE_DETAILS = "select u.TID, u.merch_id, u.txn_ref_no, u.gateway_trans_id, u.txn_date, u.amount, d.merchant_category_code, u.response_code from upi_transaction u inner join detail d on d.TID=u.TID where u.bank_code=? and txn_date between ? and ?";
	
	String SUM_OF_REFUNDED_AMT = "select sum(refund_amount) from upi_refund u where u.txn_ref_no=? and u.bank_code=? and (u.resp_code = '00' or u.resp_code = '000' or u.resp_code = 's' or u.resp_code = 'S')";

	String UPI_TRANSACTION_BTW_DATES = "select * from upi_transaction t where t.bank_code = ? and (t.created between ? and ?) ";
	
	String GET_MPAN="select mg.merchant_pan from merchant_group mg join detail d on mg.tid=d.tid where mg.tid=? and mg.scheme_id = '02'";
	
	String GET_DIRECT_SMS_PRIMARY_MERCHANT="SELECT md.`mobile_number`, md.device_id, md.platform , mm.is_inapp_activated , mm.is_primary  FROM `mobile_details_mvisa_id_details_link` mm  JOIN mobile_details md ON md.mobile_details_id=mm.mobile_details_id  join `mvisa_id_details` t on t.mvisa_id_details_id = mm.mvisa_id_details_id where t.mvisa_id = ? and  mm.is_primary=1";

	String INSERT_MRL_TXN = "INSERT"
			+ " INTO MAGNUS_TRANSACTION_STAGING(MID,TID,MERCHANT_NAME,MERCHANT_CITY,MCC,MERCHANT_POSTAL_CODE,MERCHANT_VPA,ACQ_BANK_IDENTIFICATION,MERCHANT_CURRENCY_CODE,SETTLEMENT_FLAG,SETTLEMENT_DATE_TIME,CARD_NUMBER,TRANSACTION_DATE,TRANSACTION_TIME,TRANSACTION_AMOUNT,CUSTOMER_VPA,BQR_PRIMARY_ID,BQR_SECONDARY_ID,UPI_TRANSACTION_ID,RRN,AUTH_SOURCE,TRANSACTION_TYPE,RESPONSE_CODE,SETTLEMENT_AMOUNT,TRANSACTION_ID,TIP_AMOUNT,DEVICE_TYPE,TOTAL_AMOUNT,AGG_TRANSACTION_IDENTIFIER,CONVENIENCE_FEE_FLAG,CONVENIENCE_FEE_AMOUNT,REFUND_RRN,ADDITIONAL_DATA,PROCESSING_CODE,POS_ENTRY_MODE,MERCHANT_STATE,CITY_CODE,MOTO_ECOM_INDICATOR,BATCH_DATE,ENTRY_MODE,POS_TERMINAL_CAPABILITY,CHANNEL_TYPE,MRL_STATUS,MESSAGE_TYPE,FLAG,APPROVAL_CODE,TRANSACTION_MPAN,TERMINAL_BATCH_NUMBER,TRANSACTION_CURRENCY_CODE,SALE_TRANSACTION_TYPE,UNIQUE_INVOICE_NUMBER,TLE_COMPLIANT_FLAG,SERVICE_CODE)"
			+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	String GET_MRL_UPI_DATA = "SELECT" +
			"	d.merchant_id AS Merchant_Id," +
			"	d.TID AS Tid," +
			"	cd.name As Merchant_Name," +
			"	cd.city_name As Merchant_City," +
			"	d.merchant_category_code As Mcc," +
			"	d.postal_code As Merchant_Postal_code," +
			"	d.upi_vpa As Merchant_Vpa," +
			"	d.bank_code As Bank_Code," +
			"	d.currency_code As Merchant_Currency_Code," +
			"	'' As Settlement_Flag," +
			"	'' As Settlement_Date_time," +
			"	DATE_FORMAT(ut.txn_date, '%y%m%d') As Transaction_Date," +
			"	DATE_FORMAT(ut.txn_date, '%H%i%S') As Transaction_TIME," +
			"	(ut.amount) * 100 As Transaction_Amount," +
			"	ut.customer_vpa As Customer_Vpa," +
			"	ut.gateway_trans_id As Upi_Transaction_Id," +
			"	ut.txn_ref_no  As rrn," +
			"	ut.auth_code As Approval_Code," +
			"	ut.response_code As Response_Code," +
			"	(ut.amount) * 100 As Settlement_Amount," +
			"	ut.tr_id As Transaction_id," +
			"	'' As Agg_Transaction_Identifier," +
			"	'' As  Convenience_Fee_Flag," +
			"	'' As Convenience_Fee_Amount," +
			"   DATE_FORMAT(current_date(), '%y%m%d') AS Batch_Date" +
			" FROM " +
			" upi_transaction ut " +
			" JOIN detail d ON " +
			" ut.tid = d.tid " +
			" JOIN contact_detail cd ON " +
			" ut.tid = cd.tid " +
			" LEFT JOIN merchant_aggregator_link  mal ON " +
			" ut.merch_id = mal.mvisa_merchant_id " +
			" WHERE ut.txn_ref_no = ? and (ut.response_code = '00' OR ut.response_code = '000')";
	
	
	String GET_MRL_BQR_DATA ="select" +
			"	d.merchant_id AS Merchant_Id," +
			"	d.TID AS Tid," +
			"	td.mms_batch_number AS mms_batch_number," +
			"	td.mms_sceheme_transaction_Id AS mms_sceheme_transaction_Id," +
			"	cd.name  As Merchant_Name," +
			"	cd.city_name  As Merchant_City," +
			"	d.merchant_category_code As Mcc," +
			"	d.postal_code As Merchant_Postal_code," +
			"	d.bank_code As Bank_Code," +
			"	d.currency_code As Merchant_Currency_Code," +
			"	'' As Settlement_Flag," +
			"	'' As Settlement_Date_time," +
			"	DATE_FORMAT(td.date, '%y%m%d') As Transaction_Date," +
			"   DATE_FORMAT(td.date, '%H%i%S') As Transaction_TIME," +
			"	(td.amount) * 100 As Transaction_Amount," +
			"	td.primary_id As Bqr_Primary_Id," +
			"   td.secondary_id As Bqr_Secondary_Id," +
			"	td.ref_no  As rrn," +
			"	td.auth_code As Approval_Code," +
			"	td.transaction_type As Transaction_Type," +
			"	(td.settlement_amount) * 100 As Settlement_Amount," +
			"	'' As Agg_Transaction_Identifier," +
			"   td.primary_id As Transaction_id," +
			"	'' As  Convenience_Fee_Flag," +
			"	'' As Convenience_Fee_Amount," +
			"    td.additional_data As Additional_Data," +
			"   DATE_FORMAT(current_date(), '%y%m%d') AS Batch_Date" +
			" FROM " +
			" transaction_detail td " +
			" JOIN detail d ON " +
			" td.tid = d.tid " +
			" JOIN contact_detail cd ON " +
			" td.tid = cd.tid " +
			" LEFT JOIN merchant_aggregator_link  mal ON " +
			" td.mvisa_merchant_id = mal.mvisa_merchant_id " +
			" WHERE td.ref_no =?";
	
	String  UPDATE_MRL_SETTLEMENT_FLAG = "UPDATE upi_transaction SET txn_settled_flag = 'N', updated = CURRENT_TIMESTAMP WHERE txn_ref_no = ?";
	
	String UPDATE_MMS_BQR_FLAG="update transaction_detail set mms_flag=? where ref_no=?";
	String UPDATE_MRL_BQR_FLAG="update transaction_detail set mrl_flag=? where ref_no=?";
	String UPDATE_MRL_UPI_FLAG="update upi_transaction set mrl_flag=? where txn_ref_no=?";
	
	String INSERT_IPG_REQUEST="insert into bqr_transactions_table('json_request') values(?)";
	
	String INSERT_MMS_TXN = "INSERT INTO transaction_detail (mvisa_merchant_id,date,currency_code,amount,customer_name,"
			+ "customer_pan,auth_code,ref_no,primary_id,secondary_id,settlement_amount,transaction_type,TID,bank_code,additional_data, merchant_id, mms_batch_number, mms_sceheme_transaction_Id) "
			+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	String CHECK_REFUND_ID_BQR = "select count(1) from refund_transaction t where t.ref_no = ? and t.auth_code = ? and t.TID = ? and reqRefundId=? ";
	
	String CHECK_REFUND_ID_UPI = "select reqRefundId , refund_amount from upi_refund t where t.txn_ref_no = ? and t.TID = ?";
	
	String GET_RECON_DATA="select  d.program_type, u.TID, u.merch_id, u.txn_ref_no, u.gateway_trans_id, u.txn_date, u.amount, d.merchant_category_code, u.response_code from upi_transaction u inner join detail d on d.TID=u.TID where u.bank_code=? and txn_date between ? and ?";
	
	String INSERT_AMEX_NOTIFICATION = "INSERT INTO amex_transaction (Type,Primary_Account_Number,Full_Pan,Expiry_Date,Processing_Code,"
			+ "Transaction_Amount,Tip_Amount,Transaction_Time,System_Trace_Number,Transaction_Time_Local,Reconciliation_Date,Acquirer_Reference_Data,Retrieval_Reference_Number,Acquirer_Id,Approval_Code, Action_Code, Card_Acceptor_Id, Card_Acceptor_Id_Type,Card_Acceptor_Name,Currency_Code,Pos_Dc,Additional_Data,Notification_Status,TTMS_Status) "
			+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0,0)";
	
	String CHECK_HAMC_EMAILID = "select secret,clientId,emailId from hmac_details t where t.emailId = ?";
	
	String GET_SECRET_FOR_HAMC = "select secret from hmac_details t where t.clientId = ?";
	
	String INSERT_CLIENTID_SECRET = "INSERT INTO hmac_details (emailId,clientId,secret) VALUES(?,?,?)";
}
