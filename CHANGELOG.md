# Changelog

All notable project changes are recorded here.

## [0.1.0] - 2026-10-03

First frozen runtime release.

### Added

- Runtime Skill entry point: `skill/SKILL.md`.
- Progressive-disclosure references for:
  - core engineering rules;
  - Java/Spring;
  - Vue/JavaScript/TypeScript;
  - anti-patterns and abstraction review;
  - final diff review.
- Research-oriented candidate rules v1.1.
- Six-case coding benchmark suite.
- Cooperative sub-Agent/worktree isolation runbook.
- Full-standard calibration artifacts.
- GLM-5.3 Runtime Skill A/B artifacts.
- Codex Runtime Skill acceptance artifacts.

### Calibrated

- Split rule strength into MUST / SHOULD / REVIEW TRIGGER / CONVENTION.
- Kept task-scope authorization separate from code-quality review triggers.
- Rejected fixed line-count/nesting/duplication thresholds as automatic refactor rules.
- Kept mapping semantics explicit and resisted generic mapper/framework construction.
- Clarified null / absence / failure semantics without mandating Optional everywhere.
- Clarified async cancellation/abort as a distinct lifecycle outcome.

### Validation summary

Runtime Skill A/B results:

- GLM-5.3: baseline 588 / 600, Skill 587 / 600.
- Codex participant: baseline 596 / 600, Skill 591 / 600.

No systematic scope creep or over-engineering was observed in either Runtime Skill round.

The release does not claim statistically proven score improvement; both rounds have small sample sizes and near-ceiling baselines.

### Known limitations

- Current benchmark suite contains only six small/medium coding cases.
- Each Runtime Skill condition has one sample per case.
- Frontend Case 05 has no full runtime harness.
- Codex Case 04 exposed a conditional SSE callback-error policy difference; current evidence does not justify another rule change.
- No project license has been selected yet.
