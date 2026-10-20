# JALRAKSHAK — Phase 2 Implementation Plan

**Version:** 1.0  
**Status:** PLANNING — Awaiting approval to begin implementation  
**Governed by:** AGENTS.md (technology stack, phase order, non-negotiable rules)  
**Roadmap:** JALRAKSHAK_Development_Plan.md  
**Prerequisites:** Phase 1 deliverables committed (`b27ed52`)  

---

## 1. Phase 2 Objectives, Scope, and Non-Goals

### 1.1 Objectives
- Create a running Spring Boot 3.x backend with Maven, Java 17, connected to MongoDB via Spring Data MongoDB
- Implement authentication (registration, login, JWT RS256 tokens, refresh, logout, user profile)
- Implement Farm CRUD and Crop Cycle CRUD APIs with validation, pagination, and farm-scoped authorization
- Implement global exception handling with RFC 7807 Problem Details error format
- Establish health/readiness endpoints, structured logging, and secret management via environment variables
- Achieve test coverage: unit tests (≥80% for services/validators), integration tests (Testcontainers), API contract tests (Pact), security tests

### 1.2 Scope (In-Phase)
| Area | Deliverable |
|------|-------------|
| **Project Setup** | Maven multi-module (or single-module) Spring Boot project; Java 17; Spring Boot 3.2+ |
| **MongoDB** | Spring Data MongoDB configuration; connection pooling; 8 collections with indexes; native JSON schema validators |
| **Auth** | JWT RS256 (asymmetric); access token 15min; refresh token 7d rotating; BCrypt cost 12; password min 8 chars + complexity |
| **APIs** | `/api/v1/auth/*`, `/api/v1/farms/*`, `/api/v1/crop-cycles/*` per MONGODB_SCHEMA_AND_API_SPEC.md §5 |
| **Validation** | Bean Validation (JSR-380) + custom validators; cross-field; enum; coordinate bounds |
| **Error Handling** | Global `@ControllerAdvice`; RFC 7807 format; traceId correlation |
| **Observability** | `/actuator/health`, `/actuator/info`; structured JSON logs; traceId propagation; no PII in logs |
| **Security Config** | Spring Security filter chain; stateless JWT auth; CORS restricted; CSRF disabled; method-level `@PreAuthorize` |
| **Tests** | Unit (JUnit 5 + Mockito); Integration (@SpringBootTest + Testcontainers); Contract (Pact); Security (ZAP/SAST) |

### 1.3 Non-Goals (Deferred)
| Item | Deferred To |
|------|-------------|
| Weather integration (fetch, cache, freshness, circuit breaker) | Phase 3 |
| ML service client, feature engineering, decision engine, recommendation persistence | Phase 5 |
| React/Vite frontend | Phase 6 |
| Full security hardening (rate limiting, audit logging, secret rotation runbook) | Phase 7 |
| Pilot execution | Phase 7 |

### 1.4 Dependencies
- **Phase 1 complete** — API contracts, MongoDB schemas, acceptance criteria, security requirements documented and approved
- **MongoDB instance** — Local (Docker) or hosted (Atlas); connection URI available via env var
- **RS256 key pair** — Private key (PEM) for signing; public key for verification (can be generated in Phase 2 setup)
- **Java 17+, Maven 3.9+** — Installed on dev/CI machines

### 1.5 Definition of Done (Phase 2)
All items from MVP_ACCEPTANCE_AND_SECURITY.md §5.2 must pass:
- [ ] Spring Boot project created with Maven/Gradle, Java 17, Spring Boot 3.x
- [ ] MongoDB connection configured (Spring Data MongoDB); repositories for all 8 collections
- [ ] Schema validators applied (native MongoDB + application-level)
- [ ] Auth endpoints implemented (register, login, refresh, logout, me) with JWT RS256
- [ ] Farm CRUD endpoints implemented with validation, pagination, authorization
- [ ] Crop cycle CRUD endpoints implemented (one active per farm)
- [ ] Global exception handler with RFC 7807 error format
- [ ] Unit tests ≥ 80% coverage for services, validators, decision rules
- [ ] Integration tests for all endpoints (Testcontainers MongoDB)
- [ ] API contract tests (Pact) for all public endpoints
- [ ] Security tests: auth, rate limit, validation, headers
- [ ] Documentation: OpenAPI/Swagger generated from code
- [ ] All Phase 2 acceptance criteria (AC-01 to AC-06, AC-F08, AC-F09) passing

---

## 2. Repository and Backend Project Structure

