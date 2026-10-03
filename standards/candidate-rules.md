# Candidate Coding Rules v0

> Status: **research synthesis, not final skill**
>
> This document consolidates the two independent project reports and their cross-review. It intentionally separates hard defects from defaults, review signals, and team conventions.
>
> **Observed ≠ Recommended.**
>
> A source example proves that a problem or pattern exists in the reviewed codebase. It does **not** automatically prove that one specific replacement is universally correct.

---

## 1. Rule strength

### MUST

A violation is normally a correctness defect, contract break, misleading implementation, or a high-confidence engineering error.

An agent should fix or avoid these unless the surrounding code establishes an explicit, documented exception.

### SHOULD

A strong default that usually improves maintainability or clarity, but depends on context.

An agent may deviate when it can explain why the local design is clearer or better aligned with repository conventions.

### REVIEW TRIGGER

A mechanical signal that tells the agent to stop and inspect the code.

A trigger does **not** mean “refactor automatically.” It means: evaluate responsibility, cognitive load, duplication, coupling, and whether a change would actually improve the code.

### CONVENTION

A consistency choice.

Conventions can matter a lot inside one repository, but must not be presented as universal engineering truth.

---

# 2. MUST — hard invariants

## M-01. Never dereference before validating a nullable value

Bad pattern:

```java
String reasoning = metadata.get("reasoningContent").toString();

if (reasoning != null) {
    ...
}
```

The check does not protect the dereference that already happened.

**Rule**

- Validate a nullable value before dereferencing it.
- Check the object that will actually be accessed, not a nearby or similarly named object.
- Do not add defensive null checks after the dangerous operation and treat that as safety.

**Evidence from reviewed projects**

- AI Robot: metadata value converted with `.toString()` before null handling.
- Seckill: an object was dereferenced before a later null check.
- Xiaohashu: a helper checked one map and dereferenced another.

---

## M-02. Validation must be active on the real execution path

A validation annotation that looks right but is imported from the wrong package is worse than no validation because it creates false confidence.

**Rule**

- When using declarative validation, verify the actual annotation/import and the framework entry point that activates it.
- Container validation and element validation must both be considered when both matter.
- DTO validation does not replace business-state validation.

**Evidence**

- Weblog used a similarly named `@Validate` annotation from an unrelated library on a request path, so expected Spring validation did not run.

---

## M-03. Do not silently convert failure into success

**Rule**

A catch block must have an explicit purpose:

- recover with a documented fallback;
- translate while preserving the cause;
- isolate a permitted partial failure;
- or propagate.

Do **not** catch an unexpected failure and then return `null`, `0`, `false`, an empty result, or a success response if that changes failure into an indistinguishable normal result.

Explicit fallback is allowed when it is part of the contract.

**Good exception**

A display-only stock parser may return a known fallback value when malformed cache text is intentionally tolerated.

**Bad exception**

A failed service/RPC call returns `null`, and callers later treat that as “entity absent.”

---

## M-04. Preserve failure causes when translating exceptions

**Rule**

- When translating an unexpected exception, retain the original cause.
- Do not use `e.printStackTrace()` in production code.
- Do not throw a generic exception that erases the original context.
- When interruption is caught, preserve the interruption contract where the platform requires it.

**Evidence**

- Seckill contains a positive example that restores the interrupt flag and preserves the cause while unwrapping `ExecutionException`.

---

## M-05. A function's completion contract must be truthful

This applies especially to Promise/Future/reactive/streaming code.

**Rule**

If a caller is expected to await completion or catch failure, the function must return/await the real asynchronous operation.

Do not:

- start a Promise-producing operation inside `try/catch` without returning or awaiting it and then assume the catch protects later rejection;
- wrap an existing Promise in a new Promise without correctly resolving/rejecting it;
- pass the result of an assignment to `.finally(...) ` instead of a callback;
- report “completed” before the underlying operation has actually completed.

**Evidence**

- AI Robot front end contained a `fetchEventSource` call whose Promise was not connected to the outer `try/catch`.
- Weblog contained a Promise wrapper whose resolve/reject functions were never called.
- AI Robot contained a `.finally(deleteFlag = false)`-style misuse.

---

## M-06. Resource cleanup must operate on the real owned handle

**Rule**

The component/method that owns a resource must retain the actual handle needed to release it at lifecycle end.

Examples:

- ThreadLocal state → clear in `finally`;
- lock → unlock only according to ownership contract;
- stream/file → close in scope;
- AbortController / listener / observer / timer → keep the actual reference and release/cancel it;
- component teardown must not call a “cleanup” function that is disconnected from the real resource.

**Evidence**

- Positive: ThreadLocal cleanup in Xiaohashu, scoped lock/file cleanup in Seckill/AI Robot.
- Negative: an AI Robot SSE cleanup path referenced a handle that had never been connected to the active request.

---

## M-07. One state/code must have one stable meaning inside the same contract

**Rule**

- A status code must not map to different meanings in different views/components.
- A field must not silently switch semantic meaning across layers.
- Unknown states must have an explicit policy.
- When a finite result set is known, success must not be inferred merely because no currently listed failure branch matched.

**Evidence**

- AI Robot front end had two conflicting status-code-to-label mappings.
- Seckill had a known result state that was mapped at one boundary but not handled in the later order path.

---

## M-08. Environment-specific values must not be hardcoded in business/UI code

**Rule**

Move environment-dependent values behind configuration or an existing project-level configuration mechanism.

Examples:

- backend base URLs;
- IPs/hosts;
- environment-specific service endpoints;
- deploy-specific ports;
- model/provider lists when they are runtime-owned configuration.

Do not confuse environment configuration with stable business constants.

**Evidence**

- Hardcoded localhost SSE URLs and a hardcoded public IP appeared in reviewed front-end code.

---

## M-09. Remove dead code instead of commenting it out

**Rule**

- Delete superseded implementation code; version control preserves history.
- Do not use production `main()` methods as ad-hoc tests.
- Test/debug controllers must be isolated from production exposure using the repository's accepted mechanism.

**Evidence**

All four projects contained commented-out historical implementation blocks or test-like code in production sources.

---

## M-10. Logs must not expose secrets or create fake observability

**Rule**

- Never log passwords, access tokens, verification codes, or equivalent secrets.
- Avoid blindly serializing full request/response objects when they may contain sensitive or very large data.
- A log message must contain meaningful context; avoid empty-message logging such as `log.error("", e)`.
- Hot paths such as per-stream-chunk/per-scroll/per-loop operations must not emit uncontrolled info-level logs.
- If an exception is logged, use the logging API correctly so the exception/cause is preserved.

**Evidence**

The reviewed projects contained token/verification-code logging, full request/response logging, empty log messages, and per-SSE-chunk logging.

---

## M-11. Protocol values must be decoded at a boundary with an explicit unknown-value policy

**Rule**

When interacting with Lua scripts, MQ payloads, external services, cache encodings, or similar boundaries:

- translate protocol values into meaningful internal results once;
- define what unknown values mean;
- do not let bare numeric/string codes drift through the business layer;
- do not invent fragile delimiter protocols when an existing typed/structured representation is available.

**Evidence**

- Positive: Seckill mapped Lua numeric results to meaningful enums.
- Negative: another path used a hand-built delimiter string and manual `split`.

---

## M-12. Same domain absence/failure must not change meaning depending on cache path

**Rule**

If “entity does not exist” is one domain condition, a cache hit/miss path must not randomly transform it between:

- exception,
- `null`,
- success-with-null,
- empty collection,
- system failure.

Choose the semantic contract first, then make cache/DB paths preserve it.

**Evidence**

The review found paths where DB-miss and cached-null produced different outward meanings.

---

## M-13. Conditional assignment must be deliberate, never accidental

For JavaScript/TypeScript-style conditions, accidental assignment such as:

```js
if (e.success = true) {
    ...
}
```

is a correctness bug.

**Rule**

- Do not use assignment as a condition in normal application code.
- Enable the repository's static-analysis rule that catches accidental conditional assignment where available.

---

# 3. SHOULD — strong defaults, not universal laws

## S-01. Prefer one clear task per method/component

A unit should be explainable with one coherent responsibility.

Prefer extracting:

- a meaningful validation step;
- a pure transformation;
- an independently understandable business sub-step;
- a resource lifecycle;
- a reusable mapping rule.

Do **not** extract a helper merely to reduce line count if the reader now has to jump around more.

---

## S-02. Prefer guard clauses when they flatten simple failure paths

Guard clauses are useful when they make the main flow read top-to-bottom.

Do not force every branch into early return.

For finite state handling, completeness is often more important than flatness.

---

## S-03. Prefer visible, auditable data mapping at boundaries

