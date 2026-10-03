# Benchmark evaluation — per-case comparison of the 12 anonymized runs

Method notes (apply uniformly to every run):

- Case assignment is read from each diff's file paths: Case 01 → R01, R12; Case 02 → R04, R11; Case 03 → R03, R05; Case 04 → R06, R08; Case 05 → R07, R10; Case 06 → R02, R09. Each case has exactly two runs, consistent with the stated structure.
- Rubric dimension 7 (Verification quality) is **not available to judge** for all 12 runs: the workspace contains diffs only — no test output, no run transcripts — so no run can be credited or penalized for verification. All totals below are therefore out of **95** (dimensions 1–6). No diff contains a verification claim, so none is penalized for over-claiming either.
- Runs are compared on quality only. No condition labels are assigned and none are inferred.
- All scores are grounded in the per-run files `scores/R01.md` … `scores/R12.md`, which quote the diff evidence.

## Totals summary (out of 95; dimension 7 excluded as not judgeable)

| Run | Case | 1. Correct /30 | 2. Readab. /20 | 3. Contract /15 | 4. Scope /15 | 5. Abstr. /10 | 6. Repo fit /5 | Total /95 |
|-----|------|----|----|----|----|----|----|----|
| R01 | 01 | 30 | 19 | 14 | 15 | 10 | 5 | **93** |
| R12 | 01 | 30 | 18 | 12 | 14 | 10 | 5 | **89** |
| R04 | 02 | 30 | 14 | 11 | 13 | 5 | 5 | **78** |
| R11 | 02 | 30 | 18 | 14 | 14 | 10 | 5 | **91** |
| R03 | 03 | 30 | 18 | 15 | 15 | 10 | 5 | **93** |
| R05 | 03 | 30 | 18 | 13 | 15 | 10 | 5 | **91** |
| R06 | 04 | 30 | 19 | 14 | 15 | 10 | 5 | **93** |
| R08 | 04 | 26 | 18 | 11 | 15 | 10 | 5 | **85** |
| R07 | 05 | 30 | 18 | 12 | 15 | 10 | 5 | **90** |
| R10 | 05 | 30 | 19 | 14 | 15 | 10 | 5 | **93** |
| R02 | 06 | 30 | 18 | 13 | 15 | 10 | 5 | **91** |
| R09 | 06 | 30 | 18 | 13 | 15 | 10 | 5 | **91** |

---

## Case 01 — complex validation refactor: R01 vs R12

**Correctness — tie (30/30 both).** Both preserve observable behavior exactly. R01 keeps the negative-form predicate structure (`isInvalid` with three domain helpers); R12 inverts to positive form (`isValid`, `areValidPrices`, `isNonNegative`, `areValidPriorities`). Both limit rewrites were verified equivalent to `Math.max(1, Math.min(MAX_RESULTS, limit <= 0 ? 4 : limit))`. Neither touches tests, records, or the public signature.

**Readability — R01 slightly ahead (19 vs 18).** Both name all magic numbers and split validation by domain. R12's positive form reads like a specification, but it spreads one rule across up to three helper levels (`isValid` → `areValidPrices` → `isNonNegative`); R01's three helpers each sit one level below `normalize`.

**Contract quality — R01 ahead (14 vs 12).** R01 documents the public method itself: `* Validates the raw input and returns its canonical form... @throws BusinessException with {@code INVALID_CRITERIA} when any rule fails`, plus the subtle `// Length limits apply to the raw values, before trimming.` R12 documents only the limit subtlety (`// The default (4) is within bounds, so no lower clamp is needed.`) and leaves the public contract undocumented, to be reconstructed from four helpers.

**Scope — R01 slightly ahead (15 vs 14).** R01 restructures only the validation/normalization logic. R12 additionally rewrites the untouched `trimToNull` into a ternary — harmless nearby cleanup, one point off.

**Abstraction / repo fit — tie (10/10, 5/5).** Neither introduces types; both keep the file's static-helper style.

**Net:** R01 93, R12 89. Close runs; R01 wins on documented public contract and slightly flatter helper structure, R12's positive-form validation is its main counter-attraction.

---

## Case 02 — mechanical mapping: R11 vs R04

This is the sharpest contrast in the suite, and per the rubric's comparison note ("sometimes the simpler change should win"), the simpler run wins here.