### 2.1 Monorepo Layout (Recommended)
```
CU-PROJECT/
├── .github/
│   └── workflows/           # CI/CD pipelines
├── docs/                    # Phase 1 deliverables + this plan
├── backend/                 # Spring Boot application
│   ├── pom.xml              # Maven build
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/jalrakshak/
│   │   │   │   ├── config/          # Security, MongoDB, Web, Jackson, OpenAPI
│   │   │   │   ├── controller/      # REST controllers (auth, farm, crop-cycle)
│   │   │   │   ├── service/         # Business logic services
│   │   │   │   ├── repository/      # Spring Data MongoDB repositories
│   │   │   │   ├── model/           # Domain models / MongoDB documents
│   │   │   │   ├── dto/             # Request/response DTOs
│   │   │   │   ├── exception/       # Custom exceptions, global handler
│   │   │   │   ├── validation/      # Custom validators
│   │   │   │   ├── security/        # JWT filter, token provider, UserDetailsService
│   │   │   │   └── JalrakshakApplication.java
│   │   │   └── resources/
│   │   │       ├── application.yml           # Base config
│   │   │       ├── application-dev.yml       # Dev profile
│   │   │       ├── application-prod.yml      # Prod profile
│   │   │       ├── application-test.yml      # Test profile
│   │   │       └── db/mongodb/               # Mongock migrations (optional)
│   │   └── test/
│   │       ├── java/com/jalrakshak/          # Unit + integration tests
│   │       └── resources/
│   │           └── application-test.yml
│   └── target/
├── frontend/                # React + Vite (Phase 6)
├── ml-service/              # Python FastAPI (Phase 4)
└── docker-compose.yml       # Local dev stack (MongoDB, Redis, etc.)
```

### 2.2 Spring Boot Module Structure (Single Module for MVP)
```
backend/
├── pom.xml
└── src/main/java/com/jalrakshak/
    ├── JalrakshakApplication.java
    ├── config/
    │   ├── MongoConfig.java              # MongoClientSettings, indexes, converters
    │   ├── SecurityConfig.java           # SecurityFilterChain, JWT, CORS, method security
    │   ├── WebConfig.java                # Jackson, CORS, async, problem details
    │   ├── OpenApiConfig.java            # SpringDoc OpenAPI 3
    │   └── JacksonConfig.java            # ISO-8601, enums as strings
    ├── controller/
    │   ├── AuthController.java           # POST /auth/register, /login, /refresh, /logout, GET /me
    │   ├── FarmController.java           # CRUD /farms, /farms/{id}
    │   └── CropCycleController.java      # CRUD /farms/{farmId}/crop-cycles, /active, /complete
    ├── service/
    │   ├── AuthService.java              # register, login, refresh, logout, password encoding
    │   ├── FarmService.java              # CRUD, validation, authorization checks
    │   ├── CropCycleService.java         # CRUD, single-active enforcement, stage calc
    │   └── JwtService.java               # token generation, validation, claims extraction
    ├── repository/
    │   ├── UserRepository.java
    │   ├── FarmRepository.java
    │   ├── CropCycleRepository.java
    │   ├── WeatherObservationRepository.java
    │   ├── SoilMoistureReadingRepository.java
    │   ├── IrrigationRecommendationRepository.java
    │   ├── FarmerActionRepository.java
    │   └── ModelMetadataRepository.java
    ├── model/
    │   ├── User.java
    │   ├── Farm.java
    │   ├── CropCycle.java
    │   ├── WeatherObservation.java
    │   ├── SoilMoistureReading.java
    │   ├── IrrigationRecommendation.java
    │   ├── FarmerAction.java
    │   └── ModelMetadata.java
    ├── dto/
    │   ├── auth/
    │   │   ├── RegisterRequest.java
    │   │   ├── LoginRequest.java
    │   │   ├── TokenResponse.java
    │   │   └── UserResponse.java
    │   ├── farm/
    │   │   ├── FarmCreateRequest.java
    │   │   ├── FarmUpdateRequest.java
    │   │   └── FarmResponse.java
    │   ├── cropcycle/
    │   │   ├── CropCycleCreateRequest.java
    │   │   ├── CropCycleUpdateRequest.java
    │   │   └── CropCycleResponse.java
    │   └── common/
    │       ├── PageResponse.java
    │       └── ErrorResponse.java
    ├── exception/
    │   ├── GlobalExceptionHandler.java   # @ControllerAdvice, RFC 7807
    │   ├── ResourceNotFoundException.java
    │   ├── ValidationException.java
    │   ├── UnauthorizedException.java
    │   └── ConflictException.java
    ├── validation/
    │   ├── ValidEnum.java                # Custom enum validator
    │   ├── ValidCoordinates.java         # Lat/lon bounds
    │   ├── UniqueActiveCropCycleValidator.java
    │   └── CrossFieldValidator.java
    ├── security/
    │   ├── JwtAuthenticationFilter.java  # OncePerRequestFilter, token validation
    │   ├── JwtTokenProvider.java         # RS256 sign/verify, claims
    │   ├── CustomUserDetailsService.java # UserDetails from UserRepository
    │   └── SecurityConstants.java        # Header names, token prefixes
    └── util/
        ├── TraceIdFilter.java            # X-B3-TraceId propagation
        └── GeoJsonUtils.java             # Point helpers
```