A mapping should make important semantic changes easy to inspect:

- renamed fields;
- default values;
- precision/unit changes;
- missing-value policy;
- type conversion.

Use hand-written mapping, Builder, MapStruct, or another mechanism based on repetition and semantic complexity.

Do not require a Converter class for every one-off mapping.

---

## S-04. Abstract shared concepts and shared change reasons, not merely similar syntax

Two blocks that look similar are not automatically one abstraction.

Extract when the pieces:

- represent the same concept;
- are expected to evolve together;
- share the same validation/transformation rule;
- and become easier to understand after extraction.

Avoid “universal” helpers with many booleans or configuration flags.

---

## S-05. Prefer explicit dependency construction in ordinary service code

Constructor injection is a strong candidate default for Spring service code because dependencies are visible at construction time.

However this remains **pending external validation** for the final skill, including lifecycle/framework exceptions and how strongly it should be stated.

---

## S-06. Keep transport-layer concerns from leaking unnecessarily into business logic

A service method should not return a web/HTTP wrapper merely because the controller uses one.

However, do **not** reject a type only because it is named `Response` or `PageResponse`.

Review the actual semantics:

- Does it encode HTTP/presentation concerns?
- Is it an application result type?
- Is pagination itself part of the use-case contract?
- Would the service still make sense outside the current transport?

This remains **pending external validation**.

---

## S-07. Use Stream for short, pure transformations; use direct loops when control flow is clearer

Prefer Stream when it expresses an obvious mapping/filtering pipeline.

Prefer a loop when the operation includes:

- multiple assignments;
- ordered side effects;
- complex branching;
- early break/continue;
- IO;
- mutable accumulation that becomes harder to reason about in lambdas.

Do not optimize for “functional-looking” code.

---

## S-08. Empty collection, missing entity, unknown state, and system failure should be modeled intentionally

Do not introduce `null` as an extra state when it adds no useful distinction.

But do not mechanically replace every nullable result with `Optional`, empty objects, or empty collections.

Choose based on the actual contract.

---

## S-09. Keep semantic constants and units visible

Use meaningful names for values whose business meaning matters:

- state codes;
- TTLs;
- thresholds;
- capacity limits;
- time units;
- retry counts;
- Redis key structure.

Do not extract every literal. Local algorithm constants, obvious counters, or low-level buffer sizes do not automatically deserve global constants.

---

## S-10. Centralize repeated key/protocol formatting

If a Redis key/message key/cache key format is shared, keep its format in one place and provide a clearly named builder/helper.

Do not define a central helper and then bypass it with manual concatenation elsewhere.

---

## S-11. Decouple cache representation from API representation when they have different lifecycles

Serializing a response VO directly into cache may be acceptable for a very local, short-lived cache.

But when cache data has an independent lifecycle, evolving the API response should not silently break cache compatibility.

Review whether a dedicated cache representation or explicit compatibility policy is warranted.

This is a **SHOULD/review rule**, not a universal ban on caching response-shaped data.

---

## S-12. Front-end mutable state should have a clear owner

Prefer:

- props/emits or another explicit ownership contract;
- derived/computed state instead of manually synchronized duplicates;
- one source for status mapping;
- one place that interprets transport/business success.

Do not duplicate the same truth in multiple stores/components unless those copies have a real independent lifecycle.

---

## S-13. Front-end/backend shared contracts need one authority, not necessarily one runtime location

Do not maintain contradictory copies of status/model definitions.

Possible solutions include:

- generated/shared types;
- protocol definitions;
- a validated local constant for a stable contract;
- runtime configuration/API for dynamic server-owned data.

Do **not** blindly require every stable enum to be fetched from the server at runtime.

---

## S-14. Tests should prove behavior and boundaries, not merely execute code

Prioritize tests around:

- null/unknown-state behavior;
- error/fallback semantics;
- async completion;
- serialization round trips;
- time boundaries;
- idempotency/duplicate handling;
- important transformations.

Do not create tests whose only purpose is line coverage or getter/setter mirroring.

The reviewed tutorial projects did not provide enough strong test examples, so final test guidance requires external sources.

---

# 4. REVIEW TRIGGERS — inspect, do not auto-refactor

The values below are deliberately **soft triggers**. They are not pass/fail thresholds.

## R-01. Long method

Trigger review around roughly **40–60 lines of meaningful logic**, or earlier when the method mixes several independent responsibilities.

Ask:

