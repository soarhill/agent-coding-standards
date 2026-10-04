# v0.1.0 Release Notes

`agent-coding-standards` v0.1.0 is the first productized installable Coding Skill release.

## Install this

```text
dist/engineering-coding-standards/
```

The package contains only:

```text
SKILL.md
references/core.md
references/java-spring.md
references/vue-js-ts.md
references/review.md
```

## What changed during productization

The benchmarked candidate under `skill/` was useful but still carried some handbook-like and duplicated guidance.

The release package keeps only the behavior-shaping rules most likely to matter during coding:

- stay inside the authorized scope;
- keep failure/state/async/cleanup contracts truthful;
- keep important mapping semantics visible;
- prefer lower cognitive load over fewer lines;
- abstract shared meaning rather than repeated syntax;
- respect reasonable repository conventions;
- review the real diff and real verification signal before finishing.

Research notes, benchmark artifacts, evidence classifications, and long rule catalogs remain in the repository but do not ship as runtime Skill context.

## Evidence

The project has completed:

1. source review;
2. independent cross-review;
3. external documentation / OSS validation;
4. full-standard calibration;
5. Runtime Skill A/B validation with GLM-5.3;
6. Runtime Skill A/B validation with a Codex participant configuration;
7. final read-only smoke testing of the productized package.

The smoke test passed with no blocking issues. The package is self-contained, all references resolve internally, and simulated Java / Vue usage follows the intended progressive-disclosure path.

The benchmark rounds do not prove that the Skill always raises scores. They do support that the benchmarked runtime candidate did not systematically cause scope creep or over-engineering.

## Why no more pre-release edits

The smoke test reported only non-blocking observations. Those are deliberately left for real-world usage rather than triggering another pre-release rewrite.

This keeps v0.1.0 evidence-driven instead of repeatedly tuning wording to hypothetical edge cases.

## Next phase

Use the Skill on real projects.

When a concrete bad behavior appears:

1. capture the failure;
2. reduce it to a reproducible case;
3. add it to the benchmark;
4. reproduce where practical;
5. only then decide whether the Skill should change.
