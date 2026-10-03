# Candidate Coding Rules v1.1

> Status: **externally validated candidate standard, not final Skill**
>
> v1.1 merges:
> - two independent reviews of four tutorial codebases;
> - the two cross-reviews;
> - second-stage validation against official documentation and multiple independent mature repositories;
> - first-round A/B Agent calibration (GLM-5.3, 6 cases × baseline/standards).
>
> The benchmark caused only a narrow async-cancellation clarification. Other rules were intentionally left unchanged to avoid overfitting one model and one small suite.
>
> **Observed ≠ Recommended. Popular ≠ Correct.**
>
> A project example can prove that a pattern exists. A framework document can prove a mechanism. Neither automatically proves that one implementation is the only maintainable choice.

---

# 1. How to read this document

## 1.1 Rule strength

### MUST

A correctness, contract, lifecycle, or high-confidence safety invariant.

A MUST applies to code created or changed by the agent unless the task or repository establishes a real exception.

### SHOULD

A strong maintainability default.

A SHOULD may be overridden when the local design is clearer, the repository has a justified convention, or the task has constraints that make the default inappropriate.

### REVIEW TRIGGER

A signal to inspect code more carefully.

A trigger does **not** authorize a refactor and does **not** mean the code is wrong.

### CONVENTION

A team/repository consistency choice, not universal engineering truth.

## 1.2 Evidence state is separate from rule strength

Rule strength answers **how strongly the agent should obey the rule**.

Evidence state answers **how confident we are in the basis for the rule**.

Evidence states used here:

- **VERIFIED_MECHANISM** — language/framework/library contract confirmed from official sources.
- **VALIDATED_PRACTICE** — supported by official guidance plus multiple independent codebases, but still a design default.
- **MAINTAINABILITY_CANDIDATE** — strong engineering reasoning, but not a mechanism-level fact.
- **CONVENTION_ONLY** — consistency choice.
- **UNCALIBRATED_TRIGGER** — useful review signal whose numeric threshold or false-positive rate has not yet been calibrated on agent tasks.

## 1.3 Scope guardrail

These standards **do not expand task scope by themselves**.

Default behavior:

- apply rules to new code, modified code, and directly related paths;
- if unrelated legacy code violates a rule, note it rather than silently cleaning the repository;
- a REVIEW TRIGGER means “inspect this area,” not “start a refactor”;
- repository-local conventions win when they are reasonable and do not create a correctness or maintainability defect;
- architecture redesign is out of scope unless the task explicitly asks for it.

This scope rule is as important as the coding rules themselves.

---

# 2. MUST — hard invariants

## M-01. A nullable value needs a valid non-null guarantee before dereference

**Evidence:** VERIFIED_MECHANISM

A non-null guarantee may come from:

- the type system;
- validated input;
- an earlier guard;
- a proven control-flow branch;
- a framework contract that is actually active.

Do **not** add redundant null checks merely to satisfy the rule.

What is forbidden:

- dereference first, check later;
- check a nearby object but dereference a different one;
- assume a framework contract that is not actually active.

---

## M-02. Validation must be active on the real execution path

**Evidence:** VERIFIED_MECHANISM

For Java/Spring validation:

- use validation annotations from the intended validation/Spring packages;
- confirm the execution path actually activates validation;
- distinguish MVC parameter validation from Spring method validation;
- remember that class-level method validation depends on Spring's validation/proxy mechanism;
- container validation and element validation are separate concerns;
- DTO shape validation does not replace database/business-state/authorization validation.

For generated or edited validation code, import origin is part of the review.

---

## M-03. Do not silently turn an unexpected failure into a normal success

**Evidence:** VERIFIED_MECHANISM + MAINTAINABILITY_CANDIDATE

A catch/recovery branch must have an intentional contract:

- recover using a documented fallback;
- translate while preserving the cause;
- isolate a permitted partial failure;
- or propagate.

Do not make an unexpected failure indistinguishable from:

- `null`;
- `0`;
- `false`;
- an empty result;
- a success response.