---

## 3. Java and Spring Boot Setup

### 3.1 Build Configuration (`pom.xml` Key Dependencies)
```xml
<properties>
    <java.version>17</java.version>
    <spring-boot.version>3.2.5</spring-boot.version>
    <mongodb.driver.version>5.1.0</mongodb.driver.version>
    <testcontainers.version>1.19.7</testcontainers.version>
    <pact.version>4.5.12</pact.version>
</properties>

<dependencies>
    <!-- Core -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- Security / JWT -->
    <dependency>
        <groupId>com.nimbusds</groupId>
        <artifactId>nimbus-jose-jwt</artifactId>
        <version>9.37.3</version>
    </dependency>

    <!-- API Docs -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.3.0</version>
    </dependency>

    <!-- Rate Limiting -->
    <dependency>
        <groupId>com.github.bucket4j</groupId>
        <artifactId>bucket4j-spring-boot3-starter</artifactId>
        <version>1.2.0</version>
    </dependency>

    <!-- Mongock (migrations) -->
    <dependency>
        <groupId>com.github.cloudyrock.mongock</groupId>
        <artifactId>mongock-spring-v5</artifactId>
        <version>4.3.20</version>
    </dependency>
    <dependency>
        <groupId>com.github.cloudyrock.mongock</groupId>
        <artifactId>mongodb-springdata-v3-driver</artifactId>
        <version>4.3.20</version>
    </dependency>

    <!-- Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>testcontainers</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>mongodb</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>au.com.dius.pact</groupId>
        <artifactId>pact-junit5</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### 3.2 Configuration Profiles and Environment Variables

| Profile | File | Use Case |
|---------|------|----------|
| `default` | `application.yml` | Base config, overridden by env-specific |
| `dev` | `application-dev.yml` | Local dev; relaxed security; embedded MongoDB or Docker |
| `test` | `application-test.yml` | Testcontainers MongoDB; fixed ports; no external calls |
| `prod` | `application-prod.yml` | TLS, strict CORS, external MongoDB Atlas, mTLS for ML |

**Environment Variables (Required):**
```bash
# MongoDB
MONGODB_URI=mongodb://user:pass@host:27017/jalrakshak?replicaSet=rs0
MONGODB_DATABASE=jalrakshak

# JWT (RS256)
JWT_PRIVATE_KEY_PATH=file:/secrets/jwt-private.pem   # Or JWT_PRIVATE_KEY_PEM (inline)
JWT_PUBLIC_KEY_PATH=file:/secrets/jwt-public.pem     # Or JWT_PUBLIC_KEY_PEM
JWT_ACCESS_TTL_MINUTES=15
JWT_REFRESH_TTL_DAYS=7
JWT_ISSUER=jalrakshak
JWT_AUDIENCE=jalrakshak-frontend

# Server
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev

# Logging
LOG_LEVEL=INFO
LOG_FORMAT=json

# CORS (comma-separated origins)
CORS_ALLOWED_ORIGINS=http://localhost:5173,https://app.jalrakshak.example.com
```

**Secrets Management:**
- Private key stored in `/secrets/jwt-private.pem` (mounted volume / vault injection)
- **Never** commit keys to repo; `.gitignore` excludes `*.pem`, `*.key`, `secrets/`
- CI/CD injects secrets at runtime

---

## 4. MongoDB Connectivity and Spring Data Configuration

### 4.1 MongoClient Configuration
```java
@Configuration
@EnableMongoRepositories(basePackages = "com.jalrakshak.repository")
public class MongoConfig {

