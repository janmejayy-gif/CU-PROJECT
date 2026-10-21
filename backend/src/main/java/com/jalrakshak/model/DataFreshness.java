package com.jalrakshak.model;

public class DataFreshness {

    private Double weatherDataAgeHours;
    private Double soilMoistureAgeHours;
    private Integer forecastHorizonHours;

    public DataFreshness() {
    }

    public Double getWeatherDataAgeHours() { return weatherDataAgeHours; }
    public void setWeatherDataAgeHours(Double weatherDataAgeHours) { this.weatherDataAgeHours = weatherDataAgeHours; }

    public Double getSoilMoistureAgeHours() { return soilMoistureAgeHours; }
    public void setSoilMoistureAgeHours(Double soilMoistureAgeHours) { this.soilMoistureAgeHours = soilMoistureAgeHours; }

    public Integer getForecastHorizonHours() { return forecastHorizonHours; }
    public void setForecastHorizonHours(Integer forecastHorizonHours) { this.forecastHorizonHours = forecastHorizonHours; }
}
