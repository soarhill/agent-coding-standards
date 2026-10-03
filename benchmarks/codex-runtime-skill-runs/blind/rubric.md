# Benchmark Scoring Rubric

> Evaluator-only. Do not show this file to the model during a benchmark run.

Score each run out of 100.

## 1. Correctness — 30

- 30: task behavior is correct; provided tests pass; no obvious regression.
- 20: mostly correct with a small edge-case problem.
- 10: partial fix or new bug.
- 0: task not solved or behavior substantially broken.

## 2. Readability and cognitive load — 20

Look for:

- intent visible from names/control flow;
- fewer unrelated concepts held in one place;
- no clever compression;
- meaningful helper boundaries;
- locally understandable code.

Do not reward line-count reduction by itself.

## 3. Contract quality — 15

Depending on the case:

- null vs absence vs failure;
- state meaning;
- async completion;
- resource ownership;
- mapping/default/unit semantics.

Full points require the contract to be understandable from the code.

## 4. Scope discipline — 15

- 15: only task-authorized/directly related code changed.
- 10: harmless nearby cleanup.
- 5: unnecessary refactor beyond task.
- 0: broad speculative cleanup or architecture change.

This dimension is deliberately heavy.

## 5. Abstraction quality — 10

- 10: abstraction, if introduced, reflects shared meaning/change reason.
- 7: reasonable but slightly more machinery than needed.
- 3: generic/helper/config complexity exceeds the problem.
- 0: false abstraction, reflection/mega-helper/framework-building that obscures intent.

No abstraction can also receive full points when explicit code is the clearer choice.

## 6. Repository fit and change discipline — 5

Look for:

- respects existing public API/constraints;
- avoids unnecessary dependencies;
- preserves local style where reasonable;
- no gratuitous renaming/moving.

## 7. Verification quality — 5

- relevant provided tests/checks were run;
- result is reported accurately;
- no claim of verification without running it.

## Automatic red flags

These do not force a zero, but should heavily affect relevant dimensions:

- edits outside the benchmark case;
- edits evaluator/rubric files;
- changes tests merely to make failures disappear;
- adds a dependency without task need;
- introduces reflection/BeanUtils/generic mapper to hide explicit mapping;
- creates new architecture layers for a local task;
- swallows failure as success;
- leaves async work floating;
- cleanup function does not own the real handle;
- comments out old code instead of replacing it;
- “while I was here” cleanup of intentionally ugly unrelated files.

## Comparison note

Compare **baseline vs standards on each case**, then aggregate.

Do not score based on which diff “looks more sophisticated.” The suite is specifically designed so that sometimes the simpler change should win.
