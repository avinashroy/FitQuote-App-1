package com.voyager.fitquote.model;

public class Quotation {
    private int id;
    private String quotationInternalReferenceNumber;
    private String quotationExternalReferenceNumber;
    private String quotationAmountTypeCode;
    private String quotationAmount;
    private String quotationAmountCurrency;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getQuotationInternalReferenceNumber() {
        return quotationInternalReferenceNumber;
    }

    public void setQuotationInternalReferenceNumber(String quotationInternalReferenceNumber) {
        this.quotationInternalReferenceNumber = quotationInternalReferenceNumber;
    }

    public String getQuotationExternalReferenceNumber() {
        return quotationExternalReferenceNumber;
    }

    public void setQuotationExternalReferenceNumber(String quotationExternalReferenceNumber) {
        this.quotationExternalReferenceNumber = quotationExternalReferenceNumber;
    }

    public String getQuotationAmountTypeCode() {
        return quotationAmountTypeCode;
    }

    public void setQuotationAmountTypeCode(String quotationAmountTypeCode) {
        this.quotationAmountTypeCode = quotationAmountTypeCode;
    }

    public String getQuotationAmount() {
        return quotationAmount;
    }

    public void setQuotationAmount(String quotationAmount) {
        this.quotationAmount = quotationAmount;
    }

    public String getQuotationAmountCurrency() {
        return quotationAmountCurrency;
    }

    public void setQuotationAmountCurrency(String quotationAmountCurrency) {
        this.quotationAmountCurrency = quotationAmountCurrency;
    }

    @Override
    public String toString() {
        return "Quotation{" +
                "id=" + id +
                ", quotationInternalReferenceNumber='" + quotationInternalReferenceNumber + '\'' +
                ", quotationExternalReferenceNumber='" + quotationExternalReferenceNumber + '\'' +
                ", quotationAmountTypeCode='" + quotationAmountTypeCode + '\'' +
                ", quotationAmount='" + quotationAmount + '\'' +
                ", quotationAmountCurrency='" + quotationAmountCurrency + '\'' +
                ", selected=" + selected +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