    @Bean
    public MongoClientSettings mongoClientSettings(
            @Value("${spring.data.mongodb.uri}") String uri,
            @Value("${spring.data.mongodb.database}") String database) {
        return MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(uri))
            .retryWrites(true)
            .retryReads(true)
            .writeConcern(WriteConcern.ACKNOWLEDGED)
            .readPreference(ReadPreference.primaryPreferred())
            .applyToClusterSettings(builder ->
                builder.serverSelectionTimeout(5, TimeUnit.SECONDS))
            .build();
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClientSettings settings,
                                        @Value("${spring.data.mongodb.database}") String database) {
        return new MongoTemplate(MongoClients.create(settings), database);
    }

    // Custom converters for UUID, enums, GeoJSON
    @Bean
    public MappingMongoConverter mappingMongoConverter(
            MongoDatabaseFactory factory, MongoMappingContext context) {
        MappingMongoConverter converter = new MappingMongoConverter(
            new DefaultDbRefResolver(factory), context);
        converter.setCustomConversions(customConversions());
        return converter;
    }
}
```

### 4.2 Collection Initialization and Indexes
- **Native JSON Schema Validators** — Applied via `MongoTemplate` on startup (or Mongock migration)
- **Indexes** — Created via `@Indexed` annotations on model fields + compound indexes in repository `@Query` or Mongock
- **TTL Indexes** — `weather_observations.createdAt` (90 days); `model_metadata` optional

### 4.3 Document Models (Mapping to MONGODB_SCHEMA_AND_API_SPEC.md §2)
Each model class maps 1:1 to the defined collections:
- `User` → `users` (UUID `userId`, email/phone sparse unique, BCrypt `passwordHash`)
- `Farm` → `farms` (GeoJSON `Point` location, 2dsphere index, `userId` FK)
- `CropCycle` → `crop_cycles` (enum `crop`, `growthStage`, `status`, `cropCalendar` embedded)
- `WeatherObservation` → `weather_observations` (TTL index, GeoJSON location)
- `SoilMoistureReading` → `soil_moisture_readings`
- `IrrigationRecommendation` → `irrigation_recommendations` (inputSnapshot embedded)
- `FarmerAction` → `farmer_actions` (unique `recommendationId`)
- `ModelMetadata` → `model_metadata` (version unique, isActive flag)

**Reference Strategy:** All cross-collection refs use `String (UUID v4)` — not `ObjectId` — for portability to ML service.

---

## 5. Authentication and Authorization Design

### 5.1 JWT RS256 Implementation
| Component | Detail |
|-----------|--------|
| **Algorithm** | RS256 (RSASSA-PKCS1-v1_5 with SHA-256) |
| **Key Size** | 2048-bit minimum (4096 recommended) |
| **Key Format** | PKCS#8 PEM (private), X.509 PEM (public) |
| **Key Generation** | `openssl genrsa -out jwt-private.pem 4096 && openssl rsa -in jwt-private.pem -pubout -out jwt-public.pem` |
| **Token Structure** | Header: `{"alg":"RS256","typ":"JWT"}`; Claims: `sub=userId`, `roles`, `iat`, `exp`, `iss`, `aud` |
| **Access Token TTL** | 15 minutes (configurable via `JWT_ACCESS_TTL_MINUTES`) |
| **Refresh Token TTL** | 7 days (configurable via `JWT_REFRESH_TTL_DAYS`); rotating; stored hashed in Redis/Mongo |
| **Revocation** | Refresh token blacklist (Redis SET with TTL); checked on refresh |

### 5.2 Token Flow
```mermaid
sequenceDiagram
    Farmer->>POST /auth/register: email, phone, password, name
    Spring Boot->>MongoDB: Save User (BCrypt hash)
    Spring Boot-->>Farmer: 201 UserResponse
    Farmer->>POST /auth/login: identifier, password
    Spring Boot->>BCrypt: Verify password
    Spring Boot->>JwtService: Generate access (RS256) + refresh tokens
    Spring Boot->>Redis: Store refresh token hash (TTL=7d)
    Spring Boot-->>Farmer: 200 TokenResponse {access, refresh, expiresIn}
    Farmer->>GET /api/...: Authorization: Bearer <access>
    JwtFilter->>JwtService: Validate signature, exp, iss, aud
    JwtFilter->>UserRepository: Load user by sub
    JwtFilter->>SecurityContext: Set Authentication
    Controller->>Service: Authorized call
```

### 5.3 Password Policy (SEC-05)
| Rule | Value | Implementation |
|------|-------|----------------|
| Minimum length | **8 characters** (confirmed) | `@Size(min=8)` on DTO |
| Uppercase | At least 1 | Custom validator / regex |
| Lowercase | At least 1 | Custom validator / regex |
| Digit | At least 1 | Custom validator / regex |
| Special char | At least 1 (`!@#$%^&*`) | Custom validator / regex |
| Common passwords | Rejected (zxcvbn) | `zxcvbn` library integration |
| Hashing | BCrypt cost 12 | `BCryptPasswordEncoder(12)` |

