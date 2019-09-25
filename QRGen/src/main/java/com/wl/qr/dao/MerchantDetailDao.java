package com.wl.qr.dao;

import java.util.List;
import java.util.Map;

import com.wl.qr.model.MerchantDetails;

public interface MerchantDetailDao {
	public MerchantDetails getMerchantInfo(String mid, String tid, String bankCode);
	public List<Map<String, Object>> getMpanList(String tid, String bankCode);
	String getVisaMPAN(String tId);
}
