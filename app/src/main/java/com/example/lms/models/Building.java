package com.example.lms.models;

public class Building {

    private Long buildingId;
    private String buildingName;
    private Long buildingId1;

    public Building() {
    }

    public Building(Long buildingId, String buildingName, Long buildingId1) {
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.buildingId1 = buildingId1;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Long getBuildingId1() {
        return buildingId1;
    }

    public void setBuildingId1(Long buildingId1) {
        this.buildingId1 = buildingId1;
    }
}
