# JALRAKSHAK — System Architecture Document

**Version:** 1.0 (Phase 1 Deliverable)  
**Status:** PROPOSED — NEEDS STAKEHOLDER APPROVAL  
**Governed by:** AGENTS.md (technology stack, phase order, non-negotiable rules)  
**Roadmap:** JALRAKSHAK_Development_Plan.md  

---

## 1. System Context

```mermaid
flowchart TB
    subgraph Farmer_Context["Farmer Environment"]
        F[("Farmer\n(Smartphone)")]
    end

    subgraph External_Services["External Services"]
        WAPI[("Weather / Forecast API\n(IMD / OpenWeather / Custom)")]
        SM[("Soil Moisture Sensor\n(Optional IoT)")]
    end

    subgraph JALRAKSHAK_System["JALRAKSHAK System"]
        FE[["React + Vite Frontend\n(Farmer Dashboard)"]]
        BE[["Spring Boot Backend\n(REST API, Auth, Orchestration)"]]
        DB[("MongoDB\n(Primary Data Store)")]
        ML[["Python FastAPI ML Service\n(Irrigation Need Prediction)"]]
        MODEL[("Trained ML Model\n(Scikit-learn / XGBoost)")]
    end

    F <-- "HTTPS / REST + JWT" --> FE
    FE <-- "HTTPS / REST + JWT" --> BE
    BE <-- "Spring Data MongoDB" --> DB
    BE <-- "HTTPS / REST" --> WAPI
    BE <-- "HTTPS / REST (Internal)" --> ML
    ML <-- "In-memory / Model Registry" --> MODEL
    SM -.->|"Optional Ingestion Path"| BE

    classDef external fill:#f9f,stroke:#333;
    classDef jalrakshak fill:#bbf,stroke:#333;
    class WAPI,SM external;
    class FE,BE,DB,ML,MODEL jalrakshak;
```

### Context Narrative
- **Farmer** interacts via **React/Vite SPA** over HTTPS.
- **Frontend** calls **Spring Boot REST API** with JWT Bearer tokens.
- **Spring Boot** owns: authentication, validation, business logic, data persistence (MongoDB), weather integration, ML service orchestration, decision engine.
- **MongoDB** stores all persistent data: users, farms, crop cycles, weather observations, soil readings, recommendations, farmer actions.
- **Python FastAPI ML Service** is **private** (not exposed to frontend); Spring Boot calls it with validated features, receives prediction + metadata.
- **Weather API** is external; Spring Boot handles timeouts, retries, caching, freshness.
- **Soil Moisture Sensor** is optional; ingestion path defined but implementation deferred.

---

## 2. Component Architecture

