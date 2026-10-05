# Java / Spring

Load this only for relevant Java/Spring work.

## Naming, packages, and build modules

For newly introduced Java code, follow the repository first. When no stronger local convention exists:

- package segments are lowercase; avoid camelCase and underscore-based package naming;
- keep the existing root package rather than inventing another namespace;
- class and interface names use UpperCamelCase and describe a noun, role, or capability;
- method and variable names use lowerCamelCase and express behavior or meaning;
- constants use clear `UPPER_SNAKE_CASE` names;
- avoid uncommon abbreviations that save a few characters but hide meaning.

Do not blindly import organization-specific rules such as mandatory `Impl` suffixes or singular package names into a repository that follows another coherent convention.

For Maven/Gradle modules and published Java artifacts:

- follow the naming pattern already established by sibling modules;
- keep logical module/project names aligned with their physical directories when practical;
- for Maven artifact IDs, prefer lowercase letters, digits, and hyphens;
- use names that describe the product/component responsibility rather than temporary implementation detail;
- avoid unnecessarily deep or redundant module paths;
- do not create a new build module merely to reorganize a few files; module creation is a larger boundary/build decision.

## Validation

- Confirm validation annotations come from the intended packages.
- Confirm the real execution path actually activates validation.
- Distinguish request/parameter validation from business-state validation.
- Container validation and element validation are separate concerns.

Do not move business rules into Bean Validation merely because an annotation can express part of the condition.

## Javadoc and API contracts

Use Javadoc when callers need contract information that names and types alone do not make clear.

Prioritize it for:

- public or protected APIs with non-obvious behavior;
- interfaces and abstract methods whose implementations must obey a contract;
- parameters with special units, ranges, nullability, ordering, or sentinel meanings;
- return values with important absence/state semantics;
- declared or meaningful exceptional behavior;
- compatibility, lifecycle, or usage constraints callers must know.

Do not mechanically add Javadoc to trivial getters/setters or methods whose contract is already obvious from the signature and surrounding repository convention.

Javadoc should describe the caller-visible contract and important constraints, not translate the method name into prose.

Follow the repository's existing Javadoc language and formatting convention.

## Exceptions and logging

- Preserve the original cause when translating unexpected exceptions.
- Preserve interruption semantics where required.
- In server/business code, use the repository's normal logging/error mechanism.
- With SLF4J-style logging, keep the throwable in the form that preserves stack/cause information.

Do not add catch/log/rethrow layers without a real ownership or translation reason.

## Collections and mapping

For `Collectors.toMap`:

- mapped values must not be null;
- duplicate-key behavior must be intentional;
- fail-fast is valid when duplicates are invalid;
- use a meaningful merge policy only when duplicates are legitimate.

Choose the collection type that matches the semantics:

- `Set` for membership / uniqueness / existence;
- `Map` when both key and value carry meaning;
- ordered variants only when iteration order is part of the behavior.

Do not use a `Map<K, V>` as a disguised set when the values are irrelevant.

For field mapping:

- keep renamed fields, defaults, units, missing-value policy, and transformations visible;
- use existing MapStruct/Builder patterns when they genuinely fit;
- do not invent a mapper framework, reflection layer, or large one-off builder merely to avoid explicit assignments.

Large mechanical mapping is a review trigger, not a domain-redesign command.

## Mutable parameter objects

Be explicit when helpers mutate caller-owned builders or parameter objects such as `MapSqlParameterSource`.

If the caller reuses the same object for another query or operation, avoid hidden augmentation that changes later assumptions. Prefer a new parameter object, a clearly named mutating helper, or a local copy when that makes ownership easier to reason about.

## Streams

Use Stream for short, pure, obvious transformations.

Prefer a direct loop when branching, side effects, early exit, IO, or mutable assembly makes the loop easier to understand.

## SQL composition

Dynamic SQL helpers should expose semantic intent at their call sites.

Avoid boolean mode parameters whose meaning is unclear from the call site, especially when multiple booleans appear together. If two branches currently produce the same fragment, remove the dead distinction rather than preserving a speculative mode.

Keep query fragments named by what they mean, not by incidental implementation details.

## Spring-specific review trigger

If a transaction encloses remote calls, file IO, waits, or other slow/blocking work, review duration and failure semantics.

Do not move work out of the transaction automatically; correctness comes first.
