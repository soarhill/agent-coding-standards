# Worker report — 03-skill (verbatim final message)

The diff reviews clean against the checklist: every failure path now has an intentional outcome, `null` keeps exactly one stable meaning ("user does not exist"), causes are preserved, no scope was expanded, and the exact verification command from TASK.md was run with a real PASS.

## Report

**Skill reference files actually loaded** (progressive disclosure per SKILL.md workflow):
- `skill/SKILL.md`
- `skill/references/core.md` (required for every task)
- `skill/references/java-spring.md` (Java code; its exceptions section applies — no Spring/logging-framework code present)
- `skill/references/review-checklist.md` (final diff review)
- Not loaded: `references/anti-patterns.md` (refactor/duplication/mapping work — not this task), `references/vue-js-ts.md` (no Vue/JS/TS code)

**Root cause**: `UserRpcService.findUser` collapsed three distinct outcomes — "user not found", "upstream failed response", and "upstream threw" — all into `return null`. That both caused the NPE at `user.nickname()` in `ProfileService` and destroyed the failure contract.

**Files changed** (all in `D:\Desktop\study\agent-coding-runtime-skill-bench\03-skill\benchmarks\cases\03-error-null-contract\`):
- `UserRpcService.java` — `findUser` now returns `null` only for the business outcome "user does not exist"; every upstream failure (thrown exception, null response, `success=false`, found response missing data) throws `UpstreamUserServiceException` with a non-blank, id-tagged message; the caught `RuntimeException` is wrapped with its cause preserved. The ad-hoc `System.err` print was removed since the propagated exception now carries that information (no logging framework exists and dependencies are forbidden).
- `ProfileService.java` — `getDisplayName(long)` signature unchanged; `user == null ? "Anonymous" : user.nickname()`; upstream failures propagate unchanged. Short Javadoc on both methods makes the null-vs-throw contract explicit.
- `UpstreamUserServiceException.java` — added a `(String, Throwable)` constructor for cause-preserving translation (per core.md/java-spring.md); the original `String` constructor is kept.

Tests, `UserClient`, `UserClientResponse`, and `UserDto` were not touched; no dependencies added.

**Verification result** (exact command from TASK.md):
```
$ javac *.java && java ProfileServiceTest
PASS
```
All four scenarios pass: found user returns "Ada", missing user returns "Anonymous", failed response and thrown exception both surface as `UpstreamUserServiceException` with non-blank messages.
