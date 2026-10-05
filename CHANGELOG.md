# Changelog

All notable project changes are recorded here.

## [0.1.0] - 2026-10-05

First productized runtime release.

### Added

- Installable package at `dist/engineering-coding-standards/`.
- Thin `SKILL.md` plus four runtime references:
  - core;
  - Java/Spring;
  - Vue/JavaScript/TypeScript;
  - final review.
- Comment/documentation discipline covering useful implementation comments, repository-local comment language, stale comments, and Java API contract documentation.
- Cognitive-load review rules for dead/no-op distinctions, opaque boolean call sites, hidden mutation, data-structure semantics, and multi-phase methods.
- Naming and local-organization discipline for packages, modules, classes/files, Java build artifacts, and Vue components.
- External naming validation against Alibaba P3C, Google Java Style, Oracle package guidance, Maven/Gradle conventions, Vue's official style guide, and npm package-name guidance.
- Research-oriented candidate rules v1.1.
- Six-case coding benchmark suite.
- Cooperative sub-Agent/worktree isolation runbook.
- Full-standard calibration artifacts.
- GLM-5.3 Runtime Skill A/B artifacts.
- Codex Runtime Skill acceptance artifacts.

### Productization

- Separated the installable Skill from the research/benchmark repository.
- Kept `skill/` unchanged as benchmark provenance.
- Removed runtime-only duplication between anti-pattern and checklist references.
- Removed research history and generic handbook material from the installable package.
- Kept the runtime focus on scope discipline, truthful contracts, lifecycle correctness, reviewable mappings, useful comments, meaningful names, low cognitive load, local consistency, and proportional abstraction.

### Real-project calibration

A SearchService produced with the Skill was correct and reasonably structured, but exposed quality gaps that the earlier benchmark cases did not stress:

- a boolean mode parameter whose branches were behaviorally identical;
- call sites with several raw booleans whose meaning required jumping to the callee;
- a helper that silently mutated a caller-owned SQL parameter object later reused by another query;
- a `Map<K, V>` used where only key membership mattered;
- a large method requiring the reader to track several independent loading/matching/aggregation phases.

A subsequent review also found that the runtime Skill only said "follow local naming conventions" and did not give enough guidance for naming new packages/modules/files.

The naming update deliberately keeps architecture out of scope: it governs how a justified package/module/file is named and organized, not whether the system should adopt feature packages, layered packages, bounded contexts, microservices, or multi-module architecture.

### Validation summary

Benchmark rounds did not demonstrate a stable score improvement, but they also did not show systematic scope creep or over-engineering.

The productized package previously passed a read-only smoke test with no blocking issues. The later cognitive-load and naming updates are based on observed real-project output plus targeted official-source validation rather than benchmark-point chasing.

### Known limitations

- Current benchmark suite contains six small/medium cases and does not yet contain reduced regression cases for the real-project naming/cognitive-load findings.
- Each Runtime Skill condition has one sample per case.
- Frontend Case 05 has no full runtime harness.
- JS/TS routing is intentionally broad and may be refined later if real Node.js usage exposes ambiguity.
- No project license has been selected yet.
