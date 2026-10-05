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
- remove cognitive noise such as dead distinctions and opaque boolean call sites;
- make mutation/ownership visible and choose data structures that express intent;
- split genuinely multi-phase work by semantic phase when that lowers cognitive load;
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
7. read-only smoke testing of the productized package;
8. real-project calibration on code generated with the Skill.

The benchmark rounds do not prove that the Skill always raises scores. They do support that the benchmarked runtime candidate did not systematically cause scope creep or over-engineering.

Real-project calibration showed that correctness and scope discipline alone are not enough: code can still be harder to read than necessary when it preserves no-op flags, hides mutation, uses data structures that obscure intent, or makes one method coordinate too many semantic phases. v0.1.0 now calls out these issues directly without imposing arbitrary size thresholds or architecture redesign.

## Next phase

Use the Skill on real projects.

When a concrete bad behavior appears:

1. capture the failure;
2. reduce it to a reproducible case;
3. add it to the benchmark when practical;
4. reproduce where practical;
5. only then decide whether the Skill should change.
