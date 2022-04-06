package com.example.lms.models;

import java.util.List;

public class Order {

    private Long orderId;
    private String orderNo;
    private String orderDate;
    private String orderTime;
    private Customer customer;
    private User user;
    private Long customerId;
    private Double totalAmount;
    private Long additionalAmount;
    private Double discount;
    private Long orderStatus;
    private String expressStatus;
    private String remarks;
    private List<OrderDetails> orderDetails;
    private Double netVatAmt;



    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return (orderNo != null ? orderNo: "");
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(Long additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public String getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(String expressStatus) {
        this.expressStatus = expressStatus;
    }

    public String getRemarks() {
        return (remarks != null? remarks.trim(): "");
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getCustomerId() {
        return (customerId != null? customerId: 0);
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Long getOrderStatus() {return orderStatus;}

    public void setOrderStatus(Long orderStatus) {
        this.orderStatus = orderStatus;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public void setNetVatAmt(double netVatAmt ){
        this.netVatAmt = netVatAmt;
    }
    public Double getNetVatAmt(){
        return this.netVatAmt;
    }
}
