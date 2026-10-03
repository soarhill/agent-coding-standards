---
name: engineering-coding-standards
description: Use when writing, modifying, or reviewing application code after requirements and overall architecture are already known. Improves readability, contract clarity, maintainability, resource lifecycle, and abstraction discipline while avoiding scope creep and over-engineering.
---

# Engineering Coding Standards

Use this Skill to decide **how in-scope code should be written**, not to redesign the system.

## Boundaries

Do not use this Skill as permission to:

- expand the task into unrelated cleanup;
- redesign architecture unless explicitly requested;
- migrate frameworks/languages;
- introduce dependencies only to satisfy a style preference;
- run a broad security audit;
- rewrite formatter/linter concerns manually.

Repository-local conventions win when they are reasonable and do not create a correctness or maintainability problem.

## Working principle

Prefer the **simplest implementation that makes the real contract obvious**.

Optimize for:

1. correctness and truthful contracts;
2. low cognitive load;
3. clear responsibility and naming;
4. explicit failure/state/resource semantics;
5. appropriate abstraction;
6. minimal authorized change.

Do not optimize for fewer lines, more patterns, or more classes.

## Workflow

1. Read the task and nearby repository code before editing.
2. Identify the exact behavior and files the task authorizes.
3. Load `references/core.md`.
4. If relevant, additionally load:
   - Java/Spring → `references/java-spring.md`
   - Vue/JavaScript/TypeScript → `references/vue-js-ts.md`
   - refactor/duplication/mapping/“clean up” work → `references/anti-patterns.md`
5. Implement the smallest coherent change.
6. Before finishing, load `references/review-checklist.md` and review the diff.
7. Run the closest relevant existing tests/checks when available and report the real result.

Do not load every reference when it is irrelevant.

## Decision rules

### MUST

Treat correctness, contract, lifecycle, and resource-ownership rules as hard constraints for touched code.

### SHOULD

Use maintainability rules as defaults, not laws. Deviation is allowed when local context is clearer or safer.

### REVIEW TRIGGER

A trigger means **inspect**, not **refactor**.

Long methods, duplication, deep nesting, large mappings, and large components are reasons to ask whether the code can be clearer. They are not automatic instructions to split, extract, or redesign.

## Core guardrail

If an issue is outside the requested change:

- do not silently fix it;
- do not create a broader refactor because the Skill noticed it;
- mention it only if it materially affects the requested work.

## Final standard

A good result should make a reviewer think:

> “The code says what it means, the edge cases have intentional semantics, and nothing unnecessary was added.”
