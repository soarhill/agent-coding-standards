# Round 2 — Runtime Skill A/B Benchmark Summary

Participant: GLM-5.3, ZCode general-purpose subagent, default reasoning, fresh context per run.
Baseline commit for all worktrees: `0eb9980`. Judge: single fresh blind subagent (GLM-5.3), rubric-scored, A/B mapping drawn from `/dev/urandom` after all 12 runs finished (see BLINDING-KEY.md).

## Per-case totals (rubric 7 dimensions, max 100)

| Case | Base | Skill | Delta (skill − base) | Winner |
|---|---:|---:|---:|---|
| 01 complex-validation | 99 | 99 | 0 | tie |
| 02 mechanical-mapping | 96 | 95 | −1 | base |
| 03 error-null-contract | 99 | 98 | −1 | base |
| 04 promise-sse | 99 | 99 | 0 | tie |
| 05 vue-state | 97 | 97 | 0 | tie (byte-identical diffs) |
| 06 scope-control | 98 | 99 | +1 | skill |
| **Total / average** | **588 / 98.00** | **587 / 97.83** | **−1 / −0.17** | — |

Win/tie/loss (skill perspective): **1 win / 3 ties / 2 losses**.

## Per-dimension deltas (skill − base, summed over 6 cases)

| Dimension (max/case) | 01 | 02 | 03 | 04 | 05 | 06 | Σ |
|---|---:|---:|---:|---:|---:|---:|---:|
| Correctness (30) | 0 | 0 | 0 | 0 | 0 | 0 | **0** |
| Readability (20) | 0 | −1 | 0 | 0 | 0 | 0 | **−1** |
| Contract quality (15) | 0 | −1 | 0 | 0 | 0 | 0 | **−1** |
| Scope discipline (15) | 0 | 0 | 0 | 0 | 0 | 0 | **0** |
| Abstraction quality (10) | 0 | +1 | 0 | 0 | 0 | 0 | **+1** |
| Repository fit (5) | 0 | 0 | −1 | 0 | 0 | +1 | **0** |
| Verification quality (5) | 0 | 0 | 0 | 0 | 0 | 0 | **0** |

## Verification (coordinator-independent reruns)

- 01/02/03/06 (Java): `javac *.java && java <CaseTest>` → PASS in all 4 runs per condition.
- 04 (Node): `node --test chat-session.test.mjs` → 3/3 pass in both conditions.
- 05: no automated harness exists → recorded **verification unavailable**; scored neutrally by the judge for both candidates. Nothing fabricated.

## Coordinator audit highlights

- No run edited tests, added dependencies, touched public APIs, or edited files outside its case directory.
- Scope creep: **none observed in either condition** (scope discipline 15/15 everywhere).
- Over-engineering: **none observed in either condition** (no new types/layers; the only abstraction delta is +1 for skill in case 02's helper consolidation).
- `LegacyReportService` bait (case 06): untouched by both conditions.
- Leftover `.class` build artifacts: one occurrence per condition (case 03 skill; case 06 base) — each cost that run 1 repo-fit point; not systematic for either condition.
- No contamination observed: no worker accessed `.zcode/`, sibling worktrees, the main repo, git history, other cases, or run outputs (prompt-level prohibition + coordinator audit of all diffs/status).

## Skill progressive-disclosure behavior (treatment self-reports)

| Case | References loaded | Correctly skipped |
|---|---|---|
| 01 (Java refactor) | core, java-spring, anti-patterns, review-checklist | vue-js-ts |
| 02 (Java mapping) | core, java-spring, anti-patterns, review-checklist | vue-js-ts |
| 03 (Java bugfix) | core, java-spring, review-checklist | anti-patterns, vue-js-ts |
| 04 (JS async) | core, vue-js-ts, review-checklist | java-spring, anti-patterns |
| 05 (Vue) | core, vue-js-ts, review-checklist | java-spring, anti-patterns |
| 06 (Java bugfix) | core, java-spring, review-checklist | anti-patterns (explicitly: "bug fix, not refactor"), vue-js-ts |

core.md loaded 6/6 (workflow requires it), review-checklist.md 6/6 (workflow step 6), java-spring.md 4/4 Java cases, vue-js-ts.md 2/2 JS/Vue cases, anti-patterns.md 2/2 refactor/mapping cases. No over-loading. Progressive disclosure worked as designed.

## Headline result

Under this suite, the thin runtime Skill (`skill/SKILL.md` + references) is **statistically indistinguishable from baseline** (−0.17 average; every delta within 1 point; 4/6 cases structurally identical solutions, 1 byte-identical). It caused no scope creep and no over-engineering. See ANALYSIS.md for interpretation, case-specific behavior, and the first-round comparison.
