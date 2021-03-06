//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.04 at 03:08:58 PM IST 
//


package com.wl.upi.pnb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{java:com.fss.upi.req}OrgTxnId"/>
 *         &lt;element ref="{java:com.fss.upi.req}ResCode"/>
 *         &lt;element ref="{java:com.fss.upi.req}ResDesc"/>
 *         &lt;element ref="{java:com.fss.upi.req}TxnType"/>
 *         &lt;element ref="{java:com.fss.upi.req}OrgTxnRefId"/>
 *         &lt;element ref="{java:com.fss.upi.req}OrgTxnTimeStamp"/>
 *         &lt;element ref="{java:com.fss.upi.req}Amount"/>
 *         &lt;element ref="{java:com.fss.upi.req}PayerVirAddr"/>
 *         &lt;element ref="{java:com.fss.upi.req}PayeeVirAddr"/>
 *         &lt;element ref="{java:com.fss.upi.req}PayeeMobileNumber"/>
 *         &lt;element ref="{java:com.fss.upi.req}PayerCode"/>
 *         &lt;element ref="{java:com.fss.upi.req}PayeeCode"/>
 *         &lt;element ref="{java:com.fss.upi.req}CurrencyCode"/>
 *         &lt;element ref="{java:com.fss.upi.req}Remarks"/>
 *         &lt;element ref="{java:com.fss.upi.req}UPI"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "orgTxnId",
    "resCode",
    "resDesc",
    "txnType",
    "orgTxnRefId",
    "orgTxnTimeStamp",
    "amount",
    "payerVirAddr",
    "payeeVirAddr",
    "payeeMobileNumber",
    "payerCode",
    "payeeCode",
    "currencyCode",
    "remarks",
    "upi"
})
@XmlRootElement(name = "req")
public class Req {

    @XmlElement(name = "OrgTxnId", namespace = "java:com.fss.upi.req", required = true)
    protected String orgTxnId;
    @XmlElement(name = "ResCode", namespace = "java:com.fss.upi.req", required = true)
    protected String resCode;
    @XmlElement(name = "ResDesc", namespace = "java:com.fss.upi.req", required = true)
    protected String resDesc;
    @XmlElement(name = "TxnType", namespace = "java:com.fss.upi.req", required = true)
    protected String txnType;
    @XmlElement(name = "OrgTxnRefId", namespace = "java:com.fss.upi.req", required = true)
    protected String orgTxnRefId;
    @XmlElement(name = "OrgTxnTimeStamp", namespace = "java:com.fss.upi.req", required = true)
    protected String orgTxnTimeStamp;
    @XmlElement(name = "Amount", namespace = "java:com.fss.upi.req", required = true)
    protected String amount;
    @XmlElement(name = "PayerVirAddr", namespace = "java:com.fss.upi.req", required = true)
    protected String payerVirAddr;
    @XmlElement(name = "PayeeVirAddr", namespace = "java:com.fss.upi.req", required = true)
    protected String payeeVirAddr;
    @XmlElement(name = "PayeeMobileNumber", namespace = "java:com.fss.upi.req", required = true)
    protected String payeeMobileNumber;
    @XmlElement(name = "PayerCode", namespace = "java:com.fss.upi.req", required = true)
    protected String payerCode;
    @XmlElement(name = "PayeeCode", namespace = "java:com.fss.upi.req", required = true)
    protected String payeeCode;
    @XmlElement(name = "CurrencyCode", namespace = "java:com.fss.upi.req", required = true)
    protected String currencyCode;
    @XmlElement(name = "Remarks", namespace = "java:com.fss.upi.req", required = true)
    protected String remarks;
    @XmlElement(name = "UPI", namespace = "java:com.fss.upi.req", required = true)
    protected UPI upi;

    /**
     * Gets the value of the orgTxnId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgTxnId() {
        return orgTxnId;
    }

    /**
     * Sets the value of the orgTxnId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgTxnId(String value) {
        this.orgTxnId = value;
    }

    /**
     * Gets the value of the resCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResCode() {
        return resCode;
    }

    /**
     * Sets the value of the resCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResCode(String value) {
        this.resCode = value;
    }

    /**
     * Gets the value of the resDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResDesc() {
        return resDesc;
    }

    /**
     * Sets the value of the resDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResDesc(String value) {
        this.resDesc = value;
    }

    /**
     * Gets the value of the txnType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTxnType() {
        return txnType;
    }

    /**
     * Sets the value of the txnType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTxnType(String value) {
        this.txnType = value;
    }

    /**
     * Gets the value of the orgTxnRefId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgTxnRefId() {
        return orgTxnRefId;
    }

    /**
     * Sets the value of the orgTxnRefId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgTxnRefId(String value) {
        this.orgTxnRefId = value;
    }

    /**
     * Gets the value of the orgTxnTimeStamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgTxnTimeStamp() {
        return orgTxnTimeStamp;
    }

    /**
     * Sets the value of the orgTxnTimeStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgTxnTimeStamp(String value) {
        this.orgTxnTimeStamp = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmount(String value) {
        this.amount = value;
    }

    /**
     * Gets the value of the payerVirAddr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayerVirAddr() {
        return payerVirAddr;
    }

    /**
     * Sets the value of the payerVirAddr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayerVirAddr(String value) {
        this.payerVirAddr = value;
    }

    /**
     * Gets the value of the payeeVirAddr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayeeVirAddr() {
        return payeeVirAddr;
    }

    /**
     * Sets the value of the payeeVirAddr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayeeVirAddr(String value) {
        this.payeeVirAddr = value;
    }

    /**
     * Gets the value of the payeeMobileNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayeeMobileNumber() {
        return payeeMobileNumber;
    }

    /**
     * Sets the value of the payeeMobileNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayeeMobileNumber(String value) {
        this.payeeMobileNumber = value;
    }

    /**
     * Gets the value of the payerCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayerCode() {
        return payerCode;
    }

    /**
     * Sets the value of the payerCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayerCode(String value) {
        this.payerCode = value;
    }

    /**
     * Gets the value of the payeeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayeeCode() {
        return payeeCode;
    }

    /**
     * Sets the value of the payeeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayeeCode(String value) {
        this.payeeCode = value;
    }

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the remarks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of the remarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemarks(String value) {
        this.remarks = value;
    }

    /**
     * Gets the value of the upi property.
     * 
     * @return
     *     possible object is
     *     {@link UPI }
     *     
     */
    public UPI getUPI() {
        return upi;
    }

    /**
     * Sets the value of the upi property.
     * 
     * @param value
     *     allowed object is
     *     {@link UPI }
     *     
     */
    public void setUPI(UPI value) {
        this.upi = value;
    }

}
