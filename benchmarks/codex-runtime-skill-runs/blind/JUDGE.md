# Raw blind assessment

## Evidence and protocol

I assessed all twelve candidates independently using only `rubric.md`, `01/TASK.md` through `06/TASK.md`, and each case's `A.diff`, `B.diff`, `A.metadata.json`, and `B.metadata.json`. The diffs include full context for the changed files. I did not read other source files, test implementations, worker narratives, prior assessments, condition mappings, or outside resources. I did not run tests or execute candidate code. The verification evidence below is the independent Coordinator metadata, not a claim that I personally ran the checks or observed what each Worker ran.

Scores use the rubric's seven maximums: correctness 30, readability 20, contract 15, scope 15, abstraction 10, repository fit 5, and verification 5. A passing check establishes the reported check's result; it does not establish every untested behavior. Java test contents are absent from the allowed evidence, so their individual assertions and edge-case coverage are unknown. For the JavaScript case, metadata supplies three test names. For the Vue case, no runtime verification result was supplied.

Across all candidates, metadata reports no changes outside the case, no changes to tests or task files, no dependency-manifest changes, and no protected-file mismatches. All whitespace checks have exit code zero. CRLF warnings in some metadata are not whitespace failures or behavioral regressions. No candidate introduces reflection, a mapping framework, new architecture layers, or an unrelated source refactor.

| Case | A total | B total | Verdict |
| --- | ---: | ---: | --- |
| 01 | 99 | 100 | B |
| 02 | 100 | 100 | tie |
| 03 | 100 | 100 | tie |
| 04 | 100 | 96 | A |
| 05 | 96 | 96 | tie |
| 06 | 100 | 100 | tie |

Anonymous aggregate totals are A 595/600 and B 592/600. These totals do not identify experimental conditions.

## Case 01: behavior-preserving criteria normalization

The task authorizes a readability refactor without changing observable normalization or validation behavior. Both candidates change only the normalizer. Both preserve validation before trimming, the original rejection exception and code, the raw length/count rules, nullable prices, priority filtering/order, and the limit default/clamp. No demonstrated behavioral regression appears in either diff.

### Candidate A: 99/100

- **Correctness: 30/30.** The positive validity predicates are logical equivalents of the original invalidity conditions, including null short-circuiting and inclusive price ordering. Category validation still precedes trimming. Priority count and raw element length are checked before null/blank filtering. The existing priority transformation is retained, and nonpositive limits still become 4 before clamping to 1 through 20. Coordinator compilation and the named test pass.
- **Readability: 19/20.** Text, price, and priority validation now have meaningful names, and the limit default is named. The inline priority normalization remains short and locally understandable. The entry point still combines the validation decision, the priority transformation, and record assembly; it also negates three positive predicates. This is a good improvement, with slightly less explicit separation of phases than a dedicated validation boundary.
- **Contract: 15/15.** The code makes required category text, nullable optional text/prices, invalid ranges, empty priorities, and limit fallback/clamping understandable. Null and blank priorities remain accepted and removed only during normalization. This preserves the original contract rather than silently normalizing invalid raw input first.
- **Scope: 15/15.** Only readability changes and the named default are introduced in the authorized method's class. No unrelated code or tests change.
- **Abstraction: 10/10.** Three validation helpers reflect distinct rule groups; the limit helper reflects a separate normalization rule. No generic validation framework or extra class is introduced. Leaving the short list transformation explicit is reasonable.
- **Repository fit: 5/5.** Public APIs, existing Java stream style, exception behavior, and dependencies are preserved. Metadata shows one source file changed.
- **Verification: 5/5.** Metadata records `javac *.java && java RecommendationCriteriaNormalizerTest` passing with exit code zero and `PASS`. The test's exact boundary coverage is not supplied; the equivalence conclusions above also rely on inspecting the diff.

### Candidate B: 100/100

