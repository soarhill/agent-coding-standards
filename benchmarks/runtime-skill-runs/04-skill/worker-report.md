# Worker report — 04-skill (verbatim final message)

Review checklist passes against the change: only the two authorized bugs were touched; failure/cancellation semantics are intentional (self-abort is commented as expected shutdown, not a UI error); cleanup acts on the real owned handle (`AbortController`, with an identity check so a finished request's `finally` can't clear a newer in-flight controller); no new abstractions, deps, or scope creep; exported names and tests untouched.

## Report

**Skill reference files actually loaded**
- `skill/SKILL.md` (entry)
- `skill/references/core.md` (always required)
- `skill/references/vue-js-ts.md` (JavaScript async/lifecycle task)
- `skill/references/review-checklist.md` (final diff review)
- Not loaded (irrelevant): `references/java-spring.md`, `references/anti-patterns.md`

**Files changed**
- `D:\Desktop\study\agent-coding-runtime-skill-bench\04-skill\benchmarks\cases\04-promise-sse\chat-session.js` (only file modified)

**Fixes**
1. *Loading/error state vs. real completion*: `fetchEventSource(...)` was a floating promise, so the synchronous `try/catch` never saw its later rejection and `finally` cleared loading immediately. Now the call is `await`ed inside `try`, so async failures reach `setError(error.message)` and `setLoading(false)` runs at real completion. Per the Skill's cancellation rule, a rejection is not reported as a UI error when `controller.signal.aborted` is true (abort via `closeSSE` is expected shutdown), with a comment stating that policy.
2. *closeSSE did nothing*: it read an `eventSource` variable that was never assigned. The real owned handle is the per-request `AbortController`; it is now stored in `activeController`, `closeSSE()` aborts it, and `finally` clears it only when it is still the current request (identity check), so a stale completion can't orphan a newer in-flight request's handle.

Constraints honored: no new dependencies, callback-style UI hooks kept, exported function names (`sendMessage`, `closeSSE`) unchanged, tests untouched.

**Exact verification result** (`node --test chat-session.test.mjs`, run from the case directory):
```
✔ sendMessage stays loading until the SSE promise completes (1.1205ms)
✔ sendMessage reports async failure (0.1865ms)
✔ closeSSE aborts the active request (0.281ms)
ℹ tests 3
ℹ pass 3
ℹ fail 0
```
