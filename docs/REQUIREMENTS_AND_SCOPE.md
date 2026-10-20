# JALRAKSHAK — Requirements and Scope Document

**Version:** 1.0 (Phase 1 Deliverable)  
**Status:** PROPOSED — NEEDS STAKEHOLDER APPROVAL  
**Governed by:** AGENTS.md (technology stack, phase order, non-negotiable rules)  
**Roadmap:** JALRAKSHAK_Development_Plan.md  

---

## 1. Project Problem Statement

Farmers in India and similar agrarian economies lack field-specific, data-driven irrigation guidance. Current practices rely on:
- Fixed calendar schedules (ignoring actual weather, soil, crop stage)
- Generic advisories (not tailored to farm location, soil type, irrigation method)
- Subjective judgment (experience-based, not quantifiable)

This leads to **over-irrigation** (water waste, energy cost, nutrient leaching, soil salinity) or **under-irrigation** (yield loss, crop stress). JALRAKSHAK addresses this by providing an **AI-assisted Smart Irrigation Decision Support System** that combines farm-specific data, weather forecasts, and ML-based irrigation-need prediction to generate explainable, actionable recommendations.

---

## 2. Goals and Non-Goals

### Goals (MVP Scope)

| ID | Goal | Success Indicator |
|----|------|-------------------|
| G1 | Farmer can register, authenticate, and create a farm profile with crop, soil, location, area, irrigation method | Profile saved, validated, retrievable via API |
| G2 | System fetches current weather and forecast for farm location | Weather data timestamped, freshness validated, stored |
| G3 | System estimates irrigation need via ML service (FastAPI) | Prediction returned with model version, confidence/metadata |
| G4 | Decision engine combines ML output + agronomic rules + forecast → recommendation | Recommendation includes action, reasons, data freshness, model version |
| G5 | Farmer sees recommendation on dashboard with explanation | UI shows action, quantity (if supported), reasons, warnings |
| G6 | Farmer can record action taken and optional feedback | Action stored with timestamp, linked to recommendation |
| G7 | All APIs secured with JWT authentication | Unauthorized requests rejected; tokens expire per policy |

### Non-Goals (Explicitly Out of MVP Scope)

| ID | Non-Goal | Rationale / Future Phase |
|----|----------|--------------------------|
| NG1 | Automatic irrigation hardware control | Safety-critical; requires separate authorized control system (Phase 7+) |
| NG2 | Multi-crop, multi-region full coverage | Start narrow per Dev Plan §6.1; expand after pilot |
| NG3 | Satellite/remote-sensing derived features | Optional enhancement (Dev Plan §204) |
| NG4 | Soil-moisture IoT sensor integration (mandatory) | Optional for MVP (Dev Plan §203, §208); contract defined, implementation later |
| NG5 | Marketplace / AGRIFAIR integration | Out of scope per AGENTS.md §1 |
| NG6 | Regional language voice assistant | Future scope (Dev Plan §205) |
| NG7 | Crop disease/pest risk module | Future scope (Dev Plan §206) |
| NG8 | Proven water-saving / yield improvement claims | Require field validation (Dev Plan §222); no invented metrics |

---

## 3. Target Users and Farmer Journeys

### Primary User: Small-to-Medium Holder Farmer
- Owns/manages 0.5–20 hectares
- Basic smartphone literacy (can use app with guided flows)
- Language: English default; regional language support planned (Hindi, Marathi, others TBD)
- Connectivity: Intermittent 3G/4G; UI must handle offline/low-bandwidth gracefully

### Core User Journeys (MVP)

