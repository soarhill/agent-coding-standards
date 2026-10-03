# Skill staging area

The candidate standard has reached **v1.1** after independent source-code reviews, cross-review, external validation, and one full A/B Agent calibration round.

The current source of truth is:

- `../standards/candidate-rules.md`

The research standard is now stable enough to produce the **first thin runtime Skill candidate**. That Skill should still be benchmarked before being treated as final.

## Calibration status

The first 6-case GLM-5.3 benchmark is complete. It showed a small aggregate gain, a strong win against one-off mapping overengineering, no observed scope creep, and one async-cancellation wording gap that was fixed in v1.1.

The next experiment should benchmark the **actual thin Skill**, not keep loading the full research-oriented candidate-rules document.

## Planned runtime structure

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

## Expected behavior

1. understand the requested change and repository-local conventions;
2. do not expand scope merely because a rule detects unrelated legacy issues;
3. apply hard correctness/contract rules to touched code;
4. use review triggers as inspection prompts, not automatic refactoring commands;
5. load language/framework-specific references only when relevant;
6. preserve architecture unless redesign is explicitly requested;
7. review the final diff for readability, failure semantics, duplication, mapping clarity, state consistency, resource lifecycle, and over-abstraction.

This skill should complement scope-control tools such as `stop-that-shit`:

- scope-control skill: **do not do unnecessary work**;
- this skill: **write the necessary code well**.
