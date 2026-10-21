package com.jalrakshak.model;

import java.io.Serializable;

public class WeatherData implements Serializable {

    private Double temperatureC;
    private Integer humidityPct;
    private Double windSpeedMs;
    private Integer windDirectionDeg;
    private Double rainfallMm;
    private Double pressureHp;
    private Double et0Mm;
    private Double solarRadiationMjM2;

    public WeatherData() {
    }

    public Double getTemperatureC() { return temperatureC; }
    public void setTemperatureC(Double temperatureC) { this.temperatureC = temperatureC; }

    public Integer getHumidityPct() { return humidityPct; }
    public void setHumidityPct(Integer humidityPct) { this.humidityPct = humidityPct; }

    public Double getWindSpeedMs() { return windSpeedMs; }
    public void setWindSpeedMs(Double windSpeedMs) { this.windSpeedMs = windSpeedMs; }

    public Integer getWindDirectionDeg() { return windDirectionDeg; }
    public void setWindDirectionDeg(Integer windDirectionDeg) { this.windDirectionDeg = windDirectionDeg; }

    public Double getRainfallMm() { return rainfallMm; }
    public void setRainfallMm(Double rainfallMm) { this.rainfallMm = rainfallMm; }

    public Double getPressureHp() { return pressureHp; }
    public void setPressureHp(Double pressureHp) { this.pressureHp = pressureHp; }

    public Double getEt0Mm() { return et0Mm; }
    public void setEt0Mm(Double et0Mm) { this.et0Mm = et0Mm; }

    public Double getSolarRadiationMjM2() { return solarRadiationMjM2; }
    public void setSolarRadiationMjM2(Double solarRadiationMjM2) { this.solarRadiationMjM2 = solarRadiationMjM2; }
}
