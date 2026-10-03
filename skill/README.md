# Skill staging area

The candidate standard has reached **v1** after two independent source-code reviews, cross-review, and external validation.

The current source of truth is:

- `../standards/candidate-rules.md`

The final runtime Skill is still intentionally not frozen.

## Why not generate SKILL.md immediately?

The remaining risk is no longer lack of research. It is **Agent behavior**:

- Will a REVIEW TRIGGER cause unnecessary refactoring?
- Will MUST rules accidentally expand task scope?
- Will the standards improve readability without creating abstraction bloat?
- Which rules need to be loaded for Java/Spring vs Vue/JS/TS?

We should calibrate those questions on real coding tasks first.

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
