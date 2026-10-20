# JALRAKSHAK — MVP Acceptance Criteria and Security Requirements

**Version:** 1.0 (Phase 1 Deliverable)  
**Status:** PROPOSED — NEEDS STAKEHOLDER APPROVAL  
**Governed by:** AGENTS.md (technology stack, phase order, non-negotiable rules)  
**Roadmap:** JALRAKSHAK_Development_Plan.md  

---

## 1. MVP Acceptance Criteria

Each criterion has a unique ID, is testable, and traces to requirements (REQUIREMENTS_AND_SCOPE.md) and architecture (SYSTEM_ARCHITECTURE.md).

### 1.1 Functional Acceptance Criteria

| ID | Criterion | Journey | Priority | Test Type | Traceability |
|----|-----------|---------|----------|-----------|--------------|
| **AC-01** | Farmer can register with email/phone, password, name; receives 201 with userId | J1 | MUST | API + E2E | FR-01 |
| **AC-02** | Farmer can login with email/phone + password; receives JWT access (15min) + refresh (7d) tokens | J1 | MUST | API + E2E | FR-02 |
| **AC-03** | Access token rejected after expiry; refresh token rotates new access token | J1 | MUST | API + Security | FR-02, SEC-03 |
| **AC-04** | Farmer can create farm profile with all required fields; validation rejects invalid input (400) | J1 | MUST | API + E2E | FR-03 |
| **AC-05** | Farmer can view own farms (paginated); cannot access other farmers' farms | J1 | MUST | API + Security | FR-03, SEC-02 |
| **AC-06** | Farmer can create crop cycle for farm; only one ACTIVE cycle per farm allowed | J1 | MUST | API + E2E | FR-04 |
| **AC-07** | Weather data fetched for farm location; stored with timestamp; freshness validated | J2 | MUST | API + Integration | FR-05, FR-06 |
| **AC-08** | If weather data >24h old, recommendation returns `INSUFFICIENT_DATA` with warning | J2, J5 | MUST | API + Integration | FR-06, Fallback |
| **AC-09** | Feature engineering computes crop stage, water balance, soil deficit correctly | G3, G4 | MUST | Unit + Integration | FR-07 |
| **AC-10** | Spring Boot calls ML service with validated features; handles timeout (3s) and retry (1x) | G3 | MUST | Integration + Chaos | FR-08 |
| **AC-11** | Decision engine combines ML prediction + rules + forecast → action + quantity + reasons | G4 | MUST | Unit + Integration | FR-09 |
| **AC-12** | Recommendation persisted with full input snapshot (farm, crop, weather, soil, model version) | G4 | MUST | Integration | FR-10 |
| **AC-13** | Dashboard returns latest recommendation with action, quantity (if applicable), reasons, warnings, freshness | J2 | MUST | API + E2E | FR-11 |
| **AC-14** | Farmer can record action (IRRIGATED/WAITED/PARTIAL) with timestamp, volume, notes | J3 | MUST | API + E2E | FR-12 |
| **AC-15** | History shows paginated recommendations + actions with timestamps | J4 | MUST | API + E2E | FR-11, FR-12 |
| **AC-16** | Alert displayed for stale weather (>24h), missing crop cycle, ML service unavailable | J4, J5 | SHOULD | E2E | FR-15 |
| **AC-17** | UI shows `INSUFFICIENT_DATA` with specific missing items when data inadequate | J5 | MUST | E2E | FR-11, Fallback |
| **AC-18** | Water quantity shown ONLY when action=IRRIGATE/PARTIAL AND validated calc exists | J2 | MUST | E2E | FR-09, Rec Output |
| **AC-19** | Multi-language: English default; Hindi keys present for all user-facing strings | J1, J2 | SHOULD | E2E | FR-13 |
| **AC-20** | Offline: cached last recommendation displayed; syncs on reconnect | J2, J4 | SHOULD | E2E | FR-14 |

### 1.2 Failure Scenario Acceptance Criteria

