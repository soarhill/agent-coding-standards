# Engineering Coding Standards Runtime Skill — v0.1.0

Runtime entry point:

- `SKILL.md`

References:

- `references/core.md`
- `references/java-spring.md`
- `references/vue-js-ts.md`
- `references/anti-patterns.md`
- `references/review-checklist.md`

## Status

**v0.1.0 is the first frozen runtime release.**

It is derived from `standards/candidate-rules.md` v1.1, but the runtime package is intentionally smaller and uses progressive disclosure.

The Skill has completed:

- source-code research and cross-review;
- official/open-source validation;
- a full-standard calibration round;
- a Runtime Skill A/B round with GLM-5.3;
- a Runtime Skill A/B round with a Codex participant configuration.

Across the two Runtime Skill rounds, no systematic scope creep or over-engineering was observed.

The benchmarks do **not** establish that the Skill will increase scores on every strong model or every task. Both Runtime Skill experiments had near-ceiling baselines and one sample per condition/case.

## Runtime design

`SKILL.md` contains only:

- scope boundaries;
- working principles;
- progressive-disclosure routing;
- rule-strength semantics;
- the core scope guardrail.

Detailed rules live in references so irrelevant framework guidance does not consume runtime context.

Expected loading pattern:

- every task → `core.md`;
- Java/Spring → `java-spring.md`;
- Vue/JS/TS → `vue-js-ts.md`;
- refactor/mapping/duplication/cleanup-sensitive tasks → `anti-patterns.md`;
- final review → `review-checklist.md`.

The Codex acceptance round recorded one procedural read-order deviation in a Vue run, but no cross-domain overloading or forbidden reference leakage.

## What v0.1.0 deliberately does not claim

It does not claim:

- universal best practices for every repository;
- guaranteed benchmark improvement;
- architecture correctness;
- security-audit coverage;
- that every REVIEW TRIGGER requires a refactor;
- that one model's preferred code shape is universally superior.

Repository-local conventions still win when they are reasonable and safe.

## After v0.1.0

Treat this version as a stable baseline for real project use.

Do not edit rules merely because a single benchmark candidate loses one or two points. Prefer collecting real-world failure cases, converting them into reproducible tests/benchmarks, and then deciding whether the Skill should change.
