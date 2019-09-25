/**
 * 
 */
package com.wl.upi.service;

import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wl.upi.dao.UPITransactionDAO;
import com.wl.upi.model.KotakUPITransactionDTO;
import com.wl.upi.model.SbiUpiTransaction;
import com.wl.util.AesUtil;
import com.wl.util.EncryptionCache;
import com.wl.util.HelperUtil;
import com.wl.util.JsonUtility;
import com.wl.util.PSPBankCodeMap;
import com.wl.util.constants.Constants;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;


/**
 * SBI PSP callback Implementation
 * Following is SBI PSP fields and their database mapping in upi_transaction table
 * upiTransRefNo - `additional_info1`
 * pgMerchantId - `additional_info2`
 * status - `additional_info3`
 * pspRefNo - `tr_id` - tr which is present in Bharat QR specs 4.0 ---> field 27 sub tag 01.
 * custRefNo - `txn_ref_no` - This is RRN for transaction. This field can be used for future reference in case of any issue with PSP
 * amount - `amount` - transactionAmount as received from BANK PSP
 * txnAuthDate - `txn_date`  - transactionTimestamp as received from BANK PSP. Need to parse this to store in DB.
 * status - `additional_info3` - This will status specified in specification.
 * statusDesc - `response_message` - response message
 * payerVPA - `customer_vpa`  - VPA of customer
 * payeeVPA - `merch_vpa` - VPA of merchant
 * responseCode - `response_code` - response code for transaction
 * approvalNumber - `auth_code` - authcode for transaction
 * 
 * @see PSPTransactionService
 * @author kunal.surana
 */
@Service("sbiPSPImpl")
public class SbiPSPImpl implements PSPTransactionService {

	@Autowired
	private UPITransactionDAO upiTransactionDAO;

	private static Logger logger = LoggerFactory.getLogger(SbiPSPImpl.class);

