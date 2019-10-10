package com.voyager.fitquote.model;

import java.util.Arrays;

public class QuotationsRequest {
    private InsuredPerson[] insuredPerson;

    private String policyStartDate;

    private String promoCode;

    private String childOnlyIndicator;

    private String paymentCurrency;

    private String paymentFrequency;

    private String quotationType;

    private String applicationReferenceNumber;



    public InsuredPerson[] getInsuredPerson() {
        return insuredPerson;
    }

    public void setInsuredPerson(InsuredPerson[] insuredPerson) {
        this.insuredPerson = insuredPerson;
    }

    public String getPolicyStartDate() {
        return policyStartDate;
    }

    public void setPolicyStartDate(String policyStartDate) {
        this.policyStartDate = policyStartDate;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getChildOnlyIndicator() {
        return childOnlyIndicator;
    }

    public void setChildOnlyIndicator(String childOnlyIndicator) {
        this.childOnlyIndicator = childOnlyIndicator;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public String getQuotationType() {
        return quotationType;
    }

    public void setQuotationType(String quotationType) {
        this.quotationType = quotationType;
    }

    public String getApplicationReferenceNumber() {
        return applicationReferenceNumber;
    }

    public void setApplicationReferenceNumber(String applicationReferenceNumber) {
        this.applicationReferenceNumber = applicationReferenceNumber;
    }

    @Override
    public String toString() {
        return "QuotationsRequest{" +
                "insuredPerson=" + Arrays.toString(insuredPerson) +
                ", policyStartDate='" + policyStartDate + '\'' +
                ", promoCode='" + promoCode + '\'' +
                ", childOnlyIndicator='" + childOnlyIndicator + '\'' +
                ", paymentCurrency='" + paymentCurrency + '\'' +
                ", paymentFrequency='" + paymentFrequency + '\'' +
                ", quotationType='" + quotationType + '\'' +
                ", applicationReferenceNumber='" + applicationReferenceNumber + '\'' +
                '}';
    }
}
