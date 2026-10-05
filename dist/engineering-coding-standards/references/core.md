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

### Remove cognitive noise

Do not keep structure that makes readers search for meaning that is not there.

In touched code:

- remove parameters or branches that do not affect behavior;
- avoid call sites with multiple raw booleans when their meanings are not obvious;
- prefer names, enums, or semantically distinct helpers when they make the choice clear without creating unnecessary machinery;
- choose data structures that express the real intent: use a set for membership/existence, and a map when the value itself matters;
- keep helper side effects visible. Avoid surprising mutation of caller-owned objects, especially when the same object is reused later under a different assumption.

Do not introduce a new options type or abstraction merely to avoid one understandable boolean. The goal is lower cognitive load, not more types.

### Preserve semantic structure

When data naturally contains multiple named pieces of meaning, keep that structure visible instead of encoding it into an incidental representation only to decode it later.

Prefer an existing domain type or a small local value type when it makes the relationship explicit.

Be suspicious of:

- delimiter-packed strings used as temporary multi-field values;
- positional arrays/tuples whose indexes carry hidden meaning;
- generic maps such as `Map<String, Object>` used only to avoid defining a clear shape.

Encoding is appropriate at a real boundary or protocol when the contract requires it. Keep that encoding localized and decode once near the boundary.

Do not introduce a type for a single obvious primitive merely to satisfy this rule.

### Split by semantic phase, not line count

A method is a review trigger when understanding it requires tracking several independent phases, responsibilities, or mutable accumulators at once.

When extraction genuinely helps, split around meaningful phases such as:

- load;
- classify;
- aggregate;
- transform;
- assemble.

Keep orchestration readable and keep each extracted step meaningful. Do not fragment a coherent algorithm into tiny pass-through helpers merely to shorten a method.

## Naming and local organization

Names should reveal the domain concept, responsibility, or behavior they represent.

- Prefer clear, specific names over vague catch-all labels such as `misc`, `common2`, `helper`, `manager`, or `utils` when a more precise concept exists.
- Prefer full words over uncommon abbreviations. Keep standard domain/protocol abbreviations when they are already conventional and unambiguous.
- Name behavior by intent, not by incidental implementation detail.
- Follow the repository's established naming, package/directory, and module conventions when they are reasonable.
- Before creating a new package, directory, module, or organizational layer, inspect nearby structure and match its granularity.
- Do not introduce a second competing organization style inside one codebase merely because another style is personally preferable.
- Do not create a new package/module boundary only to make the tree look tidy. A new boundary should group a coherent responsibility or build unit already justified by the task.
- If a name needs a comment to explain what the thing is, first consider whether the name itself can be clearer.

Do not rename unrelated existing packages, modules, files, classes, or public APIs just to normalize style.

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
