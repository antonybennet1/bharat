package com.wl.upi.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.tomcat.util.bcel.classfile.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wl.upi.model.AggregatorRequest;
import com.wl.upi.model.AggregatorTransDTO;
import com.wl.upi.model.IPGTransactionDTO;
import com.wl.upi.model.MrlDTO;
import com.wl.upi.model.TCHRequest;
import com.wl.upi.model.TxnDTO;
import com.wl.upi.model.UPITransactionDTO;
import com.wl.upi.util.QueryConstant;
import com.wl.util.EncryptionCache;
import com.wl.util.HelperUtil;
import com.wl.util.config.BankConfig;
import com.wl.util.exceptions.DaoException;
/**
 * @author faizul.mahammad
 *
 */
@Repository("upiTransactionDAO")
public class UPITransactionDAOImpl implements UPITransactionDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static Logger logger = LoggerFactory.getLogger(UPITransactionDAOImpl.class);
	
	@Autowired
	//@Qualifier("mrlJdbcTemplate")
	private JdbcTemplate mrlJdbcTemplate;

	/*@Override
	public int transactionNotification(TransactionNotification transactionNotification) {
		logger.info("inside UPITransactionDAOImpl class");
		try {
			String tid = jdbcTemplate.queryForObject(GETTING_TID,
					new Object[] { transactionNotification.getMerchVpa() }, String.class);
			logger.debug("tid-->" + tid);
			int qrcodeType = 0;

			if (transactionNotification.getQrCodeType().equalsIgnoreCase("static")) {
				qrcodeType = 1;
			} else if (transactionNotification.getQrCodeType().equalsIgnoreCase("dynamic")) {
				qrcodeType = 2;
			}
			int count = jdbcTemplate.update(INSERT_TXN, transactionNotification.getMerchId(),
					transactionNotification.getMerchVpa(), qrcodeType, transactionNotification.getUnqTxnId(),
					transactionNotification.getTrId(), transactionNotification.getTxnRefNo(),
					transactionNotification.getTxnDate(), transactionNotification.getTxnCurrency(),
					transactionNotification.getAmount(), transactionNotification.getBankCode(),
					transactionNotification.getCustomerVpa(), transactionNotification.getCustomerName(),
					transactionNotification.getStatus(), transactionNotification.getMessage(),
					transactionNotification.getFromEntity(), tid, new Timestamp(new Date().getTime()),
					new Timestamp(new Date().getTime()));
			logger.info("Records inserting transaction --->" + count);
			int updateqr = jdbcTemplate.update(UPDATE_QR, "S", transactionNotification.getUnqTxnId());
			logger.info("Records updating qr history --->" + updateqr);

			return count;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("In-side catch block");
			return 0;
		}
	}*/

	/* (non-Javadoc)
	 * @see com.wl.upi.dao.UPITransactionDAO#saveUpiTxn(java.lang.Object[])
	 */
	@Override
	public void saveUpiTxn(Object[] dataArr) {
		logger.debug("Records data --->" + Arrays.toString(dataArr));
		String query = QueryConstant.INSERT_UPI_TXN;
		int count = jdbcTemplate.update(query,dataArr);
		logger.info("Records inserting transaction --->" + count);
	}

	/* (non-Javadoc)
	 * @see com.wl.upi.dao.UPITransactionDAO#getTxnQRDetails(java.lang.String)
	 */
	@Override
	public Map<String, Object> getTxnQRDetails(String trId) {
		String query  = QueryConstant.GET_TXN_QR_DETAILS;
		logger.debug("Query:"+query);
		logger.debug("Query Parameters:"+trId);
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(query, trId);
		if(rows!=null && rows.size() >= 1)
		{
			return rows.get(0);
		}
		else 
			return null;
	}
	
	
	/* (non-Javadoc)
	 * @see com.wl.upi.dao.UPITransactionDAO#getTxnQRDetails(java.lang.String)
	 */
	@Override
	public Map<String, Object> getTxnQRDetailsFromVpa(String vpa) {
		String query  = QueryConstant.GET_TXN_QR_DETAILS_FROM_VPA;
		logger.debug("Query:"+query);
		logger.debug("Query Parameters:"+vpa);
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(query, vpa);
		if(rows!=null && rows.size() >= 1)
		{
			return rows.get(0);
		}
		else 
			return null;
	}

	@Override
	public int cancelQR(TCHRequest upiQrModel) {
		int count = 0;
		logger.debug("query paramters : "+upiQrModel.getMid()+ "|"  + upiQrModel.getTxnId() + "|"  + upiQrModel.getBankCode()+ "|" + upiQrModel.getFromEntity()+ "|" + upiQrModel.getTid());
		count = jdbcTemplate.update(QueryConstant.SET_STATUS_CANCEL, upiQrModel.getMid(), upiQrModel.getTxnId(),
				upiQrModel.getBankCode(), upiQrModel.getFromEntity(), upiQrModel.getTid());
		logger.debug("count in cancelQR -->" + count);
		return count;
	}

	/* (non-Javadoc)
	 * @see com.wl.upi.dao.UPITransactionDAO#checkUpiTxn(com.wl.upi.model.TCHRequest)
	 */
	@Override
	public UPITransactionDTO checkUpiTxn(Object[]  args) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> rows = null;;
		rows = jdbcTemplate.queryForList(QueryConstant.CHECK_UPI_TXN,  args);
		if(rows.size() == 1)
		{
			for(Map<String,Object> row : rows)
			{
				UPITransactionDTO upiTxn = new UPITransactionDTO();
				upiTxn.setMerchantId((String)row.get("merch_id"));
				upiTxn.setMerchantVpa((String)row.get("merch_vpa"));
				upiTxn.setQrCodeType((String)row.get("qr_type"));
				upiTxn.setTxnId((String)row.get("txn_id"));
				upiTxn.setBankCode((String)row.get("bankcode"));
				upiTxn.setTerminalId((String)row.get("terminal_id"));
				upiTxn.setTransactionAmount(""+(Double)row.get("amount"));
				upiTxn.setFromEntity((String)row.get("fromEntity"));
				upiTxn.setRrn((String)row.get("txn_ref_no"));
				upiTxn.setCustomerVpa((String)row.get("customer_vpa"));
				upiTxn.setMerchantTransactionId((String)row.get("tr_id"));
				return upiTxn;
			}
		}
		else 
			throw new DaoException("Expected 1 row and Actual is :"+rows.size());
		return null;
	}

	@Override
	public int updateQRStatus(String tr_id , String status) {
		int count = 0;
		count = jdbcTemplate.update(QueryConstant.SET_QR_STATUS, status , tr_id);
		logger.debug("count in cancelQR -->" + count);
		return count;
	}

	@Override
	public List<UPITransactionDTO> getQRDetailsForRefund(String bankCode) {
		// TODO Auto-generated method stub
		String query  = QueryConstant.GET_TXN_FOR_REFUND;
		logger.debug("Query:"+query);
		logger.debug("Query Parameters:"+bankCode);
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(query,bankCode );
		List<UPITransactionDTO> txnList = new ArrayList<UPITransactionDTO>();
		for(Map<String,Object> row : rows)
		{
			UPITransactionDTO upiTxn = new UPITransactionDTO();
			upiTxn.setMerchantId((String)row.get("merch_id"));
			upiTxn.setMerchantVpa((String)row.get("merch_vpa"));
			upiTxn.setQrCodeType((String)row.get("qr_type"));
			upiTxn.setTxnId((String)row.get("txn_id"));
			upiTxn.setBankCode((String)row.get("bank_code"));
			upiTxn.setTerminalId((String)row.get("TID"));
			upiTxn.setTransactionAmount(((Double)row.get("amount")).toString());
			upiTxn.setFromEntity((String)row.get("from_entity"));
			upiTxn.setMerchantTransactionId((String)row.get("tr_id"));
			upiTxn.setRrn((String)row.get("txn_ref_no"));
 			txnList.add(upiTxn);
		}
		return txnList;
	}

	@Override
	public int updateRefundStatus(String tr_id, String status) {
		// TODO Auto-generated method stub
		int count = 0;
		count = jdbcTemplate.update(QueryConstant.SET_REFUND_STATUS, status , tr_id);
		logger.debug("count in updateRefundStatus -->" + count);
		return count;
	}

	@Override
	public UPITransactionDTO checkBharatQRTxn(TCHRequest tchRequest) {
		String query = QueryConstant.CHECK_BHARATQR_TXN ;
		List<Map<String, Object>> rows = null;
		String trId = "%"+tchRequest.getTxnId() +  "%";
		rows = jdbcTemplate.queryForList(query, trId , trId);
		if(rows.size() == 1)
		{
			for(Map<String,Object> row : rows)
			{
				UPITransactionDTO upiTxn = new UPITransactionDTO();
				upiTxn.setRrn((String)row.get("ref_no"));
				upiTxn.setAuthCode((String)row.get("auth_code"));
				return upiTxn;
			}
		}
		else 
			throw new DaoException("Expected 1 row and Actual is :"+rows.size());
		return null;
	}
	
	@Override
	public boolean checkBharatQRTxnExists(String ref_no, String auth_code) {
		String query = QueryConstant.CHECK_BHARATQR_TXN_EXIST ;
		List<Map<String, Object>> rows = null;
		rows = jdbcTemplate.queryForList(query, ref_no , auth_code);
		if(rows.size() == 1){
			return true;
		} else{
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.wl.upi.dao.UPITransactionDAO#checkAggregatorTxn(com.wl.upi.model.AggregatorRequest)
	 */
	@Override
	public AggregatorTransDTO checkAggregatorTxn(AggregatorRequest aggregatorRequest) {
		logger.debug("In Dao checkAggregatorTxn(AggregatorRequest aggregatorRequest) execution starts");
		if(aggregatorRequest.getTxnId() == null || aggregatorRequest.getTxnId().isEmpty()){
			  logger.info("First getUPITransactionDetails(aggregatorRequest) method executed");
			  return getUPITransactionDetails(aggregatorRequest);
		}else if(aggregatorRequest.getTrId() == null || aggregatorRequest.getTrId().isEmpty()){
			logger.info("First getBharatQRTransactionDetails(aggregatorRequest) method executed");
			  return getBharatQRTransactionDetails(aggregatorRequest);
			}else{
				try{
					AggregatorTransDTO dto= getUPITransactionDetails(aggregatorRequest);
					logger.info("Second getUPITransactionDetails(aggregatorRequest) method executed");
					return dto;
				}catch(Exception e){
					logger.info("Second getBharatQRTransactionDetails(aggregatorRequest) method executed");
					return getBharatQRTransactionDetails(aggregatorRequest);
				}
		   }
	  }
	/*@Override
	public AggregatorTransDTO checkAggregatorTxn(AggregatorRequest aggregatorRequest) {
		logger.debug("In Dao checkAggregatorTxn(AggregatorRequest aggregatorRequest) execution starts");
		String query = QueryConstant.CHECK_BHARATQR_AGGRE_TXN ;
		List<Map<String, Object>> rows = null;
		logger.debug("query :"+query);
		logger.info("Find data for Bharat QR tansaction id " + aggregatorRequest.getTxnId());
		rows = jdbcTemplate.queryForList(query,  "%"+aggregatorRequest.getTxnId() +  "%" , "%"+aggregatorRequest.getTxnId() +  "%");
			if(rows.size() == 1)
			{
				for(Map<String,Object> row : rows)
					{
							AggregatorTransDTO dto = new AggregatorTransDTO();
							dto.setMid((String)row.get("mid"));
							dto.setMpan((String)row.get("mpan"));
							dto.setCustName((String)row.get("customer_name"));
							int curr=(int)row.get("currency_code");
							dto.setTxnCurr(String.valueOf(curr));
							double amt=(double)row.get("amount");
							dto.setTxnAmt(String.format("%.2f", amt));
							dto.setAuthCode((String)row.get("auth_code"));
							dto.setRefNo((String)row.get("ref_no"));
							dto.setPrimaryId((String)row.get("primary_id"));
							dto.setSecondaryId((String)row.get("secondary_id"));
							double sAmt=(double)row.get("settlement_amount");
							dto.setSettleAmt(String.format("%.2f", sAmt));
							Timestamp date=(Timestamp) row.get("time_stamp");
							dto.setTimeStamp(HelperUtil.toString(date,"yyyyMMddHHmmss"));
							int transType=(int)row.get("transaction_type");
							dto.setTransType(String.valueOf(transType));
							dto.setBankCode((String)row.get("bank_code"));
							try {
								String clearPan = EncryptionCache.getEncryptionUtility("APP").decrypt((String)row.get("customer_pan"));
								String encPan = EncryptionCache.getEncryptionUtility("PAN").encrypt(clearPan);
								dto.setConsumerPan(encPan);
							} catch (Exception e) {
								logger.error("|Exception while decrypting customer_pan: ",e);
							}
							dto.setAggrId(aggregatorRequest.getFromEntity());
							logger.debug("In Dao checkAggregatorTxn(AggregatorRequest aggregatorRequest BharatQr) execution ends");
							return dto;
					}
			}
			else{
				logger.debug("query :"+query);
				logger.info("Find data for UPI tr id " +aggregatorRequest.getTrId());
				query=QueryConstant.CHECK_UPI_AGGRE_TXN;
				rows = jdbcTemplate.queryForList(query,  aggregatorRequest.getTrId());
					if(rows.size() == 1)
					{
						for(Map<String,Object> row : rows)
						{
							AggregatorTransDTO dto = new AggregatorTransDTO();
							dto.setMid((String)row.get("mid"));
							double amt=(double)row.get("amount");
							dto.setTxnAmt(String.format("%.2f", amt));
							dto.setAuthCode((String)row.get("auth_code"));
							dto.setRefNo((String)row.get("txn_ref_no"));
							dto.setPrimaryId((String)row.get("primary_id"));
							dto.setSettleAmt(String.format("%.2f", amt));
							Timestamp date=(Timestamp) row.get("time_stamp");
							dto.setTimeStamp(HelperUtil.toString(date,"yyyyMMddHHmmss"));
							//dto.setTransType((String)row.get("txn_type"));
							dto.setTransType("2");
							dto.setAggrId(aggregatorRequest.getFromEntity());
							dto.setBankCode((String)row.get("bankcode"));
							dto.setTxnCurr("356");
							dto.setMerchantVpa((String)row.get("merch_vpa"));
							dto.setCustomerVpa((String)row.get("customer_vpa"));
							dto.setBankCode((String)row.get("bank_code"));
							String secondary_id;
							if ((dto.getPrimaryId().length() >= 17)) {
								secondary_id = dto.getPrimaryId().substring(dto.getPrimaryId().length() - 17);
								dto.setSecondaryId(secondary_id);
							} else {
								dto.setSecondaryId((String) row.get("primary_id"));
							}
							logger.debug("In Dao checkAggregatorTxn(AggregatorRequest aggregatorRequest UPI) execution ends");
							return dto;
						}
					}else
						throw new DaoException("For UPI Aggregator Expected 1 row and Actual is :"+rows.size());
			}
		return null;
	}*/
	@Override
	public TxnDTO getUpiTxnDetails(String trId, boolean with_vpa ,String rrn) {
		String query ="";
		if (with_vpa) {
			query  = QueryConstant.GET_UPI_TXN_BY_RRN_TR_ID;
		}else{
			query  = QueryConstant.GET_UPI_TXN_JOIN_QR_HISTORY;
		}
		
		logger.debug("Query:"+query);
		logger.debug("Query Parameters:"+trId);
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(query, trId,rrn);
		if(rows!=null && rows.size() == 1)
		{
			Map<String,Object> row = rows.get(0);
			TxnDTO txnDetail = new TxnDTO();
			txnDetail.setMerchantId((String)row.get("merch_id"));
			txnDetail.setMerchantVpa((String)row.get("merch_vpa"));
			txnDetail.setBankCode((String)row.get("bank_code"));
			txnDetail.setTid((String)row.get("TID"));
			String amt = Double.toString((Double)row.get("amount"));
			txnDetail.setTxnAmount(amt);
			txnDetail.setFromEntity((String)row.get("from_entity"));
			txnDetail.setRrn((String)row.get("txn_ref_no"));
			txnDetail.setAuthCode((String)row.get("auth_code"));
			txnDetail.setTxnDate((Timestamp)row.get("txn_date"));
			txnDetail.setCustomerVpa((String)row.get("customer_vpa"));
			txnDetail.setTrId(trId);
			txnDetail.setPrimaryId(trId);
			txnDetail.setResponseCode((String)row.get("response_code"));
			txnDetail.setResponseMessage((String)row.get("response_message"));
			txnDetail.setCreated((Timestamp)row.get("created"));
			logger.debug("row.get(program_type)---> " + row.get("program_type").getClass().getSimpleName() );
			
			//Now program type is retrived from query 
			txnDetail.setProgramType((int)row.get("program_type"));
			
			
			
			/*String program_type = row.get("program_type").getClass().getSimpleName();
			if("Integer".equals(program_type)){
				txnDetail.setProgramType((int)row.get("program_type"));
			}else if("String".equals(program_type)){
				txnDetail.setProgramType(Integer.parseInt((String)row.get("program_type")));
			}else{
				txnDetail.setProgramType(2);
			}*/
			
			String qr_type = row.get("qr_type").getClass().getSimpleName();
			if("Integer".equals(qr_type)){
				txnDetail.setQrType(String.valueOf((int)row.get("qr_type")));
			}else
			if("String".equals(qr_type)){
				txnDetail.setQrType((String)row.get("qr_type"));
			}else{
				txnDetail.setQrType("2");
			}
			
			return txnDetail;
		}
		else 
			return null;
	}
	
	@Override
	public int updateTxnSettlementFlag(String flag, String trId) {
		// TODO Auto-generated method stub
		int count = 0;
		count = jdbcTemplate.update(QueryConstant.SET_SETTLEMENT_FLAG, flag , trId);
		logger.debug("count in cancelQR -->" + count);
		return count;
	}

	@Override
	public TxnDTO getUpiTxnDetails(String tid, String rrn) {
		// TODO Auto-generated method stub
		String query  = QueryConstant.GET_UPI_TXN_DETAILS_RRN;
		logger.debug("Query:"+query);
		logger.info("Query Parameters|tid:"+tid);
		logger.info("Query Parameters|rrn:"+rrn);
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(query, tid,rrn);
		if(rows!=null && rows.size() == 1)
		{
			Map<String,Object> row = rows.get(0);
			TxnDTO txnDetail = new TxnDTO();
			txnDetail.setMerchantId((String)row.get("merch_id"));
			txnDetail.setMerchantVpa((String)row.get("merch_vpa"));
			txnDetail.setBankCode((String)row.get("bank_code"));
			txnDetail.setTid((String)row.get("TID"));
			String amt = Double.toString((Double)row.get("amount"));
			txnDetail.setTxnAmount(amt);
			txnDetail.setFromEntity((String)row.get("from_entity"));
			txnDetail.setRrn((String)row.get("txn_ref_no"));
			txnDetail.setAuthCode((String)row.get("auth_code"));
			txnDetail.setTxnDate((Timestamp)row.get("txn_date"));
			txnDetail.setCustomerVpa((String)row.get("customer_vpa"));
			txnDetail.setTrId((String)row.get("tr_id"));
			txnDetail.setPrimaryId((String)row.get("tr_id"));
			txnDetail.setResponseCode((String)row.get("response_code"));
			txnDetail.setResponseMessage((String)row.get("response_message"));
			txnDetail.setCreated((Timestamp)row.get("created"));
			txnDetail.setOriginalTrID((String)row.get("gateway_trans_id"));
			return txnDetail;
		}
		else 
			return null;
	}

	@Override
	public boolean checkUPITxnExists(String trid, String rrn) {
		String query  = QueryConstant.CHECK_UPI_TXN_EXIST;
		logger.debug("Query:"+query);
		logger.info("Query Parameters|trid:"+trid);
		logger.info("Query Parameters|rrn:"+rrn);
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(query, trid,rrn);
		if(rows.size() == 1){
			return true;
		} else{
			return false;
		}
	}

	@Override
	public int getMerchantVpaExist(String creditVpa) {
		
		String query =QueryConstant.CHECK_UPI_CREDIT_VPA_EXIST;
		logger.debug("Query:"+query);
		logger.info("Query Parameters|creditVpa :"+creditVpa);
		
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(query, creditVpa);
		if(rows.size() == 0){
			return 0;
		}else{
			return 1;
		}
		
	}
	
	private AggregatorTransDTO getUPITransactionDetails(AggregatorRequest aggregatorRequest){
		
		logger.info("Find data for UPI tr id " +aggregatorRequest.getTrId());
		String query=QueryConstant.CHECK_UPI_AGGRE_TXN;
		logger.debug("query :"+query);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query,  aggregatorRequest.getTrId(),aggregatorRequest.getTid());
			if(rows.size() == 1)
			{
				for(Map<String,Object> row : rows)
				{
					AggregatorTransDTO dto = new AggregatorTransDTO();
					dto.setMid((String)row.get("mid"));
					double amt=(double)row.get("amount");
					dto.setTxnAmt(String.valueOf(amt));
					dto.setAuthCode((String)row.get("auth_code"));
					dto.setRefNo((String)row.get("txn_ref_no"));
					dto.setPrimaryId((String)row.get("primary_id"));
					dto.setSettleAmt(String.valueOf(amt));
					Timestamp date=(Timestamp) row.get("time_stamp");
					dto.setTimeStamp(HelperUtil.toString(date,"yyyyMMddHHmmss"));
					//dto.setTransType((String)row.get("txn_type"));
					dto.setTransType("2");
					dto.setAggrId(aggregatorRequest.getFromEntity());
					dto.setBankCode((String)row.get("bankcode"));
					dto.setTxnCurr("356");
					dto.setMerchantVpa((String)row.get("merch_vpa"));
					dto.setCustomerVpa((String)row.get("customer_vpa"));
					dto.setBankCode((String)row.get("bank_code"));
					String secondary_id;
					if ((dto.getPrimaryId().length() >= 17)) {
						secondary_id = dto.getPrimaryId().substring(dto.getPrimaryId().length() - 17);
						dto.setSecondaryId(secondary_id);
					} else {
						dto.setSecondaryId((String) row.get("primary_id"));
					}
					dto.setResponseCode((String) row.get("response_code"));
					logger.info(dto.toString());
					logger.debug("In Dao checkAggregatorTxn(AggregatorRequest aggregatorRequest UPI) execution ends");
					return dto;
				}
			}else
				throw new DaoException("For UPI Aggregator Expected 1 row and Actual is :"+rows.size());
	  return null;
		
	}

	private AggregatorTransDTO getBharatQRTransactionDetails(AggregatorRequest aggregatorRequest){
		
		String query = QueryConstant.CHECK_BHARATQR_AGGRE_TXN ;
		logger.debug("query :"+query);
		logger.info("Find data for Bharat QR tansaction id " + aggregatorRequest.getTxnId());
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, aggregatorRequest.getTxnId() , aggregatorRequest.getTxnId());
		if(rows.size() == 1)
		{
			for(Map<String,Object> row : rows)
				{
						AggregatorTransDTO dto = new AggregatorTransDTO();
						dto.setMid((String)row.get("mid"));
						dto.setMpan((String)row.get("mpan"));
						dto.setCustName((String)row.get("customer_name"));
						int curr=(int)row.get("currency_code");
						dto.setTxnCurr(String.valueOf(curr));
						double amt=(double)row.get("amount");
						dto.setTxnAmt(String.valueOf(amt));
						dto.setAuthCode((String)row.get("auth_code"));
						dto.setRefNo((String)row.get("ref_no"));
						dto.setPrimaryId((String)row.get("primary_id"));
						dto.setSecondaryId((String)row.get("secondary_id"));
						double sAmt=(double)row.get("settlement_amount");
						dto.setSettleAmt(String.valueOf(sAmt));
						Timestamp date=(Timestamp) row.get("time_stamp");
						dto.setTimeStamp(HelperUtil.toString(date,"yyyyMMddHHmmss"));
						int transType=(int)row.get("transaction_type");
						dto.setTransType(String.valueOf(transType));
						dto.setBankCode((String)row.get("bank_code"));
						try {
							String clearPan = EncryptionCache.getEncryptionUtility("APP").decrypt((String)row.get("customer_pan"));
							String encPan = EncryptionCache.getEncryptionUtility("PAN").encrypt(clearPan);
							dto.setConsumerPan(encPan);
						} catch (Exception e) {
							logger.error("|Exception while decrypting customer_pan: ",e);
						}
						dto.setAggrId(aggregatorRequest.getFromEntity());
						logger.info(dto.toString());
						logger.debug("In Dao checkAggregatorTxn(AggregatorRequest aggregatorRequest BharatQr) execution ends");
						return dto;
				}
	      }
		return null;
	}
	@Override
	public void saveTransactionException(Object[] dataArr) {
		logger.debug("Exception Data --->" + Arrays.toString(dataArr));
		String query = QueryConstant.INSERT_TRANSACTION_EXCEPTION;
		int count = jdbcTemplate.update(query,dataArr);
		logger.info("Records inserting transaction Excetion --->" + count);
	}
	
	@Override
	public TxnDTO getUpiRefundTxnDetails(String tid, String rrn) {
		// TODO Auto-generated method stub
		String query  = QueryConstant.GET_REFUND_TXN;
		logger.debug("Query:"+query);
		logger.info("Query Parameters|tid:"+tid);
		logger.info("Query Parameters|rrn:"+rrn);
		List<Map<String,Object>> rows = jdbcTemplate.queryForList(query, tid,rrn);
		if(rows!=null && rows.size() == 1)
		{
			Map<String,Object> row = rows.get(0);
			TxnDTO txnDetail = new TxnDTO();
		txnDetail.setResponseCode((String)row.get("resp_code"));
			return txnDetail;
		}
		else 
			return null;
	}

	@Override
	public List<TxnDTO> getTransactionBtw(Date toDate, Date fromDate,String bankCode) {
		String query=QueryConstant.UPI_TRANSACTION_BTW_DATES;
		logger.debug("Query:"+query +   "  "+toDate +"  "+fromDate );
		// TODO Auto-generated method stub
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query,new Object[]{bankCode,toDate,fromDate});
		List<TxnDTO> txnDTOs=new ArrayList<TxnDTO>();
		TxnDTO txnDetail;
		if(rows!=null && !rows.isEmpty())
		{
		  for(Map<String,Object> row : rows)
			{
			    txnDetail = new TxnDTO();
				txnDetail.setMerchantId((String)row.get("merch_id"));
				txnDetail.setMerchantVpa((String)row.get("merch_vpa"));
				txnDetail.setBankCode((String)row.get("bank_code"));
				txnDetail.setTid((String)row.get("TID"));
				String amt = Double.toString((Double)row.get("amount"));
				txnDetail.setTxnAmount(amt);
				txnDetail.setFromEntity((String)row.get("from_entity"));
				txnDetail.setRrn((String)row.get("txn_ref_no"));
				txnDetail.setAuthCode((String)row.get("auth_code"));
				txnDetail.setTxnDate((Timestamp)row.get("txn_date"));
				txnDetail.setCustomerVpa((String)row.get("customer_vpa"));
				txnDetail.setTrId((String)row.get("tr_id"));
				txnDetail.setPrimaryId((String)row.get("tr_id"));
				txnDetail.setResponseCode((String)row.get("response_code"));
				txnDetail.setResponseMessage((String)row.get("response_message"));
				txnDetail.setCreated((Timestamp)row.get("created"));
				txnDetail.setOriginalTrID((String)row.get("gateway_trans_id"));
				txnDTOs.add(txnDetail);
			}
		   
		  }
		return txnDTOs;
	}
	
	@Override
	public void saveMRLTxn(Object[] dataArr) {
		logger.debug("Records data --->" + Arrays.toString(dataArr));
		String query = QueryConstant.INSERT_MRL_TXN;
		int count = mrlJdbcTemplate.update(query,dataArr);
		logger.info("Records inserting transaction --->" + count);
	}

	@Override
	public MrlDTO getDataForMRLUPI(String rrn,int prgmType,String batchNumber) {
		logger.debug("Inside the getDataForMRLUPI() method ");
		String query = QueryConstant.GET_MRL_UPI_DATA;
		logger.debug("Query: " + query);
		logger.debug("parameter rrn : " + rrn);
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, new Object[] { rrn });
		MrlDTO mrlDto = new MrlDTO();
		for (Map<String, Object> row : rows) {
		
			/*String julianDateReverse = new StringBuffer(rrn).reverse().toString();
			String bankCode=(String) row.get("Bank_Code");
			
			String cardNumber = julianDateReverse + bankCode;*/
			
			String bankCode=(String) row.get("Bank_Code");
			String acqBankIdentification = BankConfig.get(bankCode,"acq_bank_no");
			String reverseRrn = new StringBuffer(rrn).reverse().toString();			
			String cardNumber = acqBankIdentification + reverseRrn;
			String transactionId=(String) row.get("Transaction_id");
			
			logger.info("Acq Bank Identification No ::: "+acqBankIdentification);
			logger.info("Reverse Rrn Date ::: "+reverseRrn);
			logger.info("Card Number ::: "+cardNumber);
			
			
				mrlDto.setMerchantId((String) row.get("Merchant_Id"));
				mrlDto.settId((String) row.get("Tid"));
				mrlDto.setMerchantName((String) row.get("Merchant_Name"));
				mrlDto.setMerchantCity((String) row.get("Merchant_City"));
				mrlDto.setMcc((int) row.get("Mcc"));
				mrlDto.setMerchantPostalCode((String) row.get("Merchant_Postal_code"));
				mrlDto.setMerchant_Vpa((String) row.get("Merchant_Vpa"));
				mrlDto.setAcqBankIdentification(acqBankIdentification);
				mrlDto.setMerchantCurrencyCode((int) row.get("Merchant_Currency_Code"));
				mrlDto.setSettlementFlag((String) row.get("Settlement_Flag"));
				mrlDto.setSettlementDateTime((String) row.get("Settlement_Date_time"));
				mrlDto.setTransactionDate((String) row.get("Transaction_Date"));
				mrlDto.setTransactionTime((String) row.get("Transaction_TIME"));
				double amt = (Double) row.get("Transaction_Amount");
				mrlDto.setTransactionAmount(amt);
				mrlDto.setCustomerVpa((String) row.get("Customer_Vpa"));
				mrlDto.setUpiTransactionId((String) row.get("Upi_Transaction_Id"));
				mrlDto.setRrn((String) row.get("rrn"));
				/***UPI - 000000*/
				/***UPI -->  030*/
				mrlDto.setProcessingCode("000000");
				mrlDto.setPosEntryMode("030");
				mrlDto.setAuthSource("U");
				mrlDto.setApprovalCode((String) row.get("Approval_Code"));
				mrlDto.setTransactionType("00");
				mrlDto.setResponseCode((String) row.get("Response_Code"));
				mrlDto.setSettlementAmount(amt);
				mrlDto.setTransactionId(transactionId);
				mrlDto.setTipAmount(0);
				mrlDto.setTotalAmount(amt);
				mrlDto.setAggTransactionIdentifier((String) row.get("Agg_Transaction_Identifier"));
				mrlDto.setRefundRrn("");
				/*mrlDto.setConvenienceFeeFlag((int) row.get("Convenience_Fee_Flag"));
				float conAmt = (float) row.get("Convenience_Fee_Amount");
				mrlDto.setConvenienceFeeAmount(conAmt);*/
				mrlDto.setConvenienceFeeFlag((String) row.get("Convenience_Fee_Flag"));
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
				mrlDto.setDeviceType("8");
				mrlDto.setMerchantState("IN");
				mrlDto.setCityCode("00001");
				mrlDto.setMotoEcomIndicator("0");
				mrlDto.setBatchDate((String)row.get("Batch_Date"));
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
					mrlDto.setMrlStatus("Y");
				}
				mrlDto.setMessageType("0210");
				mrlDto.setFlag("0");
				mrlDto.setTransactionMpan("");
				mrlDto.setTerminalBatchNumber(batchNumber);
				//String transCurrencyCode = BankConfig.get((String) row.get("Bank_Code"),"transaction_currency_code");
				mrlDto.setTransactionCurrenyCode("356");
				mrlDto.setSalesTransactionType("0");
				mrlDto.setUniqueInvoiceNumber("000001");
				mrlDto.setServiceCode(000);
				mrlDto.setTleComplaintFlag(0);// need to confirm
		}
		return mrlDto;
	}

	// need to insert the newly added columns  column in mrl
	
	@Override
	public int sendDataToMRLDB(MrlDTO mrl) throws Exception {
		int count=0;
			logger.info("Inside sendDataToMRLDB -->");
			String query=QueryConstant.INSERT_MRL_TXN;
			String city,transactionId="";
			if(mrl.getMerchantCity().length()<13){
				city=mrl.getMerchantCity();
			}else{
				city=mrl.getMerchantCity().substring(0, 13);
			}if(mrl.getTransactionId()!=null){
				if(mrl.getTransactionId().length()<18){
					 transactionId=mrl.getTransactionId();
				 }else{
					 transactionId=mrl.getTransactionId().substring(mrl.getTransactionId().length() -18);
				 }
			}
			 
			Object dataArr[] = {
					mrl.getMerchantId(),
					mrl.gettId(),
					mrl.getMerchantName(),
					city,
					mrl.getMcc(),
					mrl.getMerchantPostalCode(),
					mrl.getMerchant_Vpa(),
					mrl.getAcqBankIdentification(),
					mrl.getMerchantCurrencyCode(),
					mrl.getSettlementFlag(),
					mrl.getSettlementDateTime(),
					mrl.getCardNumber(),
					mrl.getTransactionDate(),
					mrl.getTransactionTime(),
					mrl.getTransactionAmount(),
					mrl.getCustomerVpa(),
					mrl.getBqrPrimaryId(),
					mrl.getBqrSecondaryId(),
					mrl.getUpiTransactionId(),
					mrl.getRrn(),
					mrl.getAuthSource(),
					mrl.getTransactionType(),
					mrl.getResponseCode(),
					mrl.getSettlementAmount(),
					transactionId,
					mrl.getTipAmount(),
					mrl.getDeviceType(),
					mrl.getTotalAmount(),
					mrl.getAggTransactionIdentifier(),
					mrl.getConvenienceFeeFlag(),
					mrl.getConvenienceFeeAmount(),
					mrl.getRefundRrn(),
					mrl.getAdditionalData(),
					mrl.getProcessingCode(),
					mrl.getPosEntryMode(),
					mrl.getMerchantState(),
					mrl.getCityCode(),
					mrl.getMotoEcomIndicator(),
					mrl.getBatchDate(),
					mrl.getEntryMode(),
					mrl.getPosTerminalCapability(),
					mrl.getChannelType(),
					mrl.getMrlStatus(),
					mrl.getMessageType(),
					mrl.getFlag(),
					mrl.getApprovalCode(),
					mrl.getTransactionMpan(),
					mrl.getTerminalBatchNumber(),
					mrl.getTransactionCurrenyCode(),
					mrl.getSalesTransactionType(),
					mrl.getUniqueInvoiceNumber(),
					mrl.getTleComplaintFlag(),
					mrl.getServiceCode()
					};
			logger.debug(" Insert query : " + query );
		    count = mrlJdbcTemplate.update(query,dataArr);
			logger.info("Records inserting transaction for mrlUpi --->" + count);
		
		return count;
		
	}

	@Override
	public MrlDTO getDataForMrlBQR(String rrn,String mpan,int prgmType,String batchNumber,String transactionCurrencyCode) {
		logger.debug("Inside the getDataForMrlBQR() method ");
		String query = QueryConstant.GET_MRL_BQR_DATA;
		logger.debug("Query: " + query);
		logger.debug("parameter rrn : " + rrn);
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, new Object[] { rrn });
		MrlDTO mrlDto=new MrlDTO();
		for (Map<String, Object> row : rows) {
			String bankCode=(String) row.get("Bank_Code");
			mrlDto.setMerchantId((String) row.get("Merchant_Id"));
			mrlDto.settId((String) row.get("Tid"));
			mrlDto.setMerchantName((String) row.get("Merchant_Name"));
			mrlDto.setMerchantCity((String) row.get("Merchant_City"));
			mrlDto.setMcc((int) row.get("Mcc"));
			mrlDto.setMerchantPostalCode((String) row.get("Merchant_Postal_code"));
			mrlDto.setMerchant_Vpa("");
			String acqBankIdentification = BankConfig.get(bankCode,"acq_bank_no");
			mrlDto.setAcqBankIdentification(acqBankIdentification);
			mrlDto.setMerchantCurrencyCode((int) row.get("Merchant_Currency_Code"));
			mrlDto.setSettlementFlag((String) row.get("Settlement_Flag"));
			mrlDto.setSettlementDateTime((String) row.get("Settlement_Date_time"));
			mrlDto.setCardNumber("");
			mrlDto.setTransactionDate((String) row.get("Transaction_Date"));
			mrlDto.setTransactionTime((String) row.get("Transaction_TIME"));
			double amt = (Double) row.get("Transaction_Amount");
			mrlDto.setTransactionAmount(amt);
			mrlDto.setCustomerVpa("");
			mrlDto.setBqrPrimaryId((String) row.get("Bqr_Primary_Id"));
			mrlDto.setBqrSecondaryId((String) row.get("Bqr_Secondary_Id"));
			mrlDto.setUpiTransactionId("");
			mrlDto.setRrn((String) row.get("rrn"));
			mrlDto.setApprovalCode((String) row.get("Auth_Source"));
			/****Processing Code
			 * Visa -260000
			 * Mast -280000
			 * Rupay - 260000*****/
			/****POS Entry Mode
			 * Visa  --> 010
			Master --> 810
			Rupay --> 080*****/
			if(mpan.startsWith("4")){
				mrlDto.setAuthSource("V");
				mrlDto.setProcessingCode("260000");	
				mrlDto.setPosEntryMode("010");
			}
			else if(mpan.startsWith("5")) 
			{
				mrlDto.setAuthSource("M");
				mrlDto.setProcessingCode("280000");
				mrlDto.setPosEntryMode("810");
			}
			else if(mpan.startsWith("6")) 
			{
				mrlDto.setAuthSource("R");
				mrlDto.setProcessingCode("260000");
				mrlDto.setPosEntryMode("080");
			}
			//For sale so we made it 00 
			mrlDto.setTransactionType("00");
			mrlDto.setResponseCode("00");
			mrlDto.setSettlementAmount(amt);
			//mrlDto.setTransactionId((String) row.get("Transaction_id"));
			
			mrlDto.setTipAmount(0);
			mrlDto.setTotalAmount(amt);
			mrlDto.setAggTransactionIdentifier((String) row.get("Agg_Transaction_Identifier"));
			mrlDto.setRefundRrn("");
			/*mrlDto.setConvenienceFeeFlag((int) row.get("Convenience_Fee_Flag"));
			float conAmt = (float) row.get("Convenience_Fee_Amount");
			mrlDto.setConvenienceFeeAmount(conAmt);*/
			mrlDto.setConvenienceFeeFlag((String) row.get("Convenience_Fee_Flag"));
			mrlDto.setConvenienceFeeAmount(0);
			
			
			mrlDto.setAdditionalData((String) row.get("Additional_Data"));
			//mrlDto.setDeviceType("0");
			mrlDto.setDeviceType("8");
			mrlDto.setMerchantState("IN");
			mrlDto.setCityCode("00001");
			mrlDto.setMotoEcomIndicator("0");			
			mrlDto.setTerminalBatchNumber(batchNumber);
			mrlDto.setBatchDate((String)row.get("Batch_Date"));
			mrlDto.setEntryMode("S");
			mrlDto.setPosTerminalCapability("0");
			mrlDto.setChannelType("2");
			/***Programe type 7(Mobile & static) --> N
			Programe type 9 (Pos)--> Y***/
			if(prgmType == 7)
			{
				mrlDto.setMrlStatus("N");
			}
			if(prgmType == 9)
			{
				mrlDto.setMrlStatus("Y");
			}
			mrlDto.setMessageType("0210");
			mrlDto.setFlag("0");
			mrlDto.setApprovalCode((String) row.get("Approval_Code"));
			mrlDto.setTransactionMpan(mpan);			
			//String transCurrencyCode = BankConfig.get(bankCode,"transaction_currency_code");
			mrlDto.setTransactionCurrenyCode(transactionCurrencyCode);
			mrlDto.setSalesTransactionType("0");
			mrlDto.setUniqueInvoiceNumber("000001");
			mrlDto.setTransactionId((String) row.get("mms_sceheme_transaction_Id"));
			mrlDto.setTleComplaintFlag(2);
			mrlDto.setServiceCode(101);
			//mrlDto.setSchemeTransactionId((int) row.get("mms_sceheme_transaction_Id"));
		}
		return mrlDto;
	}

	@Override
	public int updateMrlSettlementFlag(String rrn) {
			int count = 0;
			count = jdbcTemplate.update(QueryConstant.UPDATE_MRL_SETTLEMENT_FLAG,rrn);
			logger.debug("Transaction not inserted in MRL DB " + count);
			return count;
	}

	@Override
	public Map<String, Double> checkRefundIdExist(String rrn, String trid) {
		logger.info("Inside the checkRefundExist method");	
		String query=QueryConstant.CHECK_REFUND_ID_UPI;
		logger.info("query : " + query);
		logger.info("parameters : ");
		/*logger.info("RefundId : " + refundId);
		logger.info("amount : " + amount);*/
		logger.info("rrn : " + rrn);
		logger.info("tid : " + trid);
		
		Map<String, Double> map = new HashMap<String, Double>();
		//String 
		List<Map<String, Object>> rows= jdbcTemplate.queryForList(query,new Object[]{rrn,trid});
		
		
		for (Map<String, Object> row : rows) {
			logger.debug("key ++ " + (String) row.get("reqRefundId"));
			logger.debug("Value ++ " + (Double)row.get("refund_amount"));
				map.put((String) row.get("reqRefundId"), (Double)row.get("refund_amount"));
			 }
		return map;
	}

	/*@Override
	public int sendDataToMRLDBRefund(MrlDTO mrl) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}*/

}

