# Runtime Skill

The first runtime candidate is now available:

- `SKILL.md`

Detailed references:

- `references/core.md`
- `references/java-spring.md`
- `references/vue-js-ts.md`
- `references/anti-patterns.md`
- `references/review-checklist.md`

## Status

This runtime Skill is derived from `standards/candidate-rules.md` v1.1.

Its source material has passed:

- independent source-code review;
- cross-review;
- official/open-source validation;
- one 6-case A/B calibration round with GLM-5.3.

It is still a **runtime candidate**, not a frozen final release.

The next step is to rerun the benchmark with:

- baseline: no Skill;
- treatment: `skill/SKILL.md` with references loaded according to the Skill workflow.

The purpose is to verify that progressive disclosure preserves the useful parts of the full standard without making Agent behavior noisier or weaker.

## Design choice

`SKILL.md` is intentionally thin.

The full research/evidence trail stays in `standards/` and `research/`. Runtime context should contain only the rules needed for the current coding task.
