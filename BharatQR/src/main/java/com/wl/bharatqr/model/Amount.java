package com.wl.bharatqr.model;

public class Amount {
	private String currencyCode;
	private float  units;
	private float  nanos;
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public float getUnits() {
		return units;
	}
	public void setUnits(float units) {
		this.units = units;
	}
	public float getNanos() {
		return nanos;
	}
	public void setNanos(float nanos) {
		this.nanos = nanos;
	}
	
}
