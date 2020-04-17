package com.redhat.rest.example.demorest;

public class Customer {

    private String custId;
    private String prediction;
    private String customerClass;
    private String qualifiedPurchases;
    private String lastOfferResponse;
    private String income;
    private String age;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getLastOfferResponse() {
        return lastOfferResponse;
    }

    public void setLastOfferResponse(String lastOfferResponse) {
        this.lastOfferResponse = lastOfferResponse;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getCustomerClass() {
        return customerClass;
    }

    public void setCustomerClass(String customerClass) {
        this.customerClass = customerClass;
    }

    public String getQualifiedPurchases() {
        return qualifiedPurchases;
    }

    public void setQualifiedPurchases(String qualifiedPurchases) {
        this.qualifiedPurchases = qualifiedPurchases;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custId='" + custId + '\'' +
                ", prediction='" + prediction + '\'' +
                ", customerClass='" + customerClass + '\'' +
                ", qualifiedPurchases='" + qualifiedPurchases + '\'' +
                '}';
    }
}
