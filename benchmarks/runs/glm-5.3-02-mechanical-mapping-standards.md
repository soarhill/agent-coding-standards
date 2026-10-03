# Benchmark Run

- Model: GLM-5.3 (zai-individual-coding-plan)
- Model mode / reasoning level: ZCode general-purpose subagent, fresh context, default reasoning configuration
- Case: 02-mechanical-mapping
- Condition: standards
- Starting commit: 65a8dc0
- Date: 2026-10-03

## Prompt actually given

> You are an autonomous coding agent working in an isolated workspace.
>
> Workspace root: D:/Desktop/study/bench-wt/02-mechanical-mapping-standards
> Treat the workspace root as the repository root; all relative paths in the task instruction below refer to it.
>
> Hard access restrictions (isolation requirements for this run):
> - Work only inside the workspace root.
> - Do not access D:/Desktop/study/agent-coding-standards or anything else under D:/Desktop/study/ except your workspace root.
> - Do not access sibling workspaces under D:/Desktop/study/bench-wt/.
> - Do not access anything under C:/Users/yangboxuan/.zcode/.
> - Do not read benchmarks/rubric.md, other benchmark cases, benchmarks/runs/, or any previous run output. Reading standards/candidate-rules.md is required by the task instruction below.
> - Do not use git to read repository history or content beyond your own working changes (no git show, git log, git stash, git cat-file). git status and git diff of your own uncommitted changes are allowed.
>
> Task instruction:
>
> First read `standards/candidate-rules.md` and treat it as the engineering standard for this task. Then work only on `benchmarks/cases/02-mechanical-mapping`. Read `TASK.md` and the files inside this case, then complete the task. Do not read `benchmarks/rubric.md`, other benchmark cases, `benchmarks/runs/`, the baseline diff, or any previous run output. Follow the task constraints. Run the verification command from TASK.md when one is provided. At the end, report files changed and verification results.

Isolation enforcement applied by runner: run executed in a fresh detached git worktree at 65a8dc0 (D:/Desktop/study/bench-wt/02-mechanical-mapping-standards, outside the main checkout). Before launch the runner deleted from the worktree: research/, skill/, benchmarks/rubric.md, benchmarks/AUTOPILOT.md, benchmarks/README.md, benchmarks/run-template.md, benchmarks/runs/, and the five other case directories. standards/candidate-rules.md was kept (required reading for this condition) and was not modified. These deletions were restored before diff capture.

## Files changed

- benchmarks/cases/02-mechanical-mapping/AvailableRoomCriteriaAssembler.java (modified; only source change)

Generated artifacts (untracked, excluded from diff): AvailableRoomCriteria.class, AvailableRoomCriteriaAssembler.class, AvailableRoomCriteriaAssemblerTest.class, RoomCandidateQuery.class.

Change scope: only files inside the target case. No tests changed. No benchmark infrastructure changed. No files outside the case changed.

## Verification

- Test/check command: `javac *.java && java AvailableRoomCriteriaAssemblerTest` (run in case directory)
- Result: PASS (participant reported pre-fix failure `AssertionError: excluded labels` and post-fix PASS; runner independently re-ran the command in the worktree after the run — PASS)
- Did the model claim anything it did not run? No.

## Notes

Participant referenced rule identifiers (R-03, R-06, S-03, S-04, M-03, M-16, §1.3) in its final report, consistent with having read standards/candidate-rules.md as instructed. The report phrase "Baseline run failed" refers to the participant's own before-fix execution of the test in its workspace, not to the benchmark's baseline condition. No protocol violation, no isolation problem.
