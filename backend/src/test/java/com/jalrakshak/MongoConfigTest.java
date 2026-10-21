package com.jalrakshak;

import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class MongoConfigTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

    @DynamicPropertySource
    static void setMongoDbUri(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () ->
            String.format("mongodb://%s:%d", mongoDBContainer.getContainerIpAddress(), mongoDBContainer.getFirstMappedPort()));
    }

    @Autowired
    private MongoClient mongoClient;

    @Test
    void mongoClientIsConfigured() {
        assertThat(mongoClient).isNotNull();
        assertThat(mongoClient.listDatabaseNames()).isNotEmpty();
    }

    @Test
    void databaseIsAccessible() {
        var databaseNames = mongoClient.listDatabaseNames().into(new java.util.ArrayList<>());
        assertThat(databaseNames).isNotEmpty();
    }
}