```mermaid
flowchart LR
    subgraph Frontend["React + Vite Frontend"]
        UI_AUTH["Auth Pages\n(Register/Login)"]
        UI_FARM["Farm Profile\nForms"]
        UI_DASH["Dashboard\n(Recommendation)"]
        UI_HIST["History &\nAlerts"]
        UI_STATE["State Mgmt\n(Context/Zustand)"]
        UI_API["API Client\n(Axios/Fetch)"]
    end

    subgraph Backend["Spring Boot Backend"]
        AUTH_CTRL["Auth Controller\n/JWT Filter"]
        FARM_CTRL["Farm Controller\n/farms, /crop-cycles"]
        REC_CTRL["Recommendation Controller\n/recommendations"]
        ACTION_CTRL["Action Controller\n/feedback"]
        AUTH_SVC["Auth Service\n(JWT, BCrypt)"]
        FARM_SVC["Farm Service\n(Validation, CRUD)"]
        WEATHER_SVC["Weather Service\n(Fetch, Cache, Freshness)"]
        FEATURE_SVC["Feature Engineering\n(Crop Stage, Water Balance)"]
        ML_CLIENT["ML Service Client\n(FastAPI, Timeout, Retry)"]
        DECISION_ENGINE["Decision Engine\n(Rules + ML + Forecast)"]
        REC_SVC["Recommendation Service\n(Persistence, Snapshots)"]
        SECURITY_CFG["Security Config\n(Spring Security)"]
    end

    subgraph Database["MongoDB"]
        COLL_USERS[("users")]
        COLL_FARMS[("farms")]
        COLL_CROPS[("crop_cycles")]
        COLL_WEATHER[("weather_observations")]
        COLL_SOIL[("soil_moisture_readings")]
        COLL_RECS[("irrigation_recommendations")]
        COLL_ACTIONS[("farmer_actions")]
        COLL_MODELS[("model_metadata")]
    end

    subgraph ML_Service["Python FastAPI ML Service"]
        ML_HEALTH["GET /health"]
        ML_PREDICT["POST /predict/irrigation-need"]
        ML_PREPROC["Preprocessing Pipeline\n(Same as Training)"]
        ML_MODEL["Loaded Model\n(v1.x.x)"]
        ML_META["Model Metadata\n(Version, Training Date, Metrics)"]
    end

    UI_AUTH --> UI_API
    UI_FARM --> UI_API
    UI_DASH --> UI_API
    UI_HIST --> UI_API
    UI_API --> AUTH_CTRL
    UI_API --> FARM_CTRL
    UI_API --> REC_CTRL
    UI_API --> ACTION_CTRL

    AUTH_CTRL --> AUTH_SVC
    FARM_CTRL --> FARM_SVC
    REC_CTRL --> REC_SVC
    ACTION_CTRL --> ACTION_CTRL

    FARM_SVC --> COLL_FARMS
    FARM_SVC --> COLL_CROPS
    WEATHER_SVC --> COLL_WEATHER
    FEATURE_SVC --> COLL_FARMS
    FEATURE_SVC --> COLL_CROPS
    FEATURE_SVC --> COLL_WEATHER
    FEATURE_SVC --> COLL_SOIL
    ML_CLIENT --> ML_PREDICT
    DECISION_ENGINE --> ML_CLIENT
    DECISION_ENGINE --> WEATHER_SVC
    DECISION_ENGINE --> FEATURE_SVC
    REC_SVC --> COLL_RECS
    REC_SVC --> COLL_ACTIONS
    AUTH_SVC --> COLL_USERS

    ML_PREDICT --> ML_PREPROC
    ML_PREPROC --> ML_MODEL
    ML_MODEL --> ML_META
```

---

## 3. Service Boundaries and Responsibilities

| Service | Responsibility | Owns Data | Exposes |
|---------|---------------|-----------|---------|
| **React Frontend** | Presentation, user interaction, form validation (client-side), state management, i18n | None (transient UI state) | N/A (consumes APIs) |
| **Spring Boot Backend** | **Single source of truth for business logic**; authz, validation, orchestration, data persistence, external integrations, decision engine | All MongoDB collections | REST API (JSON) |
| **MongoDB** | Persistent storage; document-oriented; schema validation via Spring Data | All collections | Spring Data MongoDB repositories |
| **Python FastAPI ML Service** | **Model inference only**; preprocessing, prediction, metadata; no business logic, no auth (trusted internal caller) | Model artifacts, metadata (optional) | `/health`, `/predict/irrigation-need` |
| **Weather API** | External data provider | N/A | HTTP/JSON (provider-specific) |

### Critical Boundary Rules (Enforced by AGENTS.md)

1. **Frontend NEVER calls ML service directly** — all ML requests go through Spring Boot.
2. **ML service has NO authentication** — it trusts Spring Boot (internal network / service mesh).
3. **Decision engine lives in Spring Boot** — ML provides *prediction*; Spring Boot applies *rules* and emits *recommendation*.
4. **MongoDB accessed ONLY via Spring Data MongoDB** — no raw driver bypass, no JPA/Hibernate.
5. **Secrets in environment variables only** — weather API key, JWT secret, MongoDB URI, ML service URL.

---

## 4. End-to-End Request Flow

