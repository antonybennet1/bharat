package com.wl.bharatqr.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wl.bharatqr.util.URIConstant;
import com.wl.upi.service.ReconService;


@RestController
public class ReconController {

	
	@Autowired
	private ReconService reconService;
	
	private static final Logger logger = LoggerFactory.getLogger(ReconController.class);
	
	@RequestMapping(value = URIConstant.RECON, method = { RequestMethod.POST})
	public Object ReconGenerator(@PathVariable("bankCode") String bankCode, @PathVariable("jobParm") String jobParm ) throws Exception {
		if(bankCode!=null){
			try {
				return reconService.generateFile(bankCode,jobParm);
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}	
		}else{
			logger.info("Bank code is : " + bankCode );
		}
		return null;
	}
	
	@RequestMapping(value = URIConstant.DUMMY_EMP, method = RequestMethod.GET)
	public @ResponseBody HashMap<String,String> getDummyEmployee() {
		logger.info("Start getDummyEmployee");
		HashMap<String,String> emp = new HashMap<String,String>();
		
		emp.put("Hii", "Good Morning");
		
		//emp.setCreatedDate(new Date());
		//empData.put(9999, emp);
		return emp;
	}
	
}
	
	