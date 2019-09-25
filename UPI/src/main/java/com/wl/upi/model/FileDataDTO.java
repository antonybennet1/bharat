package com.wl.upi.model;

import java.sql.Timestamp;

public class FileDataDTO
{
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
  
  public Timestamp getTxnDate()
  {
    return this.txnDate;
  }
  
  public void setTxnDate(Timestamp txnDate)
  {
    this.txnDate = txnDate;
  }
  
  public Timestamp getBusDate()
  {
    return this.busDate;
  }
  
  public void setBusDate(Timestamp busDate)
  {
    this.busDate = busDate;
  }
  
  public String getChannelFlag()
  {
    return this.channelFlag;
  }
  
  public void setChannelFlag(String channelFlag)
  {
    this.channelFlag = channelFlag;
  }
  
  public String getRrn()
  {
    return this.rrn;
  }
  
  public void setRrn(String rrn)
  {
    this.rrn = rrn;
  }
  
  public String getMid()
  {
    return this.mid;
  }
  
  public void setMid(String mid)
  {
    this.mid = mid;
  }
  
  public String getTid()
  {
    return this.tid;
  }
  
  public void setTid(String tid)
  {
    this.tid = tid;
  }
  
  public String getTxnId()
  {
    return this.txnId;
  }
  
  public void setTxnId(String txnId)
  {
    this.txnId = txnId;
  }
  
  public String getFromAcc()
  {
    return this.fromAcc;
  }
  
  public void setFromAcc(String fromAcc)
  {
    this.fromAcc = fromAcc;
  }
  
  public String getToAcc()
  {
    return this.toAcc;
  }
  
  public void setToAcc(String toAcc)
  {
    this.toAcc = toAcc;
  }
  
  public String getRespCode()
  {
    return this.respCode;
  }
  
  public void setRespCode(String respCode)
  {
    this.respCode = respCode;
  }
  
  public double getTxnAmount()
  {
    return this.txnAmount;
  }
  
  public void setTxnAmount(double txnAmount)
  {
    this.txnAmount = txnAmount;
  }
  
  public double getSetlAmount()
  {
    return this.setlAmount;
  }
  
  public void setSetlAmount(double setlAmount)
  {
    this.setlAmount = setlAmount;
  }
  
  public int getMcc()
  {
    return this.mcc;
  }
  
  public void setMcc(int mcc)
  {
    this.mcc = mcc;
  }
}
