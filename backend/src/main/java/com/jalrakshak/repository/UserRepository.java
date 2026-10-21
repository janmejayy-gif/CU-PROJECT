package com.jalrakshak.repository;

import com.jalrakshak.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
