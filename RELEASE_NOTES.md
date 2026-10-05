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

## What it focuses on

The release package keeps only behavior-shaping guidance most likely to matter during coding:

- stay inside the authorized scope;
- keep failure/state/async/cleanup contracts truthful;
- keep important mapping semantics visible;
- prefer lower cognitive load over fewer lines;
- abstract shared meaning rather than repeated syntax;
- write comments for non-obvious why/constraints/contracts rather than narrating obvious code;
- follow the repository's existing comment language and documentation conventions;
- use Javadoc for meaningful Java API contracts without documenting every trivial member;
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
7. read-only smoke testing of the productized package.

The benchmark rounds do not prove that the Skill always raises scores. They do support that the benchmarked runtime candidate did not systematically cause scope creep or over-engineering.

Before release, comment/documentation discipline was restored from the research standard and tightened using official Alibaba, Google, and Oracle guidance: code should remain self-explanatory where possible, comments should carry non-obvious intent and constraints, and comment language should follow repository/team convention rather than being hard-coded globally.

## Next phase

Use the Skill on real projects.

When a concrete bad behavior appears:

1. capture the failure;
2. reduce it to a reproducible case;
3. add it to the benchmark;
4. reproduce where practical;
5. only then decide whether the Skill should change.