- **Correctness: 30/30.** Validation predicates preserve the original invalidity expressions and short-circuit ordering. Normalization of strings, priorities, and limits is unchanged. The newly named thresholds retain the original values. Coordinator compilation and the named test pass, and the diff shows no behavioral regression.
- **Readability: 20/20.** `normalize` now expresses two clear phases: validate and construct the normalized record. The invalidity predicates align directly with the rejection guard. Field-specific threshold names expose why equal numeric values may change independently. Priority and limit transformations have short, semantic helpers, keeping the entry point easy to scan.
- **Contract: 15/15.** `validate` clearly establishes the prerequisite for using the nonnull input. Raw lengths and count are validated before transformation; null/blank optional text becomes null, null priorities become an empty list, and the default/clamp semantics remain explicit.
- **Scope: 15/15.** All changes serve readability and maintainability of the normalizer. No API, test, dependency, or unrelated class changes occur.
- **Abstraction: 10/10.** Helpers follow real validation and transformation boundaries. The constants name independent domain limits; they are not a configuration system or a false unification of unrelated fields.
- **Repository fit: 5/5.** The existing public API and Java style are retained. No new source file, dependency, or gratuitous move/rename appears.
- **Verification: 5/5.** The required compilation and test command passed according to metadata. Exact test assertions are unavailable, so the pass is not being used as proof of every boundary case.

**Verdict: B, narrowly.** Both preserve behavior and remain focused. B makes the validation/construction phases and independent field limits slightly easier to review. The one-point difference is a readability judgment, not a correctness defect in A. Neither introduces unnecessary abstraction or scope creep.

## Case 02: explicit excluded-label mapping

Both candidates replace the erroneous empty excluded-label list with `copyStrings(query.excludedLabels())`. Both retain every other source getter, positional order, null-query impossible result, and defensive list-copy convention. The task explicitly asks for reviewable mapping; labels on constructor arguments are within scope.

### Candidate A: 100/100

- **Correctness: 30/30.** The missing field now follows the same null-to-empty and defensive-copy path as the other string lists. The null-query branch retains its previous values and `impossible=true`; nonnull queries retain `impossible=false`. No other mapping expression changes. Compilation and the required test pass.
- **Readability: 20/20.** Each previously compressed null/default constructor argument is on its own line with a field label. In the nonnull branch, getter names already expose the source mapping, and the opaque final boolean is labeled. This makes both the defaults and the repaired field easy to review without machinery.
- **Contract: 15/15.** Null query means an impossible empty criterion; null list input means an empty list; nonnull lists are copied. The newly mapped excluded labels inherit those existing semantics. The code keeps required and excluded lists visibly separate.
- **Scope: 15/15.** The field fix and constructor review aids directly satisfy the task. No model, test, helper behavior, or unrelated source changes occur.
- **Abstraction: 10/10.** Existing typed copy helpers are retained. No reflection, generic mapper, library, or extra layer replaces the straightforward constructor mapping.
- **Repository fit: 5/5.** Public records and method API remain unchanged. Explicit Java mapping and existing list ownership conventions are preserved; no dependency is added.
- **Verification: 5/5.** Metadata records the required Java compilation/test command passing with exit code zero and `PASS`. Test implementation is not available, so independent mutation/aliasing test coverage is unknown; the copy behavior itself is visible in the unchanged helpers.

### Candidate B: 100/100

- **Correctness: 30/30.** Excluded labels now use the same copy helper as corresponding string-list fields. All other mapping expressions and defaults are preserved, including both impossible-state values. Compilation and the required test pass; no demonstrated regression appears.
- **Readability: 20/20.** Every constructor position has an adjacent destination label in both branches. This produces consistent review aids for the long positional mapping. The labels repeat some source getter names, but they also make the intended destination explicit and introduce no control-flow complexity.
- **Contract: 15/15.** The null query/default contract, optional list normalization, defensive copying, and required-versus-excluded distinction remain explicit. The corrected field has the same ownership semantics as its peers.
- **Scope: 15/15.** Reformatting and labels support the requested future-field review. No unrelated cleanup, model changes, or test changes occur.
- **Abstraction: 10/10.** Mapping stays explicit with the existing small copy helpers. No prohibited mapping technique or unnecessary new abstraction is introduced.
- **Repository fit: 5/5.** Existing records, public method, dependencies, and copy conventions are preserved.
- **Verification: 5/5.** The required compilation/test command passed in metadata. The test's exact field and aliasing assertions remain unknown; no broader coverage claim is made.

**Verdict: tie.** Both fix the field and make the mapping reviewable. A relies on self-describing source getters in the nonnull branch; B labels both source-to-destination positions consistently. Neither approach creates a material quality disadvantage. Comments still do not make Java positional arguments compiler-checked by field name, but no ordering error is visible in either diff.

## Case 03: absence versus upstream failure

Both candidates make `ProfileService` return `Anonymous` only for a null result. Both change `UserRpcService` so null means a successful not-found response, while transport exceptions, absent responses, unsuccessful responses, and found-without-data responses throw `UpstreamUserServiceException`. Both add a cause-preserving constructor to the existing exception class. No candidate swallows upstream failure as successful absence.

