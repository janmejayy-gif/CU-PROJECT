package com.jalrakshak.repository;

import com.jalrakshak.model.ModelMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ModelMetadataRepository extends MongoRepository<ModelMetadata, String> {
}
