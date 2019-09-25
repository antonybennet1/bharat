package com.wl.upi.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wl.upi.dao.SettlementDAO;
import com.wl.upi.dao.UPITransactionDAO;
import com.wl.upi.model.RefundRequest;
import com.wl.upi.model.SettlementDTO;
import com.wl.upi.model.TxnDTO;
import com.wl.util.HelperUtil;
import com.wl.util.constants.Constants;
import com.wl.util.model.Response;

@Service("settlementService")
public class SettlementService {

	private static Logger logger = LoggerFactory.getLogger(SettlementService.class);

	@Autowired
	@Qualifier("settlementDao")
	private SettlementDAO settlementDao;

	@Autowired
	@Qualifier("upiTransactionDAO")
	private UPITransactionDAO upiTransactionDao;

	/**
	 * Function inserts the transaction details in Thinclient settlement txn
	 * table
	 * 
	 * @param txnDetails
	 */
	public void settleTxn(TxnDTO txnDetails) {
		if (txnDetails != null && txnDetails.getProgramType() == 5) {
			logger.debug("Since merchant is of POS, no settlement done");
			return;
		}
		if (txnDetails != null
				&& ("00".equals(txnDetails.getResponseCode()) || "000".equals(txnDetails.getResponseCode()))) {
			 //int batchNumber = settlementDao.getBatchNumber(txnDetails.getTid());
			//int batchNumber = Integer.parseInt(HelperUtil.toString(txnDetails.getTxnDate(), "MMddHH"));
			
			Date d= new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MMddHH");	
			int batchNumber = Integer.parseInt(sdf.format(d));
			
			/*Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("ddHH");
			String batchNumber=sdf.format(d);*/
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
			sBean.setTxnChannel("7"); // Channel Type changed to 7 to indicate UPI transaction through Mobile APP.
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
			//
			try {
				settlementDao.save(sBean);
			} catch (Exception e) {
				logger.error("txn not settled. Exception is :", e);
			}
		} else
			logger.error(
					txnDetails.getTrId() + ":txn not settled. As reposecode is not 00:" + txnDetails.getResponseCode());
	}

	/**
	 * Function inserts the transaction details in Thinclient settlement txn
	 * table
	 * 
	 * @param txnDetails
	 */
	public void settleRefundTxn(TxnDTO txnDetails, RefundRequest request) {
		logger.info("Inside the settelement");
		//int batchNumber = settlementDao.getBatchNumber(txnDetails.getTid());
		// finalBatchNumber=Integer.parseInt(String.valueOf(batchNumber).concat(HelperUtil.toString(txnDetails.getTxnDate(),"MMddHH")));
		Date d= new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MMddHH");	
		int batchNumber = Integer.parseInt(sdf.format(d));
		//int batchNumber = Integer.parseInt(HelperUtil.toString(txnDetails.getTxnDate(), "MMddHH"));
		
		logger.debug("batch number :" + batchNumber);

		String lastSixOfRRN = txnDetails.getRrn().substring(txnDetails.getRrn().length() - 6);
		String trId = txnDetails.getTrId();
		SettlementDTO sBean = new SettlementDTO();
		if (request.getTxnType().equals("1")) {
			sBean.setTxnChannel("2");
		} else if (request.getTxnType().equals("2")) {
			sBean.setTxnChannel("7");
		}
		sBean.setMid(txnDetails.getMerchantId());
		sBean.setTid(txnDetails.getTid());
		sBean.setBatchNumber(batchNumber);
		sBean.setRrn(txnDetails.getNewRRn());
		sBean.setRequestType("REFUND");
		sBean.setTxnTimestamp(HelperUtil.toString(txnDetails.getTxnDate(), "yyyyMMddHHmmss"));
		sBean.setStanNumber(lastSixOfRRN);
		sBean.setAuthCode(txnDetails.getAuthCode() != null ? txnDetails.getAuthCode() : lastSixOfRRN);
		sBean.setResponceCode("00");
		sBean.setInvoiceNumber(lastSixOfRRN);
		sBean.setBranchCode(null);
		sBean.setTrId(trId);
		BigDecimal db = new BigDecimal(request.getRefundAmount()).movePointRight(2);
		sBean.setOriginalAmount(db.toPlainString());
		sBean.setCurrencyCode("356");
		sBean.setAdditionalAmount("000000000000");
		sBean.setTipApproved(" ");
		sBean.setExpiryDate(" ");
		sBean.setCardEntryMode("QR");
		sBean.setProcessingCode("200001"); //
		sBean.setBankCode(txnDetails.getBankCode());
		sBean.setMti("220");
		sBean.setNewRrn(txnDetails.getRrn());
		// changes by pranav to save merhant vpa
		sBean.setMerchantVpa(txnDetails.getMerchantVpa() == null ? "" : txnDetails.getMerchantVpa());
		// changes by pranav to save customer vpa
		sBean.setCustomerVpa(txnDetails.getCustomerVpa() == null ? "" : txnDetails.getCustomerVpa());
		try {
			settlementDao.save(sBean);
		} catch (Exception e) {
			logger.error("txn not settled. Exception is :", e);
		}
	}

	/**
	 * Following service used by cron job to increment the batch number when
	 * settlement is done for that day. Cron job for this will be time when TCH
	 * settlement is done
	 * 
	 * @return
	 */
	public Response updateBatchNumber() {
		Response resp = new Response();
		try {
			settlementDao.updateBatchNumber();
		} catch (Exception e) {
			resp.setStatus(Constants.FAILED.name());
			logger.error("batch number update failed. Exception is :", e);
		}
		resp.setStatus(Constants.SUCCESS.name());
		return resp;
	}
	/*
	 * public static void main(String[] args) { Date d = new Date(new
	 * java.sql.Timestamp(System.currentTimeMillis()).getTime());
	 * SimpleDateFormat sdf = new SimpleDateFormat("MMddHH"); String str =
	 * sdf.format(d); int batchNumber = Integer.parseInt(str);
	 * 
	 * System.out.println("batchNumber : " + String.format("%06d",
	 * batchNumber)); }
	 */
}