### 5.4 Authorization Rules
| Resource | FARMER | ADMIN |
|----------|--------|-------|
| `POST /auth/register` | Public | Public |
| `POST /auth/login` | Public | Public |
| `GET /auth/me` | Own user | All users |
| `POST /farms` | Create own | All |
| `GET /farms` | List own | All |
| `GET /farms/{id}` | Own only | All |
| `PUT /farms/{id}` | Own only | All |
| `DELETE /farms/{id}` | Deactivate own | All |
| `POST /farms/{farmId}/crop-cycles` | Own farm | All |
| `GET /farms/{farmId}/crop-cycles` | Own farm | All |
| `GET /farms/{farmId}/crop-cycles/active` | Own farm | All |
| `PUT /crop-cycles/{id}` | Own farm | All |
| `POST /crop-cycles/{id}/complete` | Own farm | All |

Implemented via `@PreAuthorize("@farmSecurity.isOwner(#farmId)")` or similar.

---

## 6. REST API Implementation Order

Based on MONGODB_SCHEMA_AND_API_SPEC.md §5 and dependency graph:

| Order | Endpoint | Controller | Dependencies | Acceptance Criteria |
|-------|----------|------------|--------------|---------------------|
| 1 | `POST /api/v1/auth/register` | AuthController | UserRepository, JwtService, BCrypt | AC-01 |
| 2 | `POST /api/v1/auth/login` | AuthController | UserRepository, JwtService, Redis | AC-02 |
| 3 | `POST /api/v1/auth/refresh` | AuthController | JwtService, Redis | AC-03 |
| 4 | `POST /api/v1/auth/logout` | AuthController | JwtService, Redis | AC-03 |
| 5 | `GET /api/v1/auth/me` | AuthController | JwtFilter, UserRepository | AC-02 |
| 6 | `POST /api/v1/farms` | FarmController | FarmRepository, FarmService, Validation | AC-04 |
| 7 | `GET /api/v1/farms` | FarmController | FarmRepository, Pagination | AC-05 |
| 8 | `GET /api/v1/farms/{farmId}` | FarmController | FarmRepository, AuthZ | AC-05 |
| 9 | `PUT /api/v1/farms/{farmId}` | FarmController | FarmRepository, Validation, AuthZ | AC-04, AC-05 |
| 10 | `DELETE /api/v1/farms/{farmId}` | FarmController | FarmRepository, AuthZ | AC-05 |
| 11 | `POST /api/v1/farms/{farmId}/crop-cycles` | CropCycleController | CropCycleRepository, FarmService (authZ), Validation | AC-06 |
| 12 | `GET /api/v1/farms/{farmId}/crop-cycles` | CropCycleController | CropCycleRepository, Pagination, AuthZ | AC-06 |
| 13 | `GET /api/v1/farms/{farmId}/crop-cycles/active` | CropCycleController | CropCycleRepository, AuthZ | AC-06 |
| 14 | `PUT /api/v1/crop-cycles/{cropCycleId}` | CropCycleController | CropCycleRepository, Validation, AuthZ | AC-06 |
| 15 | `POST /api/v1/crop-cycles/{cropCycleId}/complete` | CropCycleController | CropCycleRepository, AuthZ | AC-06 |

**Parallelizable Groups:**
- Group A (1-5): Auth endpoints (depend on UserRepository, JwtService, Redis)
- Group B (6-10): Farm endpoints (depend on FarmRepository, FarmService)
- Group C (11-15): Crop Cycle endpoints (depend on CropCycleRepository, FarmService for authZ)

---

## 7. Request Validation and Error Handling

### 7.1 Validation Layers
1. **DTO Level** — Bean Validation annotations (`@NotNull`, `@Size`, `@Min`, `@Max`, `@Pattern`, `@Email`, `@ValidEnum`)
2. **Cross-Field** — Custom validators (`@ValidCropCycle`, `@UniqueActiveCropCycle`, coordinate bounds)
3. **Service Level** — Business rule validation (ownership, uniqueness, state transitions)
4. **Controller Level** — `@Valid` on `@RequestBody`; `BindingResult` not used (exception thrown)

### 7.2 Global Exception Handler (RFC 7807)
```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        pd.setType(URI.create("https://jalrakshak.example.com/errors/validation-failed"));
        pd.setTitle("Validation Failed");
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("errors", ex.getBindingResult().getFieldErrors().stream()
            .map(e -> Map.of("field", e.getField(), "code", e.getCode(), "message", e.getDefaultMessage(), "rejectedValue", e.getRejectedValue()))
            .toList());
        pd.setProperty("traceId", TraceIdFilter.getTraceId());
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorized(UnauthorizedException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        pd.setType(URI.create("https://jalrakshak.example.com/errors/unauthorized"));
        pd.setTitle("Unauthorized");
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("traceId", TraceIdFilter.getTraceId());
        HttpHeaders headers = new HttpHeaders();
        headers.add("WWW-Authenticate", "Bearer error=\"invalid_token\"");
        return new ResponseEntity<>(pd, headers, HttpStatus.UNAUTHORIZED);
    }
    // ... other handlers
}
```

