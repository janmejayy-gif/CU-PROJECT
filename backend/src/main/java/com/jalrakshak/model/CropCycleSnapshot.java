package com.jalrakshak.model;

public class CropCycleSnapshot {

    private String cropCycleId;
    private String crop;
    private String growthStage;
    private Integer daysAfterPlanting;

    public CropCycleSnapshot() {
    }

    public String getCropCycleId() { return cropCycleId; }
    public void setCropCycleId(String cropCycleId) { this.cropCycleId = cropCycleId; }

    public String getCrop() { return crop; }
    public void setCrop(String crop) { this.crop = crop; }

    public String getGrowthStage() { return growthStage; }
    public void setGrowthStage(String growthStage) { this.growthStage = growthStage; }

    public Integer getDaysAfterPlanting() { return daysAfterPlanting; }
    public void setDaysAfterPlanting(Integer daysAfterPlanting) { this.daysAfterPlanting = daysAfterPlanting; }
}
