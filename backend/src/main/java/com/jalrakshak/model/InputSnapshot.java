package com.jalrakshak.model;

public class InputSnapshot {

    private FarmSnapshot farm;
    private CropCycleSnapshot cropCycle;
    private WeatherSnapshot weather;
    private SoilMoistureSnapshot soilMoisture;

    public InputSnapshot() {
    }

    public FarmSnapshot getFarm() { return farm; }
    public void setFarm(FarmSnapshot farm) { this.farm = farm; }

    public CropCycleSnapshot getCropCycle() { return cropCycle; }
    public void setCropCycle(CropCycleSnapshot cropCycle) { this.cropCycle = cropCycle; }

    public WeatherSnapshot getWeather() { return weather; }
    public void setWeather(WeatherSnapshot weather) { this.weather = weather; }

    public SoilMoistureSnapshot getSoilMoisture() { return soilMoisture; }
    public void setSoilMoisture(SoilMoistureSnapshot soilMoisture) { this.soilMoisture = soilMoisture; }
}
