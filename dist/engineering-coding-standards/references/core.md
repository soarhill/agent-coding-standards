# Core

## Scope first

Change only what the task authorizes.

Unrelated legacy code, nearby style issues, duplication elsewhere, or a review trigger do not expand scope.

## Preserve truthful contracts

- Never dereference a nullable value without a real non-null guarantee.
- Do not silently turn an unexpected failure into normal success, null, false, zero, or empty data unless that fallback is the intended contract.
- Preserve causal information when translating unexpected failures.
- Keep absence, unknown state, failure, and cancellation distinct when the domain treats them differently.
- One state/code/field should keep one stable meaning inside the same contract.
- Unknown external/protocol values need an intentional policy.

Do not mechanically replace every nullable result with Optional or an empty object. Choose the representation that makes the local contract clearest without unnecessary blast radius.

## Make async and cleanup honest

- Expose the real async completion/failure signal when callers rely on it.
- Do not report completion while the real operation is still running.
- Treat cancellation/abort as its own lifecycle outcome according to the actual library/API contract.
- Do not accidentally surface expected cancellation as a user-visible failure.
- Cleanup must operate on the real owned resource or handle.
- When state is nested, decide whether cleanup means clear or restore the previous value.

## Optimize for reviewability

Prefer code that makes important decisions visible:

- validation rules;
- state transitions;
- defaults and fallbacks;
- units and precision;
- data mapping;
- failure behavior.

A longer explicit implementation can be better than a shorter clever one.

## Comment discipline

Prefer code that explains **what it does** through naming and structure. Use comments for information the code cannot express clearly on its own.

Add or preserve comments when they explain:

- why a non-obvious decision exists;
- a business rule or invariant that is easy to accidentally break;
- boundary, failure, concurrency, ownership, or lifecycle reasoning;
- an assumption imposed by an external API, protocol, framework, or compatibility constraint;
- an intentional workaround whose simpler-looking alternative would be wrong.

Do not add comments that merely narrate obvious code. Avoid comments such as “check whether user is null”, “loop through the list”, or numbered step comments that only restate the implementation.

Do not use comments to compensate for poor naming or structure. Remove obsolete commented-out implementations instead of keeping dead code as history.

When behavior changes, update or remove comments that are no longer true.

Follow the repository's existing convention for comment language, Javadoc/JSDoc usage, and comment style. If no clear language convention exists, use the language that communicates the reasoning most clearly to the expected maintainers. Keep identifiers, API/protocol names, technical keywords, and proper nouns in their original form.

## Abstract meaning, not shape

Extract a helper/type/layer when it represents a real shared concept or shared reason to change and reduces cognitive load.

Do not extract merely because:

- code appears twice;
- a method is long;
- a condition is large;
- many fields are mapped;
- a framework pattern is available.

Avoid universal helpers controlled by flags, reflection-based mapping, or one-off framework machinery when explicit code is easier to review.

## Respect local conventions

Follow reasonable repository conventions for naming, DTO/VO vocabulary, null-check style, constructors, response envelopes, comments, and formatting.

Change convention only when it creates a real correctness or maintenance problem.