| Journey ID | Journey Name | Steps | Primary Actor |
|------------|--------------|-------|---------------|
| J1 | **Onboard & Create Farm Profile** | Register → Login → Choose language → Enter farm details (location, area, soil, irrigation method) → Add crop cycle (crop, variety, planting date) → Save | Farmer |
| J2 | **View Current Recommendation** | Open dashboard → See latest recommendation (action, quantity, reasons, data freshness) → Expand for details | Farmer |
| J3 | **Record Irrigation Action** | After viewing recommendation → Tap "Record Action" → Select action taken (IRRIGATED / WAITED / PARTIAL) → Optional: add notes/feedback → Submit | Farmer |
| J4 | **View History & Alerts** | Navigate to History → See past recommendations, actions, outcomes → See active alerts (stale data, sensor failure, weather warnings) | Farmer |
| J5 | **Handle Insufficient Data** | Open dashboard → System shows `INSUFFICIENT_DATA` with specific missing items (e.g., "Weather data >24h old", "No crop cycle active") → Farmer sees guidance on what to provide | Farmer |

---

## 4. Crop and Geographic Scope — PROPOSED DEFAULTS

> **⚠️ UNRESOLVED — REQUIRES STAKEHOLDER APPROVAL**

| Dimension | Proposed Default | Rationale | Decision Needed |
|-----------|------------------|-----------|-----------------|
| **Primary Crop (MVP)** | **Rice (Kharif season)** or **Wheat (Rabi season)** | High acreage, well-studied water requirements, representative datasets available | Confirm crop; single crop for MVP |
| **Primary Region (MVP)** | **Maharashtra (Vidarbha/Marathwada)** or **Punjab (Rice-Wheat belt)** | Diverse agro-climatic zones, existing weather station coverage, farmer outreach possible | Confirm state/district |
| **Supported Crops (Post-MVP)** | Cotton, Sugarcane, Maize, Soybean, Pulses | Prioritize by acreage and water sensitivity | Phase 6+ |
| **Supported Regions (Post-MVP)** | Expand to adjacent states with similar agro-climatic zones | Validate model transferability first | Phase 7+ |

**Decision Required:** Stakeholder to confirm **single crop + single region** for MVP pilot. Document choice here once approved.

---

## 5. Required and Optional Farmer Inputs

### 5.1 Farm Profile Inputs (Required for Recommendation)

| Field | Type | Unit | Validation | Required | Notes |
|-------|------|------|------------|----------|-------|
| `farmId` | UUID | — | Auto-generated | Yes (system) | Primary key |
| `userId` | UUID | — | FK to User | Yes (system) | Owner reference |
| `name` | String | — | 1–100 chars | Yes | Farmer-defined |
| `location` | GeoJSON Point | [lon, lat] (WGS84) | Valid coordinates | Yes | District/block auto-resolved |
| `area` | Number | hectares | >0, ≤1000 | Yes | Single contiguous plot |
| `soilType` | Enum | — | LOAM, CLAY, SANDY_LOAM, SILT, CLAY_LOAM, BLACK_COTTON, RED_LATERITIC, ALLUVIAL, OTHER | Yes | Standard Indian soil classification |
| `irrigationMethod` | Enum | — | FLOOD, SPRINKLER, DRIP, RAINFED, OTHER | Yes | Affects application efficiency |
| `waterSource` | Enum | — | CANAL, BOREWELL, TANK, RIVER, MUNICIPAL, MIXED | Yes | For water-quantity calc |
| `createdAt` | ISO8601 | — | Auto | System | |
| `updatedAt` | ISO8601 | — | Auto | System | |

### 5.2 Crop Cycle Inputs (Required for Recommendation)

| Field | Type | Unit | Validation | Required | Notes |
|-------|------|------|------------|----------|-------|
| `cropCycleId` | UUID | — | Auto-generated | Yes (system) | |
| `farmId` | UUID | — | FK to Farm | Yes (system) | |
| `crop` | Enum | — | RICE, WHEAT, COTTON, MAIZE, SUGARCANE, SOYBEAN, PIGEON_PEA, CHICKPEA, OTHER | Yes | Single crop per cycle |
| `variety` | String | — | 1–50 chars | No | e.g., "Swarna", "PBW 343" |
| `plantingDate` | ISO8601 Date | — | Not future; ≤ today | Yes | Determines growth stage |
| `expectedHarvestDate` | ISO8601 Date | — | > plantingDate | No | Optional; for planning |
| `growthStage` | Enum | — | SEEDLING, TILLERING, PANICLE_INITIATION, FLOWERING, GRAIN_FILLING, MATURITY, HARVESTED | Derived | Auto-calculated from plantingDate + crop calendar |
| `status` | Enum | — | ACTIVE, COMPLETED, ABANDONED | System | Default ACTIVE |

