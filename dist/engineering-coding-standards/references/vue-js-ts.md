# Vue / JavaScript / TypeScript

Load this only for relevant frontend work.

## Async and cancellation

- Return/await the real Promise when callers depend on completion or failure.
- Synchronous `try/catch` does not catch a later rejection from a floating Promise.
- Keep loading/error state aligned with real settlement.
- Treat cancellation/abort separately from failure according to the actual API/library contract.
- Cleanup must retain and release the real AbortController, listener, timer, subscription, or other handle.

If an API/library exposes callback-level failure behavior in addition to Promise rejection, make that failure policy intentional rather than assuming one mechanism implies the other.

## State meaning

- Keep one authoritative meaning for a status/code within the same UI contract.
- Prefer derived/computed state over manually synchronized duplicates.
- Do not maintain multiple local mappings that can drift when they represent the same backend state.

Shared frontend/backend contracts need one authority or one validation path. Stable local constants can be fine; not every enum must be fetched at runtime.

## Vue identity and control flow

- Use a stable key when logical item identity matters.
- Review index keys when items can insert/delete/reorder or DOM/form/child state must stay attached to logical items.
- Do not put `v-if` and `v-for` on the same element.

## Component boundaries

A large component is a review trigger, not a mandatory split.

Extract around real change reasons such as request lifecycle, dialogs, pagination, upload flow, or shared state transformations.

Do not build a configuration-driven mega-component merely because two screens look similar.

## JavaScript correctness

Accidental assignment in a conditional is a defect. Prefer the repository's existing lint protection when available.

Do not force a TypeScript migration in an existing JavaScript project just to satisfy this Skill.