```mermaid
sequenceDiagram
    actor Farmer
    participant FE as React Frontend
    participant BE as Spring Boot
    participant DB as MongoDB
    participant WX as Weather API
    participant ML as FastAPI ML

    Farmer->>FE: Opens Dashboard
    FE->>BE: GET /api/farms/{farmId}/recommendations/latest (JWT)
    BE->>DB: Load Farm + Active CropCycle
    BE->>WX: GET Weather + Forecast (lat, lon)
    WX-->>BE: Current + 72h Forecast
    BE->>DB: Save WeatherObservation
    BE->>BE: Validate Data Freshness (≤24h weather, ≤48h soil)
    alt Data Insufficient
        BE-->>FE: 200 OK { action: "INSUFFICIENT_DATA", warnings: [...] }
    else Data Sufficient
        BE->>BE: Feature Engineering (cropStage, waterBalance, soilDeficit, ...)
        BE->>ML: POST /predict/irrigation-need { features }
        ML-->>BE: { irrigationNeedMm, confidence, modelVersion }
        BE->>BE: Decision Engine (ML + Rules + Forecast)
        BE->>DB: Save IrrigationRecommendation (full snapshot)
        BE-->>FE: 200 OK { recommendation }
    end
    FE->>Farmer: Render Recommendation Card
    Farmer->>FE: Records Action (IRRIGATED/WAITED/PARTIAL)
    FE->>BE: POST /api/recommendations/{id}/feedback { action, notes }
    BE->>DB: Save FarmerAction (linked to Recommendation)
    BE-->>FE: 201 Created
```

---

## 5. Decision Engine: ML Prediction vs. Deterministic Rules

```mermaid
flowchart TD
    subgraph Inputs["Validated Inputs"]
        ML_PRED["ML Prediction\n(irrigationNeedMm, confidence)"]
        FORECAST["Weather Forecast\n(rainfall, ET0, humidity)"]
        RULES["Agronomic Rules\n(Crop stage thresholds,\nwater balance, safety)"]
        FRESHNESS["Data Freshness\n(weather age, soil age)"]
    end

    subgraph Decision_Engine["Decision Engine (Spring Boot)"]
        CHECK_FRESH["Freshness Gate\nALL critical data fresh?"]
        CHECK_ML["ML Available &\nIn-Range?"]
        APPLY_RULES["Apply Deterministic Rules\nPriority: Safety > Forecast > ML"]
        CALC_QTY["Calculate Quantity\n(If IRRIGATE/PARTIAL)"]
        SET_CONF["Set Confidence\n(HIGH/MEDIUM/LOW)"]
    end

    subgraph Outputs["Recommendation Output"]
        ACTION["Action Enum\nIRRIGATE / WAIT / PARTIAL / INSUFFICIENT"]
        QTY["Water Quantity (mm, L)\nOnly if validated logic exists"]
        REASONS["Human-readable Reasons\nPer rule triggered"]
        WARNINGS["Data Quality Warnings\nStale, missing, low confidence"]
        META["Model Version, Timestamp\nDecision Rules Applied"]
    end

    ML_PRED --> CHECK_ML
    FORECAST --> APPLY_RULES
    RULES --> APPLY_RULES
    FRESHNESS --> CHECK_FRESH
    CHECK_FRESH -- No --> ACTION
    CHECK_FRESH -- Yes --> CHECK_ML
    CHECK_ML -- No --> APPLY_RULES
    CHECK_ML -- Yes --> APPLY_RULES
    APPLY_RULES --> ACTION
    APPLY_RULES --> CALC_QTY
    APPLY_RULES --> REASONS
    FRESHNESS --> WARNINGS
    CHECK_ML --> SET_CONF
    APPLY_RULES --> SET_CONF
    APPLY_RULES --> META
```

### Separation Principle

| Layer | Responsibility | Example |
|-------|----------------|---------|
| **ML Model** | Estimate *continuous* irrigation need (mm) from features | "Crop needs ~28mm based on soil, stage, weather" |
| **Deterministic Rules** | Convert need → *discrete action* using safety/agronomic logic | "If forecast rain >10mm in 48h → WAIT regardless of ML" |
| **Decision Engine** | Orchestrates above; handles fallbacks; produces explainable output | Combines ML + Rules → `PARTIAL_IRRIGATION`, 15mm, reasons |

