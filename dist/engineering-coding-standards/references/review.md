# Final review

Review the **actual change**, not the whole repository.

## Contract

- Is every nullable dereference backed by a real guarantee?
- Can failure be mistaken for absence, success, empty data, or a default?
- Are state/status/protocol meanings consistent?
- Is async completion truthful?
- Is expected cancellation distinguished from failure when the contract requires it?
- Does cleanup act on the real owned handle/resource?

## Readability

- Can the changed code be explained as a small number of coherent responsibilities?
- Are important validation, mapping, defaults, units, and state transitions visible?
- Did any helper/type/layer reduce cognitive load more than it added navigation or machinery?
- Did I compress code merely to reduce lines?
- Is any non-obvious business rule, invariant, workaround, boundary, concurrency, or lifecycle decision missing the explanation a future maintainer would need?
- Did I add comments that merely repeat the code, compensate for weak naming, or leave comments that are now stale?

## Cognitive load

- Does any parameter, flag, branch, or helper imply a distinction that currently has no behavioral effect?
- Are raw boolean arguments forcing the reader to jump to the callee to understand what `true` or `false` means?
- Does a data structure express its actual semantics, or am I using a map when I only need membership?
- Did a helper unexpectedly mutate an object the caller later reuses?
- Does one method make the reader track several independent phases or mutable accumulators at once?
- If so, is there a small semantic extraction that lowers cognitive load without creating a new framework?

## Anti-overengineering

Before keeping a new abstraction, ask whether it exists because of real shared meaning or only repeated syntax.

Be suspicious of:

- a one-use builder that adds more coordinated edits than it removes;
- reflection/BeanUtils/generic property copying used to hide business mapping;
- helpers controlled by many booleans/options;
- new interfaces/classes with no independent semantic boundary;
- config-driven mega-components created only to eliminate visual duplication.

Prefer deleting unnecessary abstraction over polishing it.

## Scope

- Did I touch only files/behavior the task authorizes?
- Did a long method, duplicate block, large component, or ugly neighbor trick me into unrelated cleanup?
- Did I change repository conventions only because I prefer another style?

If an unrelated issue matters, report it instead of silently fixing it.

## Verification

- If behavior changed, run the closest relevant existing test/check when available.
- Do not modify tests merely to make the implementation pass.
- If verification is unavailable, say so plainly.
- Do not claim checks you did not run.
