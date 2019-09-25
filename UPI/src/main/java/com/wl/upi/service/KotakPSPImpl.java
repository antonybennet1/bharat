package com.wl.upi.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wl.upi.dao.UPITransactionDAO;
import com.wl.upi.model.KotakUPITransactionDTO;
import com.wl.upi.model.MultiBankUPITransaction;
import com.wl.upi.model.UPITransactionDTO;
import com.wl.util.AesUtil;
import com.wl.util.EncryptionCache;
import com.wl.util.HelperUtil;
import com.wl.util.JsonUtility;
import com.wl.util.PSPBankCodeMap;
import com.wl.util.SHA256CheckSum;
import com.wl.util.constants.Constants;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;
/**
 * 
 * 
 * KOTAK PSP callback Implementation
 * Following is KOTAK PSP fields and their database mapping in upi_transaction table
 * 
 * merchId(merchantcode)      - 'merch_id'          
 * status                     - `additional_info1`  - merchant status 
 * statusCode                 - `additional_info2`  - status code
 * type                       -  `additional_info3` - Type of the Transaction
 * gatewayTransId             - 'gateway_trans_id'  - Merchant Reference id
 * merchTransId(transactionid)      - `tr_id`       -Transaction Id
 * amount                     - `amount`             - transactionAmount as received from BANK PSP
 * transTimestamp(transactionTimestamp) - `txn_date`  - null which is not specified in FSD.
 * rrn                        - `rrn`               - This is RRN for transaction.
 * (customerVpa)             - `customer_vpa`      -  VPA of customer
 * (merchantVpa)             - `merch_vpa`         - VPA of merchant
 * code                      - `response_code`      - parameter present in request as a code not in DTO 
 * result                     - `response_message`  - parameter present in request not in DTO
 * 
 * @author ritesh.patil
 *
 */

@Service("kotakPSPImpl")
public class KotakPSPImpl implements PSPTransactionService{

	private static Logger logger = LoggerFactory.getLogger(KotakPSPImpl.class);

	@Autowired
	private UPITransactionDAO upiTransactionDAO;