**Rule Priority (Proposed):**
1. **Safety**: Sensor failure, extreme heat warning, data stale → `INSUFFICIENT_DATA` or conservative `WAIT`
2. **Forecast**: Significant rainfall expected → defer irrigation (`WAIT`/`PARTIAL`)
3. **ML + Water Balance**: ML need > threshold + no forecast rain → `IRRIGATE`
4. **Partial**: ML need > 0 but < full threshold → `PARTIAL_IRRIGATION`
5. **Default**: Insufficient evidence → `INSUFFICIENT_DATA`

---

## 6. Authentication and Security Boundaries

```mermaid
flowchart LR
    subgraph Public["Public Internet"]
        Farmer[("Farmer")]
    end

    subgraph DMZ["DMZ / Public Endpoint"]
        FE[React App\n(Static Assets)]
        BE_PUBLIC[Spring Boot\n/api/auth/**]
    end

    subgraph Private["Private Network / VPC"]
        BE_PRIVATE[Spring Boot\n/api/** (Protected)]
        DB[(MongoDB)]
        ML[FastAPI ML\n(Internal Only)]
        WX[Weather API\n(External)]
    end

    Farmer --> FE
    Farmer --> BE_PUBLIC
    FE --> BE_PRIVATE
    BE_PRIVATE --> DB
    BE_PRIVATE --> ML
    BE_PRIVATE --> WX

    classDef public fill:#ffcccc,stroke:#333;
    classDef private fill:#ccffcc,stroke:#333;
    class Farmer,FE,BE_PUBLIC public;
    class BE_PRIVATE,DB,ML,WX private;
```

### Security Design

| Layer | Mechanism | Details |
|-------|-----------|---------|
| **Transport** | TLS 1.2+ | All external + internal (mTLS ideal) |
| **Authentication** | JWT (RS256) | Access token 15min; Refresh token 7d (rotating) |
| **Authorization** | Role-based (FARMER, ADMIN) | Farm-scoped access: user owns farm → can read/write |
| **Password Storage** | BCrypt (cost 12) | Never logged |
| **Input Validation** | Bean Validation (JSR-380) + custom | All DTOs validated; sanitization |
| **Rate Limiting** | Bucket4j / Gateway | Per-user: 100/min auth, 30/min recommendation |
| **CORS** | Restricted origins | Only frontend domain(s) |
| **Secrets** | Env vars / Vault | JWT secret, Weather API key, MongoDB URI, ML service URL |
| **Audit Log** | Structured JSON | Auth decisions, data mutations, recommendation generated |
| **Sensitive Data** | No PII in logs | Mask tokens, passwords, coordinates in logs |

---

## 7. External Data Integration Boundaries

### 7.1 Weather / Forecast API

| Aspect | Design |
|--------|--------|
| **Provider** | PROPOSED: IMD (primary) + OpenWeatherMap (fallback) — NEEDS APPROVAL |
| **Data Retrieved** | Current: temp, humidity, wind, rainfall, ET₀ (calculated); Forecast: 72h hourly |
| **Units** | SI (mm, °C, m/s, %) — normalized on ingest |
| **Freshness** | Current ≤ 6h "fresh"; ≤ 24h "usable"; > 24h → `INSUFFICIENT_DATA` |
| **Caching** | In-memory (Caffeine) + MongoDB persistence; TTL 1h |
| **Timeout** | 5s connect, 10s read; circuit breaker (3 failures → 30s open) |
| **Retry** | 1x with exponential backoff |
| **Fallback** | Cached data if < 6h old; else `INSUFFICIENT_DATA` |
| **Error Handling** | Structured error response; log with correlation ID |

### 7.2 Soil Moisture Sensor (Optional)