| ID | Scenario | Expected Behavior | Priority | Test Type |
|----|----------|-------------------|----------|-----------|
| **AC-F01** | Weather API timeout (5s) | Use cached data if <6h old; else `INSUFFICIENT_DATA` with warning | MUST | Chaos / Integration |
| **AC-F02** | Weather API returns 5xx | Circuit breaker opens; fallback to cache; log error with traceId | MUST | Chaos / Integration |
| **AC-F03** | ML service timeout (3s) | Fallback to rule-based heuristic; confidence=LOW; warning shown | MUST | Chaos / Integration |
| **AC-F04** | ML service returns 503 (model loading) | Same as F03; alert in dashboard | MUST | Chaos / Integration |
| **AC-F05** | ML prediction out of trained range | `INSUFFICIENT_DATA` with "conditions outside model range" warning | MUST | Integration |
| **AC-F06** | No active crop cycle | `INSUFFICIENT_DATA` with "add crop details" guidance | MUST | API + E2E |
| **AC-F07** | Invalid farm input (area=0) | 400 with field-level validation errors | MUST | API |
| **AC-F08** | Expired/invalid JWT | 401 with `WWW-Authenticate` header; no data leaked | MUST | Security |
| **AC-F09** | Rate limit exceeded | 429 with `Retry-After` header; no data leaked | MUST | Security |
| **AC-F10** | MongoDB unavailable | 503 with generic message; no stack trace leaked | MUST | Chaos |

### 1.3 Non-Functional Acceptance Criteria

| ID | Criterion | Target | Measurement | Priority |
|----|-----------|--------|-------------|----------|
| **AC-NF01** | API p95 latency (excluding external calls) | ≤ 800ms | Load test (100 concurrent) | MUST |
| **AC-NF02** | Recommendation generation p95 (incl. weather+ML) | ≤ 2000ms | Load test (100 concurrent) | MUST |
| **AC-NF03** | Backend availability | 99.5% | Uptime monitoring (30 days) | MUST |
| **AC-NF04** | Weather data freshness | ≤ 6h "fresh", ≤ 24h "usable" | Automated check (cron) | MUST |
| **AC-NF05** | Concurrent users (pilot) | 100 | Load test | MUST |
| **AC-NF06** | Data retention (recommendations, actions) | 2 years | Policy audit | MUST |
| **AC-NF07** | Zero secrets in code/repo | 0 findings | Secret scan (GitLeaks/TruffleHog) | MUST |
| **AC-NF08** | Audit log coverage | 100% authz decisions, mutations | Log audit | MUST |
| **AC-NF09** | Frontend accessibility | WCAG 2.1 AA | Axe-core automated + manual | SHOULD |
| **AC-NF10** | Mobile responsive | Works 320px–1200px | BrowserStack / device lab | SHOULD |

---

## 2. Security Requirements

### 2.1 Authentication & Authorization

| ID | Requirement | Detail | Verification | Status |
|----|-------------|--------|--------------|--------|
| **SEC-01** | JWT Algorithm | RS256 (asymmetric); keys rotated quarterly | Key inspection | **Confirmed** — RS256 algorithm selected |
| **SEC-02** | Access Token TTL | 15 minutes (configurable via env) | Token decode + time check | Proposed |
| **SEC-03** | Refresh Token | 7 days; rotating (new refresh issued on use); revoked on logout/password change | Flow test | Proposed |
| **SEC-04** | Password Hashing | BCrypt cost 12; never logged | Code review + hash inspection | Standard |
| **SEC-05** | Password Policy | Min 8 chars, 1 upper, 1 lower, 1 digit, 1 special; no common passwords (zxcvbn) | Unit test | **Partially confirmed** — Min 8 chars confirmed; complexity rules proposed |
| **SEC-06** | Role-Based Access | `FARMER` (own resources), `ADMIN` (all); farm-scoped authorization | Integration test | Standard |
| **SEC-07** | Token Revocation | Refresh token blacklist (Redis) with TTL = refresh TTL | Integration test | Standard |
| **SEC-08** | Session Invalidation | On password change, logout, admin revoke | Integration test | Standard |

