package com.wl.bharatqr.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wl.bharatqr.util.URIConstant;
import com.wl.upi.model.MMSTransDTO;
import com.wl.upi.model.TtmsTransactiosDto;
import com.wl.upi.service.BharatQRService;
import com.wl.upi.service.PSPServiceFactory;
import com.wl.upi.service.PSPTransactionService;
import com.wl.upi.service.UPIBankService;
import com.wl.upi.service.UPIServiceFactory;
import com.wl.util.JsonUtility;
import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;
import com.wl.util.model.Request;
import com.wl.util.model.Response;

/**
 * Controller for UPI related Transactions
 * @author kunal.surana
 */
@RestController
public class TransactionController {
	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	@Qualifier("upiServiceFactory")
	private UPIServiceFactory upiServiceFactory;

	@Autowired
	@Qualifier("pspServiceFactory")
	private PSPServiceFactory pspServiceFactory;


	@Autowired
	private BharatQRService bharatQRService;
	/**
	 * URL for UPI Collect API.  
	 * This functionality is currently not being used. Kindly remove this from Java Doc once this is being used
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = URIConstant.COLLECT_UPI, method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = {
					MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public Response collectAPI(HttpServletRequest request) {
		String jsonRequest = request.getParameter("data");
		String fromEntity = request.getParameter("fromEntity");
		String bankCode = request.getParameter("bankCode");
		UPIBankService upiBankService =  upiServiceFactory.getUPIService(bankCode);
		logger.info("json request to qrservice :" + jsonRequest);
		return upiBankService.upiCollectService(fromEntity,jsonRequest);
	}

	/**
	 * URL for UPI Callback request from Bank PSP
	 * @param fromEntity this will name of bank psp as present in WL URL
	 * @param req this is {@link HttpServletRequest}
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = URIConstant.CALLBACK_UPI, method = { RequestMethod.POST}, consumes = {
			MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_FORM_URLENCODED_VALUE,MediaType.APPLICATION_XML_VALUE},
	produces={MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE})
	public Object callBackAPI(@PathVariable("fromEntity") String fromEntity, HttpServletRequest request) {
		logger.debug("Request received from PSP :"+fromEntity);
		logger.info("REquest --------> " + request);
		PSPTransactionService pspService =  pspServiceFactory.getPSPTxnService(fromEntity);
		Map<String,String> resp = (Map<String, String>) pspService.upiTransactionCallback(fromEntity, request);
		String responseCode=resp.get("callBackstatusCode");
		if (responseCode != null && ("00".equals(responseCode) || "000".equals(responseCode) || "s".equalsIgnoreCase(responseCode)
				|| "success".equalsIgnoreCase(responseCode))) {
			logger.info("Notification for request has been initiated.");
			bharatQRService.notifyForUpiTxn(resp);
		}else{
			logger.info("Notification for request has been not initiated due to fail response code."+responseCode);
		}
		String  tempRRn =resp.get("rrn");
		resp.remove("trId");
		resp.remove("bankCode");
		resp.remove("mid");
		resp.remove("merchVPA");
		resp.remove("rrn");
		if(resp.containsKey("xmlResponse")){
			System.out.println("xmlResponse "+resp.get("xmlResponse"));
			logger.info(tempRRn + "| sending response back to PSP "+resp);
			return resp.get("xmlResponse");
		}
		else
			logger.info("sending response back to PSP "+resp);
			return resp;
	}

	/**
	 * URL for BharatQR Callback request from Magnus
	 * @param parm json data sent by Magnus
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = URIConstant.CALLBACK_BHARATQR, method = { RequestMethod.GET})
	public Object bharatqrCallBackAPI(@RequestParam String parm) {
		logger.debug("Bharat QR TXN Request received from magnus :"+parm);
		Map<String,String> resp = (Map<String, String>) bharatQRService.populateBharatQRTxn(parm);

		bharatQRService.notifyForBharatQRTxn(resp);
		String tempRrn=resp.get("rrn"); 
		resp.remove("id");
		resp.remove("bankCode");
		resp.remove("mid");
		resp.remove("programType");
		resp.remove("rrn");
		logger.info(tempRrn + " | sending response back to magnus"+resp);
		return resp;
	}

	@ResponseBody
	@RequestMapping(value = {URIConstant.REFUND , URIConstant.REFUND_APP}, method = { RequestMethod.POST, RequestMethod.GET }, consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public Response refundTransaction(HttpServletRequest request) {
		String jsonRequest = request.getParameter("parm");
		Request reqObject = null;
		if(jsonRequest!=null && !jsonRequest.isEmpty())
		{
			logger.info("json request to refundTransaction :" + jsonRequest);
			reqObject = (Request) JsonUtility.parseJson(jsonRequest, Request.class);
			if (reqObject == null)
				throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
			logger.debug(reqObject.getFromEntity() + "|refundTransaction - data :" + reqObject.getData());
			return bharatQRService.refund(reqObject.getFromEntity(), reqObject.getBankCode(), JsonUtility.convertToJson(reqObject.getData()));
		}
		else
		{
			logger.debug("json request to refundTransaction recevied from APP");
			String data = request.getParameter("data");
			String fromEntity = request.getParameter("fromEntity");
			String bankCode = request.getParameter("bankCode");
			return bharatQRService.refund(fromEntity, bankCode, data);
		}
		
	}
	
	
	/*@ResponseBody
	@RequestMapping(value = URIConstant.CALLBACK_MMS, method = { RequestMethod.GET})
	public Object ttmsCallBackApi(@RequestParam String parm) {
		logger.debug("Bharat QR TXN Request received from TTMS :"+parm);
		Map<String,String> resp = (Map<String, String>) bharatQRService.populateTTMS(parm);

		bharatQRService.notifyForMMSTxn(resp);
		String tempRrn=resp.get("rrn"); 
		resp.remove("id");
		resp.remove("bankCode");
		resp.remove("mid");
		resp.remove("programType");
		resp.remove("rrn");
		logger.info(tempRrn + " | sending response back to MRL"+resp);
		return resp;
	}*/
	
	
	