### 5.3 Optional Inputs (Enhance Recommendation Quality)

| Field | Source | Type | Unit | Notes |
|-------|--------|------|------|-------|
| `soilMoistureReading` | IoT Sensor / Manual | Number | % volumetric water content (0–100) | Timestamped; quality flag |
| `soilMoistureDepth` | IoT Sensor / Manual | cm | 15, 30, 60, 90 | Depth of reading |
| `previousIrrigationDate` | Farmer record | ISO8601 | — | For water-balance calc |
| `previousIrrigationVolume` | Farmer record | mm | ≥0 | Depth equivalent |
| `fertilizerApplied` | Farmer record | Boolean + Date | — | Affects crop water demand |
| `yieldTarget` | Farmer input | t/ha | >0 | For advanced planning |

---

## 6. Recommendation Outputs

### 6.1 Action Enum (Deterministic Output)

| Value | Meaning | Display Label (EN) | Display Label (HI) |
|-------|---------|-------------------|-------------------|
| `IRRIGATE` | Full irrigation recommended | "Irrigate Now" | "अभी सिंचाई करें" |
| `WAIT` | Do not irrigate; conditions adequate | "Wait" | "प्रतीक्षा करें" |
| `PARTIAL_IRRIGATION` | Reduced irrigation recommended | "Partial Irrigation" | "आंशिक सिंचाई" |
| `INSUFFICIENT_DATA` | Cannot confidently recommend | "Data Insufficient" | "डेटा अपर्याप्त" |

### 6.2 Recommendation Response Structure

```json
{
  "recommendationId": "uuid",
  "farmId": "uuid",
  "cropCycleId": "uuid",
  "action": "IRRIGATE | WAIT | PARTIAL_IRRIGATION | INSUFFICIENT_DATA",
  "estimatedWaterQuantityMm": 25.5,
  "estimatedWaterVolumeLitres": 255000,
  "confidence": "HIGH | MEDIUM | LOW",
  "reasons": [
    "Soil moisture at 30cm depth is 18% (threshold: 22% for current stage)",
    "No significant rainfall forecast in next 72 hours",
    "Crop at PANICLE_INITIATION stage — critical water-sensitive period"
  ],
  "warnings": [
    "Weather data is 18 hours old",
    "No soil moisture sensor reading available"
  ],
  "dataFreshness": {
    "weatherDataAgeHours": 18,
    "soilMoistureAgeHours": null,
    "forecastHorizonHours": 72
  },
  "modelVersion": "v1.0.0-baseline",
  "modelPrediction": {
    "irrigationNeedMm": 28.3,
    "predictionConfidence": 0.72
  },
  "decisionRulesApplied": [
    "RULE-WB-01: Water balance deficit > 20mm → IRRIGATE",
    "RULE-FC-03: Forecast rainfall < 5mm in 72h → no deferral"
  ],
  "generatedAt": "2026-10-21T06:30:00Z",
  "validUntil": "2026-10-21T18:30:00Z"
}
```

### 6.3 Water Quantity — Display Rules

| Condition | Show Quantity? | Basis |
|-----------|----------------|-------|
| `action == IRRIGATE || PARTIAL_IRRIGATION` AND validated agronomic calc exists | **Yes** | Crop coefficient × ET₀ × area × efficiency factor |
| `action == WAIT` | No | N/A |
| `action == INSUFFICIENT_DATA` | No | N/A |
| Quantity calc not validated for crop/region | No | Show "Quantity estimate unavailable — consult local expert" |

