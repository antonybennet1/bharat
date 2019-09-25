package com.wl.bharatqr.service;

import com.google.gson.JsonObject;
import com.wl.bharatqr.model.AmexFields;
import com.wl.bharatqr.model.AmexRefundFields;
import com.wl.bharatqr.model.AmexReversalFields;
import com.wl.bharatqr.model.HmacFields;

public interface AmexTransactionService {
	public Object amexNotification(AmexFields amexFields);
	public JsonObject amexReversal(AmexReversalFields amexReversalFields);
	public JsonObject amexRefund(AmexRefundFields amexRefundFields);
	public String getClientIdAndSecret(HmacFields hmacFields);

}
