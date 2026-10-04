# Changelog

All notable project changes are recorded here.

## [0.1.0] - 2026-10-04

First productized runtime release.

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

The final `dist/` package passed a read-only smoke test with:

- no blocking issues;
- all reference paths present;
- no dependency on files outside the package;
- expected progressive-disclosure behavior for Java/Spring and Vue/JS tasks;
- no runtime research/benchmark/history material.

Non-blocking observations were intentionally not used to reopen the Skill before release.

### Known limitations

- Current benchmark suite contains six small/medium cases.
- Each Runtime Skill condition has one sample per case.
- Frontend Case 05 has no full runtime harness.
- JS/TS routing is intentionally broad and may be refined later if real Node.js usage exposes ambiguity.
- No project license has been selected yet.
