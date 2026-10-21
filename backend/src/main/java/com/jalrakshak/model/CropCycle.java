package com.jalrakshak.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;

@Document(collection = "crop_cycles")
public class CropCycle {

    @Id
    private String id;

    @Field("cropCycleId")
    private String cropCycleId;

    @Field("farmId")
    private String farmId;

    @Field("crop")
    private String crop;

    @Field("variety")
    private String variety;

    @Field("plantingDate")
    private Instant plantingDate;

    @Field("expectedHarvestDate")
    private Instant expectedHarvestDate;

    @Field("growthStage")
    private String growthStage;

    @Field("status")
    private String status;

    @Field("cropCalendar")
    private Map<String, CropStage> cropCalendar;

    @Field("createdAt")
    private Instant createdAt;

    @Field("updatedAt")
    private Instant updatedAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCropCycleId() { return cropCycleId; }
    public void setCropCycleId(String cropCycleId) { this.cropCycleId = cropCycleId; }

    public String getFarmId() { return farmId; }
    public void setFarmId(String farmId) { this.farmId = farmId; }

    public String getCrop() { return crop; }
    public void setCrop(String crop) { this.crop = crop; }

    public String getVariety() { return variety; }
    public void setVariety(String variety) { this.variety = variety; }

    public Instant getPlantingDate() { return plantingDate; }
    public void setPlantingDate(Instant plantingDate) { this.plantingDate = plantingDate; }

    public Instant getExpectedHarvestDate() { return expectedHarvestDate; }
    public void setExpectedHarvestDate(Instant expectedHarvestDate) { this.expectedHarvestDate = expectedHarvestDate; }

    public String getGrowthStage() { return growthStage; }
    public void setGrowthStage(String growthStage) { this.growthStage = growthStage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Map<String, CropStage> getCropCalendar() { return cropCalendar; }
    public void setCropCalendar(Map<String, CropStage> cropCalendar) { this.cropCalendar = cropCalendar; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