A documented display fallback, best-effort branch, or deliberately degraded result is allowed.

---

## M-04. Preserve causal information when translating unexpected failures

**Evidence:** VERIFIED_MECHANISM

When an unexpected failure is translated:

- retain the original cause when the language/platform supports causal chaining;
- do not replace a useful exception with a generic one that erases why it happened;
- preserve interruption semantics where the platform requires it.

How the failure is printed/logged is a separate logging rule, not part of this invariant.

---

## M-05. Async completion, failure, and cancellation contracts must be truthful

**Evidence:** VERIFIED_MECHANISM + FIRST_ROUND_CALIBRATION

If a caller is expected to wait for completion or catch failure, the function must return/await/propagate the **real** asynchronous operation.

Do not:

- create a floating Promise and assume outer synchronous `try/catch` handles its later rejection;
- wrap an existing Promise without correctly resolving/rejecting;
- pass the result of an assignment/expression to `.finally(...)` when a callback is required;
- report completion before the underlying async work is complete.

Treat **cancellation/abort as a distinct lifecycle outcome**:

- decide whether it means expected shutdown, user cancellation, retryable interruption, or failure for this API;
- follow the actual library/API settlement contract instead of assuming every abort rejects or every abort resolves;
- do not accidentally surface an expected lifecycle cancellation as a user-visible request failure;
- keep loading/error/cleanup state consistent with that decision.

Library-specific details still follow the actual library contract. For example, native fetch and an SSE helper may settle abort/retry differently.

---

## M-06. Cleanup must operate on the real owned resource/handle

**Evidence:** VERIFIED_MECHANISM

The owner of a resource must retain whatever handle is actually required to release, cancel, close, restore, or unlock it.

Examples:

- ThreadLocal/context state;
- locks;
- files/streams;
- listeners/observers/timers;
- AbortController/AbortSignal;
- subscriptions.

Cleanup must match the resource's real lifecycle.

For nested context-like resources, decide whether the correct operation is **clear** or **restore previous value**; do not mechanically call `remove()` everywhere.

---

## M-07. One state/code must have one stable meaning inside the same contract

**Evidence:** VERIFIED_MECHANISM + MAINTAINABILITY_CANDIDATE

- A status code must not mean different things in different views.
- A field must not silently change semantic meaning between layers.
- Unknown states need an explicit policy.
- A finite result set must not fall through to “success” merely because existing failure branches did not match.

---

## M-08. Environment-specific values must not be hardcoded in business/UI logic

**Evidence:** MAINTAINABILITY_CANDIDATE

Move deployment/environment values behind the repository's configuration mechanism.

Typical examples:

- base URLs;
- hosts/IPs;
- environment-specific ports;
- service endpoints;
- runtime-owned provider/model lists.

Do not confuse deployment configuration with stable domain constants.

---

## M-09. Same domain absence/failure must preserve the same outward meaning across cache/DB paths

**Evidence:** MAINTAINABILITY_CANDIDATE

If “entity missing” is one domain condition, a cache hit/miss must not randomly turn it into different outward semantics such as:

- exception;
- `null`;
- success-with-null;
- empty collection;
- system failure.

Choose the contract first; make cache and DB paths preserve it.

---

## M-10. Secret data must not be logged directly

**Evidence:** VERIFIED_MECHANISM / SECURITY GUIDANCE

Do not directly log:

- passwords;
- access/session tokens;
- verification codes;
- encryption keys;
- equivalent secrets.

If observability requires an identifier, use an approved redacted/masked/hashed form.

---

## M-11. When using SLF4J-style parameterized logging, preserve throwable semantics

**Evidence:** VERIFIED_MECHANISM

For SLF4J:

- pass the throwable in the position expected by the API so its stack trace is retained;
- do not replace the throwable with `e.getMessage()` when stack/cause information is needed;
- do not use an empty message as the exception log message.

This is a Java/logging-API-specific invariant, not a universal syntax rule for every logging library.

---

