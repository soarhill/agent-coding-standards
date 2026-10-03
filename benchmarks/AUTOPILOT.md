# Benchmark Autopilot Runbook

> Goal: run the entire first-round A/B benchmark with almost no human intervention.

## Role

You are the **benchmark runner**, not the benchmark evaluator.

Your job is to execute all 6 cases twice with the same participant model:

- baseline: participant must NOT read `standards/`;
- standards: participant MUST read `standards/candidate-rules.md` first.

Do not score the outputs. Do not improve one run using knowledge from the other run.

## Critical isolation requirement

Each individual run must execute in a **fresh isolated participant context**.

That means:

- no conversation history from another run;
- no prior diff/result visible;
- no baseline answer visible to the standards run;
- no standards file visible to the baseline run;
- no rubric visible to either participant.

If your environment supports child agents/subagents/fresh task contexts, use one fresh context per run.

If your environment **cannot guarantee isolated contexts**, stop before running and report exactly that limitation. Do not fake isolation.

## Fixed participant

Use the same participant model/configuration for all 12 runs.

Record:

- model name;
- reasoning/thinking mode;
- tool/agent mode if relevant.

## Cases

Run in this order:

1. `01-complex-validation`
2. `02-mechanical-mapping`
3. `03-error-null-contract`
4. `04-promise-sse`
5. `05-vue-state`
6. `06-scope-control`

For each case, run:

1. baseline
2. standards

## Baseline participant prompt

Use this exact instruction, replacing `<case>`:

> Work only on `benchmarks/cases/<case>`. Read `TASK.md` and the files inside this case, then complete the task. Do not read `standards/`, `benchmarks/rubric.md`, other benchmark cases, `benchmarks/runs/`, or any previous run output. Follow the task constraints. Run the verification command from TASK.md when one is provided. At the end, report files changed and verification results.

## Standards participant prompt

Use this exact instruction, replacing `<case>`:

> First read `standards/candidate-rules.md` and treat it as the engineering standard for this task. Then work only on `benchmarks/cases/<case>`. Read `TASK.md` and the files inside this case, then complete the task. Do not read `benchmarks/rubric.md`, other benchmark cases, `benchmarks/runs/`, the baseline diff, or any previous run output. Follow the task constraints. Run the verification command from TASK.md when one is provided. At the end, report files changed and verification results.

## Per-run procedure

For every run:

1. Restore the case to the benchmark starting state from the current benchmark baseline commit.
2. Ensure generated build artifacts from the previous run are removed.
3. Launch a fresh isolated participant context with the appropriate prompt.
4. Let the participant modify only the permitted case files.
5. Run the case's verification command when provided.
6. Save the diff under:
   - `benchmarks/runs/<model>-<case>-baseline.diff`
   - or `benchmarks/runs/<model>-<case>-standards.diff`
7. Save a matching Markdown run note using `benchmarks/run-template.md`.
8. Restore the case before the next run.

## Protect evaluator material

The participant must never read:

- `benchmarks/rubric.md`
- another case
- prior diffs
- prior run notes

The benchmark runner may read those only as needed to execute the protocol, but must not leak their contents into participant prompts.

## Change-scope recording

For every run, record whether the participant modified:

- only files inside the target case;
- files outside the target case;
- tests;
- benchmark infrastructure.

Do not silently clean up unauthorized changes. Record them in the run note, then reset before the next run.

## Vue case

Case 05 intentionally has no full build/test harness.

For that case:

- record that automated verification is unavailable;
- do not invent a successful test result;
- preserve the diff for later review.

## Completion

After all 12 runs:

1. verify that there are 12 diff files and 12 run-note files;
2. create `benchmarks/runs/SUMMARY.md` containing only factual execution metadata:
   - participant model/config;
   - each run's changed files;
   - test/check command;
   - pass/fail/not available;
   - any protocol violation or isolation problem.
3. Do **not** score which condition is better.
4. Commit only benchmark run artifacts; do not modify `standards/candidate-rules.md`.
5. Report the final commit SHA.

## Stop conditions

Stop and report instead of continuing if:

- isolated participant contexts cannot be guaranteed;
- the starting benchmark files are missing/corrupted;
- the participant cannot be prevented from reading forbidden files;
- repository state cannot be restored safely between runs.

Experimental integrity matters more than completing all 12 runs.
