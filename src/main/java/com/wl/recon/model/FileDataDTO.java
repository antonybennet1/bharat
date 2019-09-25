package com.wl.recon.model;

import java.sql.Date;
import java.sql.Timestamp;

public class FileDataDTO {

	private String mid;
	private String tid;
	private String txnId;
	private Timestamp txnDate;
	private Timestamp busDate;
	private double txnAmount;
	private double setlAmount;
	private String channelFlag;
	private int mcc;
	private String fromAcc;
	private String toAcc;
	private String respCode;
	private String rrn;
	
	public Timestamp getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(Timestamp txnDate) {
		this.txnDate = txnDate;
	}
	public Timestamp getBusDate() {
		return busDate;
	}
	public void setBusDate(Timestamp busDate) {
		this.busDate = busDate;
	}
	public String getChannelFlag() {
		return channelFlag;
	}
	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}
	
	public String getRrn() {
		return rrn;
	}
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	
	public String getFromAcc() {
		return fromAcc;
	}
	public void setFromAcc(String fromAcc) {
		this.fromAcc = fromAcc;
	}
	public String getToAcc() {
		return toAcc;
	}
	public void setToAcc(String toAcc) {
		this.toAcc = toAcc;
	}
	
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	
	
	public double getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(double txnAmount) {
		this.txnAmount = txnAmount;
	}
	public double getSetlAmount() {
		return setlAmount;
	}
	public void setSetlAmount(double setlAmount) {
		this.setlAmount = setlAmount;
	}
	public int getMcc() {
		return mcc;
	}
	public void setMcc(int mcc) {
		this.mcc = mcc;
	}
	/*@Override
	public String toString() {
		return "FileDataDTO [mid=" + mid + ", tid=" + tid + ", txnId=" + txnId + ", txnDate=" + txnDate + ", busDate="
				+ busDate + ", txnAmount=" + txnAmount + ", setlAmount=" + setlAmount + ", channelFlag=" + channelFlag
				+ ", mcc=" + mcc + ", fromAcc=" + fromAcc + ", toAcc=" + toAcc + ", respCode=" + respCode + ", rrn="
				+ rrn + "]";
	}*/
}
