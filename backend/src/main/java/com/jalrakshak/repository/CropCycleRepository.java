package com.jalrakshak.repository;

import com.jalrakshak.model.CropCycle;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CropCycleRepository extends MongoRepository<CropCycle, String> {
}
