# JALRAKSHAK — AGENTS.md

This file contains the binding operating rules for all agents working on the JALRAKSHAK repository.

## 1. Repository and scope

- GitHub repository: https://github.com/janmejayy-gif/CU-PROJECT.git
- Default branch: main
- Project root: the currently opened repository.
- Product scope: JALRAKSHAK smart irrigation only.
- AGRIFAIR marketplace features are out of scope unless explicitly approved.

## 2. Mandatory technology stack

- Frontend: React.js + JavaScript + Vite
- Main backend: Java + Spring Boot + REST APIs
- Database: MongoDB + Spring Data MongoDB
- ML service: Python + FastAPI
- ML libraries: Pandas, NumPy, Scikit-learn and XGBoost when justified
- Security: Spring Security + JWT
- Testing: appropriate automated unit, integration, API and contract tests

Do not replace these technologies or introduce another database without explicit user approval. Do not use JPA/Hibernate for MongoDB integration.

## 3. Seven non-negotiable rules

1. **Fixed technology stack**: never replace approved technologies without approval.
2. **Roadmap is the source of truth**: implement only the current approved phase and its deliverables.
3. **No unauthorized changes**: do not unnecessarily modify existing files, APIs, database structures, routes, configuration or working modules.
4. **Test before claiming completion**: report only tests and checks that actually ran, including failures.
5. **Small, controlled changes**: one clearly defined objective per task; no unrelated rewrites or scope creep.
6. **Real integrations only**: never represent mock data, fake predictions, dummy login or fabricated weather/sensor readings as real functionality.
7. **Approval before major decisions**: request approval before architecture changes, new dependencies with significant impact, database migration, API contract changes, destructive commands or scope expansion.

## 4. Development protocol

### Before every phase:
- Read AGENTS.md, JALRAKSHAK_Technology_Stack.md and JALRAKSHAK_Development_Plan.md.
- Inspect git status, current branch, recent commits, repository tree and relevant existing code.
- Confirm the current phase and its acceptance criteria.
- Report any conflict, missing document, uncommitted change or blocking dependency before proceeding.

### During implementation:
- Work only on the approved phase.
- Keep API contracts explicit and validate request/response payloads.
- Keep secrets in environment variables; never commit credentials or production data.
- Handle unavailable services, invalid inputs and stale data safely.
- Never invent model training data, accuracy metrics or water-saving results.
- Do not expose the Python ML service directly to the public frontend.

### Before a commit:
- Inspect the complete diff.
- Run applicable build, tests, formatting and API checks.
- Review for secrets, unrelated changes and accidental deletions.
- Never suppress a failure merely to obtain a passing build.

## 5. GitHub protocol

- Remote must be verified as https://github.com/janmejayy-gif/CU-PROJECT.git.
- Never force-push, rewrite shared history, delete branches or run destructive Git commands without explicit approval.
- Never discard uncommitted user changes.
- Commit only phase-related, reviewed changes.
- Push only after the relevant checks and commit review.
- If authentication, network access, tests or push fail, report the exact blocker. Never claim a push succeeded unless Git confirms it.
- Do not commit secrets, .env files, virtual environments, node_modules, build outputs or generated datasets unnecessarily.
- Preserve a clean, understandable Git history.

## 6. Phase completion report

At the end of each phase, report:
1. Scope completed
2. Files created or changed
3. Commands and tests actually run
4. Pass/fail/not-run status
5. Commit hash and GitHub push status
6. Known limitations and blockers
7. Proposed next phase — do not start it automatically

Stop at phase boundaries and wait for user approval before beginning the next phase.

A feature is complete only when its defined acceptance criteria are met. Documentation or a successful commit alone does not prove the application works.