### 7.3 Error Response Example (Matches MONGODB_SCHEMA_AND_API_SPEC.md §5.2)
```json
{
  "type": "https://jalrakshak.example.com/errors/validation-failed",
  "title": "Validation Failed",
  "status": 400,
  "detail": "One or more fields failed validation",
  "instance": "/api/v1/farms",
  "errors": [
    { "field": "areaHectares", "code": "Min", "message": "must be > 0", "rejectedValue": 0 },
    { "field": "soilType", "code": "NotNull", "message": "must not be null", "rejectedValue": null }
  ],
  "timestamp": "2026-10-21T06:30:00Z",
  "traceId": "a1b2c3d4e5f6"
}
```

---

## 8. Health, Readiness, Logging, and Secrets

### 8.1 Health Endpoints (Spring Boot Actuator)
| Endpoint | Checks |
|----------|--------|
| `GET /actuator/health` | `mongo` (ping), `db` (ping), `diskSpace`, `ping` |
| `GET /actuator/health/liveness` | Process alive |
| `GET /actuator/health/readiness` | MongoDB reachable, no critical errors |
| `GET /actuator/info` | Git commit, build version, Java version |

**Configuration:**
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info
      base-path: /actuator
  endpoint:
    health:
      show-details: always
      probes:
        enabled: true
  health:
    mongo:
      enabled: true
    db:
      enabled: true
    diskspace:
      enabled: true
```

### 8.2 Structured JSON Logging
```yaml
logging:
  pattern:
    console: "%clr(%d{ISO8601}){faint} %clr(%5p) %clr(${PID}){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}"
  format:
    console: json
  structured:
    format:
      console:
        fields:
          timestamp: "%d{ISO8601}"
          level: "%p"
          service: "jalrakshak-backend"
          traceId: "%X{traceId}"
          spanId: "%X{spanId}"
          userId: "%X{userId}"
          farmId: "%X{farmId}"
          message: "%m"
```

**TraceId Propagation:** `TraceIdFilter` reads `X-B3-TraceId` header (or generates new), puts in MDC, adds to response header.

### 8.3 Secrets Handling
| Secret | Storage | Injection |
|--------|---------|-----------|
| `JWT_PRIVATE_KEY` | Vault / K8s Secret / Docker Secret | File mount `/secrets/jwt-private.pem` or env `JWT_PRIVATE_KEY_PEM` |
| `MONGODB_URI` | Vault / K8s Secret | Env var |
| `WEATHER_API_KEY` | Vault / K8s Secret | Env var (Phase 3) |
| `ML_SERVICE_URL` | ConfigMap / Env | Env var (Phase 5) |

**No secrets in code, config files, or Docker images.**

---

## 9. Testing Strategy

### 9.1 Test Categories and Commands

| Category | Command | Scope | Gate |
|----------|---------|-------|------|
| **Unit** | `./mvnw test -Dtest=*UnitTest` | Services, validators, mappers, JwtService, decision rules (stubbed) | Required (fail build) |
| **Integration** | `./mvnw verify -Dtest=*IntegrationTest` | @SpringBootTest + Testcontainers MongoDB; WireMock for external | Required |
| **API Contract** | `./mvnw test -Dtest=*ContractTest` | Pact consumer tests (Spring Boot as producer) | Required |
| **Security** | `./mvnw test -Dtest=*SecurityTest` | Auth flows, rate limit, headers, injection attempts | Required |
| **Static Analysis** | `./mvnw spotbugs:check pmd:check` | SAST | Required |

### 9.2 Testcontainers MongoDB Configuration
```java
@Testcontainers
@SpringBootTest
class BaseIntegrationTest {

    @Container
    static final MongoDBContainer MONGO = new MongoDBContainer("mongo:7.0")
        .withExposedPorts(27017)
        .withReuse(true);

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO::getReplicaSetUrl);
        registry.add("spring.data.mongodb.database", () -> "jalrakshak_test");
    }
}
```

### 9.3 Pact Contract Tests (Producer)
```java
@ExtendWith(PactConsumerTestExt.class)
@Provider("jalrakshak-backend")
@PactBroker(url = "${pact.broker.url:http://localhost:9292}")
class AuthContractTest {

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactTest(PactVerificationContext context, HttpRequest request) {
        context.verifyInteraction();
    }

