package com.wl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/*import org.apache.tomcat.util.codec.DecoderException;
import org.apache.tomcat.util.codec.binary.Base64;*/
import org.apache.commons.codec.DecoderException;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Implement 256-bit version like: http://securejava.wordpress.com/2012/10/25/aes-256/
public class AesUtil {
	private static Logger logger = LoggerFactory.getLogger(AesUtil.class);
	private static final int KEY_SIZE = 128;
	private static final int ITERATION_COUNT= 10;
	private static final String ENCRYPTION_ALGORITHM= "AES/CBC/PKCS5Padding";
	private static final String KEY_ENCRYPTION_ALGORITHM = "RSA/ECB/PKCS1Padding";
	private byte[] iv;
	private String ivStr;
	private String salt ;
	private Cipher cipher;
	private SecretKey key;
	private static PublicKey publicKey;
	private static PrivateKey privateKey;
	boolean keyHexed = false;
	boolean dataHexed = false;
	
	/**
	 * @return the keyHexed
	 */
	public boolean isKeyHexed() {
		return keyHexed;
	}
	/**
	 * @param keyHexed the keyHexed to set
	 */
	public void setKeyHexed(boolean keyHexed) {
		this.keyHexed = keyHexed;
	}

	/**
	 * @return the dataHexed
	 */
	public boolean isDataHexed() {
		return dataHexed;
	}
	/**
	 * @param dataHexed the dataHexed to set
	 */
	public void setDataHexed(boolean dataHexed) {
		this.dataHexed = dataHexed;
	}



	static {
		//AesUtil.setPublicKey("/usr/local/apache-tomcat-7.0.72/conf/paymod115.cer");
		//AesUtil.setPublicKey("D:\\WL-DATA\\documents\\PROJECTS\\mvisa_merchant_workspace\\BharatQR\\src\\main\\resources\\paymod115.cer");
		//Security.addProvider(new BouncyCastleProvider());
	}
	