### 2.2 Input Validation & Injection Prevention

| ID | Requirement | Detail | Verification |
|----|-------------|--------|--------------|
| **SEC-09** | Bean Validation | All DTOs annotated; `@Valid` on controller methods | Code review |
| **SEC-10** | Custom Validators | Cross-field (dates, uniqueness), enum, coordinate bounds | Unit test |
| **SEC-11** | NoSQL Injection | Spring Data MongoDB repositories (parameterized); no string concatenation | Code review + SAST |
| **SEC-12** | XSS Prevention | Frontend: React auto-escapes; API: `Content-Type: application/json`; CSP header | Security scan |
| **SEC-13** | File Upload (future) | Not in MVP; if added: type/size validation, virus scan | N/A |

### 2.3 Rate Limiting & DoS Protection

| ID | Requirement | Detail | Verification |
|----|-------------|--------|--------------|
| **SEC-14** | Auth Endpoints | 10 requests/minute per IP (register/login) | Load test |
| **SEC-15** | Authenticated API | 100 requests/minute per user (token) | Load test |
| **SEC-16** | Recommendation API | 30 requests/minute per user | Load test |
| **SEC-17** | Burst Allowance | Token bucket: 2x rate for 10s burst | Load test |
| **SEC-18** | Response Headers | `X-RateLimit-Limit`, `X-RateLimit-Remaining`, `Retry-After` | API test |

### 2.4 Secrets & Configuration

| ID | Requirement | Detail | Verification |
|----|-------------|--------|--------------|
| **SEC-19** | No Secrets in Code | JWT secret, Weather API key, MongoDB URI, ML URL in env vars only | GitLeaks scan |
| **SEC-20** | Secret Rotation | Documented procedure; zero-downtime for JWT (overlap period) | Runbook review |
| **SEC-21** | TLS | TLS 1.2+ everywhere; mTLS for inter-service (prod) | Cert inspection |
| **SEC-22** | CORS | Restricted to frontend origin(s); no wildcard in prod | Header check |

### 2.5 Logging & Audit

| ID | Requirement | Detail | Verification |
|----|-------------|--------|--------------|
| **SEC-23** | Structured JSON Logs | `timestamp, level, service, traceId, spanId, userId, farmId, message` | Log sample |
| **SEC-24** | Correlation IDs | `X-B3-TraceId` propagated across FE→BE→ML→Weather | Distributed trace |
| **SEC-25** | No PII in Logs | Coordinates hashed (SHA-256 salted), tokens masked, passwords never | Log audit |
| **SEC-26** | Audit Events | Auth success/failure, farm create/update, recommendation generated, action recorded | Event log check |

### 2.6 Data Protection

| ID | Requirement | Detail | Verification |
|----|-------------|--------|--------------|
| **SEC-27** | Encryption at Rest | MongoDB encryption (Atlas/self-managed); disk encryption | Config review |
| **SEC-28** | Encryption in Transit | TLS 1.2+ all connections | Cert check |
| **SEC-29** | PII Minimization | Only email, phone, name, location; no Aadhaar, PAN, bank details | Data model review |
| **SEC-30** | Right to Deletion | `DELETE /api/v1/auth/me` anonymizes data (retains aggregated analytics) | Integration test |
| **SEC-31** | Data Retention Policy | Documented; automated TTL indexes for weather (90d), others (2y) | Policy doc + DB check |

---

## 3. Testing Strategy

### 3.1 Test Pyramid