## M-12. Protocol/encoded values need an explicit unknown-value policy

**Evidence:** MAINTAINABILITY_CANDIDATE

When decoding an external or low-level contract (Lua result, MQ field, cache encoding, external API code, etc.):

- define what unknown/unsupported values mean;
- do not silently interpret an unknown value as success;
- preserve the external value when pass-through is the actual contract.

Whether the internal representation is an enum, result object, primitive, or string is a design choice handled by SHOULD/TRIGGER rules.

---

## M-13. Accidental conditional assignment must not happen

**Evidence:** VERIFIED_MECHANISM

Accidental code such as:

```js
if (result.success = true) {
  ...
}
```

is a correctness defect.

Intentional assignment inside a condition can be legitimate when the language/style allows it and intent is explicit.

For JS/TS repositories, enable a lint rule such as `no-cond-assign` according to the repository's lint setup.

---

## M-14. Java `Collectors.toMap` value mapping must not produce null

**Evidence:** VERIFIED_MECHANISM

In the standard JDK implementation, `Collectors.toMap` ultimately relies on map/merge behavior that rejects null mapped values.

If null values are legitimate data, use a collection strategy that represents them intentionally rather than relying on `toMap`.

Duplicate-key behavior is a separate rule; do **not** mechanically add a merge function.

---

## M-15. Vue list/control-flow essentials must be respected

**Evidence:** VERIFIED_MECHANISM / OFFICIAL ESSENTIAL GUIDANCE

For Vue:

- provide a stable `key` where the Vue contract/guidance requires keyed `v-for`;
- do not place `v-if` and `v-for` on the same element.

Using an array index as the key is **not** universally forbidden; see the review trigger below.

---

## M-16. Behavior-changing work must verify the relevant existing test signal when available

**Evidence:** VALIDATED_PRACTICE

If the repository has relevant automated tests that are runnable in the task environment:

- run the affected/closest relevant tests after changing behavior;
- report what was run and whether it passed;
- if tests cannot be run, state that instead of implying verification.

This rule does not require inventing a new test framework for repositories that do not have one.

---

# 3. SHOULD — strong defaults

## S-01. Prefer one coherent task per method/component

**Evidence:** MAINTAINABILITY_CANDIDATE

A unit should be explainable with one coherent responsibility.

Extract only when doing so creates meaningful names and lowers cognitive load.

Do not split code merely to reduce line count.

---

## S-02. Prefer guard clauses when they make the main path easier to read

**Evidence:** MAINTAINABILITY_CANDIDATE

Use early failure/return when it flattens simple preconditions.

Do not force every branch into guard-clause form.

For finite states, completeness may matter more than flatness.

---

## S-03. Keep important data mapping semantics visible and auditable

**Evidence:** VALIDATED_PRACTICE

A mapping should make important changes reviewable:

- renamed fields;
- defaults;
- unit/precision changes;
- missing-value policy;
- type conversion.

Hand-written mapping, Builder, MapStruct, and other approaches are all valid when they preserve readability.

If a project uses MapStruct:

- explicitly choose an `unmappedTargetPolicy` instead of inheriting an accidental default;
- treat `expression="java(...)"` as an escape hatch and keep important logic testable/visible.

Do not require a Converter/Mapper class for every one-off transformation.

---

## S-04. Abstract shared concepts and shared change reasons, not merely similar syntax

**Evidence:** MAINTAINABILITY_CANDIDATE

Prefer abstraction when code shares:

- the same concept;
- the same reason to change;
- the same validation/transformation rule;
- compatible failure/data-ownership semantics.

Avoid “universal helpers” driven by many booleans/config flags.

---

## S-05. Ordinary Spring components should default to constructor injection

**Evidence:** VALIDATED_PRACTICE

Spring's own guidance generally advocates constructor injection for required dependencies.

Default:

- required dependencies → constructor injection;
- optional/reconfigurable dependencies → setter injection can be appropriate.

A constructor cycle is primarily a design signal, not a reason to automatically fall back to field injection.

