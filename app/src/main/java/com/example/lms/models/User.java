package com.example.lms.models;

public class User {

    private Long id;
    private String userCode;
    private String name;
    private String userPower;
    private String pinNo;
    private Boolean billCancellation;
    public User(Long id, String userCode, String name, String userPower, String pinNo) {
        this.id = id;
        this.userCode = userCode;
        this.name = name;
        this.userPower = userPower;
        this.pinNo = pinNo;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserPower() {
        return userPower;
    }

    public void setUserPower(String userPower) {
        this.userPower = userPower;
    }

    public String getPinNo() {
        return pinNo;
    }

    public void setPinNo(String pinNo) {
        this.pinNo = pinNo;
    }


    public void setBillCancellation(boolean billCancellation) {
        this.billCancellation = billCancellation;
    }

    public Boolean getBillCancellation() {
        return billCancellation;
    }
}