```
                    ┌─────────────┐
                    │   E2E (5%)  │  Cypress / Playwright
                    │  Critical   │  Farmer journeys J1-J5
                    │  Paths      │  Failure scenarios
                   ┌┴─────────────┴┐
                  ┌┴───────────────┴┐
                 ┌┴── Integration ──┴┐  Spring Boot @SpringBootTest
                │  (20%)             │  Testcontainers (MongoDB)
                │  API Contracts     │  WireMock (Weather, ML)
                │  Repository Tests  │  ML Service Contract Tests
               ┌┴────────────────────┴┐
              ┌┴──── Unit (75%) ──────┴┐  JUnit 5 + Mockito
             │  Services, Validators,  │  Decision Engine Rules
             │  Feature Engineering,   │  Feature Engineering
             │  Utils, Mappers         │  DTO Validation
             └─────────────────────────┘
```

### 3.2 Test Categories

| Category | Tools | Scope | CI Gate |
|----------|-------|-------|---------|
| **Unit** | JUnit 5, Mockito, AssertJ | Services, validators, decision rules, feature engineering, mappers | Required (fail build) |
| **Integration** | Spring Boot Test, Testcontainers (MongoDB), WireMock | Repository tests, API controllers, Weather client, ML client, Auth flow | Required |
| **API Contract** | Pact / Spring Cloud Contract | Producer (Spring Boot) + Consumer (React, ML) contracts | Required |
| **E2E** | Cypress / Playwright | Farmer journeys J1-J5, failure scenarios, offline, i18n | Required (staging) |
| **Security** | OWASP ZAP, SAST (SonarQube/CodeQL), Dependency Check | Auth, rate limit, injection, headers, dependencies | Required |
| **Performance** | k6 / Gatling | AC-NF01, AC-NF02, AC-NF05 | Required (staging) |
| **Chaos** | Chaos Mesh / Litmus | AC-F01 to AC-F10 (weather down, ML down, DB down) | Required (staging) |

### 3.3 Test Data Management

| Environment | Data Strategy |
|-------------|---------------|
| **Unit** | In-memory builders/factories; no external deps |
| **Integration** | Testcontainers MongoDB (fresh per test class); WireMock for Weather/ML |
| **Staging** | Anonymized production-like dataset; seeded via migration scripts |
| **Production** | No test data; synthetic monitoring only |

### 3.4 ML Service Testing

| Test | Description |
|------|-------------|
| **Contract Test** | Spring Boot ↔ FastAPI request/response schema validation (Pact) |
| **Model Invariants** | Prediction in valid range [0, max_crop_need]; confidence in [0,1] |
| **Feature Parity** | Preprocessing in FastAPI matches training pipeline exactly |
| **Version Pinning** | Spring Boot requests specific `modelVersion`; rejects unknown |
| **Latency** | p99 < 500ms for inference (excl. cold start) |

---

## 4. Proposed Measurable Targets (PROPOSED — NEEDS APPROVAL)

> **⚠️ These are PROPOSED targets for discussion. Do not treat as committed requirements until approved.**

| Metric | Proposed Target | Rationale | Approval Needed |
|--------|-----------------|-----------|-----------------|
| Recommendation latency (p95) | ≤ 2000ms | Includes weather (500ms) + ML (800ms) + rules | Engineering |
| Weather data freshness (fresh) | ≤ 6 hours | IMD updates ~3h; OpenWeather ~1h | Product |
| Weather data freshness (usable) | ≤ 24 hours | Beyond this, reliability drops | Product |
| ML prediction confidence threshold | ≥ 0.6 for HIGH; ≥ 0.4 for MEDIUM | Based on baseline model calibration | ML Team |
| Water quantity accuracy (vs. expert) | ±15% for validated crops | FAO-56 based calc; expert review | Agronomy |
| Farmer task completion rate | ≥ 80% (onboard → first recommendation) | Usability benchmark | UX |
| Pilot recommendation adoption | ≥ 60% farmers follow at least once | Trust indicator | Field Team |
| False irrigation alert rate | ≤ 10% (recommend IRRIGATE but rain >10mm in 24h) | Forecast reliability | ML Team |

---

## 5. Definition of Done