### Candidate A: 100/100

- **Correctness: 30/30.** A missing user follows the guarded fallback without an NPE. The client invocation is the only operation inside the runtime-exception catch, so transport errors are wrapped with their cause and validation exceptions are not unnecessarily caught again. All malformed/failure reply paths remain distinguishable from not-found. The required compilation/test passes.
- **Readability: 20/20.** A short contract comment and sequential response guards show the complete decision tree locally. `ProfileService` retains a simple fallback expression. Error messages identify the failed condition and include the user ID, aiding diagnosis without adding a helper hierarchy.
- **Contract: 15/15.** Null is reserved for successful absence; every shown upstream failure path uses a distinct exception type. Caught transport exceptions retain their cause. Existing nonnull user nicknames are returned as before. Null/blank nicknames are not redefined by this task or diff.
- **Scope: 15/15.** All three changed files are directly involved in the failure/null contract. Adding the exception constructor is a focused requirement for preserving causes. No unrelated work occurs.
- **Abstraction: 10/10.** The existing service boundary and exception type express the semantic distinction. Explicit guards are clearer than a new result hierarchy or generic error adapter here.
- **Repository fit: 5/5.** The required `getDisplayName(long)` API is preserved, no dependency is added, and the existing exception is extended compatibly with a constructor overload.
- **Verification: 5/5.** Metadata records Java compilation and `ProfileServiceTest` passing. The precise tested response combinations are not shown; the other listed guards are assessed from the source diff.

### Candidate B: 100/100

- **Correctness: 30/30.** Successful absence returns null and therefore `Anonymous`; failures no longer collapse into null. Transport runtime exceptions retain their cause. Missing/unsuccessful/found-without-data replies throw the typed exception, and compilation/the required test pass. No demonstrated regression appears.
- **Readability: 20/20.** The comment and successive guards present the contract clearly. A small local message variable gives an understandable null/blank fallback for unsuccessful responses. The extra lines remain focused on a real failure-message policy rather than adding unnecessary structure.
- **Contract: 15/15.** Absence and failure are explicit and distinct. An unsuccessful upstream response uses its nonblank error message or a clear fallback; transport exceptions preserve causes. Existing nickname-return behavior is preserved for present users.
- **Scope: 15/15.** The profile guard, RPC response classification, and exception overload are all directly authorized by the business contract. No unrelated code changes appear.
- **Abstraction: 10/10.** Existing classes and exception semantics suffice. No new class, framework, or generic helper obscures the response decisions.
- **Repository fit: 5/5.** The public profile signature remains intact and no dependency is added. The exception overload is compatible with existing callers.
- **Verification: 5/5.** Metadata reports the required Java command passing. Assertions and edge-case coverage are not available, so the pass does not establish every response variation independently.

**Verdict: tie.** Both implement the same sound absence/failure distinction with cause preservation. A provides user-ID context; B provides a cleaner fallback for empty upstream error text. Both messages still identify failures adequately, so these differences do not warrant different scores. No material contract limitation or regression is demonstrated for the requested behavior.

## Case 04: async SSE completion and request ownership

Both candidates await the returned SSE promise, keep loading set until that promise settles, capture the actual AbortController globally, abort it in `closeSSE`, suppress error UI for intentional abort, and avoid clearing a newer controller from an older request's finalizer. A additionally supplies an `onerror` callback that throws the supplied error. Metadata reports the same three passing checks for both: waiting for promise completion, reporting async failure, and aborting the active request.

### Candidate A: 100/100

- **Correctness: 30/30.** The awaited promise connects asynchronous completion/failure to the outer try/catch/finally. `closeSSE` now aborts the real request signal. The explicit error callback requests failure propagation at the SSE callback boundary. All three required tests pass and no task regression is demonstrated.
- **Readability: 20/20.** `activeController` names the owned resource accurately; capture, await, catch, and release are easy to follow. The identity check makes the reason for conditional cleanup visible. The short `onerror` callback states its failure policy directly.
- **Contract: 15/15.** Normal completion and rejection drive outer loading state; an intentional abort does not become an error message. The abort handle is the same controller passed to the request. The explicit error callback supplies a fatal-error policy instead of leaving that choice implicit in the injected provider.
- **Scope: 15/15.** Awaiting completion, error routing, abort ownership, and guarded cleanup directly address the two production bugs. Only the requested file changes; exported names and UI callback hooks remain intact.
- **Abstraction: 10/10.** One accurately named shared handle and ordinary try/catch/finally solve the local task. No new session class, event framework, or generic lifecycle layer is introduced.
- **Repository fit: 5/5.** Both exported function names and callback-style hooks are preserved, and AbortController already existed in the original code. There are no new dependencies.
- **Verification: 5/5.** Metadata records `node --test chat-session.test.mjs` passing all three named tests. It does not include a named SSE `onerror` test, overlapping-request test, or multiple-component isolation test. The onerror policy is visible in code; runtime integration with a specific provider is not independently demonstrated by the packet.