| Aspect | Design |
|--------|--------|
| **Ingestion Path** | Spring Boot endpoint: `POST /api/farms/{farmId}/soil-readings` (authenticated) |
| **Payload** | `{ sensorId, timestamp, depthCm, moisturePct, batteryPct, signalStrength }` |
| **Validation** | Range checks (0–100%), timestamp not future, sensor registered to farm |
| **Storage** | `soil_moisture_readings` collection |
| **Usage in Decision** | If reading ≤ 48h old → primary soil moisture input; else → warning + excluded |
| **Calibration** | Per-sensor offset stored in farm profile (future) |
| **MVP Status** | Contract defined; implementation Phase 3+ |

---

## 8. Development and Deployment Topology

### 8.1 Local Development
```
┌─────────────────────────────────────────────────────────────┐
│  Developer Machine                                          │
├─────────────────────────────────────────────────────────────┤
│  ┌──────────┐  ┌──────────────┐  ┌────────┐  ┌────────────┐ │
│  │ React    │  │ Spring Boot  │  │ MongoDB│  │ FastAPI    │ │
│  │ (Vite    │  │ (Maven/Gradle)│  │ (Docker│  │ (Python    │ │
│  │  dev)    │  │  :8080       │  │  :27017)│  │  :8000)    │ │
│  │  :5173   │  │              │  │        │  │            │ │
│  └──────────┘  └──────────────┘  └────────┘  └────────────┘ │
│         │             │              │            │          │
│         └─────────────┴──────────────┴────────────┘          │
│                           │                                    │
│                    .env files (secrets)                        │
└─────────────────────────────────────────────────────────────┘
```

### 8.2 Production Deployment (Proposed — NEEDS APPROVAL)

```mermaid
flowchart TB
    subgraph Cloud["Cloud / VPC"]
        LB[("Load Balancer\n(TLS Termination)")]
        FE_POD[["React Static Files\n(NGINX / CDN)"]]
        BE_POD[["Spring Boot Pods\n(HPA: 2-10 replicas)"]]
        ML_POD[["FastAPI Pods\n(HPA: 1-5 replicas)"]]
        MONGO[("MongoDB\n(Replica Set / Atlas)")]
        REDIS[("Redis\n(Caching / Rate Limit)")]
    end

    Internet[("Internet")] --> LB
    LB --> FE_POD
    LB --> BE_POD
    BE_POD --> MONGO
    BE_POD --> REDIS
    BE_POD --> ML_POD
    BE_POD --> WX[("Weather API")]
    ML_POD --> MODEL_STORE[("Model Registry\n(S3 / GCS / Local)")]

    classDef managed fill:#fff3cd,stroke:#333;
    class MONGO,REDIS,MODEL_STORE managed;
```

### 8.3 Configuration (Environment Variables)

| Variable | Service | Required | Description |
|----------|---------|----------|-------------|
| `MONGODB_URI` | Spring Boot | Yes | `mongodb://user:pass@host:27017/jalrakshak?replicaSet=rs0` |
| `JWT_SECRET` | Spring Boot | Yes | RS256 private key (PEM) or HS256 secret (min 256-bit) |
| `JWT_ACCESS_TTL_MINUTES` | Spring Boot | No | Default 15 |
| `JWT_REFRESH_TTL_DAYS` | Spring Boot | No | Default 7 |
| `WEATHER_API_KEY` | Spring Boot | Yes | Provider-specific |
| `WEATHER_API_BASE_URL` | Spring Boot | Yes | Provider endpoint |
| `WEATHER_CACHE_TTL_SECONDS` | Spring Boot | No | Default 3600 |
| `ML_SERVICE_URL` | Spring Boot | Yes | `http://ml-service:8000` (internal) |
| `ML_SERVICE_TIMEOUT_MS` | Spring Boot | No | Default 3000 |
| `SPRING_PROFILES_ACTIVE` | Spring Boot | No | `dev`, `prod`, `test` |
| `MODEL_PATH` | FastAPI | Yes | Path to serialized model artifact |
| `MODEL_VERSION` | FastAPI | Yes | e.g., `v1.0.0-baseline` |
| `FASTAPI_HOST` | FastAPI | No | Default `0.0.0.0` |
| `FASTAPI_PORT` | FastAPI | No | Default `8000` |
| `LOG_LEVEL` | Both | No | `INFO` (prod), `DEBUG` (dev) |

