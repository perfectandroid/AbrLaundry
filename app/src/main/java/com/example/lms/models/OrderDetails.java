package com.example.lms.models;

public class OrderDetails {

    private Long id;
    private Long orderId;
    private Long itemId;
    private Long userId;
    private Long serviceId;
    private Long qty;
    private Double price;
    private Double unitPrice;
    private String itemRemarks;
    private String serviceTypeName;
    private String itemName;
    private String errorCode;
    private User user;
    private Double netVatAmt;

    public OrderDetails() {

    }


    public OrderDetails(Long id, Long orderId, Long itemId, Long serviceId, Long qty, Double price, String itemRemarks, User user) {
        this.id = id;
        this.orderId = orderId;
        this.itemId = itemId;
        this.serviceId = serviceId;
        this.qty = qty;
        this.price = price;
        this.itemRemarks = itemRemarks;
        this.user = user;
    }
    public void setNetVatAmt( Double netVatAmt ){
        this.netVatAmt = netVatAmt;
    }
    public Double getNetVatAmt(){
        return netVatAmt;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getQty() {
        return (qty == null? 0: qty);
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return (price == null ? 0: price);
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getItemRemarks() {
        return (itemRemarks == null? "": itemRemarks.trim());
    }

    public void setItemRemarks(String itemRemarks) {
        this.itemRemarks = itemRemarks;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Double getUnitPrice() {
        return (unitPrice == null? 0: unitPrice);
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {return userId;}
}