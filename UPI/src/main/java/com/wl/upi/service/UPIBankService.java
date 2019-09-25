package com.wl.upi.service;

import com.wl.upi.model.RefundRequest;
import com.wl.upi.model.TxnDTO;
import com.wl.util.model.Response;

public interface UPIBankService {
	public Response upiCollectService(String fromEntity, String jsonRequest);
	public Response upiRefund(String fromEntity, String bankCode, RefundRequest jsonRequest);
	public Response upiRefund(String fromEntity, String bankCode, String jsonRequest);
	public Response doRefundJob(String string, String bankCode);
	public Response upiRefundRecon(String fromEntity, String bankCode, RefundRequest jsonRequest,TxnDTO txnDTO);
}
