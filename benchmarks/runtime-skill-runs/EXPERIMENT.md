# Round 2 — Runtime Skill A/B Benchmark

- Date: 2026-10-03
- Baseline commit (all worktrees): 0eb9980 (main, "feat: add first runtime coding standards skill")
- Worker model: GLM-5.3, ZCode general-purpose subagent, default reasoning, fresh context per run
- Conditions:
  - base: no coding standards of any kind (skill/, standards/, research/ removed)
  - skill: runtime Skill via skill/SKILL.md + references/ progressive disclosure
- Worktrees: D:/Desktop/study/agent-coding-runtime-skill-bench/<NN>-<base|skill>, pruned per condition, setup committed locally
- Judge: single fresh subagent, blinded, workspace outside main repo, sees rubric + case TASK + anonymized A/B diffs + factual verification metadata
- Workers from all 12 runs are forbidden: .zcode/, main repo, sibling worktrees, git history, other cases, run outputs
- Case 05 has no automated verification: recorded as unavailable, never fabricated

## Setup commits (pre-worker HEAD per worktree)
- 01-base: 3c954bf
- 01-skill: 0ed9c76
- 02-base: 2723715
- 02-skill: dcc9423
- 03-base: 73640c0
- 03-skill: ce9264b
- 04-base: bd746e9
- 04-skill: 2c4b7c2
- 05-base: 0087cfc
- 05-skill: 20360ed
- 06-base: 1782a58
- 06-skill: 4fe0afc
