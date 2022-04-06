package com.example.lms.models;

public class Customer {

    private Long customerId;
    private String name;
    private Area area;
    private Building building;
    private String address1;
    private String address2;
    private String mobileNo;
    private String telephoneNo;
    private String email;
    private User user;

    public Customer() {
    }

    public Customer(Long customerId, String name, String address1, String address2, String mobileNo, String telephoneNo, String email, User user) {
        this.customerId = customerId;
        this.name = name;
        this.area = area;
        this.building = building;
        this.address1 = address1;
        this.address2 = address2;
        this.mobileNo = mobileNo;
        this.telephoneNo = telephoneNo;
        this.email = email;
        this.user = user;
    }

    public Long getCustomerId() {
        return (customerId == null? 0: customerId);
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return (name == null? "":name.trim());
    }

    public void setName(String name) {
        this.name = name;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public String getAddress1() {
        return (address1 == null)? "": address1.trim();
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return (address2 == null)? "": address2.trim();
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getMobileNo() {
        return (mobileNo == null)? "": mobileNo.trim();
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getTelephoneNo() {
        return (telephoneNo == null)? "": telephoneNo.trim();
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public String getEmail() {
        return (email == null)? "": email.trim();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
