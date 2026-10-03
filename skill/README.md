# Skill staging area

The final coding-agent skill is intentionally **not generated yet**.

The current source of truth is:

- `../standards/candidate-rules.md`

Why wait?

The first research round produced both high-confidence invariants and disputed engineering preferences. Generating `SKILL.md` too early would freeze uncertain opinions into agent behavior.

The final skill should stay thin and load detailed references on demand.

Planned structure:

```text
skill/
├── SKILL.md
└── references/
    ├── core.md
    ├── java-spring.md
    ├── vue.md
    ├── anti-patterns.md
    └── review-checklist.md
```

Expected behavior:

1. understand the requested change and repository-local conventions;
2. apply hard correctness/contract rules;
3. use review triggers to inspect suspicious code without mechanically refactoring it;
4. apply language/framework defaults only when relevant;
5. preserve existing architecture unless the task explicitly asks for redesign;
6. review the produced diff for readability, failure semantics, duplication, resource lifecycle, and over-abstraction.

The skill should complement scope-control tools such as `stop-that-shit`, not duplicate them:

- scope-control skill: **do not do unnecessary work**;
- this skill: **write the necessary code well**.
