# Changelog

All notable project changes are recorded here.

## [0.1.0] - 2026-10-05

First productized runtime release.

### Added

- Installable package at `dist/engineering-coding-standards/`.
- Thin `SKILL.md` plus four runtime references:
  - core;
  - Java/Spring;
  - Vue/JavaScript/TypeScript;
  - final review.
- Comment/documentation discipline covering useful implementation comments, repository-local comment language, stale comments, and Java API contract documentation.
- Cognitive-load review rules for dead/no-op distinctions, opaque boolean call sites, hidden mutation, data-structure semantics, and multi-phase methods.
- Research-oriented candidate rules v1.1.
- Six-case coding benchmark suite.
- Cooperative sub-Agent/worktree isolation runbook.
- Full-standard calibration artifacts.
- GLM-5.3 Runtime Skill A/B artifacts.
- Codex Runtime Skill acceptance artifacts.

### Productization

- Separated the installable Skill from the research/benchmark repository.
- Kept `skill/` unchanged as benchmark provenance.
- Removed runtime-only duplication between anti-pattern and checklist references.
- Removed research history and generic handbook material from the installable package.
- Kept the runtime focus on scope discipline, truthful contracts, lifecycle correctness, reviewable mappings, useful comments, low cognitive load, local consistency, and proportional abstraction.

### Real-project calibration

A SearchService produced with the Skill was correct and reasonably structured, but exposed quality gaps that the earlier benchmark cases did not stress:

- a boolean mode parameter whose branches were behaviorally identical;
- call sites with several raw booleans whose meaning required jumping to the callee;
- a helper that silently mutated a caller-owned SQL parameter object later reused by another query;
- a `Map<K, V>` used where only key membership mattered;
- a large method requiring the reader to track several independent loading/matching/aggregation phases.

The Skill now treats these as readability/cognitive-load review signals. The update does **not** introduce fixed method-length thresholds or require new option/configuration types.

### Validation summary

Benchmark rounds did not demonstrate a stable score improvement, but they also did not show systematic scope creep or over-engineering.

The productized package previously passed a read-only smoke test with no blocking issues. This update is based on an observed real-project output rather than an isolated benchmark score.

### Known limitations

- Current benchmark suite contains six small/medium cases and does not yet contain a reduced regression case for this real-project example.
- Each Runtime Skill condition has one sample per case.
- Frontend Case 05 has no full runtime harness.
- JS/TS routing is intentionally broad and may be refined later if real Node.js usage exposes ambiguity.
- No project license has been selected yet.
