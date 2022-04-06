package com.example.lms.models;

public class Price {

    private Long id;
    private Long serviceId;
    private Long itemId;
    private String serviceName;
    private String itemName;
    private Double price;
    private String favourite;

    public Price() {
    }

    public Price(Long id, Long serviceId, Long itemId, String serviceName, String itemName, Double price, String favourite) {
        this.id = id;
        this.serviceId = serviceId;
        this.itemId = itemId;
        this.serviceName = serviceName;
        this.itemName = itemName;
        this.price = price;
        this.favourite = favourite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }
}
