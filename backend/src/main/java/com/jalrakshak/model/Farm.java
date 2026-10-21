package com.jalrakshak.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.GeoJsonPoint;

import java.time.Instant;

@Document(collection = "farms")
public class Farm {

    @Id
    private String id;

    @Field("farmId")
    private String farmId;

    @Field("userId")
    private String userId;

    @Field("name")
    private String name;

    @Field("location")
    private GeoJsonPoint location;

    @Field("district")
    private String district;

    @Field("state")
    private String state;

    @Field("country")
    private String country;

    @Field("areaHectares")
    private Double areaHectares;

    @Field("soilType")
    private String soilType;

    @Field("irrigationMethod")
    private String irrigationMethod;

    @Field("waterSource")
    private String waterSource;

    @Field("soilMoistureSensorId")
    private String soilMoistureSensorId;

    @Field("isActive")
    private boolean isActive = true;

    @Field("createdAt")
    private Instant createdAt;

    @Field("updatedAt")
    private Instant updatedAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFarmId() { return farmId; }
    public void setFarmId(String farmId) { this.farmId = farmId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public GeoJsonPoint getLocation() { return location; }
    public void setLocation(GeoJsonPoint location) { this.location = location; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Double getAreaHectares() { return areaHectares; }
    public void setAreaHectares(Double areaHectares) { this.areaHectares = areaHectares; }

    public String getSoilType() { return soilType; }
    public void setSoilType(String soilType) { this.soilType = soilType; }

    public String getIrrigationMethod() { return irrigationMethod; }
    public void setIrrigationMethod(String irrigationMethod) { this.irrigationMethod = irrigationMethod; }

    public String getWaterSource() { return waterSource; }
    public void setWaterSource(String waterSource) { this.waterSource = waterSource; }

    public String getSoilMoistureSensorId() { return soilMoistureSensorId; }
    public void setSoilMoistureSensorId(String soilMoistureSensorId) { this.soilMoistureSensorId = soilMoistureSensorId; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
