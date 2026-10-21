package com.jalrakshak.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Document(collection = "model_metadata")
public class ModelMetadata {

    @Id
    private String id;

    @Field("modelId")
    private String modelId;

    @Field("version")
    private String version;

    @Field("algorithm")
    private String algorithm;

    @Field("trainingDataset")
    private TrainingDataset trainingDataset;

    @Field("targetVariable")
    private String targetVariable;

    @Field("metrics")
    private ModelMetrics metrics;

    @Field("featureImportance")
    private List<FeatureImportance> featureImportance;

    @Field("preprocessingPipeline")
    private String preprocessingPipeline;

    @Field("trainingDate")
    private Instant trainingDate;

    @Field("deployedAt")
    private Instant deployedAt;

    @Field("isActive")
    private boolean isActive = true;

    @Field("notes")
    private String notes;

    @Field("createdAt")
    private Instant createdAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getModelId() { return modelId; }
    public void setModelId(String modelId) { this.modelId = modelId; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public TrainingDataset getTrainingDataset() { return trainingDataset; }
    public void setTrainingDataset(TrainingDataset trainingDataset) { this.trainingDataset = trainingDataset; }

    public String getTargetVariable() { return targetVariable; }
    public void setTargetVariable(String targetVariable) { this.targetVariable = targetVariable; }

    public ModelMetrics getMetrics() { return metrics; }
    public void setMetrics(ModelMetrics metrics) { this.metrics = metrics; }

    public List<FeatureImportance> getFeatureImportance() { return featureImportance; }
    public void setFeatureImportance(List<FeatureImportance> featureImportance) { this.featureImportance = featureImportance; }

    public String getPreprocessingPipeline() { return preprocessingPipeline; }
    public void setPreprocessingPipeline(String preprocessingPipeline) { this.preprocessingPipeline = preprocessingPipeline; }

    public Instant getTrainingDate() { return trainingDate; }
    public void setTrainingDate(Instant trainingDate) { this.trainingDate = trainingDate; }

    public Instant getDeployedAt() { return deployedAt; }
    public void setDeployedAt(Instant deployedAt) { this.deployedAt = deployedAt; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
