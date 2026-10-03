# Blind Judge Reveal

The 12 run diffs were shuffled and anonymized (`R01`–`R12`) before judging. The judge subagent evaluated them against `benchmarks/rubric.md` without condition labels (verified: no occurrence of "baseline"/"standards" in any score file). This file reveals the mapping.

Judge score files: `R01.md` … `R12.md` and `OVERVIEW.md` in this directory (unedited judge output, blind labels preserved).

## Mapping

> Correction after post-run audit: the originally committed reveal table accidentally swapped the run names/cases for R03 and R04. The blind Judge files themselves were correct (`R03.md` is Case 03 and `R04.md` is Case 02), as is `OVERVIEW.md`. The rows below are the corrected reveal.


| Blind ID | Run | Case | Condition | Judge total (out of 95)* |
|---|---|---|---|---|
| R01 | glm-5.3-01-complex-validation-standards | 01-complex-validation | standards | 93 |
| R02 | glm-5.3-06-scope-control-standards | 06-scope-control | standards | 91 |
| R03 | glm-5.3-03-error-null-contract-baseline | 03-error-null-contract | baseline | 93 |
| R04 | glm-5.3-02-mechanical-mapping-baseline | 02-mechanical-mapping | baseline | 78 |
| R05 | glm-5.3-03-error-null-contract-standards | 03-error-null-contract | standards | 91 |
| R06 | glm-5.3-04-promise-sse-baseline | 04-promise-sse | baseline | 93 |
| R07 | glm-5.3-05-vue-state-baseline | 05-vue-state | baseline | 90 |
| R08 | glm-5.3-04-promise-sse-standards | 04-promise-sse | standards | 85 |
| R09 | glm-5.3-06-scope-control-baseline | 06-scope-control | baseline | 91 |
| R10 | glm-5.3-05-vue-state-standards | 05-vue-state | standards | 93 |
| R11 | glm-5.3-02-mechanical-mapping-standards | 02-mechanical-mapping | standards | 91 |
| R12 | glm-5.3-01-complex-validation-baseline | 01-complex-validation | baseline | 89 |

\* The judge reported totals out of 95 because rubric dimension 7 (verification quality, 5 pts) was "not available to judge" for all runs (the blind workspace contained diffs only, no test transcripts). See the judge's own caveat in each score file.

Shuffle procedure: `ls glm-5.3-*.diff | shuf` in the main repository; mapping was held outside both the repository and the judge workspace until after judging (`D:/Desktop/study/bench-blind-map.txt`, deleted after this file was written).
