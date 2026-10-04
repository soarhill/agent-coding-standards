# agent-coding-standards

A research-driven engineering coding Skill for AI coding agents.

> Once requirements and the overall technical direction are known, help the coding agent write the necessary code clearly, maintainably, idiomatically, and without unnecessary abstraction.

## Installable Skill package

The repository contains research, standards, and benchmark evidence. The **actual installable Skill package** is intentionally much smaller:

```text
dist/
└── engineering-coding-standards/
    ├── SKILL.md
    └── references/
        ├── core.md
        ├── java-spring.md
        ├── vue-js-ts.md
        └── review.md
```

Use that directory as the runtime Skill.

The rest of this repository is the development/evidence workspace, not part of the runtime package.

## What the Skill is for

Use it when an agent is already implementing or reviewing application code and the task direction is known.

It focuses on a small set of behaviors that coding agents commonly get wrong:

- expanding scope because nearby code looks bad;
- over-abstracting simple code;
- hiding important mapping semantics;
- conflating failure, absence, state, completion, or cancellation;
- cleaning up the wrong resource/handle;
- optimizing for fewer lines instead of lower cognitive load;
- ignoring reasonable repository-local conventions;
- finishing without reviewing the real diff or verification signal.

It is **not** an architecture-design Skill, a Java/Spring handbook, a Vue handbook, a security audit, or a formatter/linter replacement.

## Why the repository is larger than the Skill

This project deliberately separates **evidence** from **runtime instruction**.

```text
research/       source-code studies and validation trail
standards/      research-oriented candidate rules
benchmarks/     A/B cases, runbooks, judge outputs, experiment artifacts
skill/          benchmarked runtime candidate kept for provenance
dist/           productized installable Skill
```

The older `skill/` directory is preserved because both Runtime Skill benchmark rounds used it. It is historical experiment input, not the preferred install target.

## Runtime design

The final package uses progressive disclosure:

1. `SKILL.md` defines scope, workflow, and the core behavioral intent.
2. `references/core.md` is loaded for coding work.
3. Only the relevant language reference is loaded.
4. `references/review.md` is used for the final diff review.

The package intentionally omits long research commentary, benchmark history, generic best-practice encyclopedias, and rules the model usually knows without prompting.

## Validation status

The research path included:

- independent source-code review;
- cross-review;
- official documentation and mature OSS validation;
- first-round full-standard calibration;
- Runtime Skill A/B validation with GLM-5.3;
- Runtime Skill A/B validation with a Codex participant configuration.

The experiments did **not** establish a stable score improvement. Strong baselines were already near the rubric ceiling and each condition had one sample per case.

What they did support:

- no systematic scope creep was observed;
- no systematic over-engineering was observed;
- progressive disclosure selected relevant references;
- the earlier cancellation/abort wording gap was corrected;
- large mappings did not automatically turn into generic frameworks.

The productized `dist/` package is a **lean derivative** of the benchmarked runtime candidate. It should receive a final smoke test before the v0.1.0 tag is created.

## Repository structure

```text
agent-coding-standards/
├── README.md
├── CHANGELOG.md
├── RELEASE_NOTES.md
├── research/
├── standards/
├── benchmarks/
├── skill/                    # benchmark provenance
└── dist/
    └── engineering-coding-standards/
```

## Maintenance policy

Do not tune the Skill to chase isolated benchmark points.

Prefer rule changes backed by:

- a concrete real-world failure;
- repeated benchmark behavior;
- a verified language/framework/library contract;
- strong evidence of systematic harm.

When a real failure is found, preserve it as a reproducible case before changing the Skill.

## License

No license has been added yet. Until one is chosen, normal copyright restrictions apply.
