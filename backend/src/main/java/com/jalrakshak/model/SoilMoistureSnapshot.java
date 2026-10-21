package com.jalrakshak.model;

public class SoilMoistureSnapshot {

    private String readingId;
    private Double moisturePct;
    private Integer depthCm;
    private Double ageHours;

    public SoilMoistureSnapshot() {
    }

    public String getReadingId() { return readingId; }
    public void setReadingId(String readingId) { this.readingId = readingId; }

    public Double getMoisturePct() { return moisturePct; }
    public void setMoisturePct(Double moisturePct) { this.moisturePct = moisturePct; }

    public Integer getDepthCm() { return depthCm; }
    public void setDepthCm(Integer depthCm) { this.depthCm = depthCm; }

    public Double getAgeHours() { return ageHours; }
    public void setAgeHours(Double ageHours) { this.ageHours = ageHours; }
}
