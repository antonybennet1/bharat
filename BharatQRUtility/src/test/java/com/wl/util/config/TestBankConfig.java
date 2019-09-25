/*package com.wl.util.config;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestBankConfig {
	
	ApplicationContext context; 
	
	@Before
	public void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext("/util-spring.xml");
	}

	@Test
	public void testGet() {
		String value = BankConfig.get("00006","max_attempts");
		Assert.assertNotNull(value);
	}

}
*/