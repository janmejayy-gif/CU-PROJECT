package com.jalrakshak.model;

public class FarmSnapshot {

    private String farmId;
    private String soilType;
    private String irrigationMethod;
    private Double areaHectares;

    public FarmSnapshot() {
    }

    public String getFarmId() { return farmId; }
    public void setFarmId(String farmId) { this.farmId = farmId; }

    public String getSoilType() { return soilType; }
    public void setSoilType(String soilType) { this.soilType = soilType; }

    public String getIrrigationMethod() { return irrigationMethod; }
    public void setIrrigationMethod(String irrigationMethod) { this.irrigationMethod = irrigationMethod; }

    public Double getAreaHectares() { return areaHectares; }
    public void setAreaHectares(Double areaHectares) { this.areaHectares = areaHectares; }
}
