/*package com.wl.instamer.dao;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.wl.instamer.model.MerchantOnboard;
import com.wl.instamer.model.MerchantOnboardResponseData;

public class TestDetailDAO {
	

	private ApplicationContext context;

    @Before
    public void setUp()
    {
    	System.out.println("inside setup before calling");
        context = new ClassPathXmlApplicationContext("/instamer-servlet.xml");

    }


	
	MerchantOnboard request = new MerchantOnboard();
	MerchantOnboardResponseData response = new MerchantOnboardResponseData();
	
	
	@Test
	@Ignore
	public void testSetJdbcTemplate() {
		
	}

	@Test
	public void testSaveDetail() {
		DetailDAO   detail = (DetailDAO) context.getBean("detailDAO");
		response.setMerchantCode("001");
		request.setMcc(10);
		request.setBranchCode(111);
		request.setMerMobileNumber((long) 1111111119);
		request.setConFeeAmount(1000.0);
		response.setTid("0090");
		request.setBnfIfsc("012");
		request.setInstAddPincode(788);
		detail.saveDetail(request, response);
	}

	@Test
	@Ignore
	public void testUpdateDetails() {
		fail("Not yet implemented");
	}

}
*/