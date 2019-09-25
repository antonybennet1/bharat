package com.wl.bharatqr.util;

public interface URIConstant {
	public static final String MERCHANT_ONBOARD = "/merchantOnboard"; 
	public static final String BHARATQR_GEN = "/qrGen/getQR"; 
	public static final String UPIQR_GEN = "/qrGen/getUpiQR";
	public static final String EntityQR_GEN = "/qr/getQR";
	public static final String CANCEL_QR = "/qr/cancelQR";
	public static final String REFUND = "/qr/refund";
	public static final String REFUND_APP = "/app/qr/refund";
	public static final String AXIS_REFUND = "/axis/refund";
	public static final String RELOAD = "/util/reloadConfig";
	public static final String RSA_ENCRYPT = "/util/rsaEncrypt";
	public static final String  CHECK_TXN_STATUS = "/qr/checkStatus";
	public static final String  CHECK_AGGTXN_STATUS = "/qr/txnEnquiry";
	public static final String  COLLECT_UPI = "/upi/collect/pull";
	public static final String  CALLBACK_UPI = "/upi/callback/{fromEntity}";
	public static final String  REFUND_JOB = "/jobs/refund/{refundFor}";
	public static final String AES_ENCRYPT = "/util/aesEncrypt";
	public static final String PAN_DECRYPT = "/util/PanDecrypt";
	public static final String UPDATE_BATCHNUMBER = "/util/updateBatchNumber";
	public static final String  CALLBACK_BHARATQR = "/callback";
	public static final String EntityQR_GEN_PSTN="/qr/getPSTNQR";
	public static final String STATICQR_GEN = "/qr/getStaticQR";
	public static final String RECON = "/recon/{bankCode}/{jobParm}";
	public static final String CHECK_AGGTXN_STATUS_PSTN="/qr/txnEnquiryPSTN";
	public static final String EntityQR_GEN_IPG="/qr/getQRIPG";
	public static final String DUMMY_EMP="/test"; 
	public static final String  CHECK_IPG_TXN_STATUS = "/qr/ipgTxnEnquiry";
	public static final String  IPG_CON_UAT = "/qr/ipg";
	public static final String  IPG_CON_PROD = "/qr/ipgProd";
	public static final String  CALLBACK_MMS = "/callback/ttms";
	public static final String  CALLBACK_MMS_POST = "/callback/ttmsv1";
	public static final String  CALLBACK_UPI_TEST = "/upi/callback/test/{fromEntity}";
}
