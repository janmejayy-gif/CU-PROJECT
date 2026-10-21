package com.jalrakshak.model;

import java.time.Instant;

public class WeatherForecast {

    private Instant validAt;
    private Double temperatureC;
    private Integer humidityPct;
    private Double rainfallMm;
    private Double et0Mm;

    public WeatherForecast() {
    }

    public Instant getValidAt() { return validAt; }
    public void setValidAt(Instant validAt) { this.validAt = validAt; }

    public Double getTemperatureC() { return temperatureC; }
    public void setTemperatureC(Double temperatureC) { this.temperatureC = temperatureC; }

    public Integer getHumidityPct() { return humidityPct; }
    public void setHumidityPct(Integer humidityPct) { this.humidityPct = humidityPct; }

    public Double getRainfallMm() { return rainfallMm; }
    public void setRainfallMm(Double rainfallMm) { this.rainfallMm = rainfallMm; }

    public Double getEt0Mm() { return et0Mm; }
    public void setEt0Mm(Double et0Mm) { this.et0Mm = et0Mm; }
}
