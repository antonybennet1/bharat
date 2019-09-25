package com.wl.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashGeneration {

	private static final Logger logger = LoggerFactory
			.getLogger(HashGeneration.class);
	
	public static char ALGO[] = {'M','D','5'};

	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public static String convertToMD5(String text) {
		logger.debug("convert to md5:"+text);
		MessageDigest md;
		byte[] md5hash = new byte[32];
		try {

			md = MessageDigest.getInstance(new String(ALGO));
			md.update(text.getBytes("iso-8859-1"), 0, text.length());
			md5hash = md.digest();
		} catch (NoSuchAlgorithmException ex) {
			logger.info(HashGeneration.class.getName() + ex.toString());
		} catch (UnsupportedEncodingException ex) {
			logger.info(HashGeneration.class.getName() + ex.toString());
		}
		return convertToHex(md5hash);
	}
	
	public static void main(String[] args) {
		System.out.println(convertToMD5("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f4gSEGChrx1qyBVGTgHcVk10"));
	}
}
