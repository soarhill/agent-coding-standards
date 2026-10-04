# Changelog

All notable project changes are recorded here.

## [0.1.0] - Unreleased

First productized runtime release candidate.

### Added

- Installable package at `dist/engineering-coding-standards/`.
- Thin `SKILL.md` plus four runtime references:
  - core;
  - Java/Spring;
  - Vue/JavaScript/TypeScript;
  - final review.
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
- Kept the runtime focus on scope discipline, truthful contracts, lifecycle correctness, reviewable mappings, local consistency, and proportional abstraction.

### Validation summary

Benchmark rounds did not demonstrate a stable score improvement, but they also did not show systematic scope creep or over-engineering.

The new `dist/` package is a lean derivative of the benchmarked candidate and requires a final smoke test before tagging v0.1.0.

### Known limitations

- Current benchmark suite contains six small/medium cases.
- Each Runtime Skill condition has one sample per case.
- Frontend Case 05 has no full runtime harness.
- No project license has been selected yet.
