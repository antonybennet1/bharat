package com.wl.qr.dao;

import java.util.Map;

public interface QrDao {
	public String getBankBin(String bankCode);

	public void insertQRtring(String qr_string, int qrType, String bank_code, int programType, String txn_id, String status,
			String terminalId, String merchant_id, String amount, String fromEntity, String trId, String merchVPA);
	Map<String, Object> fetchQRString(String mid,String tid,String qrType);
}
