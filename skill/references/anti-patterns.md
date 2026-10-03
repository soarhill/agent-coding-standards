# Anti-patterns and rejected shortcuts

Load this reference when refactoring, reviewing abstraction, or when the code “works but feels ugly”.

## Do not turn review signals into automatic refactors

Reject these rules:

- “Every method over N lines must be split.”
- “Three nesting levels means extract methods.”
- “Duplicate code appearing twice must be abstracted.”
- “A large Builder/assembler means the domain model is wrong.”
- “A large Vue component must be split.”
- “A Service returning Response/PageResponse is automatically wrong.”

Inspect semantics first.

## Avoid one-off framework building

Watch for:

- a builder whose only purpose is hiding one positional constructor call while adding more coordinated edits;
- reflection/BeanUtils/generic property copying to remove explicit mapping;
- universal helpers controlled by many booleans/options;
- configuration-driven mega-components for two similar pages;
- new interfaces/classes with only one implementation and no independent semantic boundary.

The benchmark's strongest positive result came from **not** building a large one-off mapper abstraction.

## Do not confuse fewer lines with lower complexity

Prefer explicit code when it makes:

- business rules;
- mapping semantics;
- state transitions;
- failure handling;
- units/defaults

easier to review.

A longer direct loop may be better than a compressed Stream/lambda chain.

## Do not hide failure

Reject:

- catch-and-return-null/zero/empty/success when failure should propagate;
- “fallback” that is not part of the outward contract;
- error response converted into absence;
- unknown finite state treated as success;
- cancellation accidentally displayed as request failure.

Explicit best-effort/degradation is fine when callers understand it.

## Do not over-defend

Do not add:

- repeated null checks after a non-null contract is already established;
- wrappers around every primitive/string;
- Optional everywhere;
- defensive copies everywhere without ownership need;
- try/catch/log/rethrow at every layer;
- constants for every literal;
- comments narrating obvious code.

## Do not clean the neighborhood

If unrelated legacy code is ugly but not blocking the requested task:

- leave it alone;
- note it only when materially relevant;
- do not “while I'm here” refactor it.

## Prefer local consistency over taste

Do not churn:

- `Objects.isNull` vs `== null`;
- DTO/VO/DO suffixes;
- manual vs Lombok constructors;
- response-envelope shape;
- decorative log prefixes;
- comment language;

unless the local convention creates a real correctness/maintenance problem.
