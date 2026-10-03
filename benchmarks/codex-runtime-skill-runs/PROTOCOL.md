# Codex Runtime Skill A/B protocol

Date: 2026-10-03 (Asia/Shanghai). Starting commit: `2f1007632c29d74bc4e385d9f2d94de3790a2cbc`.

This experiment reuses the fresh-context, separate-worktree, cooperative filesystem isolation and delayed reveal principles in `benchmarks/AUTOPILOT.md`. The Treatment input is the frozen Runtime Skill entry and its on-demand references, replacing the runbook's earlier standards-file treatment. No earlier experiment outputs or judgments are used.

## Participants and isolation

- Twelve independent Workers, one for each case/condition; `collaboration.spawn_agent` with `fork_turns=none` and no model/reasoning overrides. All inherit the Coordinator's current configuration. The exact runtime model identifier and reasoning setting are not exposed by that API, so neither is guessed.
- Each run uses its own detached git worktree created from the same starting commit, outside the main checkout. Every Worker is instructed to set its assigned absolute location first and specify it on later shell calls.
- Baseline reads only its TASK and necessary case files. Treatment additionally reads `skill/SKILL.md` and individual `skill/references/` files according to progressive disclosure. Treatment reports actual reads and reasons.
- Workers are prohibited from reading standards, research, the rubric, other cases, history, transcripts, other chats, the main checkout, sibling worktrees and all experiment outputs. No Worker subagents, commits or dependency changes are permitted.
- Shared system/developer instructions and available-skill catalog are unavoidable static input in both conditions. Repository and ancestor `AGENTS.md` files were absent. Workers are explicitly instructed not to load other skills.
- This is cooperative isolation with a shared filesystem, not an OS security boundary. File hashes and changed-file inspection establish write integrity; Worker read reports establish reported read compliance, not exhaustive syscall-level read auditing.

## Coordinator collection

The Coordinator does not implement or repair case code. After each Worker finishes, `capture-run.ps1` records changed files, full-context patch, independent TASK verification, output and exit code. It checks protected-file hashes, tests/TASK changes, dependency manifests and changes outside the assigned case. Compiler-generated `.class` files are recorded separately from source patches. Structural issues, unnecessary abstractions and scope are reviewed from each diff by the Coordinator and later independently by the Judge. Failures are preserved rather than repaired or selectively resampled.

Case 05 specifies no automated harness. Its verification is recorded as `not_provided`; source inspection does not establish browser/runtime success.

## Blinding

A/B identities are randomly assigned independently for each case and kept in Coordinator session memory until all six cases have been judged. The Judge is launched only after all twelve Workers complete, with `fork_turns=none`. Its isolated packet contains only the rubric, TASKs, anonymous A/B patches and factual Coordinator verification metadata. Worker narratives, reference loads, mapping and Coordinator qualitative audits are excluded. The Judge scores all twelve candidates independently and returns raw dimension-level scores and reasoning before any reveal.

Only after all six judgments are saved does the Coordinator write the reveal mapping, Worker reports, Coordinator audits, aggregate summary and analysis. One sample per condition/case supports descriptive comparisons only; it cannot estimate sampling variability or establish causal/general cross-model effects.

## Artifacts

- `PREFLIGHT.json`: configuration, starting commit and frozen-file manifest.
- `runs/NN-X/`: patch, metadata, git status, independent verification output; after reveal, Worker report and Coordinator qualitative audit.
- `blind/`: exact Judge input packet and raw evaluation.
- `REVEAL.json`: mapping published after judging completes.
- `SUMMARY.md`, `ANALYSIS.md`: revealed descriptive results and limitations.
- `capture-run.ps1`: reproducible collection procedure; not benchmark implementation.

Only files beneath `benchmarks/codex-runtime-skill-runs/` are committed. Skill, standards, case implementations and tests in the main checkout remain unchanged.
