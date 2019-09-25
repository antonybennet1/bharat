package com.wl.bharatqr.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wl.bharatqr.listeners.ContextRefreshListener;
import com.wl.bharatqr.util.URIConstant;
import com.wl.upi.service.SettlementService;
import com.wl.upi.util.Test;
import com.wl.util.AesUtil;
import com.wl.util.EncryptionCache;
import com.wl.util.HttpsClient;
import com.wl.util.config.ApplicationConfig;
import com.wl.util.constants.Constants;
import com.wl.util.model.Response;

@RestController
public class UtilController {

	private static final Logger logger = LoggerFactory.getLogger(UtilController.class);

	@Autowired
	@Qualifier("contextRefreshListener")
	private ContextRefreshListener contextRefreshListener;

	@Autowired
	@Qualifier("settlementService")
	private SettlementService settlementService;

	@ResponseBody
	@RequestMapping(value = URIConstant.RELOAD, method = {RequestMethod.GET})
	public Response reloadConfiguration(HttpServletRequest request)
	{
		Response resp = new Response();
		String ip = request.getRemoteAddr();
		logger.info("Request received from ip:"+ip+" for reloading config");
		if(ip.equals(ApplicationConfig.get("serverIP")))
		{
			//final String encryptedKey = "NlBxhN6ADhuBhLD+vcxwyuNwr6bEP+9Sh1TwV6XaMLV/+gVVlu/B8C3VVlb67v/oB3VFJ2yj+bQOoFc6ZJiIITmMZKnWcjfCDSpo+ylXQ35rRPVRECcJl60ouyZw9011+tyo8YaAp+dD4K/KlJqdP6mWVKIx/n9e4avy7LAx73M4iYxre10mq+7fWwkpaICP2Iw7UXLIs6Mo017fnbAy+CHLg9pxK/5sPlh2u8rwr7FullZbZrInmGJ+juh7b6RPHLCdRcRiYCFc3LTCBloLK7COCKCpWWzIFfy0DRDxVtJV7p8wXhOrnt47orXa6rBRmRlwQLs6f/ptPOmYJJFkHg==";
			//final String encryptedIv = "h6gjnwh0xJzwTvAeQZ5RrD2jkKjaA9xEJRGacnlR63c2bE+vAMujX+0FNHJZNuR68dqpZCyEIhpmWQ7vVMjv/yZF4f4Dz1NoMCyCrhhnZeALuVe0Nto2c4/BnOsfxnuk5RGCham8BZpJV+bcn2gfMizC63r3m0qo3YW62biz+/mZD6P6AfET97PaqpFjZ0Zo9xUjsETjztnNf3NGLCYCLEEeESZCCO3hcDdzPNFMNPWfY95JkLNkhTujzhBkGSjITD/qx472AYlwiGIZxY32v2WMEVdaTxrGJu2fv2wjqnrJoE1PmnG8BBl031g1yZQnTQhgzUKXmiE1tmRynfMOGQ==";
			contextRefreshListener.reloadConfig();
			resp.setStatus(Constants.SUCCESS.name());
		}
		else
		{
			resp.setStatus(Constants.FAILED.name());
			resp.setMessage("Unauthorized acess");
		}
		return resp;
	}


