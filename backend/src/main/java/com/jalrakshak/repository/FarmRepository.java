package com.jalrakshak.repository;

import com.jalrakshak.model.Farm;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FarmRepository extends MongoRepository<Farm, String> {
}
