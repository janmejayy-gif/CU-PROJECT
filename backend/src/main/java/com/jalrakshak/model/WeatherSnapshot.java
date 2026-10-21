package com.jalrakshak.model;

public class WeatherSnapshot {

    private String observationId;
    private String source;
    private Double currentRainfallMm;
    private Double forecastRainfall72hMm;
    private Double et0Mm;

    public WeatherSnapshot() {
    }

    public String getObservationId() { return observationId; }
    public void setObservationId(String observationId) { this.observationId = observationId; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public Double getCurrentRainfallMm() { return currentRainfallMm; }
    public void setCurrentRainfallMm(Double currentRainfallMm) { this.currentRainfallMm = currentRainfallMm; }

    public Double getForecastRainfall72hMm() { return forecastRainfall72hMm; }
    public void setForecastRainfall72hMm(Double forecastRainfall72hMm) { this.forecastRainfall72hMm = forecastRainfall72hMm; }

    public Double getEt0Mm() { return et0Mm; }
    public void setEt0Mm(Double et0Mm) { this.et0Mm = et0Mm; }
}
