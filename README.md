# agent-coding-standards

A research-driven engineering coding Skill for AI coding agents.

> Once requirements and the overall technical direction are known, help the coding agent write the necessary code clearly, maintainably, idiomatically, and without unnecessary abstraction.

## v0.1.0

The first usable runtime release is now frozen at **v0.1.0**.

Runtime entry point:

```text
skill/SKILL.md
```

The Skill uses progressive disclosure:

```text
skill/
├── SKILL.md
└── references/
    ├── core.md
    ├── java-spring.md
    ├── vue-js-ts.md
    ├── anti-patterns.md
    └── review-checklist.md
```

`SKILL.md` stays intentionally thin. Detailed language/framework rules are loaded only when relevant.

## What this Skill is for

Use it when an agent is already implementing or reviewing application code and the task direction is known.

It focuses on:

- correctness and truthful contracts;
- readable control flow and responsibility boundaries;
- null / absence / failure semantics;
- async completion, cancellation, and cleanup;
- explicit, reviewable data mapping;
- state/code consistency;
- appropriate abstraction;
- scope discipline;
- final diff review and verification.

It is **not** an architecture-design Skill and does not authorize unrelated cleanup, framework migration, dependency churn, or repository-wide refactoring.

## Core idea

**Observed ≠ Recommended. Popular ≠ Correct.**

The project began by reviewing four tutorial-lineage codebases, then used independent model review, cross-review, official documentation, mature open-source evidence, and controlled A/B coding benchmarks.

Rules are separated into:

- **MUST** — correctness / contract / lifecycle invariants;
- **SHOULD** — strong maintainability defaults;
- **REVIEW TRIGGER** — inspect carefully, but do not auto-refactor;
- **CONVENTION** — repository/team consistency choices.

The most important guardrail is simple:

> A coding standard may improve an in-scope change. It does not expand the task scope by itself.

## Validation status

v0.1.0 has gone through:

- independent source-code review;
- cross-review;
- official documentation and mature OSS validation;
- first-round full-standard calibration;
- Runtime Skill A/B validation with GLM-5.3;
- Runtime Skill A/B validation with a Codex participant configuration.

Runtime Skill benchmark summary:

| Participant | Baseline | Runtime Skill | Delta | Scope creep | Over-engineering |
|---|---:|---:|---:|---|---|
| GLM-5.3 | 588 / 600 | 587 / 600 | −1 | not observed | not observed |
| Codex participant | 596 / 600 | 591 / 600 | −5 | not observed | not observed |

These results **do not prove a stable score improvement**. The suite is small, each condition has one sample per case, and strong baselines are close to the rubric ceiling.

What the experiments do support more confidently:

- the Runtime Skill did not trigger systematic scope expansion;
- it did not trigger framework-building or abstraction bloat;
- progressive disclosure selected relevant references rather than loading everything;
- the earlier cancellation/abort wording gap was corrected;
- large mechanical mappings remained explicit rather than being automatically converted into generic machinery.

See:

- `benchmarks/runs/` — first-round full-standard calibration;
- `benchmarks/runtime-skill-runs/` — GLM-5.3 Runtime Skill A/B;
- `benchmarks/codex-runtime-skill-runs/` — Codex Runtime Skill A/B.

## Using the Skill

For an agent that supports local Skills, install or expose the `skill/` directory according to that agent's Skill mechanism.

For an agent without a dedicated Skill loader, use `skill/SKILL.md` as the entry instruction and allow it to read the referenced files on demand.

Do **not** replace the Runtime Skill with `standards/candidate-rules.md` during normal use. The latter is the research-oriented source document, not the optimized runtime context.

## Repository structure

```text
agent-coding-standards/
├── README.md
├── CHANGELOG.md
├── RELEASE_NOTES.md
├── research/                 # source reviews and validation trail
├── standards/
│   └── candidate-rules.md   # research-oriented v1.1 source of truth
├── benchmarks/
│   ├── cases/
│   ├── rubric.md
│   ├── runs/
│   ├── runtime-skill-runs/
│   └── codex-runtime-skill-runs/
└── skill/
    ├── SKILL.md
    └── references/
```

## Maintenance policy after v0.1.0

Do not tune the Skill to chase single benchmark points.

A future rule change should preferably come from at least one of:

- a real-world failure/regression observed while using the Skill;
- repeated benchmark behavior across multiple samples;
- a verified language/framework/library contract;
- strong independent evidence that the current wording causes systematic harm.

When a real failure is found, preserve it as a benchmark case before changing the rule.

## License

No license has been added yet. Until one is chosen, normal copyright restrictions apply.
