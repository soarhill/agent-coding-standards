# First-Round Benchmark Summary (factual execution metadata)

Executed 2026-10-03 per `benchmarks/AUTOPILOT.md` by a benchmark runner (coordinator) that did not author any benchmark solution code. This file records execution facts only; it does not score which condition is better. Blind-judge artifacts live in `judge/` (see `judge/REVEAL.md`).

## Participant model / configuration

- Model: GLM-5.3 (account: zai-individual-coding-plan), identical for all 12 runs.
- Mode: ZCode general-purpose subagent, one fresh isolated context per run (no conversation history carried between runs).
- Reasoning: default subagent reasoning configuration, unchanged across runs.
- Tools: full agent toolset (read/write/edit files, run shell commands).
- Environment: Windows (Git Bash), JDK 17.0.14, Node v24.21.0.

## Execution protocol

- Starting state for every run: fresh detached git worktree at baseline commit `65a8dc0`, created outside the main checkout (`D:/Desktop/study/bench-wt/<case>-<condition>`), so each run started from an identical, artifact-free copy of the case.
- Isolation enforcement per run: before launch the runner deleted from the worktree — `benchmarks/rubric.md`, `benchmarks/AUTOPILOT.md`, `benchmarks/README.md`, `benchmarks/run-template.md`, `benchmarks/runs/`, the five non-target case directories, `research/`, `skill/`, and (baseline condition only) `standards/`. The standards condition kept `standards/candidate-rules.md` (required reading, unmodified). Participant prompts additionally forbade access to the main checkout, sibling worktrees, `C:/Users/yangboxuan/.zcode/`, and git history reads (`git show/log/stash/cat-file`). Deletions were restored before diff capture; diffs contain only participant changes.
- Run order: case 01 → 06, each case baseline then standards. 12 runs total, all sequential.
- Verification: each participant ran its case's TASK.md command; the runner independently re-ran every command in the participant's worktree afterwards. Diff capture excluded generated artifacts (`*.class`, `node_modules/`, `package-lock.json` if any; none of the latter occurred).
- Case 05 (Vue) has no build/test harness per runbook: automated verification recorded as not available for both runs; diffs preserved for review.

## Per-run facts

| Run | Condition | Files changed by participant | Test/check command | Result |
|---|---|---|---|---|
| 01-complex-validation | baseline | RecommendationCriteriaNormalizer.java | `javac *.java && java RecommendationCriteriaNormalizerTest` | PASS (runner-verified) |
| 01-complex-validation | standards | RecommendationCriteriaNormalizer.java | `javac *.java && java RecommendationCriteriaNormalizerTest` | PASS (runner-verified) |
| 02-mechanical-mapping | baseline | AvailableRoomCriteriaAssembler.java | `javac *.java && java AvailableRoomCriteriaAssemblerTest` | PASS (runner-verified) |
| 02-mechanical-mapping | standards | AvailableRoomCriteriaAssembler.java | `javac *.java && java AvailableRoomCriteriaAssemblerTest` | PASS (runner-verified) |
| 03-error-null-contract | baseline | ProfileService.java, UserRpcService.java, UpstreamUserServiceException.java | `javac *.java && java ProfileServiceTest` | PASS (runner-verified) |
| 03-error-null-contract | standards | ProfileService.java, UserRpcService.java, UpstreamUserServiceException.java | `javac *.java && java ProfileServiceTest` | PASS (runner-verified) |
| 04-promise-sse | baseline | chat-session.js | `node --test chat-session.test.mjs` | PASS — 3/3 (runner-verified) |
| 04-promise-sse | standards | chat-session.js | `node --test chat-session.test.mjs` | PASS — 3/3 (runner-verified) |
| 05-vue-state | baseline | KnowledgeFiles.vue | not available (no harness) | not available |
| 05-vue-state | standards | KnowledgeFiles.vue | not available (no harness) | not available |
| 06-scope-control | baseline | OrderService.java | `javac *.java && java OrderServiceTest` | PASS (runner-verified) |
| 06-scope-control | standards | OrderService.java | `javac *.java && java OrderServiceTest` | PASS (runner-verified) |

## Change-scope record

For all 12 runs the participant modified only files inside the target case directory. No tests were modified in any run. No benchmark infrastructure was modified. No files outside the target case were changed. Generated build artifacts (`.class` files) appeared in some runs and were excluded from diffs; two participants cleaned their own artifacts, the remainder were discarded with the run worktree. One standards-condition run (case 01) created a temporary scratch verification file (`EdgeCheck.java`) during the run and deleted it; no residue in the final state.

## Protocol violations / isolation problems

None observed. Specifics verified per run: no participant touched forbidden paths (worktree `git status` showed only in-case modifications plus the runner's pre-set deletions); no participant was given any content from the rubric, run notes, other cases, or the other condition's output; both conditions received identical prompts except for the standards first-reading clause; `standards/candidate-rules.md` is byte-identical to the baseline commit (verified after all runs). Limitations, recorded for transparency rather than as violations: isolation from repo git history was enforced by prompt instruction only (the worktree's git object database technically remained reachable); the judge's blinding covered condition labels but not diff-internal content (e.g. code comments), which is inherent to diff-based evaluation.

## Artifacts

- 12 diffs: `glm-5.3-<case>-<condition>.diff`
- 12 run notes: `glm-5.3-<case>-<condition>.md` (from `benchmarks/run-template.md`)
- Blind judge output: `judge/R01.md` … `judge/R12.md`, `judge/OVERVIEW.md`, mapping in `judge/REVEAL.md`
