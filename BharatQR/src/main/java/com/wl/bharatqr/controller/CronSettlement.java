package com.wl.bharatqr.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wl.upi.model.MrlDTO;
import com.wl.upi.model.SettlementDTO;
import com.wl.upi.model.TxnDTO;
import com.wl.util.HelperUtil;
import com.wl.util.config.BankConfig;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;
import com.wl.util.model.AggregatorDetails;

public class CronSettlement {
	private static Logger logger = LoggerFactory.getLogger(CronSettlement.class);
	private static ConcurrentHashMap<String, AggregatorDetails> map = new ConcurrentHashMap<String, AggregatorDetails>();
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, String>> mapBank = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();

	static Connection con = null;
	
	static {
		Connection conn = null;
		Statement stmts = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.153.177:3306/india_qr", "acqmvisa","W0r1dl!ne@123!");
			stmts = conn.createStatement();
			map.clear();
			AggregatorDetails ag = null;
			String query = "select m.mvisa_merchant_id, a.aggregator_id , a.name , a.url from aggregator_details a join merchant_aggregator_link m on a.aggregator_id = m.aggregator_id order by m.mvisa_merchant_id";
			ResultSet rss = null;

			rss = stmts.executeQuery(query);
			while (rss.next()) {
				ag = new AggregatorDetails();
				String mid = rss.getString("mvisa_merchant_id");
				ag.setAggrId(rss.getString("aggregator_id"));
				ag.setAggrName(rss.getString("name"));
				ag.setUrl(rss.getString("url"));
				ag.setMerchantId(mid);
				map.put(mid, ag);
			}
			mapBank.clear();
			String tempBankCode = "";
			ConcurrentHashMap<String, String> configMap = null;
			String queryBank = "select bank_code , config_key , config_value from bank_config order by bank_code";
			ResultSet rssBank = null;

			rssBank = stmts.executeQuery(queryBank);
			while (rssBank.next()) {
				String bankcode = rssBank.getString("bank_code");
				String configKey = rssBank.getString("config_key");
				String configValue = rssBank.getString("config_value");
				if (!tempBankCode.equals(bankcode)) {
					tempBankCode = bankcode;
					configMap = new ConcurrentHashMap<String, String>();
					mapBank.put(bankcode, configMap);
				}
				configMap.put(configKey, configValue);
			}
			logger.debug("BankWise Config loaded " + map);

		} catch (Exception e) {
			System.out.println("AggregatorDetails=" + e);
		} finally {
			try {
				stmts.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		logger.debug("Midwise Aggregator Config loaded " + map);
	}

	

	public static void main(String[] args) {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://192.168.153.177:3306/india_qr", "acqmvisa",
					"W0r1dl!ne@123!");
			Statement stmt = con.createStatement();
			/*ResultSet rs = stmt.executeQuery(
					"select * from upi_transaction u where u.txn_settled_flag='N' order by created desc limit 5");*/
			ResultSet rs = stmt.executeQuery(
					"select * from upi_transaction u where u.txn_ref_no in ('923839316346','923921503551')");
			int ct = 0;
			while (rs.next()) {
				Map<String, String> input = new HashMap();
				input.put("trId", rs.getString(5));
				input.put("bankCode", rs.getString(10));
				input.put("mid", rs.getString(1));
				input.put("rrn", rs.getString(6));
				try {
					CronSettlement cronSettlement = new CronSettlement();
					cronSettlement.notifyForUpiTxn(input);
				} catch (Exception e) {
					System.out.println("notifyForUpiTxn=" + e);
				}
				ct++;
				System.out.println("count=" + ct);
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				con.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void notifyForUpiTxn(Map<String, String> resp) {
		// TODO Auto-generated method stub
		String trId = resp.get("trId");
		String bankCode = resp.get("bankCode");
		String mid = resp.get("mid");
		String rrn = resp.get("rrn");
		logger.debug("Checking if merchant is aggregator merchant for mid :" + mid);
		AggregatorDetails aggDetail = null;
		if (mid != null) {
			aggDetail = getAggregator(mid);
			logger.info(" aggDetail : " + aggDetail);
		}
		TxnDTO txnDetails = null;
		try {

			// Enabling code for all the banks first it will check transaction
			// by trId follows by VPA
			txnDetails = getUpiTxnDetails(trId, true, rrn);
			/*if (txnDetails == null) {
				logger.debug(bankCode + " : txn not found using trid with QR history, searching txn by RRN & tr_id");
				txnDetails = getUpiTxnDetails(trId, true, rrn);
			}*/
			if (txnDetails == null)
				throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());

		} catch (Exception e1) {
			logger.error(trId + "|Transaction details not found", e1);
			return;
		}

		String batchQuery = "select batch_number from upi_batch_number";
		ResultSet batchSet = null;
		int batchNum = 0;
		String batchNumber = null;
		try {
			Statement stmt = con.createStatement();
			batchSet = stmt.executeQuery(batchQuery);

			while (batchSet.next()) {
				batchNumber = String.format("%04d", batchSet.getInt("batch_number"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// added by velkumar
		// begin
		String cardNo = "";
		if (bankCode.length() == 5 && rrn.length() == 12) {
			StringBuffer sBuffer = new StringBuffer(rrn);
			sBuffer.reverse();
			cardNo = "0".concat(bankCode).concat(sBuffer.substring(sBuffer.length() - 10));

		}
		// end

		if (txnDetails != null && trId != null && !trId.isEmpty() && bankCode != null && !bankCode.isEmpty()
				&& aggDetail == null && !"00031".equals(txnDetails.getBankCode())) {
			logger.debug(mid + "|Not an aggregator merchant");

			if (txnDetails.getProgramType() == 7 || txnDetails.getProgramType() == 9
					|| txnDetails.getProgramType() == 13) {
				logger.debug(" fetching details for  MRL 1 ");
				if ("00".equals(txnDetails.getResponseCode()) || "000".equals(txnDetails.getResponseCode())) {
					MrlDTO mrl = getDataForMRLUPI(txnDetails.getRrn(), txnDetails.getProgramType(), batchNumber);
					try {
						// added by velkumar
						// begin
						if (cardNo.length() == 16)
							mrl.setCardNumber(cardNo);
						// end
						int i = sendDataToMRLDB(mrl);
						logger.info(" data inserted in MMS DB --> " + i + "RRN : " + txnDetails.getRrn());
						if (i > 0) {
							logger.info("Before Insert in the MMS DB " + txnDetails.getRrn());
							int count = updateMrlSettlementFlag(txnDetails.getRrn());
							logger.info("After Inserted in the MMS DB " + txnDetails.getRrn() + "||Count=" + count);
						} else {
							logger.error("Not entered in the MMS DB " + txnDetails.getRrn());
						}
					} catch (Exception e) {
						logger.error("RRN : txnDetails.getRrn() | MMS DB ENTRY  -->  " + txnDetails.getRrn() + "="
								+ e.getMessage());
					}
				} else {
					logger.debug("Cannot insert into Mrl Db as Response Code is not equal to 00 :::");
				}
			}
		}
		// 9 has been used for sending notification to MRL which follows the
		// same notification as of axis bank
		if (txnDetails != null && ("00031".equals(txnDetails.getBankCode()) && aggDetail != null)
				|| aggDetail != null) {
			logger.debug(mid + "|It is aggregator merchant");

			if (txnDetails.getProgramType() == 7 || txnDetails.getProgramType() == 9
					|| txnDetails.getProgramType() == 13) {
				logger.debug(" fetching details for  MRL 2");
				if ("00".equals(txnDetails.getResponseCode()) || "000".equals(txnDetails.getResponseCode())) {
					MrlDTO mrl = getDataForMRLUPI(txnDetails.getRrn(), txnDetails.getProgramType(), batchNumber);
					try {
						// added by velkumar
						// begin
						if (cardNo.length() == 16)
							mrl.setCardNumber(cardNo);
						// end

						int i = sendDataToMRLDB(mrl);
						logger.info(" data inserted in MRL DB --> " + i + "RRN : " + txnDetails.getRrn());
						if (i > 0) {
							logger.info("Before update Txn_Settled_Flag= " + txnDetails.getRrn());
							int count = updateMrlSettlementFlag(txnDetails.getRrn());
							logger.info("After updated Txn_Settled_Flag= " + txnDetails.getRrn() + "||Count=" + count);
						} else {
							logger.error("Not entered in the MMS DB " + txnDetails.getRrn());
						}
					} catch (Exception e) {
						logger.error("RRN : txnDetails.getRrn() | MMS DB ENTRY  -->  " + txnDetails.getRrn() + "||"
								+ e.getMessage());
					}

				} else {
					logger.debug("Cannot insert into Mrl Db as Response Code is not equal to 00 :::");
				}
			}

		}

		if (txnDetails.getProgramType() != 7 && txnDetails.getProgramType() != 9 && txnDetails.getProgramType() != 11
				&& txnDetails.getProgramType() != 13) {
			if (txnDetails != null && aggDetail != null && !"IPG".equalsIgnoreCase(aggDetail.getAggrName())
					&& !"tch".equalsIgnoreCase(txnDetails.getFromEntity())) {

				logger.info(" Aggregator is not null block  and  program type|" + txnDetails.getProgramType());
				int count = settleTxn(txnDetails);
				if (count > 0) {
					logger.info("Before update Txn_Settled_Flag= " + txnDetails.getRrn());
					int i = updateMrlSettlementFlag(txnDetails.getRrn());
					logger.info("After updated Txn_Settled_Flag= " + txnDetails.getRrn() + "||Count=" + i);
				} else {
					logger.info(
							" IF statement Transaction not inserted in TCH DB due to some exception while inserting settlement flag is N: "
									+ txnDetails.getRrn());
				}
			} else if (txnDetails != null && aggDetail == null && !"tch".equalsIgnoreCase(txnDetails.getFromEntity())) {
				logger.info(" Aggregator is null block  and  program type|" + txnDetails.getProgramType());
				int count = settleTxn(txnDetails);
				if (count > 0) {
					logger.info("Before update Txn_Settled_Flag= " + txnDetails.getRrn());
					int i = updateMrlSettlementFlag(txnDetails.getRrn());
					logger.info("After updated Txn_Settled_Flag= " + txnDetails.getRrn() + "||Count=" + i);
				} else {
					logger.info(
							" IF statement Transaction not inserted in TCH DB due to some exception while inserting settlement flag is N: "
									+ txnDetails.getRrn());
				}
			}
		}
		if (txnDetails != null && aggDetail == null && !"tch".equalsIgnoreCase(txnDetails.getFromEntity())&&txnDetails.getProgramType()==11) {
			logger.info("This is for TTMS and program type is 11||RRN= " + txnDetails.getRrn());
			logger.info("Before update Txn_Settled_Flag= " + txnDetails.getRrn());
			int i = updateMrlSettlementFlag(txnDetails.getRrn());
			logger.info("After updated Txn_Settled_Flag= " + txnDetails.getRrn() + "||Count=" + i);
		}

	}

	public TxnDTO getUpiTxnDetails(String trId, boolean with_vpa, String rrn) {
		String query = "";
		if (with_vpa) {
			query = "select  d.program_type,u.`merch_id` , u.`merch_vpa` , u.`bank_code` , u.`txn_id`, u.`TID` ,u.`amount` ,  u.`from_entity` , u.`txn_ref_no`, u.`auth_code`, "
					+ "u.`txn_date`, u.`customer_vpa`,  u.`tr_id`, u.`response_code`, u.`response_message`, u.`created`,'2' as qr_type from upi_transaction u inner join detail d on d.TID=u.TID where u.tr_id ='"
					+ trId + "' and u.txn_ref_no = '" + rrn
					+ "'  and (u.response_code = '00' or u.response_code = '000') order by `created` desc limit 1";
		}
		/*else {
			query = "select  u.`merch_id` , u.`merch_vpa` , u.`bank_code` , u.`txn_id`, u.`TID` ,u.`amount` ,  u.`from_entity` , u.`txn_ref_no`, u.`auth_code`, "
					+ "u.`txn_date`, u.`customer_vpa`,  u.`tr_id`, u.`response_code`, u.`response_message`, u.`created` , q.program_type , q.qr_type from upi_transaction u JOIN qr_history q on u.tr_id = q.tr_id where u.tr_id = '"
					+ trId + "' and u.txn_ref_no = '" + rrn
					+ "' and (u.response_code = '00' or u.response_code = '000') order by `created` desc limit 1";
		}*/

		logger.debug("Query:" + query);
		logger.debug("Query Parameters:" + trId);
		ResultSet rss = null;
		TxnDTO txnDetail = null;
		try {
			Statement stmt = con.createStatement();
			rss = stmt.executeQuery(query);
			while (rss.next()) {
				txnDetail = new TxnDTO();
				txnDetail.setMerchantId(rss.getString("merch_id"));
				txnDetail.setMerchantVpa(rss.getString("merch_vpa"));
				txnDetail.setBankCode(rss.getString("bank_code"));
				txnDetail.setTid(rss.getString("TID"));
				String amt = Double.toString(rss.getDouble("amount"));
				txnDetail.setTxnAmount(amt);
				txnDetail.setFromEntity(rss.getString("from_entity"));
				txnDetail.setRrn(rss.getString("txn_ref_no"));
				txnDetail.setAuthCode(rss.getString("auth_code"));
				txnDetail.setTxnDate(rss.getTimestamp("txn_date"));
				txnDetail.setCustomerVpa(rss.getString("customer_vpa"));
				txnDetail.setTrId(trId);
				txnDetail.setPrimaryId(trId);
				txnDetail.setResponseCode(rss.getString("response_code"));
				txnDetail.setResponseMessage(rss.getString("response_message"));
				txnDetail.setCreated(rss.getTimestamp("created"));
				logger.debug("row.get(program_type)---> " + rss.getString("program_type").getClass().getSimpleName());

				// Now program type is retrived from query
				txnDetail.setProgramType(rss.getInt("program_type"));

				String qr_type = rss.getString("qr_type").getClass().getSimpleName();
				if ("Integer".equals(qr_type)) {
					txnDetail.setQrType(String.valueOf(rss.getInt("qr_type")));
				} else if ("String".equals(qr_type)) {
					txnDetail.setQrType(rss.getString("qr_type"));
				} else {
					txnDetail.setQrType("2");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return txnDetail;
	}

	public MrlDTO getDataForMRLUPI(String rrn, int prgmType, String batchNumber) {
		logger.debug("Inside the getDataForMRLUPI() method ");
		
		String query = "SELECT" +
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
				"	DATE_FORMAT(ut.txn_date, '%Y%m%d%H%i%S') As Settlement_Date_time," +
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
				" WHERE ut.txn_ref_no = '" + rrn
				+ "' and (ut.response_code = '00' OR ut.response_code = '000')";
		logger.debug("Query: " + query);
		logger.debug("parameter rrn : " + rrn);
		ResultSet rss = null;
		MrlDTO mrlDto = null;
		try {
			Statement stmt = con.createStatement();
			rss = stmt.executeQuery(query);
			while (rss.next()) {
				mrlDto = new MrlDTO();
				String bankCode = rss.getString("Bank_Code");
				String acqBankIdentification = getBankDetails(bankCode, "acq_bank_no");
				String reverseRrn = new StringBuffer(rrn).reverse().toString();
				String cardNumber = acqBankIdentification + reverseRrn;
				String transactionId = rss.getString("Transaction_id");
				
				logger.info("Acq Bank Identification No ::: "+acqBankIdentification);
				logger.info("Reverse Rrn Date ::: "+reverseRrn);
				logger.info("Card Number ::: "+cardNumber);
				logger.info("transactionId ::: "+transactionId);
				
					mrlDto.setMerchantId(rss.getString("Merchant_Id"));
					//if tr_id starts with MR then it is TTMS POS transaction..
					
					if(transactionId!=null && transactionId.length() > 25 && transactionId.trim().startsWith("MR"))
					{
						mrlDto.settId(transactionId.substring(18, 26));	
						logger.info("inside TTMS POS condition ::: "+transactionId);
					}
					
					else
					{
						mrlDto.settId(rss.getString("Tid")); 
					}
					
					mrlDto.setMerchantName(rss.getString("Merchant_Name"));
					mrlDto.setMerchantCity(rss.getString("Merchant_City"));
					mrlDto.setMcc(rss.getInt("Mcc"));
					mrlDto.setMerchantPostalCode(rss.getString("Merchant_Postal_code"));
					mrlDto.setMerchant_Vpa(rss.getString("Merchant_Vpa"));
					mrlDto.setAcqBankIdentification(acqBankIdentification);
					mrlDto.setMerchantCurrencyCode(rss.getInt("Merchant_Currency_Code"));
					mrlDto.setSettlementFlag(rss.getString("Settlement_Flag"));
					mrlDto.setSettlementDateTime(rss.getString("Settlement_Date_time"));
					mrlDto.setTransactionDate(rss.getString("Transaction_Date"));
					mrlDto.setTransactionTime(rss.getString("Transaction_TIME"));
					double amt = rss.getDouble("Transaction_Amount");
					mrlDto.setTransactionAmount(amt);
					mrlDto.setCustomerVpa(rss.getString("Customer_Vpa"));
					mrlDto.setUpiTransactionId(rss.getString("Upi_Transaction_Id"));
					mrlDto.setRrn(rss.getString("rrn"));
					/***UPI - 000000*/
					/***UPI -->  030*/
					mrlDto.setProcessingCode("000000");
					mrlDto.setPosEntryMode("030");
					mrlDto.setAuthSource("U");
					mrlDto.setApprovalCode(rss.getString("Approval_Code"));
					mrlDto.setTransactionType("00");
					mrlDto.setResponseCode(rss.getString("Response_Code"));
					mrlDto.setSettlementAmount(amt);
					mrlDto.setTransactionId(transactionId);
					mrlDto.setTipAmount(0);
					mrlDto.setTotalAmount(amt);
					mrlDto.setAggTransactionIdentifier(rss.getString("Agg_Transaction_Identifier"));
					mrlDto.setRefundRrn("");
					/*mrlDto.setConvenienceFeeFlag((int) row.get("Convenience_Fee_Flag"));
					float conAmt = (float) row.get("Convenience_Fee_Amount");
					mrlDto.setConvenienceFeeAmount(conAmt);*/
					mrlDto.setConvenienceFeeFlag(rss.getString("Convenience_Fee_Flag"));
					mrlDto.setConvenienceFeeAmount(0);
					mrlDto.setBqrPrimaryId(transactionId);
					String secondary_id;
					if ((transactionId.length() >= 17)) {
						secondary_id = transactionId.substring(transactionId.length() - 17);
						mrlDto.setBqrSecondaryId(secondary_id);
					} else {
						mrlDto.setBqrSecondaryId((transactionId));
					}
					//mrlDto.setBqrSecondaryId("");
					mrlDto.setCardNumber(cardNumber);
					mrlDto.setAdditionalData("");
					mrlDto.setDeviceType("0");
					mrlDto.setMerchantState("IN");
					mrlDto.setCityCode("00001");
					mrlDto.setMotoEcomIndicator("0");
					mrlDto.setBatchDate(rss.getString("Batch_Date"));
					mrlDto.setEntryMode("S");
					mrlDto.setPosTerminalCapability("0");
					mrlDto.setChannelType("3");
					/***Programe type 7(Mobile & static) --> N
					Programe type 9 (Pos)--> Y***/
					if(prgmType == 7)
					{
						mrlDto.setMrlStatus("N");
					}
					if(prgmType == 9)
					{
						mrlDto.setMrlStatus("N");
					}
					mrlDto.setMessageType("0210");
					mrlDto.setFlag("0");
					mrlDto.setTransactionMpan("");
					mrlDto.setTerminalBatchNumber(batchNumber);
					//String transCurrencyCode = BankConfig.get((String) row.get("Bank_Code"),"transaction_currency_code");
					mrlDto.setTransactionCurrenyCode("356");
					mrlDto.setSalesTransactionType("0");
					mrlDto.setUniqueInvoiceNumber("000001");
			}
		} catch (Exception e) {
			System.out.println("getDataForMRLUPI=" + e);
		}
		return mrlDto;
	}

	public int sendDataToMRLDB(MrlDTO mrl) throws Exception {
		int count = 0;
		Connection conMMS =null;
		try{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		conMMS = DriverManager.getConnection("jdbc:oracle:thin:@192.168.153.91:1521:mmsbedb", "mmsdba",
				"mmsdba#123");
		// Statement stmtMMS = conMMS.createStatement();
		
		logger.info("Inside sendDataToMRLDB -->");
		String query = "INSERT"
				+ " INTO MAGNUS_TRANSACTION_STAGING(MID,TID,MERCHANT_NAME,MERCHANT_CITY,MCC,MERCHANT_POSTAL_CODE,MERCHANT_VPA,ACQ_BANK_IDENTIFICATION,MERCHANT_CURRENCY_CODE,SETTLEMENT_FLAG,SETTLEMENT_DATE_TIME,CARD_NUMBER,TRANSACTION_DATE,TRANSACTION_TIME,TRANSACTION_AMOUNT,CUSTOMER_VPA,BQR_PRIMARY_ID,BQR_SECONDARY_ID,UPI_TRANSACTION_ID,RRN,AUTH_SOURCE,TRANSACTION_TYPE,RESPONSE_CODE,SETTLEMENT_AMOUNT,TRANSACTION_ID,TIP_AMOUNT,DEVICE_TYPE,TOTAL_AMOUNT,AGG_TRANSACTION_IDENTIFIER,CONVENIENCE_FEE_FLAG,CONVENIENCE_FEE_AMOUNT,REFUND_RRN,ADDITIONAL_DATA,PROCESSING_CODE,POS_ENTRY_MODE,MERCHANT_STATE,CITY_CODE,MOTO_ECOM_INDICATOR,BATCH_DATE,ENTRY_MODE,POS_TERMINAL_CAPABILITY,CHANNEL_TYPE,MRL_STATUS,MESSAGE_TYPE,FLAG,APPROVAL_CODE,TRANSACTION_MPAN,TERMINAL_BATCH_NUMBER,TRANSACTION_CURRENCY_CODE,SALE_TRANSACTION_TYPE,UNIQUE_INVOICE_NUMBER,TLE_COMPLIANT_FLAG,SERVICE_CODE)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		;
		String city, transactionId = "";
		String bqrPrimaryId = "";
		if (mrl.getMerchantCity().length() < 13) {
			city = mrl.getMerchantCity();
		} else {
			city = mrl.getMerchantCity().substring(0, 13);
		}
		if (mrl.getTransactionId() != null) {
			if (mrl.getTransactionId().length() < 18) {
				transactionId = mrl.getTransactionId();
			} else {
				transactionId = mrl.getTransactionId().substring(mrl.getTransactionId().length() - 18);
			}
		}
		if (mrl.getBqrPrimaryId() != null) {
			if (mrl.getBqrPrimaryId().length() < 26) {

				bqrPrimaryId = mrl.getBqrPrimaryId();
				logger.info("Inside mrl.getBqrPrimaryId().length()<26 " + bqrPrimaryId + " count length :"
						+ bqrPrimaryId.length());
			} else {
				bqrPrimaryId = mrl.getBqrPrimaryId().substring(mrl.getBqrPrimaryId().length() - 26);
				logger.info("Inside else " + bqrPrimaryId + " count length :" + bqrPrimaryId.length());
			}
		}
		PreparedStatement preparedStatement = null;
		preparedStatement = conMMS.prepareStatement(query);
		preparedStatement.setString(1, mrl.getMerchantId());
		preparedStatement.setString(2, mrl.gettId());
		preparedStatement.setString(3, mrl.getMerchantName());
		preparedStatement.setString(4, city);
		preparedStatement.setInt(5, mrl.getMcc());
		preparedStatement.setString(6, mrl.getMerchantPostalCode());
		preparedStatement.setString(7, mrl.getMerchant_Vpa());
		preparedStatement.setString(8, mrl.getAcqBankIdentification());
		preparedStatement.setInt(9, mrl.getMerchantCurrencyCode());
		preparedStatement.setString(10, mrl.getSettlementFlag());
		preparedStatement.setString(11, mrl.getSettlementDateTime());
		preparedStatement.setString(12, mrl.getCardNumber());
		preparedStatement.setString(13, mrl.getTransactionDate());
		preparedStatement.setString(14, mrl.getTransactionTime());
		preparedStatement.setDouble(15, mrl.getTransactionAmount());
		preparedStatement.setString(16, mrl.getCustomerVpa());
		preparedStatement.setString(17, bqrPrimaryId);
		preparedStatement.setString(18, mrl.getBqrSecondaryId());
		preparedStatement.setString(19, mrl.getUpiTransactionId());
		preparedStatement.setString(20, mrl.getRrn());
		preparedStatement.setString(21, mrl.getAuthSource());
		preparedStatement.setString(22, mrl.getTransactionType());
		if("000".equals(mrl.getResponseCode())){
			preparedStatement.setString(23, "00");
		}else{
		preparedStatement.setString(23, mrl.getResponseCode());
		}
		preparedStatement.setDouble(24, mrl.getSettlementAmount());
		preparedStatement.setString(25, transactionId);
		preparedStatement.setDouble(26, mrl.getTipAmount());
		preparedStatement.setString(27, mrl.getDeviceType());
		preparedStatement.setDouble(28, mrl.getTotalAmount());
		preparedStatement.setString(29, mrl.getAggTransactionIdentifier());
		preparedStatement.setString(30, mrl.getConvenienceFeeFlag());
		preparedStatement.setFloat(31, mrl.getConvenienceFeeAmount());
		preparedStatement.setString(32, mrl.getRefundRrn());
		preparedStatement.setString(33, mrl.getAdditionalData());
		preparedStatement.setString(34, mrl.getProcessingCode());
		preparedStatement.setString(35, mrl.getPosEntryMode());
		preparedStatement.setString(36, mrl.getMerchantState());
		preparedStatement.setString(37, mrl.getCityCode());
		preparedStatement.setString(38, mrl.getMotoEcomIndicator());
		preparedStatement.setString(39, mrl.getBatchDate());
		preparedStatement.setString(40, mrl.getEntryMode());
		preparedStatement.setString(41, mrl.getPosTerminalCapability());
		preparedStatement.setString(42, mrl.getChannelType());
		preparedStatement.setString(43, mrl.getMrlStatus());
		preparedStatement.setString(44, mrl.getMessageType());
		preparedStatement.setString(45, mrl.getFlag());
		preparedStatement.setString(46, mrl.getApprovalCode());
		preparedStatement.setString(47, mrl.getTransactionMpan());
		preparedStatement.setString(48, mrl.getTerminalBatchNumber());
		preparedStatement.setString(49, mrl.getTransactionCurrenyCode());
		preparedStatement.setString(50, mrl.getSalesTransactionType());
		preparedStatement.setString(51, mrl.getUniqueInvoiceNumber());
		preparedStatement.setInt(52, mrl.getTleComplaintFlag());
		preparedStatement.setInt(53, mrl.getServiceCode());
		logger.debug(" Insert query : " + query);
		count = preparedStatement.executeUpdate();
		logger.info("Records inserting transaction for mrlUpi --->" + count);
		}catch(Exception e){
			System.out.println("sendDataToMRLDB="+e);
		}finally {
			try {
				conMMS.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;

	}

	public int settleTxn(TxnDTO txnDetails) {
		int counter = 0;
		if (txnDetails != null && txnDetails.getProgramType() == 5) {
			logger.debug("Since merchant is of POS, no settlement done");
			return counter;
		}
		if (txnDetails != null && ("00".equals(txnDetails.getResponseCode())
				|| "000".equals(txnDetails.getResponseCode()) || "s".equalsIgnoreCase(txnDetails.getResponseCode())
				|| "success".equalsIgnoreCase(txnDetails.getResponseCode()))) {
			// int batchNumber =
			// settlementDao.getBatchNumber(txnDetails.getTid());
			// int batchNumber =
			// Integer.parseInt(HelperUtil.toString(txnDetails.getTxnDate(),
			// "MMddHH"));
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MMddHH");
			int batchNumber = Integer.parseInt(sdf.format(d));

			/*
			 * Date d = new Date(); SimpleDateFormat sdf = new
			 * SimpleDateFormat("ddHH"); String batchNumber=sdf.format(d);
			 */
			logger.debug("batch number :" + batchNumber);
			String lastSixOfRRN = txnDetails.getRrn().substring(txnDetails.getRrn().length() - 6);
			String trId = txnDetails.getTrId();
			String secondryId = null;
			if (!trId.startsWith(txnDetails.getMerchantId())) {
				if (trId.length() > 18) {
					secondryId = trId.substring(18);
				}

			}
			SettlementDTO sBean = new SettlementDTO();
			sBean.setTxnChannel("7"); // Channel Type changed to 7 to indicate
										// UPI transaction through Mobile APP.
			sBean.setMid(txnDetails.getMerchantId());
			sBean.setTid(txnDetails.getTid());
			sBean.setBatchNumber(batchNumber);
			sBean.setRrn(txnDetails.getRrn());
			sBean.setRequestType("SALE");
			sBean.setTxnTimestamp(HelperUtil.toString(txnDetails.getTxnDate(), "yyyyMMddHHmmss"));
			sBean.setStanNumber(lastSixOfRRN);
			sBean.setAuthCode(txnDetails.getAuthCode() != null ? txnDetails.getAuthCode() : lastSixOfRRN);
			sBean.setResponceCode("00");
			sBean.setInvoiceNumber(lastSixOfRRN);
			sBean.setBranchCode(null);
			sBean.setTrId(trId);
			BigDecimal db = new BigDecimal(txnDetails.getTxnAmount()).movePointRight(2);
			sBean.setOriginalAmount(db.toPlainString());
			sBean.setCurrencyCode("356");
			sBean.setAdditionalAmount("000000000000");
			sBean.setTipApproved(" ");
			sBean.setExpiryDate(" ");
			sBean.setCardEntryMode("QR");
			sBean.setProcessingCode("000000");
			sBean.setBankCode(txnDetails.getBankCode());
			sBean.setMti("200");
			sBean.setSecondryId(secondryId);
			sBean.setMerchantVpa(txnDetails.getMerchantVpa());
			sBean.setCustomerVpa(txnDetails.getCustomerVpa());
			try {
				counter = save(sBean);
			} catch (Exception e) {
				counter = 0;
				logger.error("txn not settled. Exception is :", e);
			}
		} else
			logger.error(
					txnDetails.getTrId() + ":txn not settled. As reposecode is not 00:" + txnDetails.getResponseCode());
		return counter;
	}

	public int save(final SettlementDTO bean) {
		int count = 0;
		Connection conTCH = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conTCH = DriverManager.getConnection("jdbc:oracle:thin:@192.168.153.169:1521:THINDB", "tch", "tch987");
			// Statement stmtTCH = conTCH.createStatement();
			String query = null;

			if ("00051".equals(bean.getBankCode()) || "00011".equals(bean.getBankCode())|| "00045".equals(bean.getBankCode()))
				query = "insert into MOB_PAYMENT_TXN_P7 (P_ID,P_TXN_CHANNEL , P_MERCHANTID , "
						+ "P_TERMINALID , P_BATCH_NUMBER , P_RETRIVAL_REF_NUMBER , P_REQUEST_TYPE , P_DATE , P_STAN_NUMBER , P_AUTH_ID , "
						+ "P_RESPONSE_CODE , P_INVOICENUMBER , P_BRANCH_CODE , P_REFERENCE_VALUE , P_ORIGINAL_AMOUNT ,  "
						+ "P_CURRENCY_CODE , P_ADDAMOUNT ,  P_TIP_APPROVED , P_EXPIRYDATE , P_CARD_ENTRYMODE ,  P_PROCESSING_CODE , "
						+ "P_BANK_CODE , P_MTI ,P_EXTRA_INFO, P_SETTLEMENT_FLAG , P_CREATED , P_UPDATED, P_MERCHANT_VPA, P_CUST_VPA ,P_FIELD63 ) values (SEQ_MOB_SETTLEMENT_ID.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Y',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,?,?,?)";
			else
				query = "insert into MOB_PAYMENT_TXN (P_ID,P_TXN_CHANNEL , P_MERCHANTID , "
						+ "P_TERMINALID , P_BATCH_NUMBER , P_RETRIVAL_REF_NUMBER , P_REQUEST_TYPE , P_DATE , P_STAN_NUMBER , P_AUTH_ID , "
						+ "P_RESPONSE_CODE , P_INVOICENUMBER , P_BRANCH_CODE , P_REFERENCE_VALUE , P_ORIGINAL_AMOUNT ,  "
						+ "P_CURRENCY_CODE , P_ADDAMOUNT ,  P_TIP_APPROVED , P_EXPIRYDATE , P_CARD_ENTRYMODE ,  P_PROCESSING_CODE , "
						+ "P_BANK_CODE , P_MTI ,P_EXTRA_INFO, P_SETTLEMENT_FLAG , P_CREATED , P_UPDATED, P_MERCHANT_VPA, P_CUST_VPA ,P_FIELD63 ) values (SEQ_MOB_SETTLEMENT_ID.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Y',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,?,?,?)";
			logger.debug("Query:" + query);
			logger.debug("Parameter:" + bean);

			PreparedStatement ps = null;
			ps = conTCH.prepareStatement(query);
			ps.setString(1, bean.getTxnChannel());
			ps.setString(2, bean.getMid());
			ps.setString(3, bean.getTid());
			ps.setInt(4, bean.getBatchNumber());
			ps.setString(5, bean.getRrn()); // this will store the new RRN for
											// sale and refund
			ps.setString(6, bean.getRequestType());
			ps.setString(7, bean.getTxnTimestamp());
			ps.setString(8, bean.getStanNumber());
			ps.setString(9, bean.getAuthCode());
			ps.setString(10, bean.getResponceCode());
			ps.setString(11, bean.getInvoiceNumber());
			ps.setString(12, bean.getBranchCode());
			ps.setString(13, bean.getTrId());
			ps.setString(14, bean.getOriginalAmount());
			ps.setString(15, bean.getCurrencyCode());
			ps.setString(16, bean.getAdditionalAmount());
			ps.setString(17, bean.getTipApproved());
			ps.setString(18, bean.getExpiryDate());
			ps.setString(19, bean.getCardEntryMode());
			ps.setString(20, bean.getProcessingCode());
			ps.setString(21, bean.getBankCode());
			ps.setString(22, bean.getMti());
			ps.setString(23, bean.getSecondryId());
			ps.setString(24, bean.getMerchantVpa());
			ps.setString(25, bean.getCustomerVpa());
			ps.setString(26, bean.getNewRrn()); // storing old rrn(sale rrn) in
												// P_FIELDF63 for refund
			count = ps.executeUpdate();
			logger.info("Inserting Settlement transaction:" + count);
		} catch (Exception e) {
			System.out.println("save=" + e);
		} finally {
			try {
				conTCH.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}

	public int updateMrlSettlementFlag(String rrn) {
		int count = 0;
		String query = "UPDATE upi_transaction SET txn_settled_flag = 'Y', updated = CURRENT_TIMESTAMP WHERE txn_ref_no = '"+ rrn+"'";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(query);
			count = preparedStatement.executeUpdate();
			logger.debug("update txn_settled_flag returns :" + count);
		} catch (Exception e) {
			logger.debug("Transaction not inserted in MRL DB " + count);
		}

		return count;
	}

	public int insertIpgRequest(String jsonRequest) {
		logger.debug("inserting Ipg request in db starts");
		String query = "insert into bqr_transactions_table('json_request') values(" + jsonRequest + ")";
		PreparedStatement ps = null;
		int count = 0;
		try {
			ps = con.prepareStatement(query);
			count = ps.executeUpdate();
			logger.debug("IPg request insert in db count : " + count);
		} catch (Exception e) {
		}
		return count;
	}

	public static AggregatorDetails getAggregator(String mid) {
		return map.get(mid);
	}

	public static String getBankDetails(String bankCode, String key) {
		return mapBank.get(bankCode).get(key);
	}
	
}