	@SuppressWarnings("unchecked")
	@Override
	public Object upiTransactionCallback(String fromEntity, HttpServletRequest req) {

		logger.info("upiTransactionCallback(String fromEntity, HttpServletRequest req) method execution starts for "+fromEntity);
		String data = "";
		String decryptedData = "";
		String requestBody=""; 
		try {
			 requestBody = IOUtils.toString(req.getReader());
			requestBody=new String(requestBody.getBytes(),"UTF-8");
			logger.info("request json:"+
			requestBody);
			LinkedHashMap<String,Object> request =  (LinkedHashMap<String, Object>) JsonUtility.parseJson(requestBody,LinkedHashMap.class);
			if(request==null){
				/*Object excepArr[] ={
						fromEntity,
						"",requestBody,
						ErrorMessages.JSON_FORMAT_ERROR.toString(),"2","Before decryption"
				};
				upiTransactionDAO.saveTransactionException(excepArr);*/
				throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
				}
			data = (String) request.get("data");
			if(data==null || data.isEmpty()){
				/*Object excepArr[] ={
						fromEntity,
						"",requestBody,
						ErrorMessages.JSON_FORMAT_ERROR.toString(),"2","data is null"
				};
				upiTransactionDAO.saveTransactionException(excepArr);*/
				throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
				}
			logger.info("data:"+data);
			AesUtil aes = EncryptionCache.getEncryptionUtility(fromEntity);
			//data = new String(data.getBytes("UTF-8"));
			data = new String(data.getBytes("UTF-8"));
			decryptedData = aes.decrypt(data);
			logger.info("decrypted data :"+decryptedData);

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
		KotakUPITransactionDTO transactionDTO=(KotakUPITransactionDTO) JsonUtility.parseJson(decryptedData, KotakUPITransactionDTO.class); 
		
		String validateStr=validate(transactionDTO);
		if(!validateStr.isEmpty()){
			/*Object excepArr[] ={
					fromEntity,
					transactionDTO.getRrn(),decryptedData,
					ErrorMessages.JSON_FORMAT_ERROR.toString(),"2","Some Mandatory fields are not present"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			return createExceptionMap(transactionDTO, ErrorMessages.JSON_FORMAT_ERROR.toString());
			
			//throw new ApplicationException("Exception cause validation "+validateStr + ErrorMessages.JSON_FORMAT_ERROR.toString());
			}


		String chksum=null;
		try{
			String chksumData = transactionDTO.getMerchId()+transactionDTO.getMerchantVpa()+transactionDTO.getCustomerVpa()+transactionDTO.getMerchTransId()+
					transactionDTO.getTransTimestamp()+transactionDTO.getAmount()+transactionDTO.getGatewayTransId()+transactionDTO.getStatusCode()+transactionDTO.getRemarks()+transactionDTO.getRrn();
			logger.debug("Checksum generated values are "+chksumData);
			chksum = SHA256CheckSum.genChecksum(chksumData);
		}catch(Exception e){
			logger.error("Problem at the time of genrating Checksum ",e);
			/*Object excepArr[] ={
					fromEntity,
					transactionDTO.getRrn(),decryptedData,
					ErrorMessages.SERVER_ERROR.toString(),"2","Error Occurs during generating Checksum"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			//throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
			
			return createExceptionMap(transactionDTO, "Problem while generating Checksum");
		}
		logger.info("Generated checksum by Sha Alogo : "+chksum);
		logger.info("Checksum sent from request : "+transactionDTO.getChecksum());
		if (chksum.isEmpty() || transactionDTO.getChecksum().isEmpty()
				|| !(chksum.toUpperCase().equals(transactionDTO.getChecksum().toUpperCase()))){
			/*Object excepArr[] ={
					fromEntity,
					transactionDTO.getRrn(),decryptedData,
					ErrorMessages.CHECKSUM_NOT_MATCH.toString(),"2","Checksum doest not match"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			//throw new ApplicationException(ErrorMessages.CHECKSUM_NOT_MATCH.toString());
			
			return createExceptionMap(transactionDTO, ErrorMessages.CHECKSUM_NOT_MATCH.toString());
		}
		Map<String, Object> row = upiTransactionDAO.getTxnQRDetails(transactionDTO.getGatewayTransId());
		transactionDTO.setBankCode(PSPBankCodeMap.getBankCode(fromEntity));
		/**
		 * 
		 * Changes done for customer who does not have trid 
		 * contains only VPA so we are checking if transaction not found by trId
		 * we are finding transaction by VPA. 
		 * 
		 * Case 1 : if TrId present in request then get first record (one or multiple)
		 * CAse 2 : if vpa present & has only one record in detail table so return record
		 * Case 3 : If find multiple records for same VPA it will set terminalid as null  
		 */
		if(row!=null){
			setTxnQRDetails(row, transactionDTO);
		}else{
			Map<String, Object> row2 = upiTransactionDAO.getTxnQRDetailsFromVpa(transactionDTO.getMerchantVpa());
			transactionDTO.setBankCode(PSPBankCodeMap.getBankCode(fromEntity));
			if(row2!=null){
				setTxnQRDetailsVPA(row2, transactionDTO);
			}else{
				logger.info("UPI QR Transaction details not found");
				Object excepArr[] ={
						fromEntity,
						transactionDTO.getRrn(),decryptedData,
						ErrorMessages.TXN_QR_DETAIL_NOT_FOUND.toString(),"2","Details not found through vpa in detail table"
				};
				upiTransactionDAO.saveTransactionException(excepArr);
				//throw new ApplicationException(ErrorMessages.TXN_QR_DETAIL_NOT_FOUND.toString());
				return createExceptionMap(transactionDTO, ErrorMessages.TXN_QR_DETAIL_NOT_FOUND.toString());
			}
		}
		logger.debug("UPI txn details:"+transactionDTO);
		
		 /**Changes related to trId with refId swap value 
         *  
         *  trId   --> transactionDTO.getGatewayTransId()
         *  refId  --> transactionDTO.getMerchTransId()
         * 
         *  */
		Object dataArr[] = {
				transactionDTO.getMerchId(),
				transactionDTO.getMerchantVpa(), 
				transactionDTO.getQrCodeType(),
				transactionDTO.getTxnId(),
				transactionDTO.getGatewayTransId(), 
				transactionDTO.getRrn(),
				//HelperUtil.parseTimestamp(transactionDTO.getTransTimestamp(), "yyyy-mm-dd HH:mm:ss.S"),
				HelperUtil.parseTimestamp(transactionDTO.getTransTimestamp(), "yyyy-M-dd HH:mm:ss.S"),
				transactionDTO.getAmount(),
				transactionDTO.getBankCode(),
				transactionDTO.getCustomerVpa(),
				transactionDTO.getStatusCode(),
				transactionDTO.getRemarks(),							
				fromEntity,
				transactionDTO.getTerminalId(),
				transactionDTO.getMerchTransId(),
				null,null,null,null};
		try {
			upiTransactionDAO.saveUpiTxn(dataArr);
		} catch (Exception e) {
			logger.error(transactionDTO.getMerchTransId()+"|Exception while inserting in upi_transaction table:",e);
			/*Object excepArr[] ={
					fromEntity,
					transactionDTO.getRrn(),decryptedData,
					ErrorMessages.SERVER_ERROR.toString(),"2","Exception while inserting in upi_transaction table:"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			//throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
			return createExceptionMap(transactionDTO, ErrorMessages.SERVER_ERROR.toString());
		}
		Map<String, String> responseMap = new LinkedHashMap<>();
		responseMap.put("status", Constants.SUCCESS.name());
		responseMap.put("callBackstatusCode", transactionDTO.getStatusCode());
		responseMap.put("callBackstatusDescription", transactionDTO.getRemarks());
		responseMap.put("callBacktxnId", transactionDTO.getMerchTransId());
		responseMap.put("trId", transactionDTO.getGatewayTransId());
		responseMap.put("mid", transactionDTO.getMerchId());
		responseMap.put("bankCode", transactionDTO.getBankCode());
		//Added parameter merchVPA for checking transaction by VPA if fail to get transaction BY TrId 
		responseMap.put("merchVPA",transactionDTO.getMerchantVpa());
		responseMap.put("rrn",transactionDTO.getRrn());
		logger.info("upiTransactionCallback(String fromEntity, HttpServletRequest req) method execution ends");
		return responseMap;
		
	}

	/**
	 * This function sets the result of qr details in {@link MultiBankUPITransaction}
	 * @param rows database result received
	 * @param upiTxn instance of {@link MultiBankUPITransaction}
	 */
	private void setTxnQRDetails(Map<String,Object> row, KotakUPITransactionDTO upiTxn)
	{
		if(row!=null)
		{
				upiTxn.setMerchId((String)row.get("merch_id"));
				upiTxn.setQrCodeType((String)row.get("qr_type"));
				upiTxn.setTxnId((String)row.get("txn_id"));
				upiTxn.setBankCode((String)row.get("bank_code"));
				upiTxn.setTerminalId((String)row.get("terminal_id"));
		}
		
	}
	
	private void setTxnQRDetailsVPA(Map<String,Object> row, KotakUPITransactionDTO upiTxn)
	{
		if(row!=null)
		{
				upiTxn.setMerchId((String)row.get("merchant_id"));
				upiTxn.setBankCode((String)row.get("bank_code"));
				upiTxn.setTerminalId((String)row.get("TID"));
		}
		
	}



	private String validate(KotakUPITransactionDTO upiTxn){

		StringBuffer buffer=new StringBuffer();
		if(upiTxn.getMerchantVpa() == null || upiTxn.getMerchantVpa().isEmpty())
			buffer.append(" Merchant VPA is empty, ");
		if(upiTxn.getCustomerVpa() == null || upiTxn.getCustomerVpa().isEmpty())
			buffer.append(" Customer VPA is empty, ");
		if(upiTxn.getMerchTransId() == null || upiTxn.getMerchTransId().isEmpty())
			buffer.append(" Merchant Transaction Id is empty, ");
		if(upiTxn.getTransTimestamp() == null || upiTxn.getTransTimestamp().isEmpty())
			buffer.append(" Transaction time stamp is empty, ");
		if(upiTxn.getAmount() == null || upiTxn.getAmount().isEmpty())
			buffer.append(" Transaction amount is empty, ");
		if(upiTxn.getGatewayTransId() == null || upiTxn.getGatewayTransId().isEmpty())
			buffer.append(" Transaction gateway id is empty, ");
		if(upiTxn.getStatusCode() == null || upiTxn.getStatusCode().isEmpty())
			buffer.append(" Transaction gateway response code is empty, ");
		if(upiTxn.getRemarks() == null || upiTxn.getRemarks().isEmpty())
			buffer.append(" Transaction gateway response message code is empty, ");
		if(upiTxn.getRrn() == null || upiTxn.getMerchantVpa().isEmpty())
			buffer.append(" Rrn is empty, ");

		return buffer.toString();
	}
	
	/*private String validate( KotakUPITransactionDTO transactionDTO){

		StringBuffer buffer=new StringBuffer();
		if(transactionDTO.getMerchId() == null || transactionDTO.getMerchId().isEmpty())
			buffer.append(" Merchant code is empty,");
		if(transactionDTO.getMerchTransId() == null || transactionDTO.getMerchTransId().isEmpty())
			buffer.append(" Transaction Id is empty, ");
		if(transactionDTO.getAmount() == null || transactionDTO.getAmount().isEmpty())
			buffer.append(" Transaction amount is empty, ");
		if(transactionDTO.getRrn() == null || transactionDTO.getMerchantVpa().isEmpty())
			buffer.append(" Rrn is empty, ");

		return buffer.toString();
	}



	*//**
	 * This function sets the result of qr details in {@link KotakUPITransactionDTO}
	 * @param rows database result received
	 * @param transactionDTO instance of {@link KotakUPITransactionDTO}
	 *//*
	private void setTxnQRDetails(Map<String,Object> row, KotakUPITransactionDTO upiTxn)
	{
		if(row!=null)
		{
			upiTxn.setMerchId((String)row.get("merch_id"));
			upiTxn.setMerchantVpa((String)row.get("merch_vpa"));
			upiTxn.setQrCodeType((String)row.get("qr_type"));
			upiTxn.setTxnId((String)row.get("txn_id"));
			upiTxn.setBankCode((String)row.get("bank_code"));
			upiTxn.setTerminalId((String)row.get("terminal_id"));
		}
		else
		{
			logger.info("UPI QR Transaction details not found");
		}
	}*/
	private Object createExceptionMap(KotakUPITransactionDTO transactionDTO, String errorMessage) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("callBackstatusCode", "111");
		map.put("callBackstatusDescription", errorMessage);
		map.put("status", Constants.FAILED.name());
		if (transactionDTO != null) {
			map.put("callBacktxnId", transactionDTO.getMerchTransId());
			map.put("trId", transactionDTO.getGatewayTransId());
			map.put("mid", transactionDTO.getMerchId());
			map.put("bankCode", transactionDTO.getBankCode());
			//Added parameter merchVPA for checking transaction by VPA if fail to get transaction BY TrId 
			map.put("merchVPA",transactionDTO.getMerchantVpa());
			map.put("rrn",transactionDTO.getRrn());
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
}