- Can the method still be described as one task?
- Are several business rules mixed together?
- Would extraction create meaningful names or only more jumping?

Do not split a coherent algorithm purely to satisfy a number.

---

## R-02. Deep nesting

Three or more meaningful nesting levels should trigger a control-flow review.

Check for:

- guard clauses;
- extraction of a meaningful sub-rule;
- clearer finite-state handling;
- removal of duplicated branch work.

Do not mechanically flatten an algorithm whose nesting reflects the problem.

---

## R-03. Numbered step comments

Comments such as:

```text
// 1.
// 2.
// 3.
```

often indicate that a method has multiple conceptual stages.

Trigger a responsibility review.

Do not automatically extract each numbered step into a separate method.

---

## R-04. Repeated implementation appears a second time

Second occurrence means **review for shared concept**, not “extract automatically.”

Ask:

- Same semantics?
- Same reason to change?
- Same failure policy?
- Same data ownership?
- Would one abstraction reduce drift?

If not, duplication may be cheaper than a false abstraction.

---

## R-05. Large component/page

A Vue component/script approaching a few hundred lines, or mixing three or more independent feature areas, should trigger a boundary review.

Typical separable areas:

- upload orchestration;
- SSE/chat lifecycle;
- table pagination;
- edit/delete dialogs;
- state mapping;
- reusable API lifecycle.

Do not build a generic configuration-driven mega-component merely to reduce file count.

---

## R-06. Giant boolean condition

A condition containing many unrelated checks should trigger review.

Ask whether the condition mixes:

- input-shape validation;
- business-state validation;
- range validation;
- defaulting;
- normalization;
- construction.

Prefer exposing semantic groups when that improves reading.

Do not create one class per predicate.

---

## R-07. Mechanical field-by-field mapping

A large Builder/assembler that copies many related fields should trigger a domain-model review.

Ask:

- Are these fields truly independent?
- Do groups of fields express stable concepts such as range/include/exclude?
- Is the duplication from the mapping layer, or from a flat source/target model?

Do not hide a poor model with reflection or a universal mapper.

---

## R-08. Many boolean/configuration flags in one abstraction

This often indicates that several concepts have been forced into one helper.

Review whether the abstraction is really shared or only syntactically consolidated.

---

## R-09. Transaction contains remote/file/slow operations

Review transaction duration and failure semantics when a transactional method contains:

- network calls;
- file IO;
- large loops;
- waits;
- blocking external resources.

Do not move work out of a transaction blindly; preserve correctness first.

---

## R-10. Full-object logging or high-frequency logging

Trigger review when code logs:

- full request/response JSON;
- prompts or large content;
- every stream chunk;
- every scroll/render event;
- loop-level info messages.

Check sensitivity, volume, diagnostic value, and whether logging can affect the main path.

---

# 5. CONVENTION — consistency choices, not universal rules

The following patterns were common in the source projects or are plausible repository conventions. They must **not** be promoted to universal best practice without a project decision.

## C-01. Model suffix vocabulary

Examples:

- `DO`
- `DTO`
- `ReqVO`
- `RspVO`

The important part is that model roles are understandable and consistent.

Do not create every possible model type before there is an independent contract or reason to change.

---

## C-02. Null-style preference

`Objects.isNull(x)` vs `x == null` is mostly a style choice in ordinary Java code.

Follow the repository's local convention unless a specific API pattern benefits from one form.

---

## C-03. Error-code numbering/prefix scheme

A stable named error definition is valuable.

Whether the code is:

- numeric;
- string;
- service-prefixed;
- globally partitioned;

is a project-level convention unless there is an external protocol requirement.

---

## C-04. Response envelope shape

A project may use `Response<T>` / `PageResponse<T>`.

The existence of a response envelope is a convention/design decision.

What matters to the coding standard is whether the envelope leaks transport concerns into layers where they do not belong and whether success/failure semantics remain clear.

---

## C-05. Lombok/Builder usage

Using `@Data`, `@Builder`, constructors, etc. is not itself a quality signal.

Review:

- mutability needs;
- defaults;
- inheritance;
- serialization/deserialization path;
- whether generated methods create an unintended API.

---

## C-06. Log decoration and comment language

Prefixes such as `==>` or a particular language for comments are team style.

Comments should add information that the code does not already state, especially:

- why;
- assumption;
- boundary;
- concurrency reason;
- failure rationale.

