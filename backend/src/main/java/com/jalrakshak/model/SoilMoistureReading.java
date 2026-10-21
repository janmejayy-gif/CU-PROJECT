package com.jalrakshak.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "soil_moisture_readings")
public class SoilMoistureReading {

    @Id
    private String id;

    @Field("readingId")
    private String readingId;

    @Field("farmId")
    private String farmId;

    @Field("sensorId")
    private String sensorId;

    @Field("depthCm")
    private Integer depthCm;

    @Field("moisturePct")
    private Double moisturePct;

    @Field("temperatureC")
    private Double temperatureC;

    @Field("ecDsM")
    private Double ecDsM;

    @Field("batteryPct")
    private Integer batteryPct;

    @Field("signalStrengthDbm")
    private Integer signalStrengthDbm;

    @Field("quality")
    private String quality;

    @Field("recordedAt")
    private Instant recordedAt;

    @Field("ingestedAt")
    private Instant ingestedAt;

    @Field("createdAt")
    private Instant createdAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getReadingId() { return readingId; }
    public void setReadingId(String readingId) { this.readingId = readingId; }

    public String getFarmId() { return farmId; }
    public void setFarmId(String farmId) { this.farmId = farmId; }

    public String getSensorId() { return sensorId; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }

    public Integer getDepthCm() { return depthCm; }
    public void setDepthCm(Integer depthCm) { this.depthCm = depthCm; }

    public Double getMoisturePct() { return moisturePct; }
    public void setMoisturePct(Double moisturePct) { this.moisturePct = moisturePct; }

    public Double getTemperatureC() { return temperatureC; }
    public void setTemperatureC(Double temperatureC) { this.temperatureC = temperatureC; }

    public Double getEcDsM() { return ecDsM; }
    public void setEcDsM(Double ecDsM) { this.ecDsM = ecDsM; }

    public Integer getBatteryPct() { return batteryPct; }
    public void setBatteryPct(Integer batteryPct) { this.batteryPct = batteryPct; }

    public Integer getSignalStrengthDbm() { return signalStrengthDbm; }
    public void setSignalStrengthDbm(Integer signalStrengthDbm) { this.signalStrengthDbm = signalStrengthDbm; }

    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }

    public Instant getRecordedAt() { return recordedAt; }
    public void setRecordedAt(Instant recordedAt) { this.recordedAt = recordedAt; }

    public Instant getIngestedAt() { return ingestedAt; }
    public void setIngestedAt(Instant ingestedAt) { this.ingestedAt = ingestedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
