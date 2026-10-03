# Final review checklist

Use this after implementation. Apply it to the **current change**, not the whole repository.

1. Did I change only what the task authorized?
2. Is every nullable dereference backed by a real non-null guarantee?
3. Are validation annotations/imports and activation paths real?
4. Does every failure path have an intentional outcome?
5. Did I accidentally turn failure into success/null/empty/default?
6. Does async code expose real completion, failure, and cancellation semantics?
7. Is expected cancellation kept distinct from user-visible failure when appropriate?
8. Does cleanup act on the actual owned resource/handle?
9. Are state/status/protocol meanings consistent and is unknown-state behavior intentional?
10. Did I hardcode deployment/environment data?
11. Did I log secrets or lose throwable/cause information?
12. Are full-object/hot-path logs actually justified?
13. Are important mapping changes, defaults, units, and precision visible?
14. If I saw duplication, did I verify shared meaning/change reason before abstracting?
15. If I added a helper/type/layer, did it reduce cognitive load more than navigation/machinery?
16. If Java `toMap` is used, are null-value and duplicate-key contracts intentional?
17. If Lombok Builder is used, are defaults/inheritance/serialization paths correct?
18. If Vue lists/state are involved, are identity and single-source state semantics correct?
19. Did any review trigger make me expand scope unnecessarily?
20. If behavior changed, what relevant existing test/check did I run? If none was runnable, did I say so accurately?

Before finishing, prefer deleting unnecessary abstraction over polishing it.
