package com.example.lms.models;

public class Area {

    private Long areaId;
    private String areaName;
    private Long areaId1;



    public Long getAreaId() {
        return (areaId == null? 0: areaId);
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Long getAreaId1() {
        return (areaId1 == null? 0: areaId1);
    }

    public void setAreaId1(Long areaId1) {
        this.areaId1 = areaId1;
    }
}
