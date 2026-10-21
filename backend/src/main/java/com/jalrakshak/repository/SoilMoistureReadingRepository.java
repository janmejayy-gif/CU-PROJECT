package com.jalrakshak.repository;

import com.jalrakshak.model.SoilMoistureReading;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SoilMoistureReadingRepository extends MongoRepository<SoilMoistureReading, String> {
}
