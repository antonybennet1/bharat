package com.wl.bharatqr.util;

import org.springframework.stereotype.Service;

import com.wl.bharatqr.model.AmexFields;
import com.wl.bharatqr.model.AmexRefundFields;
import com.wl.bharatqr.model.AmexReversalFields;
import com.wl.bharatqr.model.HmacFields;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AmexFieldsValidation {
	public static final Logger logger = LoggerFactory.getLogger(AmexFieldsValidation.class);

	public String fieldsValidation(AmexFields amexFields) {
		if (StringUtils.isBlank(amexFields.getType())) {
			return getPropValues("type");
		} else if (StringUtils.isBlank(amexFields.getPrimary_account_number())) {
			return getPropValues("primary_account_number");
		} else if (StringUtils.isBlank(amexFields.getFull_pan())) {
			return getPropValues("full_pan");
		} else if (StringUtils.isBlank(amexFields.getExpiry_date())) {
			return getPropValues("expiry_date");
		} else if (!expiryDateValidation(amexFields.getExpiry_date())) {
			return getPropValues("expiry_date_invalid");
		} else if (StringUtils.isBlank(amexFields.getProcessing_code())) {
			return getPropValues("processing_code");
		} else if (StringUtils.isBlank(amexFields.getTransaction_amount())) {
			return getPropValues("transaction_amount");
		} else if (!StringUtils.isNumeric(amexFields.getTransaction_amount())) {
			return getPropValues("transaction_amount_numeric");
		} else if (amexFields.getTransaction_amount().length() != 12) {
			return getPropValues("transaction_amount_12");
		} else if (StringUtils.isBlank(amexFields.getTip_amount())) {
			return getPropValues("tip_amount");
		} else if (!StringUtils.isNumeric(amexFields.getTip_amount())) {
			return getPropValues("tip_amount_numeric");
		} else if (amexFields.getTip_amount().length() != 12) {
			return getPropValues("tip_amount_12");
		} else if (StringUtils.isBlank(amexFields.getTransaction_time())) {
			return getPropValues("transaction_time");
		} else if (StringUtils.isBlank(amexFields.getSystem_trace_number())) {
			return getPropValues("system_trace_number");
		} else if (StringUtils.isBlank(amexFields.getTransaction_time_local())) {
			return getPropValues("transaction_time_local");
		} else if (StringUtils.isBlank(amexFields.getReconciliation_date())) {
			return getPropValues("reconciliation_date");
		} else if (StringUtils.isBlank(amexFields.getAcquirer_reference_data())) {
			return getPropValues("acquirer_reference_data");
		} else if (StringUtils.isBlank(amexFields.getRetrieval_reference_number())) {
			return getPropValues("retrieval_reference_number");
		} else if (StringUtils.isBlank(amexFields.getAcquirer_id())) {
			return getPropValues("acquirer_id");
		} else if (StringUtils.isBlank(amexFields.getApproval_code())) {
			return getPropValues("approval_code");
		} else if (StringUtils.isBlank(amexFields.getAction_code())) {
			return getPropValues("action_code");
		} else if (StringUtils.isBlank(amexFields.getCard_acceptor_id())) {
			return getPropValues("card_acceptor_id");
		} else if (amexFields.getCard_acceptor_id().length() != 15) {
			return getPropValues("card_acceptor_id_15");
		} else if (StringUtils.isBlank(amexFields.getCard_acceptor_name())) {
			return getPropValues("card_acceptor_name");
		} else if (StringUtils.isBlank(amexFields.getCurrency_code())) {
			return getPropValues("currency_code");
		} else if (StringUtils.isBlank(amexFields.getPos_dc())) {
			return getPropValues("pos_dc");
		} else if (StringUtils.isBlank(amexFields.getAdditional_data().toString())) {
			return getPropValues("additional_data");
		} else if (StringUtils.isBlank(amexFields.getCard_acceptor_id_type())) {
			return getPropValues("card_acceptor_id_type");
		} else {

			switch (amexFields.getCard_acceptor_id_type()) {
			case "QR":
				break;
			case "SE":
				break;
			case "AIN_SE":
				break;
			default:
				return getPropValues("card_acceptor_id_type_invalid");
			}
		}

		return getPropValues("success");
	}

	public String amexReversalValidation(AmexReversalFields amexReversalFields) {
		if (StringUtils.isBlank(amexReversalFields.getRetrieval_identifier())) {
			return getPropValues("retrieval_identifier");
		}  else if (StringUtils.isBlank(amexReversalFields.getAmount())) {
			return getPropValues("amount");
		} else if (StringUtils.isBlank(amexReversalFields.getMerchant_country_code())) {
			return getPropValues("merchant_country_code");
		}else
		return getPropValues("success");
	}
	
	public String amexRefundValidation(AmexRefundFields amexRefundFields) {
		if (StringUtils.isBlank(amexRefundFields.getRetrieval_reference())) {
			return getPropValues("retrieval_reference");
		}  else if (StringUtils.isBlank(amexRefundFields.getMerchant_id())) {
			return getPropValues("merchant_id");
		} else if (StringUtils.isBlank(amexRefundFields.getCurrency_code())) {
			return getPropValues("currency_code");
		}else if (StringUtils.isBlank(amexRefundFields.getAmount())) {
			return getPropValues("amount");
		}else if (StringUtils.isBlank(amexRefundFields.getMerchant_country_code())) {
			return getPropValues("merchant_country_code");
		}else if (StringUtils.isBlank(amexRefundFields.getMerchant_category_code())) {
			return getPropValues("merchant_category_code");
		}else if (StringUtils.isBlank(amexRefundFields.getNonce())) {
			return getPropValues("nonce");
		}else if (StringUtils.isBlank(amexRefundFields.getLocal_timestamp())) {
			return getPropValues("local_timestamp");
		}else if (StringUtils.isBlank(amexRefundFields.getAcquirer_country_code())) {
			return getPropValues("acquirer_country_code");
		}else if (StringUtils.isBlank(amexRefundFields.getAcquirer_id())) {
			return getPropValues("acquirer_id");
		}else if (StringUtils.isBlank(amexRefundFields.getRecon_date())) {
			return getPropValues("recon_date");
		}else if (StringUtils.isBlank(amexRefundFields.getMerchant_address().getCity())) {
			return getPropValues("city");
		}else if (StringUtils.isBlank(amexRefundFields.getMerchant_address().getName())) {
			return getPropValues("name");
		}else if (StringUtils.isBlank(amexRefundFields.getMerchant_address().getPostal_code())) {
			return getPropValues("postal_code");
		}else
		return getPropValues("success");
	}

	public InputStream inputStream;

	public String getPropValues(String key) {
		String result = "";

		try {
			Properties prop = new Properties();
			String propFileName = "amexMessages.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			// get the property value and print it out
			return prop.getProperty(key);

		} catch (Exception e) {
			logger.error("getPropValues method key=" + key, e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				logger.error("getPropValues method key=" + key, e);
			}
		}
		return result;
	}

	public boolean expiryDateValidation(String expiryDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyy");
			YearMonth expiration = YearMonth.parse(expiryDate, formatter);
		} catch (Exception e) {
			logger.error("Invalid date format in expiryDate:" + e);
			return false;
		}
		return true;
	}
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private Pattern pattern;
	private Matcher matcher;
	public String emailValidation(HmacFields hmacFields){
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(hmacFields.getEmailId());
		boolean status=matcher.matches();
		if (StringUtils.isBlank(hmacFields.getEmailId())) {
			return getPropValues("emailId_missing");
		}  else if (!status) {
			return getPropValues("emailId_format");
		}else
			return getPropValues("success");
	}

}