---

## 7. Fallback Behavior for Missing/Stale/Invalid Data

| Scenario | Detection | System Response | UI Display |
|----------|-----------|----------------|------------|
| Weather API timeout (>5s) | Circuit breaker / timeout | Return cached weather if <6h old; else `INSUFFICIENT_DATA` | "Weather service unavailable — using cached data from 4h ago" / "Weather data unavailable" |
| Weather data >24h old | Timestamp check | `INSUFFICIENT_DATA` with warning | "Weather data is 26h old — recommendation may be inaccurate" |
| Forecast unavailable | Empty forecast array | Proceed with current observations only; reduce confidence | "Forecast unavailable — recommendation based on current conditions only" |
| ML service timeout/unavailable | Circuit breaker / 5xx | Fallback to rule-based heuristic (documented); mark confidence LOW | "Prediction service unavailable — using simplified rules" |
| ML prediction outside trained range | Feature range check | `INSUFFICIENT_DATA` | "Conditions outside model training range — cannot recommend" |
| No active crop cycle | Query returns none | `INSUFFICIENT_DATA` | "No active crop cycle — add crop details to get recommendation" |
| Soil moisture reading >48h old | Timestamp check | Exclude from decision; add warning | "Soil moisture reading is 2 days old — not used" |
| Invalid farm input (e.g., area=0) | Validation layer | 400 Bad Request with field errors | Form validation errors |

**Principle:** Never invent data. Always show what is missing, how old data is, and why confidence is reduced.

---

## 8. Functional Requirements

| ID | Requirement | Priority | Traceability |
|----|-------------|----------|--------------|
| FR-01 | User registration with email/phone + password | MUST | J1 |
| FR-02 | JWT-based authentication (access + refresh tokens) | MUST | J1, Security |
| FR-03 | Farm profile CRUD (create, read, update, delete own) | MUST | J1 |
| FR-04 | Crop cycle CRUD (one active per farm at a time) | MUST | J1 |
| FR-05 | Weather data fetch for farm location (current + 72h forecast) | MUST | J2 |
| FR-06 | Weather data freshness validation (max age configurable, default 24h) | MUST | J2, Fallback |
| FR-07 | Feature engineering: crop stage, water balance, soil moisture deficit | MUST | G3, G4 |
| FR-08 | ML service call with validated features, timeout (3s), retry (1x) | MUST | G3 |
| FR-09 | Decision engine: combine ML + rules + forecast → action + quantity | MUST | G4 |
| FR-10 | Recommendation persistence with full input snapshot | MUST | G4, J3 |
| FR-11 | Dashboard API: latest recommendation + history (paginated) | MUST | J2, J4 |
| FR-12 | Farmer action recording (action taken, timestamp, notes) | MUST | J3 |
| FR-13 | Multi-language support (i18n keys; English default) | SHOULD | J1, J2 |
| FR-14 | Offline-capable UI (cache last recommendation, sync on reconnect) | SHOULD | J2, J4 |
| FR-15 | Alert/notification for stale data, sensor failure, extreme weather | SHOULD | J4 |

---

## 9. Non-Functional Requirements

| ID | Requirement | Target | Notes |
|----|-------------|--------|-------|
| NFR-01 | API response time (p95) | ≤ 800ms | Excluding external weather/ML calls |
| NFR-02 | Recommendation generation (p95) | ≤ 2000ms | Including weather + ML calls |
| NFR-03 | Availability (backend) | 99.5% | Excluding planned maintenance |
| NFR-04 | Data freshness (weather) | ≤ 6h for "fresh"; ≤ 24h for "usable" | Configurable |
| NFR-05 | Concurrent users (MVP pilot) | 100 | Scale later |
| NFR-06 | Data retention (recommendations, actions) | 2 years | GDPR/PDPB compliant |
| NFR-07 | Secrets management | Environment variables / vault | No secrets in code/repo |
| NFR-08 | Audit logging | All authz decisions, data mutations | Structured JSON logs |
| NFR-09 | Accessibility (frontend) | WCAG 2.1 AA | Farmer-friendly |
| NFR-10 | Mobile-first responsive UI | <320px to >1200px | Touch-friendly |

