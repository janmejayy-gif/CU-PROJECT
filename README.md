# JALRAKSHAK

AI-assisted Smart Irrigation Decision Support System for farmers.

## Purpose

JALRAKSHAK helps farmers make informed irrigation decisions by combining farm-specific data (crop, soil, location, area, irrigation method) with weather forecasts and ML-based irrigation need predictions. The system provides actionable recommendations — `IRRIGATE`, `WAIT`, `PARTIAL_IRRIGATION`, or `INSUFFICIENT_DATA` — with explanations, estimated water quantities (when supported), and data freshness indicators.

**Final irrigation decisions always remain with the farmer.**

## Approved Technology Stack

| Layer | Technology |
|-------|------------|
| Frontend | React.js + JavaScript + Vite |
| Main Backend | Java + Spring Boot + REST APIs |
| Database | MongoDB + Spring Data MongoDB |
| ML Service | Python + FastAPI |
| ML Libraries | Pandas, NumPy, Scikit-learn, XGBoost (when justified) |
| Security | Spring Security + JWT |
| Weather Integration | External weather/forecast API |
| Optional Hardware | Soil-moisture IoT sensor |

## Planned Services

1. **React Frontend** — Farmer dashboard, onboarding, farm profiles, recommendations, history, alerts
2. **Spring Boot Backend** — REST APIs, business logic, validation, authentication, data persistence, weather integration, ML service orchestration, decision engine
3. **MongoDB** — Farmer profiles, farms, crop cycles, weather observations, soil readings, recommendations, farmer actions
4. **Python FastAPI ML Service** — Irrigation need prediction model inference endpoint

## Prerequisites

- Node.js 18+ (for frontend)
- Java 17+ (for Spring Boot)
- Python 3.10+ (for ML service)
- MongoDB 6+ (local or hosted)
- Weather API credentials (to be configured)

## Authoritative Documents

- [JALRAKSHAK_Technology_Stack.md](JALRAKSHAK_Technology_Stack.md) — Detailed stack, architecture, API boundaries, development order
- [JALRAKSHAK_Development_Plan.md](JALRAKSHAK_Development_Plan.md) — Phases, deliverables, data entities, MVP checklist, technical issues
- [AGENTS.md](AGENTS.md) — Binding operating rules for all contributors

## Development Phases

1. **Phase 1** — Requirements & Architecture (completed)
2. **Phase 2** — Spring Boot Backend & Database
3. **Phase 3** — Weather & Input Data Pipeline
4. **Phase 4** — Python ML Service
5. **Phase 5** — Irrigation Decision Engine & Service Integration
6. **Phase 6** — React Farmer Dashboard
7. **Phase 7** — Security, Validation & Pilot

## Status

Repository initialized. No application code implemented yet.

## License

Proprietary — part of KRISHISETU platform.