    @State("a user exists with email farmer@example.com")
    void userExists() {
        // Seed test data
    }
}
```

### 9.4 Security Tests
| Test | Tool | Criteria |
|------|------|----------|
| Auth bypass attempts | JUnit + MockMvc | 401/403 on protected endpoints without token |
| Expired token rejection | JUnit + MockMvc | 401 with `WWW-Authenticate` |
| Rate limit enforcement | JUnit + MockMvc | 429 after threshold; `Retry-After` header |
| SQL/NoSQL injection | OWASP ZAP / manual | No data exfiltration; parameterized queries |
| Password policy enforcement | JUnit | 400 on weak passwords; BCrypt cost 12 verified |

---

## 10. Ordered Implementation Tasks

| Task ID | Task | Dependencies | Expected Files | Test Command | Acceptance Criteria |
|---------|------|--------------|----------------|--------------|---------------------|
| **T01** | Initialize Spring Boot project (Maven, Java 17, SB 3.2+) | None | `backend/pom.xml`, `backend/src/main/java/.../JalrakshakApplication.java` | `./mvnw compile` | Project compiles |
| **T02** | Configure MongoDB connection, profiles, env vars | T01 | `application.yml`, `application-dev.yml`, `application-test.yml`, `MongoConfig.java` | `./mvnw test -Dtest=MongoConfigTest` | Connects to Testcontainers MongoDB |
| **T03** | Create 8 document models with annotations, indexes | T02 | `model/*.java` (8 files) | `./mvnw test -Dtest=*RepositoryTest` | Documents persist, indexes created |
| **T04** | Create 8 Spring Data repositories | T03 | `repository/*.java` (8 files) | `./mvnw test -Dtest=*RepositoryTest` | CRUD operations work |
| **T05** | Implement JWT RS256 token provider, filter, UserDetailsService | T02 | `security/JwtTokenProvider.java`, `JwtAuthenticationFilter.java`, `CustomUserDetailsService.java`, `SecurityConstants.java` | `./mvnw test -Dtest=Jwt*Test` | Tokens sign/verify; claims correct |
| **T06** | Configure Spring Security filter chain, CORS, method security | T05 | `config/SecurityConfig.java`, `WebConfig.java` | `./mvnw test -Dtest=SecurityConfigTest` | Public auth endpoints; protected others; CORS ok |
| **T07** | Implement AuthService (register, login, refresh, logout, me) | T03, T05 | `service/AuthService.java`, `dto/auth/*.java` | `./mvnw test -Dtest=AuthServiceTest` | BCrypt hash; tokens issued; refresh rotates |
| **T08** | Implement AuthController (5 endpoints) | T07 | `controller/AuthController.java` | `./mvnw test -Dtest=AuthControllerIntegrationTest` | AC-01, AC-02, AC-03 pass |
| **T09** | Implement global exception handler (RFC 7807) | T01 | `exception/GlobalExceptionHandler.java`, `exception/*.java` | `./mvnw test -Dtest=GlobalExceptionHandlerTest` | Error format matches spec |
| **T10** | Implement FarmService + FarmController (CRUD + pagination + authZ) | T03, T06 | `service/FarmService.java`, `controller/FarmController.java`, `dto/farm/*.java` | `./mvnw test -Dtest=Farm*IntegrationTest` | AC-04, AC-05 pass |
| **T11** | Implement CropCycleService + Controller (CRUD + single-active enforcement) | T03, T06, T10 | `service/CropCycleService.java`, `controller/CropCycleController.java`, `dto/cropcycle/*.java` | `./mvnw test -Dtest=CropCycle*IntegrationTest` | AC-06 pass |
| **T12** | Add custom validators (enum, coordinates, unique active crop cycle) | T10, T11 | `validation/*.java` | `./mvnw test -Dtest=*ValidatorTest` | 400 on invalid; cross-field works |
| **T13** | Configure Actuator health, OpenAPI/Swagger, JSON logging, TraceId filter | T01 | `config/OpenApiConfig.java`, `config/JacksonConfig.java`, `util/TraceIdFilter.java` | `./mvnw test -Dtest=ActuatorHealthTest` | `/actuator/health` works; Swagger UI at `/swagger-ui.html` |
| **T14** | Write unit tests (≥80% coverage for services, validators, JwtService) | T07-T12 | `*Test.java` in `src/test` | `./mvnw test jacoco:report` | Coverage ≥80% |
| **T15** | Write integration tests for all endpoints (Testcontainers) | T08-T11 | `*IntegrationTest.java` | `./mvnw verify -Dtest=*IntegrationTest` | All endpoints tested |
| **T16** | Write Pact contract tests for public endpoints | T08-T11 | `*ContractTest.java` | `./mvnw test -Dtest=*ContractTest` | Contracts published/verified |
| **T17** | Run security tests (auth, rate limit, headers, injection) | T06, T09 | `*SecurityTest.java` | `./mvnw test -Dtest=*SecurityTest` | AC-F08, AC-F09 pass |
| **T18** | Verify all Phase 2 acceptance criteria | T01-T17 | N/A | Full test suite | AC-01 to AC-06, AC-F08, AC-F09 |

---

## 11. Task Dependencies Graph

```
T01
 └─ T02
     ├─ T03 → T04
     └─ T05 → T06
T03 → T07 → T08
T03, T06 → T10 → T11
T03, T06, T10 → T11
T10, T11 → T12
T01 → T13
T07-T12 → T14
T08-T11 → T15
T08-T11 → T16
T06, T09 → T17
T01-T17 → T18
```

**Critical Path:** T01 → T02 → T03 → T07 → T08 → T15 → T18 (Auth vertical slice first)

---

## 12. Mapping to Requirements and Acceptance Criteria

| Task | Requirement IDs | Acceptance Criteria |
|------|-----------------|---------------------|
| T01-T02 | NFR-07, NFR-08 | — |
| T03-T04 | FR-01, FR-03, FR-04 | — |
| T05-T06 | FR-02, SEC-01, SEC-02, SEC-03, SEC-06, SEC-07, SEC-08 | AC-02, AC-03, AC-F08 |
| T07-T08 | FR-01, FR-02 | AC-01, AC-02, AC-03 |
| T09 | NFR-08, SEC-09, SEC-10 | AC-F07, AC-F08, AC-F09 |
| T10 | FR-03, SEC-02, SEC-06, SEC-09, SEC-10 | AC-04, AC-05 |
| T11 | FR-04, SEC-02, SEC-06, SEC-09, SEC-10 | AC-06 |
| T12 | SEC-09, SEC-10 | AC-04, AC-06, AC-F07 |
| T13 | NFR-03, NFR-08 | AC-NF03, AC-NF07, AC-NF08 |
| T14-T17 | All | All Phase 2 ACs |

---

## 13. Risks and Mitigations

| Risk | Likelihood | Impact | Mitigation |
|------|------------|--------|------------|
| RS256 key management complexity | Medium | High | Generate keys in setup script; document rotation procedure; use vault in prod |
| Testcontainers flakiness (CI) | Medium | Medium | Reuse containers; fixed ports; health checks; retry logic |
| MongoDB schema evolution conflicts | Low | High | Native validators + Mongock migrations; test down-migrations |
| Password policy too strict for farmers | Low | Medium | zxcvbn scoring; graceful error messages; usability test in Phase 6 |
| Rate limiting not in Phase 2 scope | High | Low | Document as Phase 7; implement Bucket4j config placeholder |

---

## 14. Remaining Decisions (Blockers for Phase 2 Start)

| Decision | Status | Needed For |
|----------|--------|------------|
| Weather API provider & credentials | **Unresolved** | Phase 3 (not blocking Phase 2) |
| JWT key pair generation & storage procedure | **Partially resolved** — RS256 confirmed; key gen/storage/rotation pending | T05 (AuthService) |
| Password complexity rules (beyond min 8) | **Partially resolved** — Min 8 confirmed; upper/lower/digit/special/zxcvbn proposed | T07 (AuthService) |
| Rate limiting tiers | **Unresolved** | Phase 7 (not blocking Phase 2) |
| Language support priority | **Unresolved** | Phase 6 (not blocking Phase 2) |
| Water quantity formula validation | **Unresolved** | Phase 5 (not blocking Phase 2) |
| Pilot scope | **Unresolved** | Phase 7 (not blocking Phase 2) |

**Phase 2 can start with:** Generated RS256 key pair (dev), proposed password complexity rules, default rate limits (configurable). Remaining decisions deferred to later phases as noted.

---

## 15. Source of Truth Note

> **This plan is governed by `AGENTS.md`** which mandates:
> - Fixed technology stack: React/Vite, Spring Boot, **MongoDB + Spring Data MongoDB**, Python/FastAPI, Spring Security/JWT
> - Roadmap is source of truth: Phase 2 deliverables only; no Phase 3+ implementation
> - MongoDB is the **only approved database**; PostgreSQL recommendation in Development Plan §3 is **superseded** by AGENTS.md §2 and Technology Stack §8.1
> - No JPA/Hibernate for MongoDB integration
> - Test before claiming completion: report only tests that actually ran
> - Real integrations only; no mock data or invented metrics
> - Confirmed stakeholder decisions: **Rice (Kharif)** crop, **Uttar Pradesh** region, **RS256** JWT algorithm, **minimum 8-character** password

All tasks, configs, and acceptance criteria herein derive from the Phase 1 deliverables (`docs/*.md`) and must be verified against them during implementation.