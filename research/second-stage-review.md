# Second-stage review decisions

This document records how the second-stage evidence changed `standards/candidate-rules.md` from v0 to v1.

## Inputs

1. **Codex challenge review**
   - focused on over-generalization, rule strength, task-scope expansion, architecture bias, and uncalibrated triggers;
   - did not re-scan the four source projects or perform external research.

2. **Z Code external validation**
   - validated the original external-validation backlog against official documentation first;
   - compared multiple independent mature repositories;
   - explicitly did not upgrade a rule merely because a large project used it.

## High-impact decisions

### Scope and evidence were separated from severity

v1 adds two independent dimensions:

- rule strength: MUST / SHOULD / REVIEW TRIGGER / CONVENTION;
- evidence state: VERIFIED_MECHANISM / VALIDATED_PRACTICE / MAINTAINABILITY_CANDIDATE / CONVENTION_ONLY / UNCALIBRATED_TRIGGER.

It also adds a global scope guardrail: a standard does not authorize unrelated repository cleanup or architecture redesign.

### Rules downgraded or narrowed

- Dead/commented historical code moved from MUST to SHOULD; production-reachable debug/test entry points became review triggers.
- Full-object and hot-path logging moved out of the hard-secret rule into SHOULD/TRIGGER.
- Cache/API representation coupling became a compatibility review trigger rather than an automatic CacheDTO requirement.
- Service `Response/Result/PageResponse` handling became semantic review, not a name-based prohibition.
- Long-method, nesting, and component-size numbers were removed as mandatory stop conditions; they remain uncalibrated signals.
- Mechanical field mapping now triggers mapping review, not domain-model redesign.
- Numbered step comments are only an auxiliary clue, not a standalone trigger.

### Mechanism-backed rules confirmed or added

- validation must really be active on the execution path;
- async completion/failure contracts must be truthful;
- cleanup must use the actual resource handle;
- accidental conditional assignment is a correctness issue;
- `Collectors.toMap` cannot accept a null mapped value;
- Vue keyed-list / `v-if` + `v-for` essentials are enforced according to official Vue guidance;
- SLF4J throwable handling and direct secret logging rules were separated and clarified.

### Java/Spring defaults validated

- constructor injection remains a SHOULD for ordinary Spring required dependencies;
- manual constructor vs Lombok-generated constructor is a convention;
- HTTP semantics should remain at the HTTP adapter boundary;
- application result types named Response/Result are not inherently wrong;
- MapStruct remains optional, but explicit unmapped-target policy is recommended when MapStruct is used.

### Front-end rules refined

- state ownership and shared-contract authority remain SHOULD;
- array-index keys are conditional review concerns rather than a universal ban;
- new Vue projects default toward TypeScript, while existing JavaScript repositories are not forced to migrate;
- API boundaries should still expose usable type information.

### Testing policy refined

- behavior-changing work must run relevant existing tests when available and report the result;
- bug fixes should add regression tests when practical;
- a new catch/fallback branch without corresponding test change is a review trigger, not automatic failure.

## Remaining work

External research is no longer the main bottleneck.

The next phase is **Agent calibration**:

- run the same coding tasks with and without the standards;
- inspect whether review triggers cause unnecessary refactors;
- measure whether the standards reduce unreadable compression, accidental duplication, hidden failure semantics, and resource-lifecycle mistakes;
- tune the runtime Skill so it stays concise and does not become a giant policy prompt.