	@ResponseBody
	@RequestMapping(value = URIConstant.CALLBACK_MMS, method = { RequestMethod.POST, RequestMethod.GET }, consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public Object ttmsCallBackApi(HttpServletRequest request) {
		
		String jsonRequest = request.getParameter("parm");
		Request reqObject = null;
		if(jsonRequest!=null && !jsonRequest.isEmpty())
		{
			logger.debug("Bharat QR TXN Request received from TTMS :"+jsonRequest);
			Map<String,String> resp = (Map<String, String>) bharatQRService.populateTTMS(jsonRequest);

			bharatQRService.notifyForMMSTxn(resp);
			String tempRrn=resp.get("rrn"); 
			resp.remove("id");
			resp.remove("bankCode");
			resp.remove("mid");
			resp.remove("programType");
			resp.remove("rrn");
			logger.info(tempRrn + " | sending response back to MRL"+resp);
			return resp;
		}else{
			
		}
		return reqObject;
	}
	
	@ResponseBody
	@RequestMapping(value = URIConstant.CALLBACK_MMS_POST, method = { RequestMethod.POST, RequestMethod.GET }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public Object ttmsCallBackApiPOST(@RequestBody MMSTransDTO ttmsDto) {
		
		/*String jsonRequest = request.getParameter("parm");
		Request reqObject = null;
		if(jsonRequest!=null && !jsonRequest.isEmpty())
		{*/
			logger.debug("Bharat QR TXN Request received from TTMS :"+ttmsDto);
			Map<String,String> resp = (Map<String, String>) bharatQRService.populateTTMSRequestBOdy(ttmsDto);

			bharatQRService.notifyForMMSTxn(resp);
			String tempRrn=resp.get("rrn"); 
			resp.remove("id");
			resp.remove("bankCode");
			resp.remove("mid");
			resp.remove("programType");
			resp.remove("rrn");
			logger.info(tempRrn + " | sending response back to MRL"+resp);
			return resp;
		/*}else{
			
		}
		return reqObject;*/
		
		
	}
	
	/*@ResponseBody
	@RequestMapping(value = URIConstant.REFUND, method = { RequestMethod.POST, RequestMethod.GET }, consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public Response refundTransactionAXIS(HttpServletRequest request) {
		String jsonRequest = request.getParameter("parm");
		logger.info("json request to refundTransaction :" + jsonRequest);
		Request reqObject = (Request) JsonUtility.parseJson(jsonRequest, Request.class);
		if (reqObject == null)
			throw new ApplicationException(ErrorMessages.JSON_FORMAT_ERROR.toString());
		logger.debug(reqObject.getFromEntity() + "|refundTransaction - data :" + reqObject.getData());
		UPIBankService upiBankService =  upiServiceFactory.getUPIService(reqObject.getBankCode());
		return    upiBankService.upiRefund(reqObject.getFromEntity(), reqObject.getBankCode() , JsonUtility.convertToJson(reqObject.getData())); 
	}*/

	@ResponseBody
	@RequestMapping(value = URIConstant.CALLBACK_UPI_TEST, method = { RequestMethod.POST}, consumes = {
			MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_FORM_URLENCODED_VALUE,MediaType.APPLICATION_XML_VALUE},
	produces={MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE})
	public Object callBackAPITest(@PathVariable("fromEntity") String fromEntity, HttpServletRequest request) {
		logger.debug("Request received from PSP :"+fromEntity);
		logger.info("REquest --------> " + request);
		PSPTransactionService pspService =  pspServiceFactory.getPSPTxnService(fromEntity);
		Map<String,String> resp = (Map<String, String>) pspService.upiTransactionCallback(fromEntity, request);
		String responseCode=resp.get("callBackstatusCode");
		if (responseCode != null && ("00".equals(responseCode) || "000".equals(responseCode) || "s".equalsIgnoreCase(responseCode)
				|| "success".equalsIgnoreCase(responseCode))) {
			logger.info("Notification for request has been initiated.");
			bharatQRService.notifyForUpiTxn(resp);
		}else{
			logger.info("Notification for request has been not initiated due to fail response code."+responseCode);
		}
		String  tempRRn =resp.get("rrn");
		resp.remove("trId");
		resp.remove("bankCode");
		resp.remove("mid");
		resp.remove("merchVPA");
		resp.remove("rrn");
		if(resp.containsKey("xmlResponse")){
			System.out.println("xmlResponse "+resp.get("xmlResponse"));
			logger.info(tempRRn + "| sending response back to PSP "+resp);
			return resp.get("xmlResponse");
		}
		else
			logger.info("sending response back to PSP "+resp);
			return resp;
	}
	
	 @ResponseBody
	 @RequestMapping(method = RequestMethod.GET, value = "/getTransactionCount")
	    public Object getTransactionCount(@RequestParam Map<String, String> inputFields) {
		return bharatQRService.getCount(inputFields.get("bank_code"),inputFields.get("date"));
	    }
	 
	 


}
