# Vue / JavaScript / TypeScript reference

Load this only for relevant frontend code.

## Async behavior

- Return/await the real Promise when the caller relies on completion/failure.
- Synchronous `try/catch` does not catch a later rejection from a floating Promise.
- `.finally(...)` needs the intended callback/handler, not the result of an assignment.
- Keep loading/error state aligned with real completion.
- Treat cancellation/abort separately from failure according to the actual library/API contract.
- Lifecycle cleanup must retain and release the real AbortController/listener/timer/subscription handle.

## State ownership

Prefer:

- explicit props/emits or equivalent ownership;
- computed/derived state instead of manually synchronized duplicate state;
- one authoritative status/code mapping when multiple views represent the same backend state.

Do not force all response interpretation into one global request layer when different use cases have legitimately different success/partial-success/processing behavior.

## Shared contracts

Frontend/backend shared contracts need one authority or one validation path.

Valid approaches include:

- generated/shared types;
- protocol definitions;
- validated local constants for stable contracts;
- runtime API/config for dynamic server-owned data.

Do not require every stable enum to be fetched at runtime.

## Vue list/control flow

- Use stable keys when logical identity matters.
- Do not put `v-if` and `v-for` on the same element.
- Array index as `:key` is not universally wrong, but review it carefully if the list can insert/delete/reorder or if child/DOM/form state must stay attached to logical items.

## Component boundaries

A large component is a review trigger, not a mandatory split.

Extract around real change points such as:

- upload orchestration;
- SSE/chat lifecycle;
- pagination;
- dialogs;
- reusable request lifecycle;
- shared state transformations.

Do not build a config-driven mega-component merely because two pages look similar.

## JavaScript vs TypeScript

For new Vue projects, TypeScript is a strong default.

For existing JavaScript repositories:

- do not force a migration;
- use TS, JSDoc, or declarations where boundary type information materially improves correctness and maintenance.

## Common correctness check

Accidental conditional assignment such as `if (result.success = true)` is a defect.

Use the repository's lint rule (for example `no-cond-assign`) when available.
