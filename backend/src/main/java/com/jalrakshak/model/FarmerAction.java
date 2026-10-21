package com.jalrakshak.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "farmer_actions")
public class FarmerAction {

    @Id
    private String id;

    @Field("actionId")
    private String actionId;

    @Field("recommendationId")
    private String recommendationId;

    @Field("farmId")
    private String farmId;

    @Field("actionTaken")
    private String actionTaken;

    @Field("actionTimestamp")
    private Instant actionTimestamp;

    @Field("waterAppliedMm")
    private Double waterAppliedMm;

    @Field("waterAppliedLitres")
    private Double waterAppliedLitres;

    @Field("durationMinutes")
    private Integer durationMinutes;

    @Field("notes")
    private String notes;

    @Field("outcomeObserved")
    private String outcomeObserved;

    @Field("outcomeTimestamp")
    private Instant outcomeTimestamp;

    @Field("feedback")
    private String feedback;

    @Field("createdAt")
    private Instant createdAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getActionId() { return actionId; }
    public void setActionId(String actionId) { this.actionId = actionId; }

    public String getRecommendationId() { return recommendationId; }
    public void setRecommendationId(String recommendationId) { this.recommendationId = recommendationId; }

    public String getFarmId() { return farmId; }
    public void setFarmId(String farmId) { this.farmId = farmId; }

    public String getActionTaken() { return actionTaken; }
    public void setActionTaken(String actionTaken) { this.actionTaken = actionTaken; }

    public Instant getActionTimestamp() { return actionTimestamp; }
    public void setActionTimestamp(Instant actionTimestamp) { this.actionTimestamp = actionTimestamp; }

    public Double getWaterAppliedMm() { return waterAppliedMm; }
    public void setWaterAppliedMm(Double waterAppliedMm) { this.waterAppliedMm = waterAppliedMm; }

    public Double getWaterAppliedLitres() { return waterAppliedLitres; }
    public void setWaterAppliedLitres(Double waterAppliedLitres) { this.waterAppliedLitres = waterAppliedLitres; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getOutcomeObserved() { return outcomeObserved; }
    public void setOutcomeObserved(String outcomeObserved) { this.outcomeObserved = outcomeObserved; }

    public Instant getOutcomeTimestamp() { return outcomeTimestamp; }
    public void setOutcomeTimestamp(Instant outcomeTimestamp) { this.outcomeTimestamp = outcomeTimestamp; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
