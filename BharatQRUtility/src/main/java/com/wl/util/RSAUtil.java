package com.wl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wl.util.RSAUtil.Encoding;

public class RSAUtil {

	private static Logger logger = LoggerFactory.getLogger(RSAUtil.class);

	private static final String KEY_ENCRYPTION_ALGORITHM = "RSA/ECB/PKCS1Padding";
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private Cipher cipher;
	private Encoding encoding=Encoding.BASE64;

	public RSAUtil()
	{
	}
	public RSAUtil(String keyStoreFile,String keyStoreType, String keyStorePassword,String alias,String publicKeyFileName) throws Exception {
		super();
		setPublicKey(publicKeyFileName);
		setPrivateKey(keyStoreFile, keyStoreType, keyStorePassword, alias);
		this.cipher = Cipher.getInstance(KEY_ENCRYPTION_ALGORITHM);
	}
	
	public RSAUtil(String keyStoreFile,String keyStoreType, String keyStorePassword,String alias) throws Exception {
		super();
		setPrivateKey(keyStoreFile, keyStoreType, keyStorePassword, alias);
		this.cipher = Cipher.getInstance(KEY_ENCRYPTION_ALGORITHM);
	}
	
	public RSAUtil(byte[] publicKeybytes) throws Exception {
		super();
		setPublicKey(publicKeybytes);
		this.cipher = Cipher.getInstance(KEY_ENCRYPTION_ALGORITHM);
	}
	
	public RSAUtil(byte[] publicKeybytes,byte[] privateKeyBytes) throws Exception {
		super();
		setPublicKey(publicKeybytes);
		setPrivateKey(privateKeyBytes);
		this.cipher = Cipher.getInstance(KEY_ENCRYPTION_ALGORITHM);
	}

	public void setPublicKey(String publicKeyFileName) throws Exception
	{
		FileInputStream fileInputStream = null;
		try {
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			fileInputStream = new FileInputStream(new File(publicKeyFileName));
			X509Certificate cert = (X509Certificate) certFactory.generateCertificate(fileInputStream);
			publicKey = cert.getPublicKey();
		} catch (Exception e) {
			logger.error("Exception while setting private key",e);
			throw new Exception("Unable to get public key");
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

	/**
	 * @param keyBytes this is byte array of base64 encoded public key string which  needs to be stored in DB Bankwise provided by bank 
	 * @throws Exception
	 */
	public void setPublicKey(byte[] keyBytes) throws Exception{
		try{
			String pubKey = new String(keyBytes, "UTF-8");
			keyBytes = HelperUtil.base64(pubKey);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(spec);
			logger.debug(String.format("public Key format: %s, algorithm: %s", publicKey.getFormat(), publicKey.getAlgorithm()));
			logger.debug("Public Key : " + publicKey.getEncoded().toString());
			this.publicKey = publicKey;
		}catch(Exception e)
		{
			logger.error("Exception while setting public key",e);
			throw new Exception("Unable to get public key");
		}
	}
	
	/**
	 * @param keyBytes this is byte array of base64 encoded private key string which  needs to be stored in DB Bankwise provided by bank 
	 * @throws Exception
	 */
	public void setPrivateKey(byte[] keyBytes) throws Exception{
		try{
			//String pubKey = new String(keyBytes, "UTF-8");
			//keyBytes = HelperUtil.base64(pubKey);
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory.generatePrivate(spec);
			logger.debug(String.format("Private Key format: %s, algorithm: %s", privateKey.getFormat(), privateKey.getAlgorithm()));
			logger.debug("Private Key : " + privateKey);
			this.privateKey = privateKey;
		}catch(Exception e)
		{
			logger.error("Exception while setting Private key",e);
			throw new Exception("Unable to get Private key");
		}
	}


	public  void setPrivateKey(String keyStoreFile,String keyStoreType, String keyStorePassword,String alias) throws Exception
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

	public String encrypt(String data)
	{
		String str = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			if(Encoding.BASE64.equals(encoding))
				str = new String(HelperUtil.base64(cipher.doFinal(data.getBytes())));
			else
				if(Encoding.HEX.equals(encoding))
					str = HelperUtil.hex(cipher.doFinal(data.getBytes())).toUpperCase();
			return str;
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			logger.error("Exception while encrypting secret key",e);
		}
		catch(Exception e)
		{
			logger.error("Exception while encrypting secret key",e);
		}
		return str;
	}

	public String decrypt(String encrypted)
	{
		String str = null;
		if(encrypted!=null && !encrypted.isEmpty())
		{
			try {
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
				if(Encoding.BASE64.equals(encoding))
					str = new String(cipher.doFinal(HelperUtil.base64(encrypted)));
				else
					if(Encoding.HEX.equals(encoding))
						str =  new String(cipher.doFinal(HelperUtil.hex(encrypted)));

				//str =  new String(cipher.doFinal(HelperUtil.base64(encrypted)));
			} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
				// TODO Auto-generated catch block
				logger.error("Exception while decrypting ",e);
			}
			catch(Exception e)
			{
				logger.error("Exception while decrypting ",e);
			}
		}
		return str;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
	}

	public enum Encoding{
		BASE64, HEX
	}

	
	public static void main(String[] args) throws Exception {
		String publickey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3CNMzagOLKzq3nJG90yX"+
 "Ozl8Gk9y8+Ab0drTT7HRoqEGBcOqQFDXm9IdC8QxEXxyqn+RwmC+AGsPByqKFUNZ"+
 "QELVID1XQmTZfDensndSktpBdkL5iSpjOsl1kvO9108fBJjNjGlPP3yWvbfcdwsX"+
 "zNFn/wxjcO841PuL8gi8k4lbJF9ClwYvZt+WCabMrK4M7Mrg6TLJgsBXFIK29U6y"+
 "1jSvYhAmQVNGut23HbhzMrhSTVH1UlZzrmnHpqAxhmM+IKFFlSt3Fxe/IiaZBoOE"+
 "22ZKuYrtmWuu+VEADRtb4o8KWy83sNaRvLxbnQxb0up5So5TdZvP5wF9QOKdVFFP"+
 "OQIDAQAB";
		RSAUtil rsa = new RSAUtil();
		//rsa.setPublicKey(publickey.getBytes());
		rsa = new RSAUtil(publickey.getBytes());
		rsa.setEncoding(Encoding.HEX);
		String checksum = "8169220195@axisATOSATOSAPP037122000040052610002000287096607582018-10-11T13:01:32.001+05:301.00AXI959ab767d24e4fd6b88cd88e8aff2b5400TRANSACTION HAS BEEN APPROVED";
		String encryptedChecksum = rsa.encrypt(checksum);
		System.out.println("Encrypted : " + encryptedChecksum );
		
		
		
	}
}
