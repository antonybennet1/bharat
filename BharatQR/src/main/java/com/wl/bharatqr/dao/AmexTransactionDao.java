package com.wl.bharatqr.dao;

import java.util.List;
import java.util.Map;

import com.wl.bharatqr.model.AmexFields;
import com.wl.bharatqr.model.HmacFields;

public interface AmexTransactionDao {
	public String amexNotificationDao(AmexFields amexFields);
	public List<Map<String, Object>> isMailIdAlreadyExist(HmacFields hmacFields);
	public List<Map<String, Object>> getAuthorizationDetails(String clientId);
	public String insertClientIdAndSecret(HmacFields hmacFields);
}
