# Agent Coding Standards Benchmarks

This suite evaluates whether coding guidance improves or preserves engineering quality **without causing scope creep or over-engineering**.

## Cases

| Case | Main signal |
|---|---|
| 01-complex-validation | readability, responsibility, giant conditions |
| 02-mechanical-mapping | explicit mapping vs false abstraction |
| 03-error-null-contract | null/failure/absence semantics |
| 04-promise-sse | async completion, failure, cancellation |
| 05-vue-state | state meaning, list identity, local duplication |
| 06-scope-control | focused bug fixing and scope discipline |
| 07-structure-and-intent | semantic data shape, map-vs-set intent, proportional refactoring |

Case 07 was added after real-project calibration and is **not** part of the historical six-case scores below.

## Completed experiment rounds

### 1. Full candidate-standard calibration

Location:

```text
benchmarks/runs/
```

Condition pair:

- baseline: no candidate standard;
- treatment: full `standards/candidate-rules.md`.

This round exposed an async cancellation wording gap and informed candidate rules v1.1.

### 2. Runtime Skill — GLM-5.3

Location:

```text
benchmarks/runtime-skill-runs/
```

Condition pair:

- baseline: no Skill / standards / research context;
- treatment: `skill/SKILL.md` with progressive-disclosure references.

Result: 588 vs 587 / 600 (baseline vs treatment). No scope creep or over-engineering observed. Progressive disclosure behaved as intended.

### 3. Runtime Skill — Codex acceptance

Location:

```text
benchmarks/codex-runtime-skill-runs/
```

Condition pair is the same Runtime Skill A/B protocol.

Result: 596 vs 591 / 600 (baseline vs treatment). No scope creep or over-engineering observed. One Vue treatment run recorded a procedural reference-read-order deviation. The main score difference came from a conditional SSE `onerror` contract preference in Case 04.

## Experimental interpretation

The completed experiment rounds used the original six cases and are **not** a statistical proof that the Skill helps or harms coding quality.

Important limitations:

- one sample per condition per case;
- one Judge per round;
- strong baselines near the rubric ceiling;
- heterogeneous cases rather than repeated identical trials;
- cooperative filesystem isolation rather than an OS security sandbox.

Use the results to detect large behavioral regressions and qualitative patterns, not to over-interpret one-point differences.

## Reproduction rules

For a clean A/B run:

- same participant model/configuration across both conditions;
- fresh participant context for every run;
- identical starting commit;
- separate worktree for each run;
- no access to prior results, rubric, sibling worktrees, hidden transcripts, or the other condition;
- baseline physically lacks Skill/standards/research material;
- treatment sees only the frozen Runtime Skill and references;
- coordinator independently reruns available verification;
- Judge is launched only after all participant runs complete;
- Judge sees anonymous diffs, task text, rubric, and factual verification metadata;
- reveal condition labels only after all scores are final.

`AUTOPILOT.md` records the cooperative isolation model established during local-agent testing.

## What counts as a useful result

A treatment is not better merely because the diff is larger or contains more abstractions.

Evaluate:

- correctness;
- readability and cognitive load;
- contract clarity;
- scope discipline;
- abstraction quality;
- repository fit;
- verification quality.

A simple, explicit solution can and often should beat a framework-shaped one.

## Future benchmark work

The original six cases are close to saturation for strong coding agents. New cases should preferentially come from real failures.

Examples:

- transaction + remote-call boundaries;
- multi-state workflows;
- cache/DB semantic drift;
- concurrent resource ownership;
- retries/idempotency under partial failure;
- mappings where generic abstraction is genuinely tempting.

For effect-size questions, repeat each condition multiple times before drawing conclusions.
