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
- Kept the runtime focus on scope discipline, truthful contracts, lifecycle correctness, reviewable mappings, useful comments, local consistency, and proportional abstraction.

### Validation summary

Benchmark rounds did not demonstrate a stable score improvement, but they also did not show systematic scope creep or over-engineering.

The productized package previously passed a read-only smoke test with no blocking issues. The comment discipline added before release is a small, evidence-backed restoration of guidance already present in the research standard rather than a new architectural or framework rule.

### Known limitations

- Current benchmark suite contains six small/medium cases.
- Each Runtime Skill condition has one sample per case.
- Frontend Case 05 has no full runtime harness.
- JS/TS routing is intentionally broad and may be refined later if real Node.js usage exposes ambiguity.
- No project license has been selected yet.