Do not preserve tutorial-style line-by-line narration as a universal standard.

---

# 6. Explicitly rejected over-generalizations

The following candidate rules were proposed during research but are **not accepted as hard rules** in v0.

## X-01. “Every method over 50 lines must be split”

Rejected.

Line count is a review trigger, not a mandatory refactor.

## X-02. “Duplicate code appearing twice must be extracted”

Rejected.

Second occurrence triggers evaluation. Shared syntax without shared semantics/change reason is not enough.

## X-03. “Every `Collectors.toMap` must define a merge function”

Rejected.

The code must define the **uniqueness/conflict contract**.

If duplicate keys indicate corrupt or invalid data, failing is valid. A merge function is needed only when duplicate keys are legitimate and the merge policy is meaningful.

## X-04. “Catch blocks must never return defaults/empty values”

Rejected.

Explicit, documented fallback/degradation is valid. Silent failure masquerading as normal success is not.

## X-05. “Anything expressible as Bean Validation must not be checked in Service”

Rejected.

Static input-shape rules often fit declarative validation. Business state, authorization, database-dependent checks, ordering, and use-case rules may belong in service/application logic.

## X-06. “Service must never return any type named Response/PageResponse”

Rejected as a name-based rule.

Inspect the type's semantics and coupling, not just its class name.

## X-07. “Front end must never define backend status constants”

Rejected.

The real requirement is one authoritative contract and no divergent copies. Shared/generated/local-validated definitions can all be valid.

## X-08. “Every business exception is warn without stack; every system exception is error with stack”

Rejected.

Log level and stack policy depend on expectedness, frequency, impact, and diagnostic need—not merely exception class.

---

# 7. External validation backlog

Before the final Skill is produced, the following SHOULD/technology-specific rules require independent verification using official documentation and multiple mature codebases.

1. **Spring dependency injection**
   - constructor injection defaults;
   - lifecycle/framework exceptions.

2. **Service/application/transport boundaries**
   - when response envelopes are appropriate;
   - pagination/application result types;
   - exception vs explicit result modeling.

3. **Bean Validation**
   - `@Valid` / `@Validated` activation;
   - nested/container validation;
   - null semantics;
   - custom validator boundaries.

4. **Lombok/Jackson defaults**
   - Builder field defaults;
   - inheritance;
   - deserialization behavior.

5. **Stream/Collectors**
   - duplicate-key semantics;
   - collection mutability;
   - ordering;
   - readable use in mapping/aggregation.

6. **MapStruct/manual mapping**
   - null/default handling;
   - unmapped-field policy;
   - semantic conversion visibility.

7. **Promise/SSE lifecycle**
   - Promise propagation;
   - `finally`;
   - AbortController;
   - fetch-event-source behavior for the relevant version.

8. **Vue/Pinia**
   - ownership and derived state;
   - composable boundaries;
   - stable keys;
   - lifecycle cleanup;
   - JS vs TS/JSDoc strategy.

9. **Logging**
   - structured context;
   - exception/cause handling;
   - hot-path logging;
   - secret redaction.

10. **Testing**
    - async completion tests;
    - boundary tests;
    - isolation and repeatability.

---

# 8. AI coding review questions

After generating or modifying code, the agent should ask:

1. Can a reader explain each changed method/component in one sentence?
2. Did I compress logic merely to reduce lines?
3. Did I introduce an abstraction just because two blocks look similar?
4. Does every failure path have an intentional outcome?
5. Are null/empty/missing/failure states distinguishable where they need to be?
6. Does async code return the real completion/failure signal?
7. Does cleanup release the actual resource/handle?
8. Is a status/code interpreted in more than one place?
9. Did I hardcode environment values or duplicate protocol definitions?
10. Did I leave commented-out code, debug code, secret logs, or noisy logs?
11. Are important mappings and unit/precision changes visible?
12. Did I follow repository-local conventions where they are reasonable?
13. Did any review trigger fire? If yes, did I evaluate it rather than mechanically refactor?

---

# 9. Next step

This file is the **v0 candidate matrix expressed as rules**.

Next:

1. externally validate only the disputed SHOULD/framework rules;
2. revise this document into a stable v1;
3. split stable content into:
   - core standards;
   - Java/Spring reference;
   - Vue reference;
   - anti-patterns;
   - review checklist;
4. create the actual `SKILL.md`;
5. run A/B coding tasks with and without the skill to check whether it improves code without causing over-abstraction.