---

## 10. Assumptions, Dependencies, Risks, Open Questions

### Assumptions
| ID | Assumption | Impact if False |
|----|------------|-----------------|
| A1 | Weather API (e.g., IMD, OpenWeather, WeatherAPI) provides reliable current + 72h forecast for target region | Fallback to rule-only; reduced accuracy |
| A2 | Representative labeled dataset exists for chosen crop/region for ML training | Use rule-based prototype (Dev Plan §154) |
| A3 | Farmers have smartphones with internet access (at least intermittent) | Offline-first UI critical |
| A4 | Spring Boot + MongoDB + FastAPI can be deployed on available infrastructure (cloud/VM) | Deployment architecture may need adjustment |
| A5 | Agronomic rules for water quantity are documented and validated by domain expert | Quantity estimates hidden until validated |

### External Dependencies
| Dependency | Purpose | Fallback |
|------------|---------|----------|
| Weather/Forecast API | Current conditions + 72h forecast | Cached data + rule-based |
| MongoDB (hosted or self-managed) | Primary data store | N/A — critical |
| Python ML service (FastAPI) | Irrigation need prediction | Rule-based heuristic |
| SMS/Email gateway (optional) | Alerts/notifications | In-app only |

### Risks
| Risk | Likelihood | Impact | Mitigation |
|------|------------|--------|------------|
| No suitable labeled dataset for ML | Medium | High | Start with rule-based prototype; collect data during pilot |
| Weather API unreliable in target region | Medium | High | Multi-provider strategy; aggressive caching |
| Agronomic rules not validated by expert | High | High | Engage expert before Phase 5; hide quantity until validated |
| Farmer adoption low due to usability | Medium | High | User testing in Phase 6; simple language, guided flows |
| Data privacy / PDPB compliance | Low | High | Minimize PII; encryption at rest; consent flow |

### Open Stakeholder Questions (Require Approval)

| # | Question | Proposed Default | Decision Owner |
|---|----------|------------------|----------------|
| Q1 | **Primary crop for MVP?** | Rice (Kharif) | Product / Agronomy |
| Q2 | **Primary region for MVP?** | Maharashtra (Vidarbha) | Product / Field Team |
| Q3 | **Weather API provider?** | IMD (free) + OpenWeather (backup) | Engineering |
| Q4 | **Water quantity formula validation?** | FAO-56 crop coefficient × ET₀ × area / efficiency | Agronomy Expert |
| Q5 | **JWT access token TTL?** | 15 min (access) / 7 days (refresh) | Security |
| Q6 | **Password policy?** | Min 8 chars, 1 upper, 1 lower, 1 digit, 1 special | Security |
| Q7 | **Rate limiting policy?** | 100 req/min per user (auth), 30 req/min (recommendation) | Engineering |
| Q8 | **Language support priority?** | English + Hindi | Product |
| Q9 | **Pilot farmer count / duration?** | 50 farmers / 8 weeks | Product / Field |

---

## 11. Source of Truth Note

> **This document is governed by `AGENTS.md`** which mandates:
> - Fixed technology stack: React/Vite, Spring Boot, **MongoDB + Spring Data MongoDB**, Python/FastAPI, Spring Security/JWT
> - Roadmap is source of truth: Phase 1 deliverables only; no Phase 2 implementation
> - MongoDB is the **only approved database**; PostgreSQL recommendation in Development Plan §3 is **superseded** by AGENTS.md §2 and Technology Stack §8.1
> - No JPA/Hibernate for MongoDB integration
> - Real integrations only; no mock data or invented metrics

All requirements herein are **proposed** and require stakeholder review before Phase 2 implementation begins.