**Correctness — tie (30/30 both).** Both fix the actual defect (`excludedLabels` was mapped to `List.of()` instead of the query's list) and preserve the null-query branch exactly (all-null scalars, empty lists, `impossible` flag: R04 `.impossible(true)`, R11 `absent()` with `true`). Both keep the records and the explicit per-field mapping; no reflection/deps.

**Readability — R11 ahead (18 vs 14).** R11 gives every mapping line a name (`List<String> excludedLabels = copyOf(query.excludedLabels());`) at a cost of zero new structure. R04's `from` also reads as named builder lines, but adds ~110 lines of fluent-setter boilerplate and a `build()` that is still a 17-argument positional call — the hazard is moved from `from` into `build`, and the reviewer now cross-checks two ordered lists instead of one.

**Contract quality — R11 ahead (14 vs 11).** Both document the null-query fallback. R11's comments are accurate. R04's Javadoc claims `the compiler points at the latter until it matches the record`, which is only true for arity/types — same-typed components (e.g. `minRent`/`maxRent`, or any two list fields) can still be transposed in `build()` and compile — an overstated safety claim in a change whose purpose is review safety.

**Scope — R11 ahead (14 vs 13).** R11's only extra is merging the two pre-existing copy helpers into `copyOf` (both call sites already being edited). R04's builder is confined to the task's file and serves the task's stated reviewability goal, but its size brushes against "refactor beyond what the task needs".

**Abstraction — R11 well ahead (10 vs 5).** R11's `absent()` and two-line `copyOf` reflect real concepts. R04's hand-rolled 17-field builder for a single construction site means every future field costs four coordinated edits (field, setter, mapping line, build argument) versus one — machinery exceeding the problem, though not a false abstraction.

**Repo fit — tie (5/5).** Both avoid dependencies/reflection and keep formatting consistent.

**Net:** R11 91, R04 78. Both are behaviorally correct; the difference is entirely how much structure was purchased for the same reviewability goal.

---

## Case 03 — error/null contract: R03 vs R05

**Correctness — tie (30/30 both).** Both eliminate the `null`-collapse (`return null` on failure with `System.err.println` swallow) by throwing `UpstreamUserServiceException` on transport failure, null/unsuccessful response, and found-but-no-data (the literal NPE source), while mapping genuine not-found to `"Anonymous"` in `ProfileService`. Both preserve `getDisplayName(long)`, add only a cause-preserving exception constructor, and change no tests.

**Contract quality — R03 ahead (15 vs 13).** R03 encodes the tri-state in types: `Optional<UserDto> findUser(...)` with `Optional.empty()` for absence, exception for failure, Javadoc on both methods, and `Never returns null.` — the conflation the case punishes becomes unrepresentable at this boundary. R05 keeps `UserDto`/null with an accurate Javadoc (`Returns the user, or null only when the upstream service confirms the user does not exist`) and a consumer-side comment, so the contract is understandable — full rubric threshold — but still enforced only by documentation. One nuance in R03's favor on robustness, one in R05's: R03 maps a null `nickname` to `"Anonymous"` (documented); R05 would still return null there, same as the original layer behavior; the stated contract covers neither.

**Scope / blast radius — R05 slightly safer (15 vs 15 as scored, with a caveat noted).** Both touch the same three files, all load-bearing. R05 leaves `findUser`'s public signature unchanged; R03 changes its return type to `Optional<UserDto>`, which is the cleaner contract but a wider public-surface change. Whether other callers exist is not available to judge from the diffs; the task pins only `getDisplayName`, so neither is out of bounds, and both received 15.

**Readability — tie (18/18).** Both are flat guard sequences with clear Javadoc; R05's densest moment is a three-call `response.errorMessage()` ternary; R03's is the two-file Optional hop.

**Abstraction / repo fit — tie (10/10, 5/5).** `Optional` is platform vocabulary, not invented machinery; R05 adds nothing at all.

**Net:** R03 93, R05 91. Both fully solve the contract; R03's type-encoded absence is the stronger contract, R05's smaller signature footprint is the more conservative fit. The gap is contract encoding only.

---

## Case 04 — promise/SSE: R06 vs R08

**Correctness — R06 ahead (30 vs 26).** The two runs make the same two core fixes: `await fetchEventSource(...)` so completion/rejection drives `finally`/`catch`, and replacing the dead, never-assigned `let eventSource` with the real handle (`activeController = controller` on send; `controller.abort()` in `closeSSE()`), both with the same stale-slot guard (`if (activeController === controller) { activeController = null; }`). The difference is the abort edge: R06 classifies an abort triggered by `closeSSE()` as intentional shutdown — `// An abort triggered by closeSSE() is an intentional shutdown, not a UI error.` guarding `if (!controller.signal.aborted) { setError(error.message); }` — while R08 sets `setError(error.message)` unconditionally, so closing the component mid-request surfaces an `AbortError` message in the error state. Both stated bugs are fixed in both runs; R08 retains one small edge-case problem (rubric band between 30 and 20, scored near 30).

**Contract quality — R06 ahead (14 vs 11).** R06 makes all three contracts of this case explicit: async completion observable (await), cleanup owns the real handle (abort of the wired controller), and intentional-shutdown-vs-failure distinguished. R08 meets the first two but conflates the third — after R08, error state means either "request failed" or "user closed", which is exactly the state-meaning ambiguity the case is about.

**Readability — R06 slightly ahead (19 vs 18).** Near-identical diffs; R06's abort comment documents the one non-obvious branch, R08 has no corresponding intent note.

**Scope, abstraction, repo fit — tie (15/15, 10/10, 5/5).** Both confine themselves to `chat-session.js` and the broken paths, use no new abstraction, and keep exported names and callback hooks.

**Net:** R06 93, R08 85. Same architectural fix; the separation is one guarded branch and its comment.

---

## Case 05 — Vue state: R10 vs R07

**Correctness — tie (30/30 both).** Both fix bug 2 identically (`:key="index"` → `:key="file.id"`, dropping the unused index binding) and fix bug 1 by deleting the shifted duplicate mapper (`editStatusText`: `0 'Pending', 1 'Vectorizing', 2 'Completed', 3 'Failed'`) in favor of a single source of truth carrying the full table range (`0 'Uploading' … 4 'Failed'`). Neither touches codes, APIs, or styling. Both share the same unverifiable assumption (a stable unique `file.id`, inferred from domain shape; not visible in diff context).

**Contract quality — R10 ahead (14 vs 12).** R10 writes the data contract down where it is enforced:

```
// Single authoritative mapping of backend file status codes to display text,
// shared by the table and the edit dialog.
// Backend contract (codes must not change): 0 Uploading, 1 Pending, ...
```

R07 unifies correctly but leaves the code→label meaning implicit in bare `0/1/2/3` literals of the surviving if-chain.

**Readability — R10 slightly ahead (19 vs 18).** R10's one-line-per-code map is the clearest form of this contract; its cost is renaming the surviving mapper (`statusText`), a moment of template re-orientation. R07 is the more minimal diff and keeps the file's existing style.

**Scope — R07's virtue, both 15.** R07 changes exactly two expressions plus a deletion; R10's extra surface (map + renamed accessor) is still confined to the two bugs' shared cause.

**Abstraction / repo fit — tie (10/10, 5/5).** R10's map is data, not machinery; R07 adds no abstraction at all.

**Net:** R10 93, R07 90. Both clean fixes; R10 pays three small extra lines for a documented contract and a table-driven mapping, R07 wins on minimal footprint. This is the closest "style over minimalism" trade in the suite and neither run is penalized for its choice of emphasis.

---

## Case 06 — scope control: R09 vs R02

**Correctness — tie (30/30 both), with a structural difference in coverage.** R09 deny-lists the two named states:

```java
if (existing.getStatus() == OrderStatus.PAID
        || existing.getStatus() == OrderStatus.CANCELLED) {
    return;
}
```

R02 allow-lists the writable state:

```java
if (existing.getStatus() == OrderStatus.PENDING_PAYMENT) {
    repository.updateStatus(orderNo, OrderStatus.PENDING_PAYMENT);
}
```

Against the task as worded ("already-paid or cancelled must not be overwritten"), both are correct, and both preserve the idempotent re-assert while still pending. They diverge only for hypothetical other non-pending statuses (unknown from the diffs — not available to judge): R02 would protect them, R09 would not. Neither divergence is observable against the stated requirement, so no correctness separation.

**Contract quality — tie (13/13).** R09's comment states the policy as terminality ("never regress an order that already reached a terminal state (paid or cancelled)"); R02's states it as progression ("must not regress an order that has already moved past PENDING_PAYMENT… only re-assert the pending state while the order is still awaiting payment"). R02's allow-list is the safer default for future statuses (protection does not depend on remembering to extend a list), R09's is the more literal transcription of the requirement; these offset.

**Readability — tie (18/18).** Both are one guard with a three- or two-line intent comment; early-return (R09) and wrap-in-condition (R02) are equally readable at this size.

**Scope — tie (15/15).** Both make a single-hunk change in `OrderService.java`; the intentionally unrelated `LegacyReportService.java` bait is untouched in both.

**Abstraction / repo fit — tie (10/10, 5/5).** No abstraction, no API change, no dependencies in either.

**Net:** R02 91, R09 91 — a genuine tie. The only substantive difference (deny-list vs allow-list) is not decidable from the provided material because the full `OrderStatus` enum is not visible in either diff.

---

## Cross-case observations

- **No automatic red flags fired in any of the 12 runs.** No out-of-case edits, no edits to tests, no added dependencies, no reflection/BeanUtils-style mapping, no commented-out old code, no swallowed failures, no floating async work left behind. The `LegacyReportService.java` bait (case 06) was untouched by both of its runs.
- **All 12 runs solve their case's primary defect** (dimension 1 ≥ 26 everywhere). The score spread comes almost entirely from dimensions 2–5: how much structure was added for the same behavioral result (case 02 is the extreme: 30/30 correctness in both runs, 13-point total gap), whether contracts are documented or type-encoded (cases 01, 03, 05), and edge-classification discipline (case 04's abort handling).
- **Uniform limitation:** verification quality could not be assessed for any run (diffs only, no transcripts); totals are out of 95 accordingly.
