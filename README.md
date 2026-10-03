# agent-coding-standards

A research-driven coding standards project for AI coding agents.

## Goal

This repository is not intended to teach an agent how to design an entire system architecture. Its focus is narrower:

> Once the product requirement and overall technical direction are known, help the coding agent produce code that is clear, maintainable, natural, consistent, and appropriately abstracted.

The project started from a review of four tutorial codebases, followed by independent analysis from two different coding models and cross-review between them. Because the four projects come from the same tutorial lineage, repeated patterns are treated as observations—not automatically as best practices.

**Observed ≠ Recommended.**

## Current stage

We are currently in the **candidate-rule consolidation** stage.

The repository will separate rules into four strengths:

- **MUST** — correctness or contract invariants; violations are usually clear defects.
- **SHOULD** — strong defaults that normally improve maintainability, but can have contextual exceptions.
- **REVIEW TRIGGER** — mechanical signals that tell an agent to stop and inspect the design; they do not automatically require refactoring.
- **CONVENTION** — team/project consistency choices, not universal best practices.

## Planned structure

```text
agent-coding-standards/
├── README.md
├── research/
│   └── README.md
├── standards/
│   └── candidate-rules.md
└── skill/
    └── README.md
```

The final skill and language/framework references will be created only after the candidate rules have been reviewed and the disputed rules have been externally validated.

## Design principles

1. Prefer lower cognitive load over fewer lines of code.
2. Expose intent instead of compressing logic.
3. Abstract shared concepts and shared change reasons—not merely similar syntax.
4. Preserve explicit contracts for failure, completion, state, data shape, and resource ownership.
5. Avoid both under-engineering and over-engineering.
6. Do not let a local style preference masquerade as an industry rule.
7. When working in an existing repository, respect local conventions unless they create a clear correctness or maintainability problem.
