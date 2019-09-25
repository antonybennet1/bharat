/*package com.wl.util.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestApplicationConfig {

	ApplicationContext context; 
	
	@Before
	public void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext("/util-spring.xml");
	}

	@Test
	public void testGet() {

		String value = ApplicationConfig.get("allowed_integrity_check");
		Assert.assertNotNull(value);
	}

}
*/