	@ResponseBody
	@RequestMapping(value = URIConstant.AES_ENCRYPT, method = {RequestMethod.GET})
	public Response encryptKeys(@RequestParam("data") String data,@RequestParam("fromEntity") String fromEntity)
	{
		Response resp = new Response();
		String encData = null;
		try {
			AesUtil aes = EncryptionCache.getEncryptionUtility(fromEntity);
			encData = aes.encrypt(data);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error Initializing AES util",e);
			resp.setStatus(Constants.FAILED.name());
		}
		resp.setStatus(Constants.SUCCESS.name());
		resp.setMessage(encData);
		return resp;
	}

	
	@ResponseBody
	@RequestMapping(value = URIConstant.PAN_DECRYPT, method = {RequestMethod.GET})
	public Response decryptKeys(@RequestParam("fromEntity") String fromEntity,@RequestParam("enc") String  request)
	{
		Response resp = new Response();
		String encData = "";
		String requestBody="";
		String decryptedData = "";
		String data = "";
		try {
			
			logger.info("request : " + request);
			//AesUtil aes = EncryptionCache.getEncryptionUtility(fromEntity);
			//requestBody = IOUtils.toString(request.getReader());
			//requestBody=new String(request.getBytes(),"UTF-8");
			//logger.info("request json:"+requestBody);
			 decryptedData=EncryptionCache.getEncryptionUtility(fromEntity).decrypt(request);
			//AesUtil aes = EncryptionCache.getEncryptionUtility(fromEntity);
			//data = new String(data.getBytes("UTF-8"));
			//data = new String(data.getBytes("UTF-8"));
			//decryptedData = aes.decrypt(request);
			logger.info("decrypted data :"+decryptedData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error Initializing AES util",e);
			resp.setStatus(Constants.FAILED.name());
		}
		resp.setStatus(Constants.SUCCESS.name());
		resp.setMessage(decryptedData);
		return resp;
	}
	/**
	 * Following service is used to update batchnumber in upi_batch_number table. This table will always have 1 record which is batch number. Cronjob will update this batch number every day
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = URIConstant.UPDATE_BATCHNUMBER, method = {RequestMethod.GET})
	public Response updateBatchNumber()
	{
		Response resp = settlementService.updateBatchNumber();
		logger.debug("cron for updating batch number:"+resp.getStatus());
		return resp;
	}
	
	
	@ResponseBody
	@RequestMapping(value = URIConstant.IPG_CON_UAT, method = {RequestMethod.GET})
	public Response ipgCon(HttpServletRequest request)
	{
		Response re = new Response();
		String jsonRequest = request.getParameter("parm");
		
		String uri="http://172.16.27.223:7004/bharatQrResponse";
		//String queryString="{\"txn_currency\":\"356\",\"customer_vpa\":\"9152992797@upi\",\"transaction_type\":\"2\",\"merchant_vpa\":\"051378247760000.mab@pnb\",\"secondary_id\":\"63795797519218242\",\"bank_code\":\"00051\",\"aggregator_id\":\"01061991\",\"primary_id\":\"90511422163795797519218111\",\"ref_no\":\"111111111111\",\"settlement_amount\":\"1.00\",\"mid\":\"051378247760002\",\"tr_id\":\"90511422163795797519218111\",\"txn_amount\":\"1.00\",\"time_stamp\":\"20190220142257\",\"tid\":\"12345678\"}";
		
		System.out.println("Request : " + jsonRequest);
		
		
		
		Map<String, String> map = new HashMap<>();
		map.put("Content-type", "application/json");
		System.out.println("Content-Type : application/json");
		
		
		String res =Test.send(uri, jsonRequest, map);
		re.setMessage(res);
		
		return re;
	}
	
	@ResponseBody
	@RequestMapping(value = URIConstant.IPG_CON_PROD, method = {RequestMethod.GET})
	public Response ipgHttpsCon(HttpServletRequest request)
	{
		Response re = new Response();
		String jsonRequest = request.getParameter("parm");
		
		String uri="http://172.16.27.223:7004/bharatQrResponse";
		//String queryString="{\"txn_currency\":\"356\",\"customer_vpa\":\"9152992797@upi\",\"transaction_type\":\"2\",\"merchant_vpa\":\"051378247760000.mab@pnb\",\"secondary_id\":\"63795797519218242\",\"bank_code\":\"00051\",\"aggregator_id\":\"01061991\",\"primary_id\":\"90511422163795797519218111\",\"ref_no\":\"111111111111\",\"settlement_amount\":\"1.00\",\"mid\":\"051378247760002\",\"tr_id\":\"90511422163795797519218111\",\"txn_amount\":\"1.00\",\"time_stamp\":\"20190220142257\",\"tid\":\"12345678\"}";
		
		System.out.println("Request : " + jsonRequest);
		
		
		
		Map<String, String> map = new HashMap<>();
		map.put("Content-type", "application/json");
		System.out.println("Content-Type : application/json");
		
		
		String res =HttpsClient.send(uri, jsonRequest, map);
		re.setMessage(res);
		
		return re;
	}
}
