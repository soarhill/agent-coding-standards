# Java / Spring reference

Load this only for relevant Java/Spring code.

## Validation

- Validation annotations/imports must come from the intended validation/Spring packages.
- Confirm that the actual execution path activates validation.
- Distinguish MVC request validation from Spring method validation/proxy behavior.
- Container validation and element validation are separate concerns.
- Declarative DTO validation does not replace database-dependent, authorization, ordering, or business-state checks.

## Dependency injection

For ordinary Spring components:

- required dependencies should default to constructor injection;
- optional/reconfigurable dependencies may justify setter injection;
- constructor cycles are a design signal, not a reason to automatically fall back to field injection.

Manual constructor vs Lombok-generated constructor is a repository convention.

## Service / transport boundaries

Keep HTTP-specific semantics such as status codes, headers, and HTTP problem representations at the HTTP adapter boundary unless there is a real reason otherwise.

Do not reject a service type merely because it is named `Response`, `Result`, `VO`, or `PageResponse`. Review semantics:

- does it encode HTTP/presentation concerns?
- or is it a transport-agnostic application/use-case result?

## Streams and collections

Use Stream for short, pure, obvious transformations.

Prefer a direct loop when code has:

- ordered side effects;
- complex branching;
- early break/continue;
- IO;
- multi-step mutable assembly.

For `Collectors.toMap`:

- mapped values must not be null;
- duplicate-key behavior must be intentional;
- if duplicates are invalid, fail-fast is valid;
- if duplicates are legal, provide a meaningful merge policy;
- do not add `(a, b) -> a` just to suppress an exception.

Do not assume `Stream.toList()` and `Collectors.toList()` have identical mutability/compatibility behavior.

## Mapping

Hand-written mapping, Builder, and MapStruct can all be valid.

Prefer the option that keeps semantic changes visible and avoids unnecessary machinery.

If MapStruct is already used:

- explicitly choose an `unmappedTargetPolicy`;
- treat `expression="java(...)"` as an escape hatch;
- keep meaningful business conversion testable and visible.

Large field-by-field mapping is a **mapping review trigger**, not a command to invent a builder, reflection mapper, or redesign the domain model.

## Lombok

Review `@Builder` carefully when:

- field initializers are expected to become builder defaults;
- inheritance fields are expected in the builder;
- Jackson/deserialization uses a different construction path.

Check `@Builder.Default`, `@SuperBuilder`, and actual serialization behavior as relevant.

Lombok usage itself is not a quality signal.

## Exceptions and logging

- Preserve causes when translating unexpected exceptions.
- Preserve interruption semantics where required.
- In long-running application/server code, use the repository's normal logging/error mechanism rather than ad-hoc `printStackTrace()`.
- For SLF4J-style logging, pass the throwable in the API position that preserves the stack/cause.
- Do not replace the throwable with only `e.getMessage()` when diagnostics need the stack.
- Do not use empty exception log messages.
