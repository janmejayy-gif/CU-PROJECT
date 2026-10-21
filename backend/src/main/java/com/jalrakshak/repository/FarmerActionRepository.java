package com.jalrakshak.repository;

import com.jalrakshak.model.FarmerAction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FarmerActionRepository extends MongoRepository<FarmerAction, String> {
}
