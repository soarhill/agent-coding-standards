# v0.1.0 Release Notes

`agent-coding-standards` v0.1.0 is the first frozen Runtime Skill release.

## Why this exists

AI coding agents are often functionally correct but still produce code that is:

- harder to review than necessary;
- over-abstracted;
- inconsistent about null/failure/state semantics;
- careless about async/resource lifecycle;
- tempted to clean unrelated legacy code.

This Skill focuses on the implementation layer after requirements and overall technical direction are known.

## Runtime package

Use:

```text
skill/SKILL.md
```

with its on-demand references under:

```text
skill/references/
```

The full `standards/candidate-rules.md` remains research material and should not normally be loaded into runtime coding context.

## Design principles

- lower cognitive load over fewer lines;
- explicit contracts over clever compression;
- abstract shared meaning, not repeated syntax;
- review triggers do not authorize refactors;
- repository conventions matter;
- correctness and lifecycle semantics are stronger than style preferences;
- the Skill must not expand task scope on its own.

## Evidence behind v0.1.0

The release combines:

1. review of four source projects;
2. independent model analysis and cross-review;
3. official documentation and mature open-source validation;
4. full-standard A/B calibration;
5. Runtime Skill A/B validation with GLM-5.3;
6. Runtime Skill A/B acceptance with a Codex participant configuration.

The Runtime Skill did not show a stable benchmark-score improvement on these near-ceiling cases. More importantly for a constraint Skill, neither validation round showed systematic scope creep or over-engineering.

## What is intentionally frozen

v0.1.0 does **not** add a new rule in response to the Codex Case 04 score difference.

That difference depends on a specific SSE provider callback/retry contract that was not fully present in the benchmark evidence. Changing the Skill to chase that single result would be benchmark overfitting.

## Next phase

Use v0.1.0 on real projects.

When the Skill causes a concrete bad change:

1. capture the failure;
2. reduce it to a reproducible case;
3. add the case to the benchmark;
4. reproduce across repeated runs where practical;
5. only then consider changing the rule.

That makes future versions evidence-driven rather than preference-driven.
