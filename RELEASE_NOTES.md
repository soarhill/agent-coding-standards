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
- use names and local package/module organization that reveal responsibility;
- remove cognitive noise such as dead distinctions and opaque boolean call sites;
- make mutation/ownership visible and choose data structures that express intent;
- preserve meaningful data shape instead of packing named values into incidental strings/containers only to decode them later;
- split genuinely multi-phase work by semantic phase when that lowers cognitive load;
- prefer lower cognitive load over fewer lines;
- abstract shared meaning rather than repeated syntax;
- write comments for non-obvious why/constraints/contracts rather than narrating obvious code;
- follow the repository's existing comment and naming conventions when they are coherent;
- use Javadoc for meaningful Java API contracts without documenting every trivial member;
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
8. real-project calibration on code generated with the Skill;
9. targeted naming/package/module validation against Alibaba P3C, Google Java Style, Oracle, Maven, Gradle, Vue, and npm guidance;
10. a reduced real-project regression case for semantic structure and intent.

The historical A/B rounds used the original six cases, so the new Case 07 does not retroactively change those scores.

Real-project calibration showed that correctness and scope discipline alone are not enough: code can still be harder to read than necessary when it preserves no-op flags, hides mutation, uses data structures that obscure intent, coordinates too many semantic phases in one method, introduces weak names/organization, or encodes structured internal data into a delimiter string only to parse it again.

The naming rules intentionally do **not** choose the application's architecture. They guide naming and local organization after a package/module/file boundary is already justified by the task and surrounding repository.

## Next phase

Use the Skill on real projects and run the new regression case before release.

When a concrete bad behavior appears:

1. capture the failure;
2. reduce it to a reproducible case;
3. add it to the benchmark when practical;
4. reproduce where practical;
5. only then decide whether the Skill should change.
