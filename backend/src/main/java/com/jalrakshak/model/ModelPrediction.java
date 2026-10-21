package com.jalrakshak.model;

import java.util.List;

public class ModelPrediction {

    private Double irrigationNeedMm;
    private Double predictionConfidence;
    private List<String> featuresUsed;

    public ModelPrediction() {
    }

    public Double getIrrigationNeedMm() { return irrigationNeedMm; }
    public void setIrrigationNeedMm(Double irrigationNeedMm) { this.irrigationNeedMm = irrigationNeedMm; }

    public Double getPredictionConfidence() { return predictionConfidence; }
    public void setPredictionConfidence(Double predictionConfidence) { this.predictionConfidence = predictionConfidence; }

    public List<String> getFeaturesUsed() { return featuresUsed; }
    public void setFeaturesUsed(List<String> featuresUsed) { this.featuresUsed = featuresUsed; }
}
