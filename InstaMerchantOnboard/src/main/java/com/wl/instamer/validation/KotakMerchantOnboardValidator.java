package com.wl.instamer.validation;

import java.util.ArrayList;
import java.util.List;

import com.wl.instamer.model.MerchantOnboard;
import com.wl.instamer.util.Property;

public class KotakMerchantOnboardValidator extends AbstractValidator<MerchantOnboard> {

	@Override
	public List<String> validate(MerchantOnboard obj) {
		ArrayList<String> errorCodes= new ArrayList<String>();
		String [] allInputFields= Property.getKotakBankMandateFields().toString().split(",");	
		if(!isMandatoryFieldsPresent(MerchantOnboard.class, obj, allInputFields))
		{
			errorCodes.add("E-100");
		}
		return errorCodes;
	}

}
