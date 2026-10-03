# Benchmark Run

- Model: GLM-5.3 (zai-individual-coding-plan)
- Model mode / reasoning level: ZCode general-purpose subagent, fresh context, default reasoning configuration
- Case: 04-promise-sse
- Condition: baseline
- Starting commit: 65a8dc0
- Date: 2026-10-03

## Prompt actually given

> You are an autonomous coding agent working in an isolated workspace.
>
> Workspace root: D:/Desktop/study/bench-wt/04-promise-sse-baseline
> Treat the workspace root as the repository root; all relative paths in the task instruction below refer to it.
>
> Hard access restrictions (isolation requirements for this run):
> - Work only inside the workspace root.
> - Do not access D:/Desktop/study/agent-coding-standards or anything else under D:/Desktop/study/ except your workspace root.
> - Do not access sibling workspaces under D:/Desktop/study/bench-wt/.
> - Do not access anything under C:/Users/yangboxuan/.zcode/.
> - Do not read standards/, benchmarks/rubric.md, other benchmark cases, benchmarks/runs/, or any previous run output.
> - Do not use git to read repository history or content beyond your own working changes (no git show, git log, git stash, git cat-file). git status and git diff of your own uncommitted changes are allowed.
>
> Task instruction:
>
> Work only on `benchmarks/cases/04-promise-sse`. Read `TASK.md` and the files inside this case, then complete the task. Do not read `standards/`, `benchmarks/rubric.md`, other benchmark cases, `benchmarks/runs/`, or any previous run output. Follow the task constraints. Run the verification command from TASK.md when one is provided. At the end, report files changed and verification results.

Isolation enforcement applied by runner: run executed in a fresh detached git worktree at 65a8dc0 (D:/Desktop/study/bench-wt/04-promise-sse-baseline, outside the main checkout). Before launch the runner deleted from the worktree: standards/, research/, skill/, benchmarks/rubric.md, benchmarks/AUTOPILOT.md, benchmarks/README.md, benchmarks/run-template.md, benchmarks/runs/, and the five other case directories. These deletions were restored before diff capture.

## Files changed

- benchmarks/cases/04-promise-sse/chat-session.js (modified; only change)

Change scope: only files inside the target case. No tests changed. No benchmark infrastructure changed. No files outside the case changed. No untracked artifacts left behind.

## Verification

- Test/check command: `node --test chat-session.test.mjs` (run in case directory, Node v24.21.0)
- Result: PASS — 3 tests, 3 pass, 0 fail (participant reported 3/3 pass; runner independently re-ran the command in the worktree after the run — 3 pass, 0 fail)
- Did the model claim anything it did not run? No.

## Notes

No protocol violation, no isolation problem.
