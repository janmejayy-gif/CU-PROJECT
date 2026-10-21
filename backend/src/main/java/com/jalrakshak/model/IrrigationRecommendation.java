package com.jalrakshak.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Document(collection = "irrigation_recommendations")
public class IrrigationRecommendation {

    @Id
    private String id;

    @Field("recommendationId")
    private String recommendationId;

    @Field("farmId")
    private String farmId;

    @Field("cropCycleId")
    private String cropCycleId;

    @Field("action")
    private String action;

    @Field("estimatedWaterQuantityMm")
    private Double estimatedWaterQuantityMm;

    @Field("estimatedWaterVolumeLitres")
    private Double estimatedWaterVolumeLitres;

    @Field("confidence")
    private String confidence;

    @Field("reasons")
    private List<String> reasons;

    @Field("warnings")
    private List<String> warnings;

    @Field("dataFreshness")
    private DataFreshness dataFreshness;

    @Field("modelVersion")
    private String modelVersion;

    @Field("modelPrediction")
    private ModelPrediction modelPrediction;

    @Field("decisionRulesApplied")
    private List<String> decisionRulesApplied;

    @Field("inputSnapshot")
    private InputSnapshot inputSnapshot;

    @Field("generatedAt")
    private Instant generatedAt;

    @Field("validUntil")
    private Instant validUntil;

    @Field("createdAt")
    private Instant createdAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRecommendationId() { return recommendationId; }
    public void setRecommendationId(String recommendationId) { this.recommendationId = recommendationId; }

    public String getFarmId() { return farmId; }
    public void setFarmId(String farmId) { this.farmId = farmId; }

    public String getCropCycleId() { return cropCycleId; }
    public void setCropCycleId(String cropCycleId) { this.cropCycleId = cropCycleId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Double getEstimatedWaterQuantityMm() { return estimatedWaterQuantityMm; }
    public void setEstimatedWaterQuantityMm(Double estimatedWaterQuantityMm) { this.estimatedWaterQuantityMm = estimatedWaterQuantityMm; }

    public Double getEstimatedWaterVolumeLitres() { return estimatedWaterVolumeLitres; }
    public void setEstimatedWaterVolumeLitres(Double estimatedWaterVolumeLitres) { this.estimatedWaterVolumeLitres = estimatedWaterVolumeLitres; }

    public String getConfidence() { return confidence; }
    public void setConfidence(String confidence) { this.confidence = confidence; }

    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }

    public List<String> getWarnings() { return warnings; }
    public void setWarnings(List<String> warnings) { this.warnings = warnings; }

    public DataFreshness getDataFreshness() { return dataFreshness; }
    public void setDataFreshness(DataFreshness dataFreshness) { this.dataFreshness = dataFreshness; }

    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }

    public ModelPrediction getModelPrediction() { return modelPrediction; }
    public void setModelPrediction(ModelPrediction modelPrediction) { this.modelPrediction = modelPrediction; }

    public List<String> getDecisionRulesApplied() { return decisionRulesApplied; }
    public void setDecisionRulesApplied(List<String> decisionRulesApplied) { this.decisionRulesApplied = decisionRulesApplied; }

    public InputSnapshot getInputSnapshot() { return inputSnapshot; }
    public void setInputSnapshot(InputSnapshot inputSnapshot) { this.inputSnapshot = inputSnapshot; }

    public Instant getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(Instant generatedAt) { this.generatedAt = generatedAt; }

    public Instant getValidUntil() { return validUntil; }
    public void setValidUntil(Instant validUntil) { this.validUntil = validUntil; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