### 8.4 Health Checks

| Service | Endpoint | Checks |
|---------|----------|--------|
| Spring Boot | `GET /actuator/health` | DB connectivity, Weather API reachable, ML service reachable, Disk space |
| FastAPI | `GET /health` | Model loaded, Preprocessing pipeline OK, Disk space |
| MongoDB | `db.runCommand({ping:1})` | Primary reachable, replication lag < 10s |
| React (static) | `GET /` | 200 OK, index.html served |

### 8.5 Logging

- **Format:** Structured JSON (Logstash/ELK compatible)
- **Fields:** `timestamp`, `level`, `service`, `traceId`, `spanId`, `userId`, `farmId`, `message`, `error`
- **Correlation:** `traceId` propagated via HTTP headers (`X-B3-TraceId`) across services
- **Sampling:** 100% errors/warn; 10% info/debug (configurable)
- **No PII:** Coordinates hashed, tokens masked, passwords never logged

---

## 9. Proposed Architecture Decisions (Require Approval)

| ADR ID | Decision | Options | Proposed | Rationale |
|--------|----------|---------|----------|-----------|
| ADR-001 | Database | PostgreSQL vs MongoDB | **MongoDB** | AGENTS.md §2; flexible schema for evolving farm data; Spring Data MongoDB native |
| ADR-002 | ML Service Communication | Sync HTTP vs Async Queue | **Sync HTTP** (with timeout/circuit breaker) | Simpler for MVP; latency acceptable (<3s); async adds complexity |
| ADR-003 | Auth Token Strategy | JWT (stateless) vs Session (stateful) | **JWT (RS256)** | Scales horizontally; no session store; mobile-friendly |
| ADR-004 | Weather Provider | IMD vs OpenWeather vs Custom | **IMD + OpenWeather fallback** | IMD free for Indian context; OpenWeather global backup |
| ADR-005 | Model Serving | FastAPI (Python) vs TensorFlow Serving vs ONNX Runtime | **FastAPI** | Team Python expertise; easy preprocessing; versioning built-in |
| ADR-006 | Frontend State Management | React Context vs Redux vs Zustand | **Zustand** | Lightweight, TypeScript-friendly, minimal boilerplate |
| ADR-007 | API Versioning | URL path (`/api/v1/`) vs Header | **URL Path** | Explicit, cacheable, browser-debuggable |
| ADR-008 | Data Validation | Bean Validation only vs Bean + Custom | **Bean + Custom** | Cross-field, business-rule validation needs custom validators |

---

## 10. Unresolved Approvals Required

| # | Decision | Impact | Owner | Deadline |
|---|----------|--------|-------|----------|
| 1 | Weather API provider & credentials | Weather service implementation | Engineering | Phase 3 |
| 2 | ML service deployment topology (sidecar vs separate) | Network, scaling, latency | DevOps | Phase 4 |
| 3 | Decision engine rule set (initial version) | Recommendation logic | Agronomy + Engineering | Phase 5 |
| 4 | JWT key management (rotation, RS256 vs HS256) | Security, operations | Security | Phase 2 |
| 5 | Rate limiting tiers (free vs paid?) | API gateway config | Product | Phase 7 |

---

## 11. Source of Truth Note

> **This document is governed by `AGENTS.md`** which mandates:
> - Fixed technology stack: React/Vite, Spring Boot, **MongoDB + Spring Data MongoDB**, Python/FastAPI, Spring Security/JWT
> - Roadmap is source of truth: Phase 1 deliverables only; no Phase 2 implementation
> - MongoDB is the **only approved database**; PostgreSQL recommendation in Development Plan §3 is **superseded** by AGENTS.md §2 and Technology Stack §8.1
> - No JPA/Hibernate for MongoDB integration
> - ML service **not exposed to public frontend** — Spring Boot is the sole orchestrator
> - Real integrations only; no mock data or invented metrics

All architecture decisions herein are **proposed** and require stakeholder review before Phase 2 implementation begins.