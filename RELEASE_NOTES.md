# v0.1.0 Release Notes

`agent-coding-standards` v0.1.0 packages the project as a small installable Coding Skill rather than shipping the research workspace as runtime context.

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

The project has completed source review, cross-review, external validation, full-standard calibration, and two Runtime Skill A/B rounds.

Those experiments do not prove that the Skill always raises benchmark scores. They do support that the benchmarked runtime candidate did not systematically cause scope creep or over-engineering.

The final `dist/` package is a lean derivative and should pass one final smoke test before the v0.1.0 tag is created.

## Next phase

Use the Skill on real projects.

When a concrete bad behavior appears:

1. capture the failure;
2. reduce it to a reproducible case;
3. add it to the benchmark;
4. reproduce where practical;
5. only then decide whether the Skill should change.
