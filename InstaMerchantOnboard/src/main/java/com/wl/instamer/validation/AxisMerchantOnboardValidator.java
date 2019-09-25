package com.wl.instamer.validation;

import java.util.ArrayList;
import java.util.List;

import com.wl.instamer.model.MerchantOnboard;
import com.wl.instamer.util.Property;

public class AxisMerchantOnboardValidator extends AbstractValidator<MerchantOnboard> {

	@Override
	public List<String> validate(MerchantOnboard obj) {
		ArrayList<String> errorCodes= new ArrayList<String>();
		String [] allInputFields= Property.getAxisBankMandateFields().split(",");	
		if(!isMandatoryFieldsPresent(MerchantOnboard.class, obj, allInputFields))
		{
			errorCodes.add("ERWL003");
		}
		return errorCodes;
	}
	
	/*public static void main(String[] args) {
	//	String jsonInput="{\"bank_code\":\"00031\",\"dob\":\"20150715\",\"appNo\":\"123456\",\"customerId\":\"123456\",\"merchant_creation_date\":\"5444\" ,\"panNo\" :\"CBBPB4743B\" ,\"legalName\" :\" Netra ENT\" ,\"dbaName\" :\" Test ME\" ,\"dob\" :\" 19900102\" ,\"zoneCode\" :\" 400067\",\"businessEntity\":\"123456\" ,\"contactPerson\" :\"Ramesh\" ,\"addressLine1\" :\" Malad\" ,\"addressLine2\" :\" Mumbai\" ,\"pinCode\" :\" 400065\" ,\"telephone\" :\"9123456780\" ,\"district\" :\" Mumbai\" ,\"mobileNo\" :\" 9123456780\" ,\"emailID\" :\" abc@gmail.com\" ,\"paymentBy\" :\"Nutan\" ,\"accountNo\" :\" 1234567890\" ,\"accountLable\" :\"abc\" ,\"bnfIfsc\" :\"AX123456789\" ,\"bnfName\" :\" abc\" ,\"bnfAccountNo\" :\" 123456\" ,\"onus\" :\" 12\" ,\"seCode\" :\" 1234\" ,\"tipFlag\" :\" Y\" ,\"conFeeFlag\" :\" Y\" ,\"conFeeAmount\" :\"12\" }";
	String jsonInput="{\"bank_code\":\"\",\"dob\" :\"20150715\" ,\"appNo\" :\"123456\" ,\"customerId\" :\"123456\" ,\"merchant_creation_date\" :\"5444\" ,\"panNo\" :\"CBBPB4743B\" ,\"legalName\" :\" Netra ENT\" ,\"dbaName\" :\" Test ME\" ,\"dob\" :\" 19900102\" ,\"zoneCode\" :\" 400067\" ,\"businessEntity\" :\"123456\" ,\"contactPerson\" :\"Ramesh\" ,\"addressLine1\" :\" Malad\" ,\"addressLine2\" :\" Mumbai\" ,\"pinCode\" :\" 400065\" ,\"telephone\" :\"9123456780\" ,\"district\" :\" Mumbai\" ,\"mobileNo\" :\" 9123456780\" ,\"emailID\" :\" abc@gmail.com\" ,\"paymentBy\" :\"Nutan\" ,\"accountNo\" :\" 1234567890\" ,\"accountLable\" :\"abc\" ,\"bnfIfsc\":\"AX123456789\",\"bnfName\":\"abc\",\"bnfAccountNo\" :\" 123456\" ,\"onus\" :\" 12\" ,\"seCode\":\"1234\",\"tipFlag\":\"Y\",\"conFeeFlag\":\"Y\",\"conFeeAmount\":\"12\"}";
	MerchantOnboard obj=(MerchantOnboard) JsonUtility.parseJsonToObject(jsonInput , MerchantOnboard.class );
	System.out.println(jsonInput);
	System.out.println("encrypted string"+AesUtil.encrypt(jsonInput));
	System.out.println("decrypted string --"+AesUtil.decrypt("VQosld7Uu+UWU0+UtOkZLx+lwdg+Jnu56rFtFriugnFcAyeftWkiBNsnh0Vm/T92rVbJF/7eiUXQiuusbQS5Qj7Ceok6oC2ApJMm9GQNCoHKX/2BRo0Bdhhhtmdtn6Br46Ncln5t1N0I05+GyP9ErA=="));
	String [] allInputFields= Property.getAxisBankMandateFields().toString().split(",");	
	//System.out.println(new AxisMerchantOnboardValidator().validateIfAnyNullOrEmpty(allInputFields ,obj));
	}*/
}	
