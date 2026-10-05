# Task

This small assembler works, but a recent review found that its implementation makes the reader recover semantics from incidental data representations.

Refactor it for maintainability without changing behavior.

Constraints:

- Keep the public signature of `MatchAssembler.build(...)`.
- Keep output order and values unchanged.
- Do not add dependencies.
- Do not change the tests.
- Do not introduce a framework, hierarchy, or generalized abstraction for this one class.
- Prefer code whose data structures reveal intent.

Run:

```bash
javac *.java && java MatchAssemblerTest
```
