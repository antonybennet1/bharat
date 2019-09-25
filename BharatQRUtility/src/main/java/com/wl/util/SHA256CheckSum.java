package com.wl.util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wl.util.constants.ErrorMessages;
import com.wl.util.exceptions.ApplicationException;

/**
 * This function is used for checksum generation and verification. 
 * @author kunal.surana
 */

public class SHA256CheckSum {

	
	private static final Logger logger = LoggerFactory.getLogger(SHA256CheckSum.class);
	/**
	 * Testing purpose
	 * 
	 * @param args
	 * @throws NoSuchAlgorithmException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {
		/*String originalcksm = "CC8999AA86DD10C474CFFA8047B332F01A2766FE54E7A59A4864799FD624C5C8";
		System.out.println(genChecksum(
				"merchantCode=MERCHANT123|reservationId=100000274061812|txnAmount=458.00|currencyType=INR|appCode=ET|pymtMode=Internet|txnDate=20150717|securityID=CRIS|RU=http://localhost:7001/eticketing/BankResponse"));
		String geneartedcksm = genChecksum(
				"merchantCode=MERCHANT123|reservationId=100000274061812|txnAmount=458.00|currencyType=INR|appCode=ET|pymtMode=Internet|txnDate=20150717|securityID=CRIS|RU=http://localhost:7001/eticketing/BankResponse");
		if (originalcksm.equals(geneartedcksm.toUpperCase())) {
			System.out.println(" !!!!!  Matching checksum found ");
		}*/
		//a260da9a7a61d18c5404948e4b1f54973f8356a303237b52ffc4aac3f26017fd
		String yesChecksum="pranav";
		//String yesChecksum = "aditya@sbiYES0000000010083SBID58BA1E806E64D43BB42A30620E2017:10:03 04:21:411.00727616009706SBI0C5307EFDCF34319922ED4452480F4C900Success";
		System.out.println("Yes CheckSum:"+genChecksum(yesChecksum).toUpperCase());
	}

	/**
	 * Computes SHA256 checksum
	 * @param input data for which checksum needs to be computed
	 * @return returns hex value of hash generated
	 */
	public static String genChecksum(String input) {
		MessageDigest mDigest=null;
		try {
			mDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new ApplicationException(ErrorMessages.SERVER_ERROR.toString());
		}
		byte[] result = mDigest.digest(input.getBytes());
		return HelperUtil.hex(result);
	}

	/**
	 * verifies checksum against the input data sent
	 * @param input input data of request for which checksum needs to be verified
	 * @param checkSum checksum received in request
	 * @return true if checksum is verified else false
	 */
	public static boolean verCheckSum(String input, String checkSum) {
		String generatedChecksum = genChecksum(input);
		logger.debug("input for checksum generation:"+input);
		logger.debug("Generated checksum :"+generatedChecksum);
		logger.debug("Checksum received :"+checkSum);
		if (checkSum!=null && checkSum.toUpperCase().equals(generatedChecksum.toUpperCase())) // checksum check
		{
			return true;
		} else {
			return false;
		}
	}

}