### 5.1 Phase 1 (This Phase) — Done When:
- [ ] All 4 documents created: `REQUIREMENTS_AND_SCOPE.md`, `SYSTEM_ARCHITECTURE.md`, `MONGODB_SCHEMA_AND_API_SPEC.md`, `MVP_ACCEPTANCE_AND_SECURITY.md`
- [ ] All documents reviewed for internal consistency (cross-references valid, no contradictions)
- [ ] All documents reviewed against `AGENTS.md` (MongoDB only, no JPA, stack compliance)
- [ ] All documents reviewed against `JALRAKSHAK_Development_Plan.md` (Phase 1 deliverables covered)
- [ ] All documents reviewed against `JALRAKSHAK_Technology_Stack.md` (service boundaries, API contracts, collections)
- [ ] Database conflict resolved: MongoDB documented as approved; PostgreSQL noted as superseded
- [ ] All `PROPOSED — NEEDS APPROVAL` items explicitly marked and listed in "Unresolved Approvals"
- [ ] Documents committed to `docs/` folder; no application code

### 5.2 Phase 2 — Done When:
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

### 5.3 Phase 3 — Done When:
- [ ] Weather service implemented (provider abstraction, IMD + OpenWeather)
- [ ] Caching (Caffeine + MongoDB persistence) with TTL
- [ ] Freshness validation (configurable thresholds)
- [ ] Circuit breaker + retry + timeout
- [ ] Integration tests with WireMock (success, timeout, 5xx, stale data)
- [ ] All Phase 3 acceptance criteria (AC-07, AC-08, AC-F01, AC-F02) passing

### 5.4 Phase 4 — Done When:
- [ ] Dataset identified, documented, split (train/val/test)
- [ ] Baseline model trained (Scikit-learn/XGBoost) with documented metrics
- [ ] FastAPI service with `/health` and `/predict/irrigation-need`
- [ ] Preprocessing pipeline matches training exactly
- [ ] Model versioning, metadata stored in MongoDB
- [ ] Contract tests with Spring Boot (Pact)
- [ ] All Phase 4 acceptance criteria (AC-10, AC-F03, AC-F04, AC-F05) passing

### 5.5 Phase 5 — Done When:
- [ ] Feature engineering service (crop stage, water balance, soil deficit)
- [ ] Decision engine with rule set (safety → forecast → ML → partial → insufficient)
- [ ] Recommendation persistence with full input snapshot
- [ ] ML service integration with timeout/retry/circuit breaker
- [ ] Fallback to rule-based heuristic when ML unavailable
- [ ] End-to-end integration tests (Spring Boot + MongoDB + Mock ML + Mock Weather)
- [ ] All Phase 5 acceptance criteria (AC-09, AC-11, AC-12, AC-18) passing

### 5.6 Phase 6 — Done When:
- [ ] React + Vite project with TypeScript, React Router, Zustand, Axios
- [ ] Auth pages (register, login) with form validation, i18n (en, hi)
- [ ] Farm profile forms (multi-step, validation, map picker for location)
- [ ] Dashboard with recommendation card (action, quantity, reasons, warnings, freshness)
- [ ] History page (paginated recommendations + actions)
- [ ] Action recording flow (modal, validation, feedback)
- [ ] Offline caching (Service Worker / IndexedDB) for last recommendation
- [ ] Accessibility audit (WCAG 2.1 AA)
- [ ] All Phase 6 acceptance criteria (AC-13, AC-14, AC-15, AC-16, AC-17, AC-19, AC-20) passing

### 5.7 Phase 7 — Done When:
- [ ] Spring Security fully configured (JWT filter, method security, CORS, CSRF disabled for stateless)
- [ ] Password policy, rate limiting, audit logging implemented
- [ ] Security test report (ZAP, SAST, dependency check) — 0 critical/high
- [ ] Stale weather, missing data, sensor failure, ML outage tested (chaos)
- [ ] Pilot executed (50 farmers, 8 weeks, target crop/region)
- [ ] Pilot feedback report with limitations documented
- [ ] Model monitoring plan (drift detection, retraining triggers)
- [ ] Deployment runbook, environment config guide
- [ ] All Phase 7 acceptance criteria (all AC-F, AC-NF, SEC-*) passing

