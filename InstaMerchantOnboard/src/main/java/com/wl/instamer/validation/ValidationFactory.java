package com.wl.instamer.validation;

import com.wl.instamer.model.MerchantOnboard;

public class ValidationFactory {
	
	public static Validator<MerchantOnboard> getMerchantOnboardValidator(String bankcode)
	{
		Validator<MerchantOnboard>  validator = null;
		switch (bankcode)
		{
		case "00031" :
			validator = new AxisMerchantOnboardValidator();
			break;
		case "00079" :
			validator = new KotakMerchantOnboardValidator();
			break;
		}
		return validator;
	}
	
}
