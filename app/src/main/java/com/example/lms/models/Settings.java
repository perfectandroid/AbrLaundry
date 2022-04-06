package com.example.lms.models;

public class Settings {

    private Long id;
    private Long routeCode;
    private String routeName;
    private Long noOfBillCopies;
    private Long printerPageSize;
    private Boolean enableSettlement;
    private Long priceChange;
    private String companyname;
    private String companyaddress;
    private String address1;
    private String address2;


    private Boolean enableDiscount;



    public Settings() {

    }

    public Settings(Long id, Long routeCode, String routeName, Long noOfBillCopies, Long printerPageSize, Boolean enableSettlement, Long priceChange, Boolean enableDiscount,String companyname,String companyaddress,String address1,String address2) {
        this.id = id;
        this.routeCode = routeCode;
        this.routeName = routeName;
        this.noOfBillCopies = noOfBillCopies;
        this.printerPageSize = printerPageSize;
        this.enableSettlement = enableSettlement;
        this.priceChange = priceChange;

        this.enableDiscount = enableDiscount;
        this.companyaddress=companyname;
        this.companyaddress=companyaddress;
        this.address1 = address1;
        this.address2 = address2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(Long routeCode) {
        this.routeCode = routeCode;
    }

    public Long getNoOfBillCopies() {
        return noOfBillCopies;
    }

    public void setNoOfBillCopies(Long noOfBillCopies) {
        this.noOfBillCopies = noOfBillCopies;
    }

    public Boolean getEnableSettlement() {
        return enableSettlement;
    }

    public void setEnableSettlement(Boolean enableSettlement) {
        this.enableSettlement = enableSettlement;
    }

    public Long getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(Long priceChange) {
        this.priceChange = priceChange;
    }



    public Boolean getEnableDiscount() {
        return enableDiscount;
    }

    public void setEnableDiscount(Boolean enableDiscount) {
        this.enableDiscount = enableDiscount;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Long getPrinterPageSize() {
        return printerPageSize;
    }

    public void setPrinterPageSize(Long printerPageSize) {
        this.printerPageSize = printerPageSize;
    }
    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }
    public String getCompanyaddress() {
        return companyaddress;
    }

    public void setCompanyaddress(String companyaddress) {
        this.companyaddress = companyaddress;
    }

    public String getAddress1() {
        return this.address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }
}
