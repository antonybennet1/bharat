/*package com.wl.util;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wl.util.RSAUtil.Encoding;

public class TestRSAUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(TestRSAUtil.class);
	
	private RSAUtil rsa;

	@Before
	public void setUp() throws Exception {
		rsa= new RSAUtil("D:\\WL-DATA\\documents\\PROJECTS\\mvisa_merchant_workspace\\bharatqr-application\\BharatQR\\src\\main\\resources\\paymod115.keystore", "JKS", "pass@1234" , "paymod115","D:\\WL-DATA\\documents\\PROJECTS\\mvisa_merchant_workspace\\bharatqr-application\\BharatQR\\src\\main\\resources\\paymod115.cer");
		rsa.setEncoding(Encoding.HEX);
	}

	@Test
	public void testEncrypt() throws UnsupportedEncodingException, Exception
	{
		String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3CNMzagOLKzq3nJG90yX"+
				"Ozl8Gk9y8+Ab0drTT7HRoqEGBcOqQFDXm9IdC8QxEXxyqn+RwmC+AGsPByqKFUNZ"+
				"QELVID1XQmTZfDensndSktpBdkL5iSpjOsl1kvO9108fBJjNjGlPP3yWvbfcdwsX"+
				"zNFn/wxjcO841PuL8gi8k4lbJF9ClwYvZt+WCabMrK4M7Mrg6TLJgsBXFIK29U6y"+
				"1jSvYhAmQVNGut23HbhzMrhSTVH1UlZzrmnHpqAxhmM+IKFFlSt3Fxe/IiaZBoOE"+
				"22ZKuYrtmWuu+VEADRtb4o8KWy83sNaRvLxbnQxb0up5So5TdZvP5wF9QOKdVFFP"+
				"OQIDAQAB";
		String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxQu2NlFHzRr/GEgi1rw1"+
				"WcjonM8bpiaww4lco1rQ/EC6gGYPYrAKnTcNcXKE7ikSNBpHo7QsEQvcyKH80Fhd"+
				"Nl7OD6EPGJkZjcJ1cCc7C9oSy5+rS3vLYk1r0d55FdQWABfrjbSbC0sLOn4A9L5Z"+
				"3pljogK8h6aM+z5YMOAlZ8W3lIMxM6Tpk6fSVAg8MQIPsAKW5vpEwu7+OyYnCqGK"+
				"UHcQuFfmqTPnVd53uCjdg6QI5o4Hq009JAbgPfQdFsygcYsycFZOk4WHnQSdA+ZD"+
				"551wJkdbuP86RhiQrjuzQu/4jAw4zEFsvmi8OmNko/3aZStOUJkFkL8D8oeipj+w"+
				"OQIDAQAB";
		
		rsa= new RSAUtil(publicKeyStr.getBytes("UTF-8"));
		rsa.setEncoding(Encoding.HEX);
		String str = "venksittest@axisATOSATOSAPP0751260313027887201120940227217622017-07-20T12:14:28.291+05:301.11AXI395742a4aefe4cba9085a81a4858736d00Success";
		System.out.println(rsa.encrypt(str));
	}
	
	@Test
	@Ignore
	public void testDecrypt() throws Exception
	{
		String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3CNMzagOLKzq3nJG90yX"+
				"Ozl8Gk9y8+Ab0drTT7HRoqEGBcOqQFDXm9IdC8QxEXxyqn+RwmC+AGsPByqKFUNZ"+
				"QELVID1XQmTZfDensndSktpBdkL5iSpjOsl1kvO9108fBJjNjGlPP3yWvbfcdwsX"+
				"zNFn/wxjcO841PuL8gi8k4lbJF9ClwYvZt+WCabMrK4M7Mrg6TLJgsBXFIK29U6y"+
				"1jSvYhAmQVNGut23HbhzMrhSTVH1UlZzrmnHpqAxhmM+IKFFlSt3Fxe/IiaZBoOE"+
				"22ZKuYrtmWuu+VEADRtb4o8KWy83sNaRvLxbnQxb0up5So5TdZvP5wF9QOKdVFFP"+
				"OQIDAQAB";
			
		RSAUtil rsa = null;
		try {
			FileInputStream fis = new FileInputStream("C:\\Users\\kunal.surana\\Desktop\\axis_upi\\wl_axis_privkey.pkcs8");
			DataInputStream privateKeyStream = new DataInputStream(fis);
		
			byte[] keyBytes = new byte[privateKeyStream.available()];
			privateKeyStream.read(keyBytes);
			privateKeyStream.close();
			rsa = new RSAUtil(publicKeyStr.getBytes(),keyBytes);
			rsa.setEncoding(Encoding.HEX);
		} catch (Exception e1) {
			logger.error("Unable to initialize RSA ",e1);
		}
		String encChecksum = "B35D1AB90FF14DC3D081E48FFE000939CFFB5FA44EC12342ED73630E72A3637F22A63C9C267A965F8315243E919CD15D406969CAD1A73270F3F312890D51D400B7E4D2EC6B1A94366EE97197B6F928F075CBAF3D6098153AEF3981715FCDC67450641D896873CCA91EE63F6CB3EE1E616150E1255892DE47AC385C943A8CB5A18C51E1E120C4C6DD8D5CDB23AE323328663D17215235CD8CB3A457611A0D49551AE9E0857E6BA19FA4D3B30665636C11AFEA23917DDE3453421837EA56106508FD5087155529BD97CF36B845D0247EC82D2701C36224A4FA08C34F6FA978E156CF05C2413B15493CCD073E896B5FC240CA06C35052E098D748A92102A36A0EF3";
		if(rsa!=null)
		{
			String decryptedChecksum = rsa.decrypt(encChecksum);
			logger.debug("decryptedChecksum:"+decryptedChecksum);
		}
		
		rsa= new RSAUtil(publicKeyStr.getBytes("UTF-8"));
		System.out.println(rsa.decrypt("84CE785404141FC7412232D6D4D6E80309BA11B2CA909590F72F4405512C9FC8E3B306DA241AFE4D8194AD04ECC4599AF4252473E52DEBCA462D8A43740A55C24EB478269C924C0FEC8883DDCF334F13C7A3AB665FD87B441D5508CC0B8FF3476E09A14D15046727CD5BAD62CB235CB90F17B7335AAB4A69A6DCA75B8DB296A0D2FB086ACA84BCC7ECA15B088F62B82112B7CD6950E4C75CBB0BFC0D20C09AC75A879B2E0553987A8CD708E4BBD014F5F0C3D5B28D8A64A56F578E4F900FDAE0BDE6753C5C4830E07D284B7B9C809B90F7B2DE929426671284D3C41BD9A2BFA67BF78F0F3D17B217D2D764D1EC5FB385EBFE19084A742D3BD7DD6CC072C30BAD"));
	}


}
*/