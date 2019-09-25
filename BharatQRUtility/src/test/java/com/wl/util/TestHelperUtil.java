/*package com.wl.util;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.apache.commons.codec.DecoderException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestHelperUtil {

	private static final Logger logger = LoggerFactory.getLogger(TestHelperUtil.class);
	
	@Test
	public final void testBase64ByteArray() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testBase64String() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testHexByteArray() throws DecoderException {
		byte[] unhex = HelperUtil.hex("00000000000000000000000000000000");
		System.out.println(unhex);
	}

	@Test
	public final void testHexString() throws DecoderException {
		byte[] b = HelperUtil.hex("F27D5C9927726BCEFE7510B1BDD3D137");
		logger.debug(""+new String(b).length());
		Assert.assertNotNull(b);
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRandom() {
		fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testParseTimestamp()	{
		
		Timestamp t = HelperUtil.parseTimestamp("2017-11-09 12:16:57", "yyyy-MM-dd HH:mm:ss");
		logger.info("SQL timestamp :"+t);
		Assert.assertNotNull(t);
		
	}

}
*/