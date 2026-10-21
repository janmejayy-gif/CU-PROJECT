package com.jalrakshak.model;

import java.util.List;

public class TrainingDataset {

    private String source;
    private Integer samples;
    private Integer features;
    private String dateRange;
    private List<String> crops;
    private List<String> regions;

    public TrainingDataset() {
    }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public Integer getSamples() { return samples; }
    public void setSamples(Integer samples) { this.samples = samples; }

    public Integer getFeatures() { return features; }
    public void setFeatures(Integer features) { this.features = features; }

    public String getDateRange() { return dateRange; }
    public void setDateRange(String dateRange) { this.dateRange = dateRange; }

    public List<String> getCrops() { return crops; }
    public void setCrops(List<String> crops) { this.crops = crops; }

    public List<String> getRegions() { return regions; }
    public void setRegions(List<String> regions) { this.regions = regions; }
}
