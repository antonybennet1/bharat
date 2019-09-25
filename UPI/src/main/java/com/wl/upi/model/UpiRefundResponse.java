package com.wl.upi.model;

public class UpiRefundResponse {
	
	private String code;
	private String result;
	private Object data;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "Response [code=" + code + ", result=" + result + ", data=" + data + "]";
	}

}
