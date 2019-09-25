/*import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wl.qr.service.QRService;
import com.wl.util.JsonUtility;
import com.wl.util.model.Response;

public class TestQrGen {

	ApplicationContext context;
	QRService qrService;

	@Before
	public void setUp() {
		context = new ClassPathXmlApplicationContext("qr-spring.xml");
		qrService = (QRService) context.getBean("qrService");
	}

	@Test
	public void testQRGeneration() {
		String jsonRequest = "{'MID':'000022600018181','TID':'22060471','bankCode':'00075','qrType':1,'enrollType':1,'amount':'10.00'}";
		Response resp = qrService.entityQRGen("TCH","",jsonRequest);
		System.out.println(JsonUtility.convertToJson(resp));

	}
}
*/