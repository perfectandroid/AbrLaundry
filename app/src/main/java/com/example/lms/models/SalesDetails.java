package com.example.lms.models;

public class SalesDetails {

    public String customerName;
    public String bilNo;
    public String bilDate;
    public String item;
    public Double total;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBilNo() {
        return bilNo;
    }

    public void setBilNo(String bilNo) {
        this.bilNo = bilNo;
    }

    public String getBilDate() {
        return bilDate;
    }

    public void setBilDate(String bilDate) {
        this.bilDate = bilDate;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
