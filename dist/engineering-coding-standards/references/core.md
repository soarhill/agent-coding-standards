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