	/* (non-Javadoc)
	 * @see com.wl.upi.service.PSPTransactionService#upiTransactionCallback(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public Object upiTransactionCallback(String fromEntity, HttpServletRequest req) {
		logger.debug("Starts UPITransaction callback method");
		String data = req.getQueryString();
		logger.debug("query string data :"+data);
		//data = data.split("\\?")[1]; 
		String decryptedData = "";
		String pgMerchantId = "";
		String requestBody="";
		/* Following code moved from Controller to Service as this will be bank specific : Start */
		try {
			requestBody = IOUtils.toString(req.getInputStream(),"UTF-8");
			logger.info("requestBody:",requestBody);
			data =URLDecoder.decode(requestBody, "UTF-8");
			logger.info("data:"+data);
			
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) JsonUtility.parseJson(data.split("=")[1], LinkedHashMap.class);
			if(map == null){
				/*Object excepArr[] ={
						fromEntity,
						"",requestBody,
						ErrorMessages.JSON_FORMAT_ERROR.toString(),"2","Before decryption"
				};
				upiTransactionDAO.saveTransactionException(excepArr);*/
				throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
				}
			logger.info("resp:"+map.get("resp"));
			logger.info("pgMerchantId:"+map.get("pgMerchantId"));
			pgMerchantId = map.get("pgMerchantId");
			AesUtil aes = EncryptionCache.getEncryptionUtility(fromEntity);
			decryptedData = aes.decrypt(map.get("resp"));
			logger.info("decrypted data :"+decryptedData);
			JSONObject reqJson = new  JSONObject(decryptedData);
			JSONObject txnJson = reqJson.getJSONObject("apiResp");
			decryptedData = txnJson.toString();
			logger.info("txn data :"+decryptedData);
		} catch (Exception e) {
			/*Object excepArr[] ={
					fromEntity,
					"",requestBody,
					ErrorMessages.SERVER_ERROR.toString(),"2","Aes Initialization failed while decryption"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			logger.error("Aes Initialization failed",e);
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
		/* Following code moved from Controller to Service as this will be bank specific : End */
		//map.put("resp",data);
		
		SbiUpiTransaction upiTxn = (SbiUpiTransaction) JsonUtility.parseJson(decryptedData,
				SbiUpiTransaction.class);
		if(upiTxn==null){
			/*Object excepArr[] ={
					fromEntity,
					"",decryptedData,
					ErrorMessages.JSON_FORMAT_ERROR.toString(),"2","After Decryption"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			//throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
			return createExceptionMap(upiTxn, ErrorMessages.JSON_FORMAT_ERROR.toString());
			}
		
		
		Map<String, Object> row = upiTransactionDAO.getTxnQRDetails(upiTxn.getMerchantTransactionId());
		upiTxn.setBankCode(PSPBankCodeMap.getBankCode(fromEntity));
		if(row!=null){
			setTxnQRDetails(row, upiTxn);
		}else{
			Map<String, Object> row2 = upiTransactionDAO.getTxnQRDetailsFromVpa(upiTxn.getMerchantVpa());
			if(row2!=null){
				setTxnQRDetailsVPA(row2, upiTxn);
			}else{
				logger.info("UPI QR Transaction details not found");
				Object excepArr[] ={
						fromEntity,
						upiTxn.getRrn(),decryptedData,
						ErrorMessages.TXN_QR_DETAIL_NOT_FOUND.toString(),"2","Details not found through vpa in detail table"
				};
				upiTransactionDAO.saveTransactionException(excepArr);
				//throw new ApplicationException(ErrorMessages.TXN_QR_DETAIL_NOT_FOUND.toString());
				return createExceptionMap(upiTxn, ErrorMessages.TXN_QR_DETAIL_NOT_FOUND.toString());
			}
		}
		logger.debug("UPI txn details:"+upiTxn);
		
		// calling DAO method
		
		Object dataArr[] = {
		upiTxn.getMerchantId(),
		upiTxn.getMerchantVpa(), 
		upiTxn.getQrCodeType(),
		upiTxn.getTxnId(),
		upiTxn.getMerchantTransactionId(), 
		upiTxn.getRrn(),
		HelperUtil.parseTimestamp(upiTxn.getTransactionTimestamp(), "yyyy-MM-dd hh:mm:ss a"),
		upiTxn.getTransactionAmount(), 
		upiTxn.getBankCode(),
		upiTxn.getCustomerVpa(), 
		upiTxn.getGatewayResponseCode(), 
		upiTxn.getGatewayResponseMessage(),							
		fromEntity, 
		upiTxn.getTerminalId(),
		upiTxn.getGatewayTransactionId(),
		upiTxn.getUpiTransRefNo(),							
		pgMerchantId,
		upiTxn.getStatus(),
		upiTxn.getAuthCode() };		
		
		try {
			upiTransactionDAO.saveUpiTxn(dataArr);
		} catch (Exception e) {
			/*Object excepArr[] ={
					fromEntity,
					upiTxn.getRrn(),decryptedData,
					ErrorMessages.SERVER_ERROR.toString(),"2","Exception while inserting in upi_transaction table:"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			logger.error(upiTxn.getMerchantTransactionId()+"|Failed to insert in upi_transaction table",e);
			return createExceptionMap(upiTxn, ErrorMessages.SERVER_ERROR.toString());
		}
		
		Map<String, String> responseMap = new LinkedHashMap<>();
		responseMap.put("status", Constants.SUCCESS.name());
		responseMap.put("callBackstatusCode", upiTxn.getStatus());
		responseMap.put("callBackstatusDescription", upiTxn.getGatewayResponseMessage());
		responseMap.put("callBacktxnId", upiTxn.getMerchantTransactionId());
		responseMap.put("trId", upiTxn.getMerchantTransactionId());
		responseMap.put("mid", upiTxn.getMerchantId());
		responseMap.put("bankCode", upiTxn.getBankCode());
		//Added parameter merchVPA for checking transaction by VPA if fail to get transaction BY TrId 
		responseMap.put("merchVPA",upiTxn.getMerchantVpa());
		responseMap.put("rrn",upiTxn.getRrn());
		return responseMap;
	}
	
	private Object createExceptionMap(SbiUpiTransaction upiTxn, String errorMessage) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("callBackstatusCode", "111");
		map.put("callBackstatusDescription", errorMessage);
		map.put("status", Constants.FAILED.name());
		if (upiTxn != null) {
			map.put("callBacktxnId", upiTxn.getMerchantTransactionId());
			map.put("trId", upiTxn.getMerchantTransactionId());
			map.put("mid", upiTxn.getMerchantId());
			map.put("bankCode", upiTxn.getBankCode());
			//Added parameter merchVPA for checking transaction by VPA if fail to get transaction BY TrId 
			map.put("merchVPA",upiTxn.getMerchantVpa());
			map.put("rrn",upiTxn.getRrn());
		} else {
			map.put("callBacktxnId", "");
			map.put("trId", "");
			map.put("mid", "");
			map.put("bankCode", "");
			// Added parameter merchVPA for checking transaction by VPA if fail
			// to get transaction BY TrId
			map.put("merchVPA", "");
			map.put("rrn", "");
		}

		return map;
	}

	/**
	 * This function sets the result of qr details in {@link SbiUpiTransaction}
	 * @param rows database result received
	 * @param upiTxn instance of {@link SbiUpiTransaction}
	 */
	private void setTxnQRDetails(Map<String,Object> row, SbiUpiTransaction upiTxn)
	{
		if(row!=null)
		{
				upiTxn.setMerchantId((String)row.get("merch_id"));
				upiTxn.setQrCodeType((String)row.get("qr_type"));
				upiTxn.setTxnId((String)row.get("txn_id"));
				upiTxn.setBankCode((String)row.get("bank_code"));
				upiTxn.setTerminalId((String)row.get("terminal_id"));
		}
		
	}
	
	private void setTxnQRDetailsVPA(Map<String,Object> row, SbiUpiTransaction upiTxn)
	{
		if(row!=null)
		{
				upiTxn.setMerchantId((String)row.get("merchant_id"));
				upiTxn.setBankCode((String)row.get("bank_code"));
				upiTxn.setTerminalId((String)row.get("TID"));
				upiTxn.setQrCodeType("0");
				upiTxn.setTxnId(upiTxn.getTxnId());
		}
		
	}
}