### Candidate B: 96/100

- **Correctness: 28/30.** All demonstrated promise-completion, promise-rejection, and active-abort behavior is repaired and the three tests pass. The candidate does not explicitly route an SSE error callback to failure, leaving the production SSE-error part of the task dependent on the injected provider rejecting its returned promise. The packet does not establish that guarantee. This modest deduction reflects incomplete demonstrated error-path handling, not a claim that an additional test failed.
- **Readability: 20/20.** Resource capture, awaited completion, abort suppression, and identity-checked cleanup are short and direct. The renamed handle accurately matches the object being managed.
- **Contract: 13/15.** The returned-promise and abort contracts are clear. There is no explicit policy for an SSE provider that handles errors through callbacks and keeps its promise pending, for example by retrying. Under such provider semantics the outer error state would not be updated by this code at the callback failure. That is a conditional contract limitation; the provider implementation is not included in the evidence.
- **Scope: 15/15.** Every change is directly related to completion, failure propagation, cancellation, or releasing the active handle. No unrelated cleanup or API change appears.
- **Abstraction: 10/10.** The local controller and ordinary async control flow are appropriate. No unnecessary abstractions or dependencies are introduced.
- **Repository fit: 5/5.** Existing exports and UI callback signatures remain unchanged. The code uses the AbortController already present in the original implementation.
- **Verification: 5/5.** The exact required command passed all three named tests. Their names demonstrate returned-promise failure coverage, not separate SSE callback/retry behavior. No failed test or incorrect verification claim is shown.

**Verdict: A.** A provides an explicit SSE error-boundary policy in addition to the shared promise and abort fixes. B's simpler diff is clear and passes all supplied checks, but does not establish propagation for provider-managed SSE error events. This finding is about the visible contract and its coverage limit, not an invented provider-specific regression.

**Shared limitation, outside a demonstrated regression:** the module holds one controller, so simultaneous calls retain cancellation ownership only for the latest request. Although an older finalizer cannot erase that latest handle, each finalizer still calls `setLoading(false)` unconditionally. Overlapping calls sharing loading state could therefore clear loading while another request remains pending. The task and tests do not establish overlapping-call or multi-component requirements; I do not treat this as a newly demonstrated regression or penalize one candidate relative to the other.

## Case 05: Vue status consistency and logical row identity

The two diffs are identical. Both use `file.id` as the row key, remove the unused loop index, and use one status function for the table and dialog. The existing table's numeric-code mapping and unknown fallback are retained; the conflicting dialog mapping is removed. No styling, unrelated markup, API call, or backend code is changed.

### Candidate A: 96/100

- **Correctness: 30/30.** Both views now necessarily use the same code-to-text function. Stable logical file IDs replace positional keys, allowing Vue to associate a row/input with its file when positions change. The supplied sample IDs are distinct. No behavioral regression is visible in the diff; runtime verification is considered separately below.
- **Readability: 20/20.** `statusText` clearly names the shared operation, and its five explicit branches plus unknown fallback are locally understandable. The template calls it consistently. The loop contains only the variable it uses.
- **Contract: 15/15.** Backend numeric codes are preserved, the shared display mapping is explicit, and logical identity is expressed by `file.id`. The task requires consistent display, and does not provide external evidence redefining the actual business names of those codes. Missing or duplicate IDs are not demonstrated in the packet.
- **Scope: 15/15.** Only the duplicated status behavior and positional row identity are changed. Classes, controls, other markup, Composition API state, and API behavior remain intact.
- **Abstraction: 10/10.** Sharing status text reflects exactly one common mapping. No enum framework, generic configuration object, extra component, or invented identity layer is introduced.
- **Repository fit: 5/5.** Vue 3 `script setup` and `ref` usage, existing formatting, backend codes, and dependency set are retained.
- **Verification: 1/5.** Metadata explicitly says no verification command/result was provided. Whitespace and protected-file checks supply limited change-hygiene evidence, but no Vue compilation, browser observation, or insert/remove/reorder interaction check is documented. This is a material verification limitation, not evidence of a failed UI.