Manual constructor vs Lombok-generated constructor is a CONVENTION choice.

---

## S-06. HTTP-specific semantics should stay at the HTTP adapter boundary

**Evidence:** VALIDATED_PRACTICE

HTTP-specific concepts such as:

- status codes;
- headers;
- HTTP problem representations;
- transport serialization annotations;

should not leak into domain logic without a real reason.

Application/use-case result objects named `Response`, `Result`, or `PageResponse` can be completely valid if they are transport-agnostic.

Whether a service result type is a layering problem is handled by a REVIEW TRIGGER, not by name-based prohibition.

---

## S-07. Use Stream for short pure transformations; use direct loops when control flow is clearer

**Evidence:** VALIDATED_PRACTICE

Stream is a good fit for short, visible transformation/filter pipelines.

Prefer a loop when code involves:

- ordered side effects;
- complex branching;
- early break/continue;
- IO;
- multi-step mutable assembly.

Do not optimize for “functional-looking” code.

---

## S-08. Model empty, missing, unknown, and failed states intentionally

**Evidence:** MAINTAINABILITY_CANDIDATE

Do not introduce `null` as an extra state when it adds no useful distinction.

Do not mechanically replace every nullable result with `Optional`, empty objects, or empty collections.

Choose the contract intentionally.

---

## S-09. Keep semantic constants, units, and protocol meanings visible

**Evidence:** MAINTAINABILITY_CANDIDATE

Use meaningful names for values whose meaning matters:

- state codes;
- TTLs;
- thresholds;
- retry counts;
- time units;
- capacity limits;
- key formats.

Do not extract every literal into a global constant.

---

## S-10. Shared key/protocol formatting should have one authoritative definition

**Evidence:** MAINTAINABILITY_CANDIDATE

If a key/protocol format is shared, keep one authority for the format.

That authority may be:

- a constant;
- a builder/helper;
- a protocol type;
- an existing repository abstraction.

Do not create a new helper merely because two strings look similar.

---

## S-11. Front-end mutable state should have a clear owner

**Evidence:** VALIDATED_PRACTICE

Prefer:

- explicit props/emits or equivalent ownership;
- derived/computed state instead of manually synchronized duplicates;
- a single authoritative status mapping;
- clear separation between shared transport-envelope interpretation and use-case-specific success/partial-success/processing behavior.

Do not force all response interpretation into one global request layer when use cases genuinely differ.

---

## S-12. Shared front-end/back-end contracts need one authority or one validation path

**Evidence:** VALIDATED_PRACTICE

Avoid independently maintained contradictory copies of the same contract.

Valid approaches include:

- shared/generated types;
- protocol definitions;
- validated local constants for stable contracts;
- runtime API/config for dynamic server-owned data.

Do not require every stable enum to be fetched at runtime.

---

## S-13. Tests should prove observable behavior and important boundaries

**Evidence:** VALIDATED_PRACTICE

Prioritize tests for:

- error/fallback semantics;
- unknown/null states;
- async completion;
- serialization round trips;
- time boundaries;
- idempotency/duplicates;
- important mappings.

Do not write tests only to mirror getters/setters or inflate line coverage.

For bug fixes, add a regression test when a practical test harness exists and the defect is testable without disproportionate setup.

---

## S-14. Remove superseded implementation code instead of keeping commented-out history

**Evidence:** MAINTAINABILITY_CANDIDATE

Version control already stores history.

Do not turn this into unrelated repository cleanup; apply it to touched/directly related code.

Ad-hoc `main()` methods or debug/test controllers are not automatically wrong, but should trigger a reachability/scope review.

---

## S-15. Use the repository's normal logging/error mechanism in server/business code

**Evidence:** VALIDATED_PRACTICE

In long-running application/server code, prefer the repository's logging and error-propagation mechanism over ad-hoc `printStackTrace()`.

Do not introduce a logging framework solely to replace simple output in a CLI/bootstrap/example context.

---

