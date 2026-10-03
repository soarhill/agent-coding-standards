# Core standards

Load this reference for every coding task.

## Hard invariants

### Preserve real contracts

- A nullable value needs a real non-null guarantee before dereference.
- Do not check after dereference or check the wrong object.
- Do not silently convert unexpected failure into normal success, `null`, `0`, `false`, or empty data unless that fallback is part of the contract.
- When translating unexpected failures, preserve causal information when supported.
- One state/code/field must keep one stable meaning inside the same contract.
- Unknown finite states need an intentional policy; do not fall through to “success” by accident.
- Cache/DB/transport paths must preserve the same outward meaning for the same domain condition.

### Async and resource lifecycle

- Async functions must expose the real completion/failure signal when callers rely on it.
- Do not leave a Promise/Future/reactive operation floating while reporting completion.
- Treat cancellation/abort as a distinct lifecycle outcome. Decide whether it means expected shutdown, user cancellation, retryable interruption, or failure according to the actual API/library contract.
- Do not accidentally show expected cancellation as a user-visible failure.
- Cleanup must act on the real owned handle/resource.
- For nested context state, decide whether cleanup means clear or restore previous value.

### Configuration, protocol, and logging

- Environment-specific hosts/ports/base URLs/runtime-owned provider settings belong in repository configuration, not scattered business/UI code.
- External/protocol values need an explicit unknown-value policy.
- Do not directly log secrets such as passwords, access/session tokens, verification codes, or encryption keys.
- Avoid blind full-object and uncontrolled hot-path logging.

## Strong defaults

### Keep responsibilities understandable

Prefer one coherent task per method/component.

Extract only when the new boundary:

- has a meaningful name;
- represents a real sub-rule or transformation;
- lowers cognitive load;
- can be understood without excessive jumping.

Do not split merely to reduce line count.

### Prefer readable control flow

Guard clauses are useful for simple preconditions and failure paths when they flatten the main flow.

Do not force every branch into early-return form. Finite-state completeness can matter more than flatness.

### Keep transformations auditable

Important mappings should make visible:

- renamed fields;
- defaults;
- missing-value policy;
- unit/precision changes;
- semantic conversion.

Do not hide explicit business mappings behind reflection, generic property copying, or a custom mini-framework just to remove lines.

### Abstract meaning, not shape

Extract shared code when it shares:

- the same concept;
- the same reason to change;
- compatible failure semantics;
- compatible data ownership.

Similar syntax alone is insufficient.

### Model states intentionally

Distinguish empty, missing, unknown, failure, and cancellation when the domain does.

Do not mechanically use `Optional`, empty objects, or null everywhere. Choose the representation that makes the local contract clearest without unnecessary blast radius.

### Preserve semantic values

Name values whose domain meaning matters: TTLs, state codes, retry counts, limits, units, protocol meanings.

Do not turn every literal into a constant.

## Review triggers

Inspect, but do not auto-refactor, when you see:

- a method that is hard to explain as one task;
- deeply nested/hard-to-simulate control flow;
- similar logic appearing again;
- giant boolean conditions mixing unrelated rules;
- large mechanical field mappings;
- abstractions driven by many boolean/config flags;
- transactions containing slow/remote/blocking work;
- cache models tied directly to API models with independent lifecycles;
- production-reachable debug/test entry points;
- new fallback/degradation paths without corresponding test consideration.

A trigger never expands task scope by itself.
