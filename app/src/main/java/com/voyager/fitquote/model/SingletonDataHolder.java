package com.voyager.fitquote.model;

import java.util.ArrayList;
import java.util.List;

public class SingletonDataHolder {
    private static SingletonDataHolder me;

    private String applicationNo;
    private String age;
    private List<Quotation> quotations = new ArrayList<>();
    private String totalSteps;
    private String customerId;
    private String discount;
    private String lifestyle;
    private Quotation selectedQuotation;


    private SingletonDataHolder() {}

    public static SingletonDataHolder getInstance() {
        if (me != null) return  me;

        me = new SingletonDataHolder();
        return me;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }

    public String getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(String totalSteps) {
        this.totalSteps = totalSteps;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getLifestyle() {
        return lifestyle;
    }

    public void setLifestyle(String lifestyle) {
        this.lifestyle = lifestyle;
    }

    public Quotation getSelectedQuotation() {
        return selectedQuotation;
    }

    public void setSelectedQuotation(Quotation selectedQuotation) {
        this.selectedQuotation = selectedQuotation;
    }
}