### Candidate B: 96/100

- **Correctness: 30/30.** The common status function removes view disagreement, and the file-ID key supplies logical identity across row position changes. Supplied IDs are distinct and no visible regression appears. The identical diff supports the same assessment as A.
- **Readability: 20/20.** The shared function name and explicit mapping are clear; both call sites are readily visible. Removing the unused index simplifies the loop without unrelated compression.
- **Contract: 15/15.** Numeric codes, the existing table mapping, and the unknown fallback remain explicit. The row key communicates file identity. External backend label truth and behavior with missing/duplicate IDs are not established by the packet.
- **Scope: 15/15.** Changes address exactly the two requested bugs and leave styling, unrelated markup, API calls, and Composition API structure intact.
- **Abstraction: 10/10.** One semantic display function removes actual duplication. No unnecessary layers, framework, or new component is introduced.
- **Repository fit: 5/5.** Existing Vue 3 structure, source style, and dependencies are preserved.
- **Verification: 1/5.** No runtime or compilation verification was supplied. Metadata records successful whitespace/protected-file hygiene checks only; those cannot establish DOM input identity during reordering.

**Verdict: tie.** The source changes are identical and focused, and the metadata provides the same verification limitation. Both fixes are supported by source inspection; neither has demonstrated runtime UI coverage.

## Case 06: duplicate create messages preserve order state

Both candidates remove the unconditional duplicate-path write to `PENDING_PAYMENT` and replace it with a comment explaining status preservation. Both retain new-order insertion and the invariant failure when a duplicate-key response cannot be reconciled with an existing order. The comments are replacement explanations, not commented-out old code.

### Candidate A: 100/100

- **Correctness: 30/30.** An existing paid or cancelled order can no longer be overwritten by this duplicate branch because the branch performs no status write. New orders are still inserted as pending. The missing-order invariant still throws rather than silently succeeding. Compilation and the required test pass; no regression is visible.
- **Readability: 20/20.** The existing short control flow remains unchanged, and the comment explains why a duplicate with an existing order requires no mutation. The removed write was the defect; additional branching would obscure this idempotent behavior.
- **Contract: 15/15.** First creation establishes pending state; duplicate creation preserves the existing state. An inconsistent duplicate lookup remains a failure. The state contract is clear from the insert, guarded lookup, and explanatory comment.
- **Scope: 15/15.** Exactly the faulty status write is removed. The intentionally unrelated report service is untouched, and metadata identifies only `OrderService.java` as changed.
- **Abstraction: 10/10.** No helper or new layer is needed for removing one incorrect write. Keeping the focused duplicate branch explicit is appropriate.
- **Repository fit: 5/5.** Public API, repository calls other than the faulty update, exception behavior, and dependencies are preserved.
- **Verification: 5/5.** The required Java compilation/test command passed with `PASS` and exit code zero. Specific status assertions in the test are unavailable; preservation of every existing status in this branch is directly supported by removal of its write.

### Candidate B: 100/100

- **Correctness: 30/30.** The duplicate branch now leaves all existing statuses intact, including paid and cancelled, while preserving initial pending insertion and the missing-order exception. Compilation and the required test pass. No regression is demonstrated.
- **Readability: 20/20.** The brief comment states the no-update intent in the already short catch branch. Removing an adjacent blank line does not materially change clarity.
- **Contract: 15/15.** Duplicate create is state-preserving; first create remains pending; inconsistent duplicate lookup still fails. No failure is swallowed as successful absence.
- **Scope: 15/15.** Only the defective write and a directly adjacent blank line change. The unrelated report service is not edited. Generated `.class` files listed as excluded from the diff are test/build artifacts, not additional source refactoring or source-scope violations.
- **Abstraction: 10/10.** The simple deletion needs no new abstraction. The candidate avoids generic idempotency machinery or speculative status predicates.
- **Repository fit: 5/5.** Public methods, dependencies, and the existing repository/invariant structure remain unchanged.
- **Verification: 5/5.** Metadata records the required Java command passing. Test assertions are not included in the packet; source inspection independently establishes that this branch cannot reset an existing status by calling `updateStatus`.

**Verdict: tie.** The behavior and quality are equivalent. The difference in wording and blank-line placement has no scoring significance. Neither performs scope creep or unnecessary abstraction.

## Protocol closeout

All six cases and all twelve candidates have been fully assessed before producing outputs. No condition reveal was requested or sought. Only the two new output files, `JUDGE.md` and `scores.json`, are written in this packet. Protocol deviations: none.