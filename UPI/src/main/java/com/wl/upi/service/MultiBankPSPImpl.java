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
import com.wl.upi.model.TxnDTO;
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
 * @author ritesh.patil
 *
 */
@Service("multiBankPSPImpl")
public class MultiBankPSPImpl implements PSPTransactionService{


	@Autowired
	private UPITransactionDAO upiTransactionDAO;

	private static Logger logger = LoggerFactory.getLogger(MultiBankPSPImpl.class);


	@SuppressWarnings("unchecked")
	@Override
	public Object upiTransactionCallback(String fromEntity, HttpServletRequest req) {

		logger.info("upiTransactionCallback(String fromEntity, HttpServletRequest req) method execution starts for "+ fromEntity);
		String data = "";
		String decryptedData = "";
		String requestBody="";
		try {
			 requestBody = IOUtils.toString(req.getReader());
			requestBody=new String(requestBody.getBytes(),"UTF-8");
			logger.info("request json:"+requestBody);
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
			logger.error("Aes Initialization failed",e);
			/*Object excepArr[] ={
					fromEntity,
					"",requestBody,
					ErrorMessages.SERVER_ERROR.toString(),"2","Aes Initialization failed while decryption"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}

		MultiBankUPITransaction upiTxn = (MultiBankUPITransaction) JsonUtility.parseJson(decryptedData,
				MultiBankUPITransaction.class);
		String validateStr=validate(upiTxn);
		if(!validateStr.isEmpty()){
			/*Object excepArr[] ={
					fromEntity,
					upiTxn.getRrn(),decryptedData,
					ErrorMessages.JSON_FORMAT_ERROR.toString(),"2","Some Mandatory fields are not present"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			
			return createExceptionMap(upiTxn, ErrorMessages.JSON_FORMAT_ERROR.toString());
			//throw new ApplicationException("Exception cause validation "+validateStr + ErrorMessages.JSON_FORMAT_ERROR.toString());
			}


		String chksum=null;
		try{
			String chksumData = upiTxn.getMerchId()+upiTxn.getMerchantVpa()+upiTxn.getCustomerVpa()+upiTxn.getMerchTransId()+
					upiTxn.getTransTimestamp()+upiTxn.getTransactionAmount()+upiTxn.getGatewayTransId()+upiTxn.getGatewayResCode()+upiTxn.getGatewayReseMsg()+upiTxn.getRrn();
			logger.debug("Checksum generated values are "+chksumData);
			chksum = SHA256CheckSum.genChecksum(chksumData);
		}catch(Exception e){
			logger.error("Problem at the time of genrating Checksum "+e);
			/*Object excepArr[] ={
					fromEntity,
					upiTxn.getRrn(),decryptedData,
					ErrorMessages.SERVER_ERROR.toString(),"2","Error Occurs during generating Checksum"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			//throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
			return createExceptionMap(upiTxn, "Problem while genrating Checksum");
		}
		logger.info("Generated checksum by Sha Alogo : "+chksum);
		logger.info("Checksum sent from request : "+upiTxn.getChecksum());
		if (chksum.isEmpty() || upiTxn.getChecksum().isEmpty()
				|| !(chksum.toUpperCase().equals(upiTxn.getChecksum().toUpperCase()))){
			/*Object excepArr[] ={
					fromEntity,
					upiTxn.getRrn(),decryptedData,
					ErrorMessages.CHECKSUM_NOT_MATCH.toString(),"2","Checksum doest not match"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			//throw new ApplicationException(ErrorMessages.CHECKSUM_NOT_MATCH.toString());
			return createExceptionMap(upiTxn, ErrorMessages.CHECKSUM_NOT_MATCH.toString());
			}

		/*TxnDTO txnDetail = upiTransactionDAO.getUpiTxnDetails(upiTxn.getMerchTransId());
		if(txnDetail!=null && "2".equals(txnDetail.getQrType()))
			throw new ApplicationException(ErrorMessages.TXN_ALREADY_DONE.toString());*/
		
		Map<String, Object> row = upiTransactionDAO.getTxnQRDetails(upiTxn.getMerchTransId());
		upiTxn.setBankCode(PSPBankCodeMap.getBankCode(fromEntity));
		/*if(row ==null)
			throw new ApplicationException(ErrorMessages.TXN_QR_DETAIL_NOT_FOUND.toString());*/
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
			setTxnQRDetails(row, upiTxn);
		}else{
			Map<String, Object> row2 = upiTransactionDAO.getTxnQRDetailsFromVpa(upiTxn.getMerchantVpa());
			if(row2!=null){
				logger.debug("Getting TID from VPA ==> " + (String)row2.get("TID"));
				upiTxn.setMerchId((String)row2.get("merchant_id"));
				upiTxn.setQrCodeType("0");
				upiTxn.setTxnId(upiTxn.getTxnId());
				upiTxn.setBankCode((String)row2.get("bank_code"));
				upiTxn.setTerminalId((String)row2.get("TID"));
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
		

		Object dataArr[] = {
				upiTxn.getMerchId(),
				upiTxn.getMerchantVpa(), 
				upiTxn.getQrCodeType(),
				upiTxn.getTxnId(),
				upiTxn.getMerchTransId(), 
				upiTxn.getRrn(),
				upiTxn.getTransTimestamp(),
				upiTxn.getTransactionAmount(),
				upiTxn.getBankCode(),
				upiTxn.getCustomerVpa(),
				upiTxn.getGatewayResCode(),
				upiTxn.getGatewayReseMsg(),							
				fromEntity,
				upiTxn.getTerminalId(),
				upiTxn.getGatewayTransId(),
				null,null,null,null};
		try {
			upiTransactionDAO.saveUpiTxn(dataArr);
		} catch (Exception e) {
			logger.error(upiTxn.getMerchTransId()+"|Exception while inserting in upi_transaction table:",e);
			/*Object excepArr[] ={
					fromEntity,
					upiTxn.getRrn(),decryptedData,
					ErrorMessages.SERVER_ERROR.toString(),"2","Exception while inserting in upi_transaction table:"
			};
			upiTransactionDAO.saveTransactionException(excepArr);*/
			//throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
			return createExceptionMap(upiTxn, ErrorMessages.SERVER_ERROR.toString());
		}
		Map<String, String> responseMap = new LinkedHashMap<>();
		responseMap.put("status", Constants.SUCCESS.name());
		responseMap.put("callBackstatusCode", upiTxn.getGatewayResCode());
		responseMap.put("callBackstatusDescription", upiTxn.getGatewayReseMsg());
		responseMap.put("callBacktxnId", upiTxn.getMerchTransId());
		responseMap.put("trId", upiTxn.getMerchTransId());
		responseMap.put("mid", upiTxn.getMerchId());
		responseMap.put("bankCode", upiTxn.getBankCode());
		//Added parameter merchVPA for checking transaction by VPA if fail to get transaction BY TrId 
		responseMap.put("merchVPA",upiTxn.getMerchantVpa());
		responseMap.put("rrn",upiTxn.getRrn());
		logger.info("upiTransactionCallback(String fromEntity, HttpServletRequest req) method execution ends");
		return responseMap;
	}


	/**
	 * This function sets the result of qr details in {@link MultiBankUPITransaction}
	 * @param rows database result received
	 * @param upiTxn instance of {@link MultiBankUPITransaction}
	 */
	private void setTxnQRDetails(Map<String,Object> row, MultiBankUPITransaction upiTxn)
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


	private String validate(MultiBankUPITransaction upiTxn){

		StringBuffer buffer=new StringBuffer();
		if(upiTxn.getMerchantVpa() == null || upiTxn.getMerchantVpa().isEmpty())
			buffer.append(" Merchant VPA is empty, ");
		if(upiTxn.getCustomerVpa() == null || upiTxn.getCustomerVpa().isEmpty())
			buffer.append(" Customer VPA is empty, ");
		if(upiTxn.getMerchTransId() == null || upiTxn.getMerchTransId().isEmpty())
			buffer.append(" Merchant Transaction Id is empty, ");
		if(upiTxn.getTransTimestamp() == null || upiTxn.getTransTimestamp().isEmpty())
			buffer.append(" Transaction time stamp is empty, ");
		if(upiTxn.getTransactionAmount() == null || upiTxn.getTransactionAmount().isEmpty())
			buffer.append(" Transaction amount is empty, ");
		if(upiTxn.getGatewayTransId() == null || upiTxn.getGatewayTransId().isEmpty())
			buffer.append(" Transaction gateway id is empty, ");
		if(upiTxn.getGatewayResCode() == null || upiTxn.getGatewayResCode().isEmpty())
			buffer.append(" Transaction gateway response code is empty, ");
		if(upiTxn.getGatewayReseMsg() == null || upiTxn.getGatewayReseMsg().isEmpty())
			buffer.append(" Transaction gateway response message code is empty, ");
		if(upiTxn.getRrn() == null || upiTxn.getMerchantVpa().isEmpty())
			buffer.append(" Rrn is empty, ");

		return buffer.toString();
	}
	
	private Object createExceptionMap(MultiBankUPITransaction upiTxn, String errorMessage) {

		Map<String, String> map = new LinkedHashMap<>();
		map.put("callBackstatusCode", "111");
		map.put("callBackstatusDescription", errorMessage);
		map.put("status", Constants.FAILED.name());
		if (upiTxn != null) {
			
			map.put("callBacktxnId", upiTxn.getMerchTransId());
			map.put("trId", upiTxn.getGatewayTransId());
			map.put("mid", upiTxn.getMerchId());
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
}
