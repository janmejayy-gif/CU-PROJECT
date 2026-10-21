package com.jalrakshak.repository;

import com.jalrakshak.model.IrrigationRecommendation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IrrigationRecommendationRepository extends MongoRepository<IrrigationRecommendation, String> {
}
