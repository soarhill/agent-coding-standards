---
name: engineering-coding-standards
description: Use when writing, modifying, or reviewing application code after requirements and overall technical direction are known. Helps keep changes scoped, contracts truthful, code readable, and abstractions proportional.
---

# Engineering Coding Standards

Use this Skill to improve **how the requested code is written**.

It does not authorize architecture redesign, repository-wide cleanup, framework migration, or unrelated refactoring.

## Workflow

1. Read the task and nearby repository code first.
2. Identify the exact behavior and files the task authorizes.
3. Read `references/core.md`.
4. Load only the relevant language reference:
   - Java / Spring → `references/java-spring.md`
   - Vue / JavaScript / TypeScript → `references/vue-js-ts.md`
5. Implement the smallest coherent change that makes the real contract clear.
6. Before finishing, read `references/review.md` and review the actual diff.
7. Run the closest relevant existing tests/checks when available. Report what really ran.

Do not load every reference by default.

## Working principles

- Prefer clear contracts over clever compression.
- Prefer lower cognitive load over fewer lines.
- Preserve the repository's reasonable local conventions.
- Keep failure, absence, state, async completion, cancellation, and cleanup semantics intentional.
- Keep important mappings visible enough to review.
- Abstract shared meaning and shared reasons to change, not merely repeated syntax.
- Treat long methods, duplication, nesting, large mappings, and large components as **review signals**, not automatic refactor commands.
- Never expand task scope merely because this Skill notices unrelated problems.

## Scope guardrail

For issues outside the requested change:

- do not silently fix them;
- do not build a broader refactor around them;
- mention them only when they materially block or endanger the requested work.

A good result should feel unsurprising in the repository: correct, readable, locally consistent, and no more elaborate than the problem requires.