## S-16. Avoid blind full-object logging and uncontrolled hot-path logging

**Evidence:** VALIDATED_PRACTICE

Review logging that serializes:

- full request/response objects;
- large prompts/content;
- every stream chunk;
- every render/scroll callback;
- tight-loop events.

Prefer aggregation, sampling, summaries, or lower verbosity when appropriate.

---

## S-17. New Vue projects should default to TypeScript unless there is a reason not to

**Evidence:** VALIDATED_PRACTICE

Vue has first-class TypeScript support and the official project scaffolding defaults toward TypeScript.

For existing JavaScript projects:

- do not force a migration;
- public API/store/request boundaries should still have usable type information via TS, JSDoc, or declaration files when practical.

---

## S-18. Every `toMap` call should have an explicit duplicate-key policy in the developer's reasoning

**Evidence:** VERIFIED_MECHANISM + VALIDATED_PRACTICE

Ask:

- Are duplicate keys invalid data?
- Or are they legal and mergeable?

If invalid, letting the collector fail is legitimate.

If legal, define a merge policy whose business meaning is clear.

Do not silently add `(a, b) -> a` just to make the exception disappear.

---

# 4. REVIEW TRIGGERS — inspect, do not auto-refactor

All triggers are scope-limited by section 1.3.

## R-01. Method is unusually long or mixes multiple responsibilities

**Evidence:** UNCALIBRATED_TRIGGER

Review when a method is difficult to explain as one task, mixes several independent business contexts, or is unusually long relative to neighboring repository code.

Numbered step comments can be an auxiliary clue, but are not a trigger by themselves.

Do not split a coherent algorithm merely to hit a number.

---

## R-02. Control flow is deeply nested or hard to mentally execute

**Evidence:** UNCALIBRATED_TRIGGER

Inspect for:

- guard clauses;
- duplicated branch work;
- explicit finite-state handling;
- meaningful sub-rules.

Do not flatten nesting when the nesting accurately reflects the problem.

---

## R-03. Similar implementation appears a second time

**Evidence:** MAINTAINABILITY_CANDIDATE

Second occurrence means **evaluate shared concept**, not “extract automatically.”

Ask:

- same semantics?
- same reason to change?
- same failure policy?
- same data ownership?
- would abstraction reduce drift without hiding meaning?

---

## R-04. Vue component/page is unusually large or mixes multiple feature domains

**Evidence:** UNCALIBRATED_TRIGGER

Review boundaries when one component owns several independent areas such as:

- upload orchestration;
- SSE/chat lifecycle;
- pagination;
- dialogs;
- state mapping;
- request lifecycle.

Observed size ranges from mature component libraries are calibration data, not universal thresholds.

Do not build a configuration-driven mega-component merely to reduce file count.

---

## R-05. Giant boolean condition

**Evidence:** MAINTAINABILITY_CANDIDATE

Inspect whether one condition mixes:

- input-shape validation;
- business-state validation;
- range checks;
- normalization;
- defaulting;
- construction.

Expose semantic groups only when that improves reading.

---

## R-06. Mechanical field-by-field mapping

**Evidence:** MAINTAINABILITY_CANDIDATE

A large assembler/builder should trigger a **mapping review**, not an automatic domain-model redesign.

First inspect:

- missing fields;
- defaults;
- units/precision;
- repeated transformation logic;
- ownership of the mapping;
- whether related fields actually share one concept.

Only discuss DTO/value-object/domain restructuring when there is independent evidence that model responsibilities are wrong and the task allows such redesign.

---

## R-07. Abstraction needs many boolean/configuration flags

**Evidence:** MAINTAINABILITY_CANDIDATE

This often means multiple concepts were forced into one helper.

Inspect whether the abstraction reflects shared semantics or only shared syntax.

---

## R-08. Transaction contains remote/file/slow work

**Evidence:** MAINTAINABILITY_CANDIDATE

Review transaction duration and failure semantics around:

- remote calls;
- file IO;
- waits;
- blocking resources;
- very large loops.

