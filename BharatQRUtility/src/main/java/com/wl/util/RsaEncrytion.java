package com.wl.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.fasterxml.jackson.databind.deser.Deserializers.Base;

//import org.apache.log4j.Logger;

//import Decoder.BASE64Decoder;

public class RsaEncrytion {

	static X509EncodedKeySpec spec = null;
	static KeyFactory keyFactory = null;
	static String merchId;
	static String merchChanId;
	static String gateWayTxnId;
	static String mobNo;
	static String txnRefundAmount;
	static String txnRefundId;
	static String refundReason;
	static String checkSum;
	static String sId;
	private static int i = 0;

	public static PublicKey readPublicKey() throws Exception {
		// Atos Public Key
		String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3CNMzagOLKzq3nJG90yX"
				+ "Ozl8Gk9y8+Ab0drTT7HRoqEGBcOqQFDXm9IdC8QxEXxyqn+RwmC+AGsPByqKFUNZ"
				+ "QELVID1XQmTZfDensndSktpBdkL5iSpjOsl1kvO9108fBJjNjGlPP3yWvbfcdwsX"
				+ "zNFn/wxjcO841PuL8gi8k4lbJF9ClwYvZt+WCabMrK4M7Mrg6TLJgsBXFIK29U6y"
				+ "1jSvYhAmQVNGut23HbhzMrhSTVH1UlZzrmnHpqAxhmM+IKFFlSt3Fxe/IiaZBoOE"
				+ "22ZKuYrtmWuu+VEADRtb4o8KWy83sNaRvLxbnQxb0up5So5TdZvP5wF9QOKdVFFP" + "OQIDAQAB";

		// String pubKey = "";
		InputStream is = new ByteArrayInputStream(pubKey.getBytes("UTF-8"));
		byte[] keyBytes = new byte[is.available()];
		i = is.read(keyBytes);
		is.close();
		pubKey = pubKey.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
		/*BASE64Decoder decoder = new BASE64Decoder();
		keyBytes = decoder.decodeBuffer(pubKey);*/
		keyBytes = Base64.decodeBase64(pubKey);
		System.out.println(":" + i);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(spec);
		System.out.print(publicKey.getEncoded().toString());
		return publicKey;
	}

	public static String encrypt(String text, PublicKey key) {
		byte[] cipherText = null;
		String encrypted="";
		try {
			final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			//cipherText = cipher.doFinal(text.getBytes());
			 encrypted = Hex.encodeHexString(cipher.doFinal(text.getBytes())).toUpperCase();
			System.out.println(encrypted);
		} catch (Exception e) {
			System.out.println("[WebpaymentS2S] : ====== Exception occured while Encryption ===== : " + e);
		}
		return encrypted;
	}

	/*public static PrivateKey readPrivateKey(String merchId, String merchChanId) throws Exception {
		String privateKey = "";

		// add Private Key At our End
		InputStream is = new ByteArrayInputStream(privateKey.getBytes("UTF-8"));
		byte[] keyBytes = new byte[is.available()];
		i = is.read(keyBytes);
		is.close();

		privateKey = privateKey.replaceAll("(-+BEGIN PRIVATE KEY-+\\r?\\n|-+END PRIVATE KEY-+\\r?\\n?)", "");

		System.out.println(":" + i);
		BASE64Decoder decoder = new BASE64Decoder();
		keyBytes = decoder.decodeBuffer(privateKey);

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(spec);

	}*/

	public static String decrypt(byte[] text, PrivateKey key) {

		byte[] dectyptedText = null;

		try {
			final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			dectyptedText = cipher.doFinal(text);

		} catch (Exception e) {
			System.out.println("[WebpaymentS2S] : ====== Exception occured while decryption ===== : " + e);
		}
		return new String(dectyptedText);
	}

	public static void main(String[] args) throws NullPointerException, Exception {

		merchId = "ATOS";
		merchChanId = "ATOSAPP";
		gateWayTxnId = "null";
		mobNo = "8169220195";

		txnRefundAmount = "11.0";
		txnRefundId = "03712200004005261000200028709660758";

		refundReason = "Recon through File Matched/Unmatched Process";
		sId = "037122000040052";

		String refundCheckSumData = merchId.concat(merchChanId).concat(txnRefundId).concat(mobNo).concat(txnRefundAmount)
				.concat(gateWayTxnId).concat(refundReason).concat(sId);

		String encryptValue = RsaEncrytion.encrypt(refundCheckSumData.toString(), readPublicKey());
		//String checksums = javax.xml.bind.DatatypeConverter.printHexBinary(encryptValue);

		System.out.println("Encrypted \n" + encryptValue);

		//byte[] byteArry = javax.xml.bind.DatatypeConverter.parseHexBinary(checksums);
		//String decryptchecksum = RsaEncrytion.decrypt(byteArry, readPrivateKey("ATOS", "ATOSAPP"));
		//System.out.println("Decrypted \n" + decryptchecksum);

	}
}
