package com.jalrakshak.model;

public class FeatureImportance {

    private String feature;
    private Double importance;

    public FeatureImportance() {
    }

    public String getFeature() { return feature; }
    public void setFeature(String feature) { this.feature = feature; }

    public Double getImportance() { return importance; }
    public void setImportance(Double importance) { this.importance = importance; }
}