Do not move work out of the transaction blindly; correctness comes first.

---

## R-09. Full-object or high-frequency logging

**Evidence:** VALIDATED_PRACTICE

Review sensitivity, volume, diagnostic value, and cost.

A practical hot-path signal is a log statement that can execute many times during one external request, such as loop bodies, stream/chunk callbacks, scroll/event handlers, or timer ticks.

---

## R-10. Cache representation and API representation share the same model

**Evidence:** MAINTAINABILITY_CANDIDATE

Ask:

- do the cache and API have the same lifecycle?
- can an API field change invalidate cached data?
- is compatibility/versioning explicit?

Do not automatically introduce `CacheDTO` or another conversion layer.

---

## R-11. Service returns a type named `Response`, `Result`, `VO`, or carries serialization/HTTP semantics

**Evidence:** VALIDATED_PRACTICE

Review the semantics, not the name:

- Does it encode HTTP status/headers?
- Is it an application/use-case result?
- Is pagination part of the use-case contract?
- Would it remain meaningful over RPC/CLI/message transport?

Do not refactor solely because a class name contains `Response`.

---

## R-12. Lombok `@Builder` interacts with field initializers or inheritance

**Evidence:** VERIFIED_MECHANISM

Trigger review when:

- a `@Builder` class has field initializers that are expected to become builder defaults;
- a `@Builder` class extends a superclass whose fields are expected in the builder.

Check `@Builder.Default`, `@SuperBuilder`, Jackson construction path, and inheritance requirements as applicable.

---

## R-13. A custom delimiter/string protocol is introduced

**Evidence:** MAINTAINABILITY_CANDIDATE

Before accepting `a|b|c` / manual `split` style protocols, review:

- escaping;
- delimiter collisions;
- missing fields;
- version compatibility;
- whether a structured existing format already solves the problem.

Do not force a typed wrapper around simple pass-through identifiers that do not need decoding.

---

## R-14. A debug/test entry point exists in production-reachable code

**Evidence:** MAINTAINABILITY_CANDIDATE

Inspect:

- production reachability;
- profile/feature protection;
- authentication;
- whether it belongs in test tooling or a separate module.

Do not delete it automatically without understanding its role.

---

## R-15. A catch/fallback/degradation branch is added without a corresponding test change

**Evidence:** VALIDATED_PRACTICE

This is a review prompt, not an automatic failure.

Ask whether the new failure path is practically testable and whether existing tests already cover the contract.

---

## R-16. Vue uses array index as `:key`

**Evidence:** VERIFIED_MECHANISM

Index keys can be acceptable for stable, non-reordered lists without child/DOM state dependence.

Review carefully when:

- items can be inserted/deleted/reordered;
- child component state matters;
- DOM/form state must stay attached to logical items.

---

# 5. CONVENTION — project consistency choices

## C-01. Model suffix vocabulary

Examples such as `DO`, `DTO`, `ReqVO`, `RspVO` are repository vocabulary.

Consistency matters; the exact suffix set is not universal.

Do not create every possible model type before it has an independent contract.

---

## C-02. Null-check syntax

`Objects.isNull(x)` vs `x == null` is ordinarily a style choice.

Follow reasonable repository convention.

---

## C-03. Error-code format

Numeric, string, service-prefixed, or globally partitioned formats are project choices unless an external protocol requires one.

Stable names and clear semantics matter more than the numbering scheme.

---

## C-04. Response envelope shape

A project may use `Response<T>` / `PageResponse<T>` or no envelope at all.

What matters is semantic clarity and layer coupling, not the mere presence of an envelope.

---

## C-05. Lombok/Builder usage

Lombok use is not itself a quality signal.

Review mutability, defaults, inheritance, equals/hashCode semantics, and serialization path.

---

## C-06. Manual vs Lombok-generated constructor injection

Both are valid implementations of constructor injection.

Follow repository convention unless the generated API creates a real problem.

---

## C-07. `Stream.toList()` vs `Collectors.toList()`

