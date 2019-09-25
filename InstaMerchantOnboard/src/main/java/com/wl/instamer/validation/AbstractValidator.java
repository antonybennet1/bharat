package com.wl.instamer.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wl.instamer.exceptions.ApplicationException;


public abstract class AbstractValidator<T> implements Validator<T> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractValidator.class);
	
	public boolean isMandatoryFieldsPresent(Class<?> type , Object object, String[] fieldArray)
	{
		try {
			List<String> missingFields = validateIfAnyNullOrEmpty(type ,object,fieldArray);
			logger.debug("Mandatory field is missing :"+missingFields);
			if(missingFields.size() > 0 )
				return false;
			else
				return true;
		} catch (ApplicationException e) {
			return false;
		}
	}
	private ArrayList<String> validateIfAnyNullOrEmpty(Class<?> type , Object object, String[] fieldArray) throws ApplicationException
	{
		ArrayList<String> errorList = new ArrayList<>();
		try {
			for (String fieldName: fieldArray) {
				fieldName = fieldName.trim();
				//logger.debug("Field Name|"+fieldName+"|");
				Field field = type.getDeclaredField(fieldName);
				field.setAccessible(true);
				if(field.get(object)==null || field.get(object).toString().trim().length()==0 )
				{
					errorList.add(fieldName);
				}
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new ApplicationException("Please check property file for mandatory field names present.",e);
		}
		return errorList;
	}
	// ----------- PanNumber------------
	protected boolean validatePanOrAdhaarNumber(String panAdhaarNumber){

		if(panAdhaarNumber.length()!= 10 || panAdhaarNumber.length()!= 12)
		{
			return false;
		}
		else if(panAdhaarNumber.length()== 10)
		{
			String ePattern = "^[a-zA-Z0-9]";
			Pattern p = Pattern.compile(ePattern);
			Matcher m = p.matcher(panAdhaarNumber);        
			return m.matches();
		}
		else if(panAdhaarNumber.length()== 12)
		{
			return panAdhaarNumber.matches( "\\d{12}" );
		}
		return false;
	}

	// -------  MerchantLegalname------------
	protected boolean validateMerchantLegalname(String merchantLegalName){

		return merchantLegalName.matches("[a-zA-Z.,\\s]+$");
	}


	// ------  validate DOB------------		
	protected boolean validateDOB(String dob){

		if(dob.length()!= 8)
		{
			return false;
		}

		return dob.matches( "\\d{8}" );

	}

	// -------  validate Business Entity------------
	protected boolean validateBusinessEntity(String validateBusinessEntity){
		String ePattern = "[0-9]";
		Pattern p = Pattern.compile(ePattern);
		Matcher m = p.matcher(validateBusinessEntity);
		return m.matches();
	}


	// -------  validate address Line1------------
	protected boolean validateaddressLine1(String addressLine1){

		return addressLine1.matches("[a-zA-Z0-9.,\\s]+$");
	}

	// -------  validate address Line2------------
	protected boolean validateaddressLine2(String addressLine2){

		return addressLine2.matches("[a-zA-Z0-9.,\\s]+$");
	}		


	// ------  validate Pin code ------------		
	protected boolean validatePinCode(String PinCode){

		return PinCode.matches( "\\d{6}" );
	}

	// ------  validate Mobile Number------------		
	protected boolean validateMobileNum(String MobileNum){

		return MobileNum.matches( "\\d{10}" );
	}		


	// -------  validate Contact Person------------
	protected boolean validateContactPerson(String validateContactPerson){

		return validateContactPerson.matches( "[a-zA-Z0-9.,\\\\s]+$"); 
	}

	//-------  validate Email------------	
	protected boolean validateEmail(String email){
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((([a-zA-Z]+\\.)+[a-zA-Z]{2,}))$";
		Pattern p = Pattern.compile(ePattern);
		Matcher m = p.matcher(email);        
		return m.matches();
	}

	// ------  validate Account Number------------		
	protected boolean validateAccountNum(String AccountNum){

		return AccountNum.matches( "\\d{10}|\\d{12}|\\d{8}" );
	}		


	//--   validate beneficiary Account Type    --------
	protected boolean validateBeneAccType(String BeneAccType){

		String ePattern = "^[a-zA-Z0-9]+$";
		Pattern p = Pattern.compile(ePattern);
		Matcher m = p.matcher(BeneAccType);        
		return m.matches();

	}

	// -------  validate Beneficiary Account Name------------
	protected boolean validateBeneAccName(String BeneAccName){
		return BeneAccName.matches("^[a-zA-Z]+$" );
	}

	// ------  validate Account Number------------		
	protected boolean validateBeneAccNum(String BeneAccNum){

		return BeneAccNum.matches( "^\\d+" );
	}


}