	/**
	 * 
	 * @param encryptedKey 
	 * @param encryptedIv
	 * @param encryptedSalt
	 * @throws Exception 
	 */
	public void init(String encryptedKey, String encryptedIv, String encryptedSalt) throws Exception
	{
		try {
			logger.debug("Aes|Encrypted key:"+encryptedKey);
			logger.debug("AES|Encrypted iv:"+encryptedIv);
			logger.debug("AES|Encrypted salt:"+encryptedSalt);
			ivStr = AesUtil.decryptSecretKey(encryptedIv);
			salt = AesUtil.decryptSecretKey(encryptedSalt);
			logger.debug("IV dec :"+ivStr);
			logger.debug("SALT dec :"+salt);
			key = generateKey(salt, AesUtil.decryptSecretKey(encryptedKey));
			if(ivStr!=null)
				cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
			else
				cipher = Cipher.getInstance("AES");
		}catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error while generating key or iv",e);
			throw new Exception("AES initialization failed");
		}
	}
	
	 

	public  String encrypt(String plaintext) {
		try {
			logger.info("key -----> " + key );
			logger.info("iv -----> " + iv );
			byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
			if(isDataHexed())
				return HelperUtil.hex(encrypted);
			else
				return HelperUtil.base64(encrypted);
		}
		catch (Exception e) {
			logger.error("Encryption error",e);
			return null;
		}
	}

	public String decrypt(String ciphertext) throws Exception {
		ciphertext = ciphertext.replaceAll(" ","+");
		if(ciphertext!=null)
		{    	
			try {
				byte encryptedData[] = null ;
				if(isDataHexed())
					encryptedData =  HelperUtil.hex(ciphertext);
				else
					encryptedData =  HelperUtil.base64(ciphertext);
				byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv,encryptedData);
				return new String(decrypted, "UTF-8");
			}
			catch (Exception e) {
				logger.error("Decryption error",e);
				throw new Exception("Decryption error");
			}
		}
		else
		{
			return null;
		}
	}
	
	private  byte[] doFinal(int encryptMode, SecretKey key, byte[] iv, byte[] bytes)throws Exception {
		logger.debug("Key Used :"+new String(key.getEncoded())+"|"+key.getEncoded().length);
		logger.debug("encrypted bytes :"+bytes.length);
		if(iv!=null && iv.length > 0)
		{
			logger.debug("IV used : "+new String(iv));
			cipher.init(encryptMode, key, new IvParameterSpec(iv));
		}
		else 
			cipher.init(encryptMode, key);
			return cipher.doFinal(bytes);
	}

	private SecretKey generateKey(String salt, String passphrase) throws Exception {
		try {
			if(salt != null)
			{
				iv =HelperUtil.hex(ivStr);
				logger.debug("IV:"+iv.length);
				SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
				KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), HelperUtil.hex(salt), ITERATION_COUNT, KEY_SIZE);
				SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
				return key;
			}else
			{
				if(ivStr!=null)
					iv =ivStr.getBytes();
				logger.debug("Key Used :"+passphrase);
				byte [] key_Array = null;
				/* Changes done to support hexed password */
				if(isKeyHexed())
					key_Array = HelperUtil.hex(passphrase);
				else
					key_Array= passphrase.getBytes();
				SecretKey key = new SecretKeySpec(key_Array, "AES");
				return key;
			}
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException | DecoderException e) {
			logger.error("AES key generation error",e);
			throw new Exception("AES key generation error");
		}
	}


	public static void setPublicKey(String publicKeyFileName)
	{
		FileInputStream fileInputStream = null;
		try {
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			fileInputStream = new FileInputStream(new File(publicKeyFileName));
			X509Certificate cert = (X509Certificate) certFactory.generateCertificate(fileInputStream);
			publicKey = cert.getPublicKey();
		} catch (Exception e) {
			logger.error("Exception while setting private key",e);
			//throw new Exception("Unable to get public key");
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public  static void setPrivateKey(String keyStoreFile,String keyStoreType, String keyStorePassword,String alias) throws Exception
	{
		try {
			// Load the KeyStore and get the signing key and certificate.
			keyStorePassword = new String(keyStorePassword);
			KeyStore ks = KeyStore.getInstance(keyStoreType);
			FileInputStream keyFileStream = new FileInputStream(keyStoreFile);
			ks.load(keyFileStream, keyStorePassword.toCharArray());
			
			KeyStore.PrivateKeyEntry  entry = (KeyStore.PrivateKeyEntry) ks.getEntry
					(alias, new KeyStore.PasswordProtection(keyStorePassword.toCharArray()));

			if(entry == null)
				throw new Exception("Key not found for the given alias.");

			keyFileStream.close();

			privateKey = entry.getPrivateKey();
			keyStorePassword = null;
		}
		catch(Exception e) {
			logger.error("Exception while setting private key",e);
			throw new Exception("Unable to get private key");
		}
		return;
	}

	public static String encryptSecretKey(String data)
	{
		Cipher cipher;
		String str = null;
		try {
			cipher = Cipher.getInstance(KEY_ENCRYPTION_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			str = new String(HelperUtil.base64(cipher.doFinal(data.getBytes())));
			return str;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			logger.error("Exception while encrypting secret key",e);
		}
		catch(Exception e)
		{
			logger.error("Exception while encrypting secret key",e);
		}
		return str;
	}

	public static String decryptSecretKey(String encrypted)
	{
		Cipher cipher=null;
		String str = null;
		if(encrypted!=null && !encrypted.isEmpty())
		{
			try {
				cipher = Cipher.getInstance(KEY_ENCRYPTION_ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, publicKey);
				str =  new String(cipher.doFinal(HelperUtil.base64(encrypted)));
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
				// TODO Auto-generated catch block
				logger.error("Exception while decrypting secret key",e);
			}
			catch(Exception e)
			{
				logger.error("Exception while decrypting secret key",e);
			}
		}
		return str;
	}


	public static void main(String[] args) throws Exception {
		
		/*String enc = AesUtil.encrypt("{\"appNo\":\"123456\",\"customerId\":\"123456\",\"mcc\":\"5444\",\"panNo\":\"\",\"legalName\":\"Netra ENT\",\"dbaName\":\"Test ME\",\"dob\":\"19900102\",\"zoneCode\":\"400067\",\"businessEntity\":\"123456\",\"contactPerson\":\"Ramesh\",\"addressLine1\":\"Malad\",\"addressLine2\":\"Mumbai\",\"pinCode\":\"400065\",\"telephone\":\"9123456780\",\"district\":\"Mumbai\",\"mobileNo\":\"9123456780\",\"emailID\":\"abc@gmail.com\",\"paymentBy\":\"Nutan\",\"accountNo\":\"1234567890\",\"accountLable\":\"abc\",\"bnfIfsc\":\"AX123456789\",\"bnfName\":\"abc\",\"bnfAccountNo\":\"123456\",\"onus\":\"12\",\"seCode\":\"1234\",\"tipFlag\":\"Y\",\"conFeeFlag\":\"Y\",\"conFeeAmount\":\"12\"}");
		System.out.println(enc);
		System.out.println(AesUtil.decrypt(enc));
		//AesUtil.setPrivateKey("D:\\PROJECTS\\qrcode_workspace\\irctcqr\\src\\main\\resources\\irctc.jks", "JKS", "VzByMWRsIW5lQDAxOCM=", "irctc_wl");
		AesUtil.setPublicKey("C:\\Users\\kunal.surana\\Desktop\\irctc.cer");
		AesUtil.setEncryptedKey("fGG5k0UDXVHAHw7kYWuBLWfX3Ury4eQbSMKJJwNN4aYW0B0aa9CoMBljgieHGQuXgN7prlB8qMgoJp7WGkb4J44p60ORulJSxMdSUe0w8whj0kadxcs3g4Rijd8yPo9WovroKv663auAY1fbyXk6re55T6hDJWbuopktPMTxLIDYNMLvaDeNYBO4J6FxQGLgjYbO0wD+0qgjioHqYjGPBCBjre8UJd0jPc9R5QFssYSrk2bSruXz1QHVeibhDCQoiczoPOhD8P0V+JrvlSoVQZgloZqbIbP/rrS18aCqYPpPkbCq2UTOy66g630WmqkDcb/TpQIZDJcLY2ZRnYtt+Q==");
		System.out.println(decryptAesSecretKey(encryptedKey,publicKey));
		AesUtil.setIV("F27D5C9927726BCEFE7510B1BDD3D137");
		AesUtil.setSALT("3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55");
	
		
		AesUtil.setPublicKey("C:\\Users\\kunal.surana\\Desktop\\irctc.cer");
		AesUtil.setIV("F27D5C9927726BCEFE7510B1BDD3D137");
		AesUtil.setSALT("3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55");
		AesUtil.setEncryptedKey("fGG5k0UDXVHAHw7kYWuBLWfX3Ury4eQbSMKJJwNN4aYW0B0aa9CoMBljgieHGQuXgN7prlB8qMgoJp7WGkb4J44p60ORulJSxMdSUe0w8whj0kadxcs3g4Rijd8yPo9WovroKv663auAY1fbyXk6re55T6hDJWbuopktPMTxLIDYNMLvaDeNYBO4J6FxQGLgjYbO0wD+0qgjioHqYjGPBCBjre8UJd0jPc9R5QFssYSrk2bSruXz1QHVeibhDCQoiczoPOhD8P0V+JrvlSoVQZgloZqbIbP/rrS18aCqYPpPkbCq2UTOy66g630WmqkDcb/TpQIZDJcLY2ZRnYtt+Q==");
		String str = AesUtil.encrypt("Hello");
		System.out.println(str);
		System.out.println(AesUtil.decrypt(str));
		System.out.println(AesUtil.encryptAesSecretKey("MZygpewJsCpRrfOr", AesUtil.getPrivateKey()));
		AesUtil.setEncryptedKey("fGG5k0UDXVHAHw7kYWuBLWfX3Ury4eQbSMKJJwNN4aYW0B0aa9CoMBljgieHGQuXgN7prlB8qMgoJp7WGkb4J44p60ORulJSxMdSUe0w8whj0kadxcs3g4Rijd8yPo9WovroKv663auAY1fbyXk6re55T6hDJWbuopktPMTxLIDYNMLvaDeNYBO4J6FxQGLgjYbO0wD+0qgjioHqYjGPBCBjre8UJd0jPc9R5QFssYSrk2bSruXz1QHVeibhDCQoiczoPOhD8P0V+JrvlSoVQZgloZqbIbP/rrS18aCqYPpPkbCq2UTOy66g630WmqkDcb/TpQIZDJcLY2ZRnYtt+Q==");
		System.out.println(decryptAesSecretKey(encryptedKey,publicKey));
		
		//System.out.println(new String(Base64.encodeBase64("W0r1dl!ne@018#".getBytes())));
		
		//String enc = AesUtil.encryptAesSecretKey("da30a3ee5e6b4b1d3255bfef05601891", publicKey);
		String enc = AesUtil.encryptAesSecretKey("MZygpewJsCpRrfOr", publicKey);
		//enc = "fXU/t8S9AKQSDHnTGvkJyAllOV7gNDzTgq+fCjaJYZAymlmmxK9TnXmJxd+6ocC1C+D31w4muzKEHlYsbLrMseguv6YpPaLM4609K2GXBLkeRhue1I/R8vAI7+oyjZjECWrWUg80/sjluKXMyLgaEB72uvp8E2LoQ8nCqCQIuL6O07VbVmQM0w04EHzeUUnRyj76E50pWDqLX0Lkdw0o0w3ESnNvPCB9yA89RqvKyoG+82Nz4gFSjIgaMToIS1u5tOoht884JzCvg4rKNdLYjsHYW0QMq8NT20UH7jlxuHaknAl3C4kKW1N/Tsjo/aLpmC1KW2sqq7v7XE95/wPcoA==";
		System.out.println(enc);
		AesUtil.setPrivateKey("D:\\PROJECTS\\qrcode_workspace\\irctcqr\\src\\main\\resources\\irctc.jks", "JKS", "VzByMWRsIW5lQDAxOCM=", "irctc_wl");
		System.out.println(AesUtil.decryptAesSecretKey(enc, privateKey));
		
		AesUtil.setPrivateKey("C:\\Users\\kunal.surana\\Desktop\\irctc.jks", "JKS", "W0r1dl!ne@018#", "irctc_wl");
		System.out.println("Paycraft enc :"+AesUtil.encryptAesSecretKey("48C3B4286FF421A4A328E68AD9E542A4", privateKey));
		System.out.println("Paycaraft IV:"+AesUtil.encryptAesSecretKey("00000000000000000000000000000000", privateKey));
		System.out.println(" IV:"+AesUtil.encryptAesSecretKey("F27D5C9927726BCEFE7510B1BDD3D137", privateKey));
		System.out.println("SAlt:"+AesUtil.encryptAesSecretKey("3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55", privateKey));
		System.out.println("KEY:"+AesUtil.encryptAesSecretKey("MZygpewJsCpRrfOr", privateKey));

		//AesUtil.setPrivateKey("D:\\PROJECTS\\qrcode_workspace\\irctcqr\\src\\main\\resources\\irctc.jks", "JKS", "VzByMWRsIW5lQDAxOCM=", "irctc_wl");
		
		AesUtil.setPublicKey("D:\\PROJECTS\\qrcode_workspace\\irctcqr\\src\\main\\resources\\paymod.cer");
		String enc = AesUtil.encryptAesSecretKey("MZygpewJsCpRrfOr", publicKey);
		enc = "0tkaBPLvQqreZ5aCcGth3S8Q4J62bWUHrOjWIZHnfYpThiTPrOKkHfA49HlYIaIqaCf305Tb0xilbdPC/5G+JeB9TZCUyJJU48EvZsTBmhaHNWRiBlqttyGU5Zoy8LZFodKJT65jSKm7H78wY4PuWrLWRkn+mX/daAPP69Jz5FQluN0PSOJ04/SkXkjY6RDA79c9A+FYsDAJoHrtoIA8MGEw5h/km+C4nfyWnt0vqPQ4K9wMvJmGymTlp75KjlGKAtpSaya/+00q7UIFyHspdoxWB5tpmSlWe5BpW7pGT7PZgUgEX/wQt8GxktPHUfiShg/z09EfNNz+7aFS14s8jg==";
		System.out.println(enc);
		AesUtil.setPrivateKey("D:\\PROJECTS\\qrcode_workspace\\irctcqr\\src\\main\\resources\\paymod.keystore", "JKS", "YXRvc0AxMjM0", "paymod");
		System.out.println(AesUtil.decryptAesSecretKey(enc, privateKey));*/

		//8s3WpLrHTC8VQXvG|16

		AesUtil ae = new AesUtil();
		
		//System.out.println(ae.decrypt("VIHFWnUxBOKi27lXmuri/16Vz7aOR5lPYjsFHerWSnsmzks8gH1eUXfuidyH2YK+2NVVAvRhAFzeIOYETeKPy6F3BRqqcGPzUDhgJ88Lpm3Bwj/JPCjbQraQXnh3mbFuwAlx4BBKr/us2LbyBc8ff5Aef7BeJIVbMcn3YsX6/CnP8SAt55e/mCLgGN3zn53T60s43ZteJBYHGeEqLvCwLfUMeoQ2wEZOXhjXNIAbYN4pMrqf5hd3IvQ/3djO+csiockwYUV+1nnxo9ZKZDK2yEd/GSceRxRN4aHX2UVIb798IOmX6qhXoR6GS2zQsh/cqz6tlyyL0s+74/SAcvkAcQ==	"));
		
		//Wf37vtAwhXmC3b8miqC5JZilthK/N30YordKKmzz0npK7WMN8JvXUpGlVb/8kCYBl7XXog/E8Cf3Lk5eNvvU9f5xh0BSDYtNdVGnszI6/UWKLU+LDtkajlQtrdg31XboK6Nf7scKsNf6NneLNNfGlFfKQwhap5dzh7hnYFt6S7M4pKFSexW6+C2Vamrn9ZLmyLC5UDjzuL75VnlMFveAfFFbYA85GxoFSvGSX8Olp5For/cqxCGKfw+s13uacB9SEa2WZQhmBHZMDDKDH8S6L9q18GdHN/1CesiQrk6ScaBQ/N4OeNGobVQ+KqurtgoGW20/VnMoU+KpLBtOafrrDw==
		//for production
		
/*		//FOR PRODUCTION
		AesUtil.setPrivateKey("C://Users//pranav.vispute//Desktop//irctc.jks", "JKS", "W0r1dl!ne@018#", "irctc_wl");
		System.out.println(AesUtil.encryptSecretKey("67d020ffbd3008d5dfa1f79bbe3c7999"));
*/		
		
		
		// FOR SIT ENCRYPTION FOR ALL KEYS and stored in database 
				AesUtil.setPrivateKey("C:\\Users\\pranav.vispute\\Desktop\\testenvironment.jks", "JKS", "venture@123", "te1.in.worldline.com");
				System.out.println(AesUtil.encryptSecretKey("IiCXSTIdyJh3Kv13"));
				//Zq4t7wzCFJ@NcRfU
				
				
				//ae.init("pranav", null, null);
				
				
				
				
	}
	
	



}
