package com.jalrakshak.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.geo.GeoJsonPoint;

import java.time.Instant;
import java.util.List;

@Document(collection = "weather_observations")
public class WeatherObservation {

    @Id
    private String id;

    @Field("observationId")
    private String observationId;

    @Field("source")
    private String source;

    @Field("location")
    private GeoJsonPoint location;

    @Field("observationType")
    private String observationType;

    @Field("observedAt")
    private Instant observedAt;

    @Field("data")
    private WeatherData data;

    @Field("forecast")
    private List<WeatherForecast> forecast;

    @Field("rawProviderResponse")
    private Object rawProviderResponse;

    @Field("createdAt")
    private Instant createdAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getObservationId() { return observationId; }
    public void setObservationId(String observationId) { this.observationId = observationId; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public GeoJsonPoint getLocation() { return location; }
    public void setLocation(GeoJsonPoint location) { this.location = location; }

    public String getObservationType() { return observationType; }
    public void setObservationType(String observationType) { this.observationType = observationType; }

    public Instant getObservedAt() { return observedAt; }
    public void setObservedAt(Instant observedAt) { this.observedAt = observedAt; }

    public WeatherData getData() { return data; }
    public void setData(WeatherData data) { this.data = data; }

    public List<WeatherForecast> getForecast() { return forecast; }
    public void setForecast(List<WeatherForecast> forecast) { this.forecast = forecast; }

    public Object getRawProviderResponse() { return rawProviderResponse; }
    public void setRawProviderResponse(Object rawProviderResponse) { this.rawProviderResponse = rawProviderResponse; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
