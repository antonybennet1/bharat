/**
 * 
 */
package com.wl.upi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wl.upi.dao.UPITransactionDAO;
import com.wl.upi.model.AggregatorRequest;
import com.wl.upi.model.AggregatorTransAXISDTO;
import com.wl.upi.model.AggregatorTransDTO;
import com.wl.upi.model.IPGTransactionDTO;
import com.wl.upi.model.TCHRequest;
import com.wl.upi.model.UPITransactionDTO;
import com.wl.util.JsonUtility;
import com.wl.util.constants.Constants;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;
import com.wl.util.exceptions.DaoException;
import com.wl.util.model.Response;

/**
 * @author faizul.mahammad
 *
 */
@Service("upiQRService")
public class UPIQRServiceImpl implements UPIQRService {

	@Autowired
	private UPITransactionDAO upiTransactionDAO;

	private static Logger logger = LoggerFactory.getLogger(UPIQRServiceImpl.class);

	@Override
	public Response checkStatus(String fromEntity, String bankCode,  String data) {
		Response response = new Response();
		response.setStatus(Constants.FAILED.name());
		TCHRequest tchRequest = (TCHRequest) JsonUtility.parseJson(data, TCHRequest.class);
		if(tchRequest==null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		tchRequest.setFromEntity(fromEntity);
		List<String> args = new ArrayList<>(2);
		args.add(tchRequest.getTxnId());
		args.add(bankCode);
		// calling dao
		UPITransactionDTO upiTxn = null;
		try {
			switch(tchRequest.getProgramType())
			{
			case "2": 
				upiTxn = upiTransactionDAO.checkBharatQRTxn(tchRequest);
				break;
			case "5": 
				try {
					upiTxn = upiTransactionDAO.checkBharatQRTxn(tchRequest);
					if(upiTxn!=null)
						break;
				} catch (DaoException  e) {
					logger.debug(tchRequest.getTxnId()+":Transaction not found in bharatqr");
				}
			case "3": 
				upiTxn = upiTransactionDAO.checkUpiTxn(args.toArray());
				break;
			default:
				response.setMessage(ErrorMessages.PROGRAM_NOT_FOUND.toString());
			}
			if(upiTxn!=null)
			{
				response.setStatus(Constants.SUCCESS.name());
				response.setMessage("Transaction Available");
				HashMap<String, String> respObject = new HashMap<>();
				respObject.put("rrn", upiTxn.getRrn());
				respObject.put("authCode", upiTxn.getAuthCode());
				respObject.put("custVPA", upiTxn.getCustomerVpa());
				respObject.put("merchVPA", upiTxn.getMerchantVpa());
				response.setResponseObject(respObject);

				upiTransactionDAO.updateQRStatus(upiTxn.getMerchantTransactionId(), "S");
				logger.debug("updateQRStatus done");
			}
		} catch (Exception e) {
			if(e instanceof DaoException)
			{	
				logger.error("Exception in checkStatus"+e.getMessage());
				response.setStatus(Constants.PENDING.name());
				response.setMessage("Transaction Not Found");
			}
			else
				logger.error("Exception in checkStatus",e);	
		}
		logger.debug("response in upiqrserviceimpl -->" + response.getMessage());
		return response;
	}

	
	
	@Override
	public Response cancelQR(String fromEntity,  String bankCode,  String data) {
		Response response = new Response();
		TCHRequest upiQrModel = (TCHRequest) JsonUtility.parseJson(data, TCHRequest.class);
		if(upiQrModel==null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		upiQrModel.setFromEntity(fromEntity);
		upiQrModel.setBankCode(bankCode);
		// calling dao
		int count = upiTransactionDAO.cancelQR(upiQrModel);
		if (count == 0) {
			response.setStatus(Constants.FAILED.name());
			response.setMessage("Transaction Not Found");
		} else {
			response.setStatus(Constants.SUCCESS.name());
			response.setMessage("success");
		}
		logger.debug("response in upiqrserviceimpl -->" + response.getMessage());
		return response;
	}


	/**
	 *  Method will get record either from BhartQR (transId) or UPI (trId) for aggregator transaction
	 * 
	 * @see com.wl.upi.service.UPIQRService#checkAggregatorTransStatus(java.lang.String, java.lang.String, java.lang.String)
	 * 
	 *@return response with respObject contains mid, mspan, customername etc.
	 */
	@Override
	public Response checkAggregatorTransStatus(String fromEntity, String bankCode, String data) {
		logger.debug("In service checkAggregatorTransStatus(String fromEntity, String bankCode, String data) execution starts");
		AggregatorRequest aggregatorRequest = (AggregatorRequest) JsonUtility.parseJson(data, AggregatorRequest.class);
		if(aggregatorRequest==null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		if(((aggregatorRequest.getTrId() == null  && aggregatorRequest.getTxnId() == null) || (aggregatorRequest.getTid().isEmpty() && aggregatorRequest.getTxnId().isEmpty())) ||
				aggregatorRequest.getTid() == null || aggregatorRequest.getTid().isEmpty() ||
				aggregatorRequest.getAmount().isEmpty()){
			throw new ApplicationException(ErrorMessages.INPUT_NOT_VALID.toString());
		 }
		
		aggregatorRequest.setFromEntity(fromEntity);
		Response response = new Response();
		try{
			AggregatorTransDTO aggregatorTransDTO=upiTransactionDAO.checkAggregatorTxn(aggregatorRequest);
			if(aggregatorTransDTO!=null)
				logger.info(aggregatorTransDTO.toString());
			else
				logger.info("Dto is null");

			if (aggregatorTransDTO != null) {
				
			     if(aggregatorTransDTO.getTransType().equals("1")){
			        /*Change related to AXIS BANK MERCHANT_VPA response param*/
				        response.setStatus(Constants.SUCCESS.name());
						if("00031".equals(aggregatorTransDTO.getBankCode()) && aggregatorTransDTO.getTransType().equals("2")){
								AggregatorTransAXISDTO dto = new AggregatorTransAXISDTO();
								dto.setMid(aggregatorTransDTO.getMid());
								dto.setTxnAmt(aggregatorTransDTO.getTxnAmt());
								dto.setAuthCode(aggregatorTransDTO.getAuthCode());
								dto.setRefNo(aggregatorTransDTO.getRefNo());
								dto.setPrimaryId(aggregatorTransDTO.getPrimaryId());
								dto.setSettleAmt(aggregatorTransDTO.getSettleAmt());
							    dto.setTimeStamp(aggregatorTransDTO.getTimeStamp());
								//dto.setTransType((String)row.get("txn_type"));
								dto.setTransType(aggregatorTransDTO.getTransType());
								dto.setAggrId(aggregatorTransDTO.getAggrId());
								dto.setBankCode(aggregatorTransDTO.getBankCode());
								dto.setTxnCurr(aggregatorTransDTO.getTxnCurr());
								dto.setMerchantVpa(aggregatorTransDTO.getMerchantVpa());
								dto.setCustomerVpa(aggregatorTransDTO.getCustomerVpa());
								dto.setSecondaryId(aggregatorTransDTO.getSecondaryId());
								response.setResponseObject(dto);
						}else if(fromEntity.equals("01061991")){
							IPGTransactionDTO ipgdto = new IPGTransactionDTO();
							ipgdto.setMid(aggregatorTransDTO.getMid());
							ipgdto.setTxnAmt(aggregatorTransDTO.getTxnAmt());
							ipgdto.setAuthCode(aggregatorTransDTO.getAuthCode());
							ipgdto.setRefNo(aggregatorTransDTO.getRefNo());
							ipgdto.setPrimaryId(aggregatorTransDTO.getPrimaryId());
							ipgdto.setSettleAmt(aggregatorTransDTO.getSettleAmt());
							ipgdto.setTimeStamp(aggregatorTransDTO.getTimeStamp());
							//dto.setTransType((String)row.get("txn_type"));
							ipgdto.setTransType(aggregatorTransDTO.getTransType());
							ipgdto.setAggrId(aggregatorTransDTO.getAggrId());
							ipgdto.setBankCode(aggregatorTransDTO.getBankCode());
							ipgdto.setTxnCurr(aggregatorTransDTO.getTxnCurr());
							ipgdto.setMerchantVpa(aggregatorTransDTO.getMerchantVpa());
							ipgdto.setCustomerVpa(aggregatorTransDTO.getCustomerVpa());
							ipgdto.setSecondaryId(aggregatorTransDTO.getSecondaryId());
							ipgdto.setTid(aggregatorRequest.getTid());
							response.setResponseObject(ipgdto);
						}else{
							response.setResponseObject(aggregatorTransDTO);
						}
			      }else{
			    	  if(aggregatorTransDTO.getResponseCode().equals("00")
								|| aggregatorTransDTO.getResponseCode().equals("000")
								|| aggregatorTransDTO.getResponseCode().equalsIgnoreCase("s")
								|| aggregatorTransDTO.getResponseCode().equalsIgnoreCase("success")){
									response.setStatus(Constants.SUCCESS.name());
							    		  if("00031".equals(aggregatorTransDTO.getBankCode()) && aggregatorTransDTO.getTransType().equals("2")){
												AggregatorTransAXISDTO dto = new AggregatorTransAXISDTO();
												dto.setMid(aggregatorTransDTO.getMid());
												dto.setTxnAmt(aggregatorTransDTO.getTxnAmt());
												dto.setAuthCode(aggregatorTransDTO.getAuthCode());
												dto.setRefNo(aggregatorTransDTO.getRefNo());
												dto.setPrimaryId(aggregatorTransDTO.getPrimaryId());
												dto.setSettleAmt(aggregatorTransDTO.getSettleAmt());
											    dto.setTimeStamp(aggregatorTransDTO.getTimeStamp());
												//dto.setTransType((String)row.get("txn_type"));
												dto.setTransType(aggregatorTransDTO.getTransType());
												dto.setAggrId(aggregatorTransDTO.getAggrId());
												dto.setBankCode(aggregatorTransDTO.getBankCode());
												dto.setTxnCurr(aggregatorTransDTO.getTxnCurr());
												dto.setMerchantVpa(aggregatorTransDTO.getMerchantVpa());
												dto.setCustomerVpa(aggregatorTransDTO.getCustomerVpa());
												dto.setSecondaryId(aggregatorTransDTO.getSecondaryId());
												response.setResponseObject(dto);
										}else if(fromEntity.equals("01061991")){
											IPGTransactionDTO ipgdto = new IPGTransactionDTO();
											ipgdto.setMid(aggregatorTransDTO.getMid());
											ipgdto.setTxnAmt(aggregatorTransDTO.getTxnAmt());
											ipgdto.setAuthCode(aggregatorTransDTO.getAuthCode());
											ipgdto.setRefNo(aggregatorTransDTO.getRefNo());
											ipgdto.setPrimaryId(aggregatorTransDTO.getPrimaryId());
											ipgdto.setSettleAmt(aggregatorTransDTO.getSettleAmt());
											ipgdto.setTimeStamp(aggregatorTransDTO.getTimeStamp());
											//dto.setTransType((String)row.get("txn_type"));
											ipgdto.setTransType(aggregatorTransDTO.getTransType());
											ipgdto.setAggrId(aggregatorTransDTO.getAggrId());
											ipgdto.setBankCode(aggregatorTransDTO.getBankCode());
											ipgdto.setTxnCurr(aggregatorTransDTO.getTxnCurr());
											ipgdto.setMerchantVpa(aggregatorTransDTO.getMerchantVpa());
											ipgdto.setCustomerVpa(aggregatorTransDTO.getCustomerVpa());
											ipgdto.setSecondaryId(aggregatorTransDTO.getSecondaryId());
											ipgdto.setTid(aggregatorRequest.getTid());
											response.setResponseObject(ipgdto);
										}else{
											response.setResponseObject(aggregatorTransDTO);
										}
									
								}else{
									
									response.setStatus(Constants.PENDING.name());
									response.setMessage("Transaction Not Found");
								}
			    	  
			      }
			  logger.debug("Successfully set response object:"+aggregatorTransDTO);
		  }else{
				response.setStatus(Constants.PENDING.name());
				response.setMessage("Transaction Not Found");
			}
		} catch (Exception e) {
			if(e instanceof DaoException)
			{	
				logger.error("Exception in checkStatus"+e.getMessage());
				response.setStatus(Constants.PENDING.name());
				response.setMessage("Transaction Not Found");
			}else{
				e.printStackTrace();
				logger.error("Exception in checkStatus"+e.getMessage());
				response.setStatus(Constants.FAILED.name());
				response.setMessage("Server Internal error");
			}
		}
		logger.debug("In service checkAggregatorTransStatus(String fromEntity, String bankCode, String data) execution ends");
		return response;
	}



	
}
