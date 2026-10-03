# Agent Coding Standards Benchmarks

This suite checks whether the standards improve coding-agent output **without causing scope creep or over-engineering**.

## What we are testing

We are not comparing Z Code vs Codex.

We are comparing the **same model** under two conditions:

- **Baseline** — the model solves the case without reading `standards/candidate-rules.md`.
- **Standards** — the same model starts from the exact same files, reads `standards/candidate-rules.md`, then solves the same task.

Use a fresh conversation for every run.

## First-round cases

| Case | Main signal |
|---|---|
| 01-complex-validation | readability, responsibility, giant conditions |
| 02-mechanical-mapping | explicit mapping vs false abstraction |
| 03-error-null-contract | null/failure/absence semantics |
| 04-promise-sse | async completion and cancellation |
| 05-vue-state | state meaning, list identity, local duplication |
| 06-scope-control | bug fix discipline and scope control |

## Run protocol

For the first round, use **one model only** across all 6 cases.

### A. Baseline run

1. Reset the case:
   ```bash
   git restore benchmarks/cases/<case>
   ```
2. Start a **new conversation** with the model.
3. Tell it:
   > Work only on this benchmark case. Read TASK.md and the files inside this case. Do not read standards/, benchmarks/rubric.md, other cases, or prior run outputs.
4. Give it the case directory.
5. Let it finish the task normally.
6. Run the case tests when the case provides a test command.
7. Save the diff:
   ```bash
   git diff -- benchmarks/cases/<case> > benchmarks/runs/<model>-<case>-baseline.diff
   ```
8. Copy `benchmarks/run-template.md` to a matching `.md` run note and fill in the facts.

### B. Standards run

1. Reset the same case again:
   ```bash
   git restore benchmarks/cases/<case>
   ```
2. Start another **new conversation**.
3. Tell the model:
   > Read standards/candidate-rules.md first. Then work only on this benchmark case and TASK.md. Do not read benchmarks/rubric.md, other cases, baseline diffs, or prior run outputs.
4. Give it the same case directory.
5. Let it finish.
6. Run the same tests.
7. Save the diff:
   ```bash
   git diff -- benchmarks/cases/<case> > benchmarks/runs/<model>-<case>-standards.diff
   ```
8. Fill in another run note.

## Fairness rules

- Same model and model mode/configuration.
- Fresh conversation every time.
- Same starting commit.
- Same task text.
- No baseline output shown to the standards run.
- No rubric shown to either run.
- Do not manually coach one run more than the other.
- If the model asks a necessary clarification, answer the same way in both conditions.
- If a case has tests, do not edit the tests unless TASK.md explicitly allows it.

## What counts as a win

A standards run is better only if it improves the code **and** avoids new damage.

We specifically care about:

- correctness;
- readability;
- clear failure/state contracts;
- reasonable abstraction;
- minimal authorized scope;
- repository fit;
- no speculative framework-building.

A larger diff is not automatically better.

## After the first round

Run all 6 cases with one model first. Then bring the 12 diffs/run notes back for scoring.

Only after the suite itself looks useful should we repeat it with a second model.
