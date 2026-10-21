package com.jalrakshak.repository;

import com.jalrakshak.model.WeatherObservation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WeatherObservationRepository extends MongoRepository<WeatherObservation, String> {
}
