# Benchmark Autopilot Runbook

> Goal: run the entire first-round A/B benchmark with almost no human intervention.

## Environment assumptions verified on Z Code

A local isolation probe established:

- child-agent **conversation context is isolated**;
- child agents do **not** inherit the coordinator's dynamic conversation history;
- static environment injection such as system prompt / AGENTS.md / skill list is shared;
- child agents share the **same filesystem and default working directory**;
- child-agent transcripts are persisted under `.zcode/` and are readable by other agents with shell access;
- there is no first-class per-agent `cwd` / worktree parameter.

Therefore this benchmark uses **fresh child-agent contexts + separate git worktrees + strict filesystem-reading constraints**.

This is sufficient for a cooperative coding benchmark, but it is not an adversarial sandbox. A worker that deliberately searches `.zcode/` could still break blinding. If adversarial isolation is required, use separate top-level sessions/OS sandboxes instead.

## Role

You are the **benchmark coordinator**.

Do not directly solve benchmark coding tasks.

Your job is to:

1. prepare isolated worktrees;
2. launch fresh worker agents;
3. collect diffs and verification metadata;
4. launch a fresh blind Judge only after all worker runs finish;
5. reveal A/B identities only after judging is complete.

## Fixed participant

Use the same worker model/configuration for all 12 runs.

Record:

- model name;
- reasoning/thinking mode;
- tool/agent mode if relevant.

Do not switch model/configuration between baseline and standards runs.

## Preflight

Before running cases:

1. confirm `AGENTS.md` and other statically injected instructions do **not** contain `candidate-rules.md` content or equivalent coding standards that would contaminate baseline;
2. record the benchmark starting commit;
3. confirm the repository is clean;
4. create one dedicated temporary worktree per run, all from the same starting commit;
5. place worktrees outside the main checkout when practical.

Suggested layout:

```text
../agent-coding-bench-worktrees/
├── 01-A/
├── 01-B/
├── 02-A/
├── 02-B/
...
├── 06-A/
└── 06-B/
```

Do not expose A/B meaning to the Judge.

## Filesystem isolation rule

Every Worker must receive an **absolute assigned worktree path**.

Its first command must be:

```bash
cd <assigned-worktree>
pwd
```

The Worker must then remain within that worktree.

Workers are explicitly forbidden from:

- reading/searching `.zcode/`;
- searching the parent workspace;
- using `find`, `rg`, `grep`, recursive listing, or equivalent outside the assigned worktree;
- reading sibling worktrees;
- reading `benchmarks/runs/`;
- reading another case;
- reading prior worker output.

If a Worker violates this, mark that run **contaminated** and rerun it in a fresh child-agent context/worktree.

## Condition isolation

### Baseline worker

Baseline may read only:

- its assigned case directory;
- its case `TASK.md`;
- normal build/test files required by that case.

Baseline must not read:

- `standards/`;
- `benchmarks/rubric.md`;
- other cases;
- run artifacts;
- `.zcode/`.

### Standards worker

Standards may additionally read:

- `standards/candidate-rules.md`.

It must not read:

- `benchmarks/rubric.md`;
- other cases;
- run artifacts;
- baseline output;
- `.zcode/`.

## Worker prompts

Use a fresh child-agent context for every run.

### Baseline

Replace `<case>` and `<worktree>`:

> You are a benchmark Worker. Work only inside the assigned worktree `<worktree>`.
>
> First run `cd <worktree>` and confirm `pwd`.
>
> Then work only on `benchmarks/cases/<case>`. Read its `TASK.md` and files, complete the task, and run the verification command from TASK.md when provided.
>
> Do not read `standards/`, `benchmarks/rubric.md`, other cases, `benchmarks/runs/`, sibling worktrees, the parent workspace, or `.zcode/`. Do not search outside your assigned worktree.
>
> Follow task constraints. At the end, report files changed and verification results.

### Standards

Replace `<case>` and `<worktree>`:

> You are a benchmark Worker. Work only inside the assigned worktree `<worktree>`.
>
> First run `cd <worktree>` and confirm `pwd`.
>
> Read `standards/candidate-rules.md` and use it as the engineering standard for this task. Then work only on `benchmarks/cases/<case>`: read `TASK.md` and files, complete the task, and run the verification command from TASK.md when provided.
>
> Do not read `benchmarks/rubric.md`, other cases, `benchmarks/runs/`, sibling worktrees, baseline output, the parent workspace, or `.zcode/`. Do not search outside your assigned worktree.
>
> Follow task constraints. At the end, report files changed and verification results.

## Cases

Run all six:

1. `01-complex-validation`
2. `02-mechanical-mapping`
3. `03-error-null-contract`
4. `04-promise-sse`
5. `05-vue-state`
6. `06-scope-control`

Each case has two conditions: baseline and standards.

A/B labels used for Judge blinding must be assigned by the coordinator and kept only in coordinator memory until judging is complete.

## Per-run procedure

For each Worker:

1. use its dedicated worktree created from the exact benchmark starting commit;
2. launch a fresh child-agent context;
3. send the correct condition prompt;
4. let the Worker edit only its assigned worktree;
5. capture:
   - changed files;
   - diff;
   - test/check command;
   - pass/fail/not available;
   - any protocol violation;
6. save the diff into the main repository's `benchmarks/runs/` only **after** the Worker has finished;
7. do not let later Workers read those artifacts.

Case 05 intentionally has no complete automated harness. Record verification as unavailable; do not fabricate success.

## Judge phase

Only after all 12 Worker runs are complete, launch **one fresh Judge child agent**.

The Judge:

- may read `benchmarks/rubric.md`;
- may read the two diffs and factual verification metadata for the current case;
- must not read `standards/candidate-rules.md`;
- must not read `.zcode/`;
- must not read filenames or notes that reveal baseline/standards identity;
- must not search the workspace;
- must score A and B independently before seeing the reveal.

For each case, copy/present the two outputs under neutral labels such as:

```text
case-01-A.diff
case-01-B.diff
```

Do not encode condition identity in Judge-visible names.

Use this Judge instruction:

> You are the blind evaluator for an A/B coding benchmark.
>
> Read `benchmarks/rubric.md`. For each case, evaluate candidate A and candidate B using only the provided task, diff, and factual verification metadata.
>
> You do not know which candidate used additional coding standards. Do not attempt to infer or discover that identity. Do not read `.zcode/`, repository history, hidden run notes, standards files, or unrelated files.
>
> Score both candidates using the rubric, provide dimension-by-dimension reasoning, identify regressions/over-engineering/scope creep, and choose the stronger result only from the code/task evidence.

## Reveal and summary

After the Judge completes **all six cases**:

1. reveal the coordinator's A/B mapping;
2. compute:
   - baseline score per case;
   - standards score per case;
   - per-dimension deltas;
   - overall average delta;
   - count of standards wins / ties / losses;
   - any cases where standards caused scope creep or over-engineering;
3. create `benchmarks/runs/SUMMARY.md`;
4. preserve Judge reasoning;
5. do **not** modify `standards/candidate-rules.md` yet.

The human/ChatGPT reviewer will decide rule changes after reading the benchmark evidence.

## Stop / rerun conditions

Stop or rerun a case if:

- the worker reads `.zcode/`;
- the worker reads sibling worktree/run output;
- baseline reads `standards/`;
- either Worker reads `benchmarks/rubric.md`;
- the starting worktree is not based on the same commit;
- files cannot be safely attributed to one Worker;
- worker context is reused between conditions.

## Interpretation

This setup protects against **accidental contamination** and ordinary cooperative-agent behavior.

It does **not** provide security-grade isolation because all child agents execute under the same local user and can technically access shared disk artifacts.

For this benchmark, do not reward or test attempts to bypass the isolation instructions. If the goal later becomes adversarial agent isolation, move the experiment to separate processes/containers/top-level sessions.
