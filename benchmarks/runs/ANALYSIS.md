# First-Round Benchmark Analysis

> Human synthesis after auditing the blind-judge artifacts.
>
> Participant: GLM-5.3, 6 cases × 2 conditions.
>
> Important bookkeeping correction: the originally committed `judge/REVEAL.md` swapped R03 and R04. The Judge files and `OVERVIEW.md` were internally consistent; the corrected mapping is now committed.

## Corrected per-case result

| Case | Baseline | Standards | Delta | Interpretation |
|---|---:|---:|---:|---|
| 01 complex validation | 89 | 93 | **+4** | standards improved semantic grouping and public-contract clarity without over-splitting |
| 02 mechanical mapping | 78 | 91 | **+13** | strongest positive signal: standards prevented a large one-off builder abstraction |
| 03 error/null contract | 93 | 91 | **-2** | both were correct; baseline encoded absence with Optional while standards preserved the existing nullable signature |
| 04 Promise/SSE | 93 | 85 | **-8** | standards fixed the two target bugs but failed to distinguish intentional cancellation from user-visible failure |
| 05 Vue state | 90 | 93 | **+3** | standards favored one explicit status authority and stable identity with little extra machinery |
| 06 scope control | 91 | 91 | **0** | both stayed surgical; no scope creep |

Aggregate (judge dimensions 1–6, max 95):

- Baseline: **89.00 average**
- Standards: **90.67 average**
- Delta: **+1.67**
- Standards: **3 wins / 2 losses / 1 tie**

All runnable cases passed their tests in both conditions. No run edited tests, benchmark infrastructure, unrelated files, or the intentional legacy bait.

## What the benchmark actually supports

### 1. The anti-overengineering rules are doing real work

Case 02 is the most important positive result.

The baseline solution created a private 17-field fluent builder, adding roughly a hundred lines of setters while retaining a positional 17-argument constructor inside `build()`. The abstraction moved the review hazard instead of removing it and increased the number of coordinated edits required for each future field.

The standards solution stayed explicit: named local values, a small `absent()` helper, and one generic defensive-copy helper.

This is direct benchmark evidence for:

- S-03 mapping semantics should stay visible;
- S-04 abstract shared meaning/change reasons, not syntax;
- R-06 large field mappings trigger mapping review, **not automatic model/builder/framework construction**;
- scope/complexity should match the problem.

No wording change is required here; v1 already points in the right direction.

### 2. Scope control did not cause “clean-code tourism”

Case 06 deliberately placed ugly unrelated legacy code next to a small bug.

Neither condition touched it.

That does not prove the scope guardrail alone caused the result—the TASK itself warned about the bait—but it shows that loading the full standard did **not** create observable cleanup creep in this suite.

Keep the scope guardrail unchanged.

### 3. Null/absence modeling is a genuine trade-off, not a reason for a new hard rule

Case 03 is only a two-point loss.

Both conditions correctly separated:

- real not-found;
- upstream failure;
- malformed successful response.

Baseline changed `findUser` to `Optional<UserDto>`, making absence type-visible. Standards preserved `UserDto/null` and documented that null has exactly one meaning, reducing signature blast radius.

The Judge preferred the type-enforced contract, but in a real repository a public/internal signature change can affect unseen callers.

Therefore **do not upgrade “use Optional for absence” into a rule** from this benchmark.

Keep S-08 contextual:

- use a type-level absence representation when it materially prevents misuse and the boundary can safely change;
- documented null may be acceptable when the existing contract and compatibility constraints justify it;
- failure must remain distinct either way.

### 4. Async cancellation is the one real wording gap

Case 04 is the clearest negative signal.

Both solutions correctly:

- awaited the real SSE Promise;
- retained the real AbortController;
- cancelled through the real handle;
- cleared ownership safely.

The standards run nevertheless sent every caught error to `setError`, while the baseline run explicitly treated an intentional abort as shutdown rather than a user-visible request failure.

This benchmark by itself is not enough to say “AbortError must always be swallowed.” Different APIs/libraries settle cancellation differently; the earlier external validation already established that library-specific behavior varies.

The missing principle is:

> **Cancellation is a distinct lifecycle outcome. Its outward meaning must be intentional and follow the actual API/library contract; an expected user/lifecycle cancellation must not accidentally become a user-visible failure.**

That is a narrow clarification to M-05, not a new architecture rule.

### 5. Small readability wins are real but should not turn into comment inflation

Cases 01 and 05 show modest gains from:

- naming semantic limits/constants;
- grouping validation by meaning;
- keeping a single state mapping;
- recording a non-obvious contract close to enforcement.

The winning diffs did not create new classes/layers.

Do not generalize this into “public methods need Javadoc” or “every status map needs a comment.” C-08 remains the right rule: comments should add information not already obvious from the code.

## Judge caveats

The blind Judge did useful work, but its score is not ground truth.

Two limitations matter:

1. It saw diffs, not the full repository/caller graph. For Case 03 it could not evaluate the real blast radius of changing `findUser` to `Optional`.
2. In Case 04 it penalized the standards run for potential AbortError reporting. That is a reasonable robustness concern, but actual cancellation settlement is library-specific, and the provided test did not assert this edge.

Therefore v1.1 should make only narrow changes supported by both the benchmark and prior mechanism research.

## Decision for v1.1

Change only the async/lifecycle wording:

- explicitly classify cancellation/abort as a separate lifecycle outcome;
- require error/loading semantics to be intentional and library-contract-aware;
- add cancellation to the final review checklist.

Do **not** change:

- Optional/null policy;
- mechanical mapping rules;
- method-length/nesting triggers;
- scope guardrail;
- Vue state rules;
- abstraction rules.

## Next calibration

The next useful experiment is no longer another rewrite of `candidate-rules.md`.

Build the actual thin runtime Skill and then run the same A/B suite against **that Skill**, because a thin `SKILL.md + references` may influence Agent behavior differently from loading the full candidate document.

After that, replicate on a second participant model if practical before calling the Skill broadly model-agnostic.