Choose based on the actual mutability/compatibility contract and repository/JDK version.

Do not treat them as interchangeable when later mutation matters.

---

## C-08. Log decoration and comment language

Prefixes such as `==>`, separator lines, or a particular comment language are team style.

Comments should add information the code does not already state: why, assumptions, boundary decisions, concurrency rationale, failure rationale.

---

# 6. Explicitly rejected over-generalizations

## X-01. “Every method over 50 lines must be split”

Rejected.

Length can be a review clue, not an automatic refactor rule.

## X-02. “Duplicate code appearing twice must be extracted”

Rejected.

Second occurrence triggers semantic comparison, not automatic abstraction.

## X-03. “Every `Collectors.toMap` must define a merge function”

Rejected.

Duplicate-key semantics decide whether fail-fast or merge is correct.

## X-04. “Catch blocks must never return defaults/empty values”

Rejected.

Explicit fallback/degradation is valid; silent failure-as-success is not.

## X-05. “Anything expressible as Bean Validation must be moved out of Service code”

Rejected.

Input-shape validation and business-state validation have different responsibilities.

## X-06. “Service must never return a type named Response/PageResponse”

Rejected.

Judge semantics, not the class name.

## X-07. “Front end must never define backend-owned status constants locally”

Rejected.

The requirement is one authority/validation path, not one mandatory storage location.

## X-08. “Every business exception is warn without stack; every system exception is error with stack”

Rejected.

Level and stack policy depend on expectedness, frequency, impact, and diagnostic need.

## X-09. “Every index key in Vue is wrong”

Rejected.

Index keys are unsafe in important dynamic/stateful cases, but not universally invalid.

---

# 7. AI coding review checklist

Apply this checklist to the **current change**, not the whole repository.

1. What files/behavior did the task actually authorize me to change?
2. Does every nullable dereference have a real non-null guarantee?
3. Are validation annotations/imports and activation paths real?
4. Does every failure path have an intentional outcome?
5. Did I accidentally turn failure into success/empty/null?
6. Does async code expose the real completion/failure/cancellation signal, and is intentional cancellation classified correctly?
7. Does cleanup act on the actual owned resource/handle?
8. Are states/codes interpreted consistently from one authority?
9. Did I hardcode deployment/environment data?
10. Did I directly log secrets or misuse the logging API?
11. Are large/full-object/hot-path logs actually justified?
12. Are important mapping changes, units, defaults, and precision visible?
13. If code looks duplicated, is the **meaning/change reason** also duplicated?
14. If a service returns a Response/Result/VO, is that transport coupling or a legitimate application result?
15. If a cache uses an API-shaped model, is compatibility/lifecycle intentional?
16. If a `toMap` is used, what are the null-value and duplicate-key contracts?
17. If Lombok Builder is used, do defaults/inheritance/serialization behave as expected?
18. In Vue, are keys, list identity, derived state, and cleanup correct?
19. Did a review trigger fire? If yes, did I inspect it **without automatically expanding scope**?
20. If behavior changed, what relevant test signal did I run or why could I not run it?

---

# 8. Remaining calibration work before final Skill

The original external-validation backlog is complete, and the first GLM-5.3 A/B calibration round is complete.

The first round found:

- a strong positive signal against one-off overengineering in mechanical mapping;
- no observed scope-creep regression;
- small readability/state-contract wins;
- one narrow async-cancellation wording gap, now addressed in M-05;
- no evidence strong enough to turn Optional/null, line thresholds, or abstraction preferences into harder rules.

The next calibration should target the **actual thin runtime Skill**, because loading a compact `SKILL.md + references` may influence Agent behavior differently from loading this full research document.

Then, if practical, replicate the suite on a second participant model before calling the Skill broadly model-agnostic.

The final Skill should be split into:

- core standards;
- Java/Spring reference;
- Vue/JS/TS reference;
- anti-patterns;
- review checklist.

Only stable, useful rules should enter the Skill. Research commentary and long evidence trails should stay outside the runtime context.
