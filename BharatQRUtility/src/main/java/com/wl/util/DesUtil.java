package com.wl.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DesUtil {
	private static Logger logger = LoggerFactory.getLogger(DesUtil.class);
	private static String iv;
	private static final String ENCRYPTION_ALGORITHM= "DESede/CBC/PKCS5Padding";
	private static Cipher cipher;
	private static SecretKey key;
	private static char[] keyStr;
	
	public static void init(String encryptedKey, String encryptedIv) throws Exception
	{
		try {
			logger.debug("3DES|Encrypted key:"+encryptedKey);
			logger.debug("3DES|Encrypted iv:"+encryptedIv);
			cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
			key = generateKey(AesUtil.decryptSecretKey(encryptedKey));
			iv = AesUtil.decryptSecretKey(encryptedIv);
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | NoSuchProviderException e) {
			// TODO Auto-generated catch block
			logger.error("Error while generating key or iv",e);
			throw new Exception("3DES key generation error");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error while generating key or iv",e);
			throw new Exception("3DES key generation error");
		}
	}

	public static String encrypt(String plaintext) {
		try {
			byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
			return HelperUtil.base64(encrypted);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
	}

	public static String decrypt(String ciphertext) {
		
		ciphertext = ciphertext.replaceAll(" ","+");
		if(ciphertext!=null)
		{    	
			try {
				byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, HelperUtil.base64(ciphertext));
				return new String(decrypted, "UTF-8");
			}
			catch (Exception e) {
				logger.debug("Exception in received request cipher text");
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	private static byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes)throws Exception {
			//cipher.init(encryptMode, key, new IvParameterSpec(HelperUtil.hex(iv)));
			cipher.init(encryptMode, key, new IvParameterSpec(iv.getBytes()));
			return cipher.doFinal(bytes);
	}

	
	private static SecretKey generateKey(String key)throws Exception {
		  logger.debug("des key:"+key);
		  keyStr = key.toCharArray();
	      DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
	      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
	      SecretKey theKey = keyFactory.generateSecret(spec);
	      return theKey;
	}

	/**
	 * @return the keyStr
	 */
	public static char[] getKeyStr() {
		return keyStr;
	}

}