---

## 6. Traceability Matrix

| Requirement | Architecture | API Spec | Schema | Acceptance Criteria |
|-------------|--------------|----------|--------|---------------------|
| FR-01 (Register) | Auth Service | POST /auth/register | users | AC-01 |
| FR-02 (JWT Auth) | Security Config | POST /auth/login, /refresh | users | AC-02, AC-03, SEC-01–08 |
| FR-03 (Farm CRUD) | Farm Service | POST/GET/PUT/DELETE /farms | farms | AC-04, AC-05 |
| FR-04 (Crop Cycle) | Farm Service | POST/GET/PUT /crop-cycles | crop_cycles | AC-06 |
| FR-05 (Weather Fetch) | Weather Service | Internal | weather_observations | AC-07 |
| FR-06 (Freshness) | Weather Service | Internal | weather_observations | AC-08, AC-F01, AC-F02 |
| FR-07 (Features) | Feature Service | Internal | — | AC-09 |
| FR-08 (ML Client) | ML Client | POST /predict | — | AC-10, AC-F03, AC-F04, AC-F05 |
| FR-09 (Decision) | Decision Engine | Internal | — | AC-11, AC-18 |
| FR-10 (Persist Rec) | Rec Service | Internal | irrigation_recommendations | AC-12 |
| FR-11 (Dashboard) | Rec Controller | GET /recommendations/latest | irrigation_recommendations | AC-13, AC-15, AC-17 |
| FR-12 (Action) | Action Controller | POST /feedback | farmer_actions | AC-14, AC-15 |
| FR-13 (i18n) | Frontend | All user-facing strings | — | AC-19 |
| FR-14 (Offline) | Frontend | Service Worker | — | AC-20 |
| FR-15 (Alerts) | Frontend + Rec | Warning fields in response | irrigation_recommendations | AC-16 |

---

## 7. Unresolved Approvals Required Before Phase 2

> **✅ Items 1 and 2 (crop, region) are CONFIRMED. Item 4 (JWT algorithm RS256) confirmed. Item 5 (password min 8 chars) confirmed. Remaining details for items 4 and 5 still pending.**

| # | Decision | Document Section | Owner | Status |
|---|----------|------------------|-------|--------|
| 1 | Weather API provider & credentials | Architecture §7.1, Schema §2.4 | Engineering | **Unresolved** |
| 2 | JWT key management (RS256 key generation, rotation) | Security §2.1 | Security | **Partially resolved** — RS256 confirmed; key generation, storage, rotation procedures pending |
| 3 | Password policy exact rules | Security §2.1 | Security | **Partially resolved** — Min 8 chars confirmed; complexity rules (upper, lower, digit, special, no common passwords) proposed |
| 4 | Rate limiting tiers (free vs paid) | Security §2.3 | Product | **Unresolved** |
| 5 | Language support priority (English + Hindi + ?) | Requirements §10 | Product | **Unresolved** |
| 6 | Water quantity formula validation (FAO-56 + expert) | Requirements §6.3 | Agronomy | **Unresolved** |
| 7 | Pilot scope (farmers, duration, success metrics) | DoD Phase 7 | Product/Field | **Unresolved** |

---

## 8. Source of Truth Note

> **This document is governed by `AGENTS.md`** which mandates:
> - Fixed technology stack: React/Vite, Spring Boot, **MongoDB + Spring Data MongoDB**, Python/FastAPI, Spring Security/JWT
> - Roadmap is source of truth: Phase 1 deliverables only; no Phase 2 implementation
> - MongoDB is the **only approved database**; PostgreSQL recommendation in Development Plan §3 is **superseded** by AGENTS.md §2 and Technology Stack §8.1
> - No JPA/Hibernate for MongoDB integration
> - Test before claiming completion: report only tests that actually ran
> - Real integrations only; no mock data or invented metrics

All acceptance criteria, security requirements, and testing strategies herein are **proposed** and require stakeholder review before Phase 2 implementation begins.