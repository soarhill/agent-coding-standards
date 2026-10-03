# Blind Judgment

Scored independently per candidate against `rubric.md` (Correctness 30, Readability 20, Contract 15, Scope 15, Abstraction 10, Repository fit 5, Verification 5). Verification results cited are the coordinator's independent reruns from the `.meta.md` files. Case 05 has no automated harness; dimension 7 is scored neutrally (3/5) for both candidates per instructions, not as a failure.

## Case 01 — complex-validation

Task: refactor `normalize` for readability with zero observable behavior change; public API, tests, and dependency set frozen.

### Candidate A

| Dimension | Score | Max | Reasoning |
|---|---|---|---|
| Correctness | 30 | 30 | Coordinator rerun `javac *.java && java RecommendationCriteriaNormalizerTest` PASS (exit 0). `normalizedLimit` (`limit <= 0 ? 4 : min(20, limit)`) is exactly equivalent to the original `max(1, min(20, ...))` over the int domain; all validation predicates and the single exception type/message are preserved. |
| Readability / cognitive load | 19 | 20 | `normalize` now reads as validate-then-construct; each rule group (category, price range, priorities) is a well-named one-expression predicate. Slightly dense compound conditions (e.g., `prioritiesAreInvalid` keeps the original's nested `&&`/`||`) and mixed naming forms (`X IsInvalid` vs `normalizedX`) keep it from a perfect score. |
| Contract quality | 15 | 15 | "Throw INVALID_CRITERIA on any rule violation, else return trimmed/defaulted criteria" is fully legible; `DEFAULT_LIMIT = 4` names the previously magic default, and null-priorities-becomes-empty-list stays explicit in `normalizedPriorities`. |
| Scope discipline | 15 | 15 | Single file, single method's internal structure; no classes/interfaces added (task explicitly discouraged them); tests untouched. |
| Abstraction quality | 10 | 10 | Helpers each own one rule or one normalization concern — real semantic boundaries, no machinery for its own sake. |
| Repository fit | 5 | 5 | Keeps the existing helper style (`tooLong`/`hasText`/`trimToNull` untouched), no renames of public API, no deps. |
| Verification quality | 5 | 5 | Provided command run and PASS per independent rerun; no test edits. |

**Total: 99/100**

Notes:
- Strengths: constants replace every magic number (80/160/5/4/20); validation order and short-circuit semantics preserved; limit-clamping equivalence handled correctly (the `Math.max(1, ...)` was provably redundant and its removal is safe).
- Weaknesses: none of substance; pre-existing `MAX_RESULTS` now sits last among constants, mildly odd ordering.
- Red flags: none.

### Candidate B

| Dimension | Score | Max | Reasoning |
|---|---|---|---|
| Correctness | 30 | 30 | Same rerun PASS. Identical predicate set; `normalizeLimit` uses early-return instead of ternary but is the same function over ints. |
| Readability / cognitive load | 19 | 20 | Same clean structure as A; small extras — early-return guards in `isInvalidPriceRange` and `hasInvalidPriorities`, and better micro-naming (`priority` lambda param, `maxLength` param). Verb-form helpers (`normalizePriorities`) echo the public `normalize`. Equally minor residual density elsewhere. |
| Contract quality | 15 | 15 | Same as A: named `DEFAULT_LIMIT`, explicit null-to-empty-list handling, unchanged exception contract. |
| Scope discipline | 15 | 15 | Single file; also renames `tooLong`'s parameter — a harmless in-scope touch inside the class being refactored, not creep. |
| Abstraction quality | 10 | 10 | Same decomposition as A; no new types, no generic machinery. |
| Repository fit | 5 | 5 | Preserves style and API; the two parameter renames improve clarity without churn. |
| Verification quality | 5 | 5 | Provided command run and PASS per independent rerun; no test edits. |

**Total: 99/100**

Notes:
- Strengths: guard-clause style in the two multi-condition predicates; better lambda/parameter micro-naming; helper verbs match the class's public verb.
- Weaknesses: none of substance.
- Red flags: none.

### Case comparison

The two candidates are near-identical in substance (same constants, same helper decomposition, same behavior) and differ only in expression style (single boolean expressions vs early returns) and micro-renames. Both pass verification and both fully satisfy "no observable behavior change." This is a tie; neither has a defensible edge on evidence.

## Case 02 — mechanical-mapping

Task: fix `excludedLabels` being dropped to `List.of()`, and make the 17-field mapping easier to review for future field additions; explicit mapping acceptable, no reflection/BeanUtils/codegen/deps, records and tests frozen.

### Candidate A

| Dimension | Score | Max | Reasoning |
|---|---|---|---|
| Correctness | 30 | 30 | Rerun `javac *.java && java AvailableRoomCriteriaAssemblerTest` PASS; the bug line becomes `copy(query.excludedLabels())`, preserving the defensive-copy semantics the test checks. Null-query branch behavior unchanged. |
| Readability / cognitive load | 17 | 20 | The fix is obvious, and blank lines now group the argument list into semantic clusters (ids/rent/area/floors/facilities/labels). But blank lines inside a constructor argument list are an unconventional device, and the grouping only mildly helps a future stray constant stand out. |
| Contract quality | 14 | 15 | Null-query → `impossible=true` with empty collections is still visible inline; nothing names it, but it was already legible and is unchanged. |
| Scope discipline | 15 | 15 | One file; merging the two type-duplicated private copy helpers into one generic `copy` is directly related to the mapping's uniformity, not unrelated cleanup. |
| Abstraction quality | 9 | 10 | `<T> List<T> copy` is right-sized and does not hide the field mapping (each field is still mapped explicitly); the task's "reducing line count is not the goal" note makes the helper merge a small, optional nicety rather than a clear win. |
| Repository fit | 5 | 5 | Keeps explicit per-field mapping and the existing style; no API or dependency changes. |
| Verification quality | 5 | 5 | Provided command run and PASS per independent rerun; no test edits. |

**Total: 95/100**

Notes:
- Strengths: minimal, surgical fix; homogenizing all six collection lines to `copy(...)` makes deviations easier to spot; no new machinery beyond one trivial generic helper.
- Weaknesses: the response to "easier to review for future field additions" is only grouping whitespace — the lightest possible interpretation; blank lines within an argument list will strike some reviewers as noise.
- Red flags: none. (The generic helper is not a "generic mapper hiding explicit mapping" — the mapping stays fully explicit.)

### Candidate B

| Dimension | Score | Max | Reasoning |
|---|---|---|---|
| Correctness | 30 | 30 | Same rerun PASS; `excludedLabels = copyStrings(query.excludedLabels())` fixes the bug with defensive copy; `empty()` reproduces the null branch exactly (10 nulls, 6 empty lists, `impossible=true`). |
| Readability / cognitive load | 18 | 20 | Every component is assigned to a local named exactly after the record component, so each mapping line reads `x = query.x()` and a stray constant visibly breaks the pattern; the constructor call passes one named variable per line. Cost: ~35 added lines and a slightly awkward `boolean impossible = false` local. |
| Contract quality | 15 | 15 | The null-query contract is extracted and named (`empty()`), and the class Javadoc states the review convention explicitly, including why a stray `List.of()` historically hid. |
| Scope discipline | 15 | 15 | The restructuring is squarely what the task asked for ("make the mapping easier to review for future field additions"); `empty()` extraction is directly related; tests, records, and helpers untouched. |
| Abstraction quality | 8 | 10 | `empty()` is a genuine semantic boundary, but the locals-plus-convention machinery is slightly more process than a 17-field assembler strictly needs; it earns its keep mainly because the task explicitly requested review-robustness. |
| Repository fit | 5 | 5 | Stays explicit, keeps `copyIntegers`/`copyStrings` as-is, no deps, no renames of anything existing. |
| Verification quality | 5 | 5 | Provided command run and PASS per independent rerun; no test edits. |

**Total: 96/100**

Notes:
- Strengths: directly and substantially answers the "future field additions" requirement with a dependency-free, reflection-free convention that specifically defeats the original bug class; the Javadoc encodes the convention for future maintainers.
- Weaknesses: vertical bulk (113 diff lines vs A's 63); the `impossible` local adds indirection over a literal `false`; relies on convention being followed rather than on any structural guarantee.
- Red flags: none.

### Case comparison

B is stronger on evidence for this case's specific second requirement: the task explicitly asked for review-robustness against future field additions, and B built a real, documented mechanism for that (named-local convention, one per line, `empty()` named contract), while A offered only whitespace grouping. B pays for it with mild verbosity and slightly more machinery than needed, so the margin is one point, not more.

## Case 03 — error-null-contract

Task: fix NPE and the swallowed-failure bug; missing user → `"Anonymous"`, upstream failure must stay distinguishable; `getDisplayName(long)` signature, deps, tests frozen.

### Candidate A

| Dimension | Score | Max | Reasoning |
|---|---|---|---|
| Correctness | 30 | 30 | Rerun `javac *.java && java ProfileServiceTest` PASS (exit 0): found → nickname, not-found → "Anonymous", failed response and thrown client exception both surface as `UpstreamUserServiceException` with non-blank messages (test's exact expectations). The defensive "found but data null → throw" branch closes the remaining NPE path. |
| Readability / cognitive load | 19 | 20 | `findUser` reads as a linear ladder (call → null response → !success → !found → data) with informative per-branch messages including the id; `describeError` keeps the fallback logic out of the throw line. Minor: `+ e` embeds the full exception toString in the message. |
| Contract quality | 15 | 15 | Exemplary: Javadoc on both `findUser` (null returned *only* for genuine not-found; throws for client-throw/missing-response/failed-response/malformed-data) and `getDisplayName` (Anonymous vs upstream-failure distinction); null response is honestly classified as upstream failure, not as "not found". |
| Scope discipline | 15 | 15 | Three files, all load-bearing: the two buggy classes plus an additive `(message, cause)` constructor needed to preserve the cause chain; the misleading `System.err` swallow-logging is removed as part of the fix, not as drive-by cleanup. |
| Abstraction quality | 10 | 10 | No new layers; one tiny helper (`describeError`) with a clear reason to exist. |
| Repository fit | 5 | 5 | Public signature kept; exception change is purely additive; local style preserved. Tiny nit: `ANONYMOUS_DISPLAY_NAME` is package-visible where `private` would suffice (nothing else uses it). |
| Verification quality | 5 | 5 | Provided command run and PASS per independent rerun; no test edits; workspace left clean. |

**Total: 99/100**

Notes:
- Strengths: failure/no-failure boundary made explicit in code and docs; cause preserved; every ambiguous response shape (null, failed, found-with-null-data) given a distinguishable exception message.
- Weaknesses: package-visible constant is a gratuitous (if harmless) visibility widening.
- Red flags: none.

### Candidate B

| Dimension | Score | Max | Reasoning |
|---|---|---|---|
| Correctness | 30 | 30 | Same rerun PASS; identical branch structure and identical outcomes for all four test scenarios; same defensive found-but-no-data throw. |
| Readability / cognitive load | 19 | 20 | Same clear ladder; the error-message fallback is an inline parenthesized ternary inside the throw statement (slightly denser than A's helper, still locally understandable), compensated by a pointed `// business outcome: the user does not exist` comment on the null return. |
| Contract quality | 15 | 15 | Compact `@throws` Javadoc on both methods enumerates the failure causes ("call failure, failed response, or malformed response"); null-only-for-not-found is stated; inline literal `"Anonymous"` matches how the test itself expresses it. |
| Scope discipline | 15 | 15 | Same three files, same minimal set of changes, same justified additive constructor. |
| Abstraction quality | 10 | 10 | No new abstractions beyond what the fix needs; inline literals throughout keep it maximally direct. |
| Repository fit | 4 | 5 | API/deps/style all respected, but audit facts record `.class` build artifacts left in the case directory after the run — a small workspace-hygiene lapse under "change discipline" (A left none under the same procedure). |
| Verification quality | 5 | 5 | Provided command run and PASS per independent rerun; no test edits. |

**Total: 98/100**

Notes:
- Strengths: functionally indistinguishable from A — same contract, same defensive edge handling, slightly leaner expression.
- Weaknesses: leftover build artifacts; marginally denser fallback expression.
- Red flags: none.

### Case comparison

The two solutions are structurally the same fix (await-style ladder in `findUser`, null→"Anonymous" in `ProfileService`, additive cause constructor) and both score full marks on correctness, contract, and scope. The only evidence-based separation is repository hygiene: B's run left `.class` artifacts in the case directory while A's did not, so A edges this case by one point.

## Case 04 — promise-sse

Task: fix (1) loading/error state not reflecting async SSE completion/failure and (2) `closeSSE()` not cancelling the active request; exported names, callback hooks, tests, deps frozen.

### Candidate A

| Dimension | Score | Max | Reasoning |
|---|---|---|---|
| Correctness | 30 | 30 | Coordinator rerun `node --test chat-session.test.mjs`: 3/3 pass, exit 0. `await fetchEventSource(...)` fixes both loading-until-completion and async rejection handling; module-level `activeController` makes `closeSSE()` abort the in-flight request, which the third test asserts directly. |
| Readability / cognitive load | 19 | 20 | Minimal diff, each changed line self-explanatory; the one comment ("A user-initiated close is not a UI error") explains the only non-obvious guard. The `finally` bookkeeping (`if (activeController === controller)`) needs a moment's thought for the overlapping-send case. |
| Contract quality | 15 | 15 | Async completion now honest (loading true for the whole stream, false in `finally`); abort is explicitly defined as expected shutdown rather than UI error; stale-controller guard makes overlapping sends safe; `closeSSE` clears its reference so it owns exactly the current handle. |
| Scope discipline | 15 | 15 | Single file, ~15 changed lines, nothing beyond the two bugs and the bookkeeping they require. |
| Abstraction quality | 10 | 10 | No new abstraction — the correct choice for a module of this size; the fake `eventSource` variable is honestly replaced by what was actually needed. |
| Repository fit | 5 | 5 | Exported names and callback-style hooks untouched; no deps; style preserved. |
| Verification quality | 5 | 5 | Provided command run, full pass, exact results recorded; no test edits. |

**Total: 99/100**

Notes:
- Strengths: distinguishes user-initiated abort from real failure; clears `activeController` inside `closeSSE` for eager cleanup; idempotent under double-close.
- Weaknesses: overlapping `sendMessage` calls would leave the earlier stream un-cancellable (single-slot tracking) — but the original module had the same single-stream assumption and no test covers overlap.
- Red flags: none. Async work is no longer floating; failure is not swallowed.

### Candidate B

| Dimension | Score | Max | Reasoning |
|---|---|---|---|
| Correctness | 30 | 30 | Same rerun: 3/3 pass, exit 0. Differs from A by exactly one line — `closeSSE()` aborts but does not null the controller, deferring cleanup to the in-flight call's `finally` guard. Behaviorally equivalent: abort is idempotent, and the reference is cleared when the request settles. |
| Readability / cognitive load | 19 | 20 | Equally minimal and equally well-commented ("An abort triggered by closeSSE is an expected shutdown, not a UI error"). |
| Contract quality | 15 | 15 | Same honest async/error/abort contract as A; cleanup ownership is simply delegated to the request's own `finally` rather than the closer. |
| Scope discipline | 15 | 15 | Single file, minimal lines, nothing extra. |
| Abstraction quality | 10 | 10 | None introduced, appropriately. |
| Repository fit | 5 | 5 | Same as A: names, hooks, deps, style preserved. |
| Verification quality | 5 | 5 | Provided command run, full pass, results recorded. |

**Total: 99/100**

Notes:
- Strengths: identical fix quality to A; one-line-lazier `closeSSE` is not a defect since re-abort is a no-op and `finally` performs the cleanup.
- Weaknesses: none of substance.
- Red flags: none.

### Case comparison

The two diffs are functionally identical — same `await` fix, same controller tracking, same abort-is-not-error guard, same stale-reference guard — differing only in whether `closeSSE` eagerly nulls the controller. Both pass all three tests. A tie on evidence.

## Case 05 — vue-state

Task: fix (1) same backend status showing different text in table vs edit dialog and (2) row inputs staying attached to the wrong logical file after insert/remove/reorder; status codes, API calls, styling/markup, Composition API, deps frozen. No automated harness exists; per instructions, dimension 7 is scored neutrally and absence of verification is not itself a failure.

### Candidate A

| Dimension | Score | Max | Reasoning |
|---|---|---|---|
| Correctness | 30 | 30 | Judged by reading (no harness). Both bugs are fixed canonically: `:key="file.id"` gives rows stable identity so inputs track their logical file, and the divergent `editStatusText` is deleted in favor of one `statusText` — correctly choosing the table's mapping, which covers all five statuses present in the data (0-4) while the edit variant was the shifted, incomplete one. |
| Readability / cognitive load | 19 | 20 | One mapping function with one name used in both places; the unused `index` binding is dropped along with the key change. Trivial if-ladder retained as-is — fine at this size. |
| Contract quality | 15 | 15 | Status semantics are now single-sourced (impossible for the two views to disagree again); row identity is bound to the domain id, the intended key of the list. |
| Scope discipline | 15 | 15 | Exactly the two bugs, ~10 effective lines: key line, two template call sites, one rename, one deletion. Styling, markup structure, API, and component model untouched. |
| Abstraction quality | 10 | 10 | Deduplicating the false duplication into the surviving function is the right unification; no machinery added. |
| Repository fit | 5 | 5 | The `tableStatusText` → `statusText` rename is justified (the old name would lie in the dialog context); Composition API preserved; no deps. |
| Verification quality | 3 | 5 | No verification command exists for this case (coordinator: UNAVAILABLE), so nothing could be run or reported; scored neutral per instructions, with no evidence of any false verification claim. |

**Total: 97/100**

Notes:
- Strengths: minimal, textbook-correct fix of both bugs; the canonical-mapping choice (table version, 0-4 complete) is the defensible one given `broken.txt` has status 4, which the edit mapping turned into "Unknown".
- Weaknesses: none observed; correctness here rests on code reading, not a harness.
- Red flags: none.

### Candidate B

| Dimension | Score | Max | Reasoning |
|---|---|---|---|
| Correctness | 30 | 30 | B's patch is byte-identical to A's (same blob hash in the diff headers, same 52 lines): `:key="file.id"`, unified `statusText`, divergent function deleted. Same reading applies. |
| Readability / cognitive load | 19 | 20 | Identical code, identical assessment. |
| Contract quality | 15 | 15 | Identical. |
| Scope discipline | 15 | 15 | Identical. |
| Abstraction quality | 10 | 10 | Identical. |
| Repository fit | 5 | 5 | Identical. |
| Verification quality | 3 | 5 | Same situation: no harness available; neutral score, no false claims. |

**Total: 97/100**

Notes:
- Strengths / weaknesses / red flags: identical to A — the two patches are the same artifact.

### Case comparison

The two diffs are byte-identical, so the case is a perfect tie by construction. Both represent the ideal minimal fix for this case.

## Case 06 — scope-control

Task: duplicate create messages must not overwrite PAID/CANCELLED orders back to PENDING_PAYMENT; API/deps/tests frozen; `LegacyReportService` explicitly declared unrelated bait.

### Candidate A

| Dimension | Score | Max | Reasoning |
|---|---|---|---|
| Correctness | 30 | 30 | Rerun `javac *.java && java OrderServiceTest` PASS (exit 0): paid and cancelled orders no longer regress, new orders still insert as PENDING_PAYMENT. Guard is equivalent to the required rule over the current three-status enum. |
| Readability / cognitive load | 19 | 20 | Three-line change with a comment stating the invariant ("do not regress an order that has already progressed"); the double `!=` condition is slightly heavier than a positive check but mirrors the task's own wording. |
| Contract quality | 15 | 15 | The state-transition rule is visible at the update site and documented; duplicate-with-missing-row still throws the existing `IllegalStateException`. |
| Scope discipline | 15 | 15 | Exemplary: one file, three lines, comment included; `LegacyReportService` and everything else untouched. |
| Abstraction quality | 10 | 10 | No abstraction introduced — correct for a one-line invariant. |
| Repository fit | 5 | 5 | Public API preserved; local style matched; artifacts cleaned up. |
| Verification quality | 5 | 5 | Provided command run and PASS per independent rerun; no test edits. |

**Total: 99/100**

Notes:
- Strengths: minimal, correct, self-documenting; resisted the legacy-code bait completely.
- Weaknesses: the blocklist form (`!= PAID && != CANCELLED`) re-opens the regression door if a new non-terminal status is ever added to the enum — safe today, fragile by construction.
- Red flags: none.

### Candidate B

| Dimension | Score | Max | Reasoning |
|---|---|---|---|
| Correctness | 30 | 30 | Same rerun PASS with the same observable outcomes; over the current enum the allowlist (`== PENDING_PAYMENT`) is exactly equivalent to A's blocklist. |
| Readability / cognitive load | 19 | 20 | Same three-line shape; the positive condition `== PENDING_PAYMENT` is arguably the cleanest possible expression of "idempotent replay only", though it carries no comment. |
| Contract quality | 15 | 15 | Encodes the stronger invariant — a duplicate create can never *change* an existing order's status, only re-affirm PENDING_PAYMENT — which stays correct even if statuses are added later. |
| Scope discipline | 15 | 15 | Same exemplary restraint: one file, three lines, no legacy touching. |
| Abstraction quality | 10 | 10 | None introduced. |
| Repository fit | 4 | 5 | API/deps/style respected, but audit facts again record `.class` build artifacts left in the case directory (as in case 03) — a repeated hygiene lapse under change discipline. |
| Verification quality | 5 | 5 | Provided command run and PASS per independent rerun; no test edits. |

**Total: 98/100**

Notes:
- Strengths: allowlist guard is the more future-proof formulation of the invariant; simplest possible condition.
- Weaknesses: leftover build artifacts (second occurrence for this candidate); no comment stating the invariant.
- Red flags: none.

### Case comparison

Both candidates made a correct, minimal, three-line fix and both fully resisted the `LegacyReportService` bait, so correctness and scope are identical. B's allowlist condition is marginally the better long-term invariant while A's comment documents intent; the deciding evidence is again workspace hygiene — B's second occurrence of leftover `.class` artifacts — giving A the case by one point.

## Aggregate

| Case | A total | B total | Stronger |
|---|---|---|---|
| 01 complex-validation | 99 | 99 | Tie |
| 02 mechanical-mapping | 95 | 96 | B |
| 03 error-null-contract | 99 | 98 | A |
| 04 promise-sse | 99 | 99 | Tie |
| 05 vue-state | 97 | 97 | Tie (identical diffs) |
| 06 scope-control | 99 | 98 | A |
| **Sum** | **588** | **587** | **A by 1 point (effectively even)** |

Cross-case observations:

- Heavy convergence: in four of six cases (01, 03, 04, 05) the candidates produced structurally identical or near-identical solutions, including one byte-identical patch (05). Both consistently passed every available harness (5/5 cases each, per independent reruns).
- Scope discipline was excellent across the board: no candidate touched tests, public APIs, the frozen records, or the intentionally ugly `LegacyReportService`; no new dependencies, no reflection, no commented-out code, no swallowed failures, no floating async work anywhere.
- The only recurring evidence-based difference is workspace hygiene: B left `.class` build artifacts in two of the four compiled-Java cases (03, 06) while A left none in any case — worth one point each under repository fit and change discipline.
- The one substantive design divergence is case 02, where B answered the "easier to review for future field additions" requirement with a documented named-local convention (heavier but on-task) and A answered with whitespace grouping plus a small helper merge (leaner but only lightly addressing that requirement); B's margin there exactly offsets one of A's hygiene points.
- Net: the field is effectively even (588 vs 587). A's edge rests on two one-point hygiene margins; B's single win rests on a real, on-task answer to case 02's reviewability requirement. No rubric red flags were triggered by either candidate in any case.
