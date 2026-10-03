# Worker report — 03-base (verbatim final message)

Task complete.

**Root cause:** `UserRpcService.findUser()` collapsed two fundamentally different outcomes into one `null` return — "upstream service successfully says the user does not exist" and "upstream service failed" (unsuccessful response, null response, or a thrown exception, which was also swallowed and logged to stderr). `ProfileService` then dereferenced that `null`, producing the production NPE and making failures indistinguishable from "not found".

**Files changed** (all under `D:\Desktop\study\agent-coding-runtime-skill-bench\03-base\benchmarks\cases\03-error-null-contract\`):

1. `UserRpcService.java` — `findUser` now has a single, documented contract:
   - returns the user when upstream reports it found;
   - returns `null` **only** when upstream succeeded and reports not-found;
   - throws `UpstreamUserServiceException` for every failure mode: client threw (root cause chained), null response, `success == false` (message falls back to "no error message provided" when blank so the message is never blank), and the malformed "found but no data" case.
2. `UpstreamUserServiceException.java` — added a `(String, Throwable)` constructor so thrown-exception failures keep their root cause for debugging; existing `(String)` constructor unchanged.
3. `ProfileService.java` — maps the null (not-found) result to `"Anonymous"`; upstream failures propagate untouched as `UpstreamUserServiceException`. Public signature `getDisplayName(long)` unchanged; the "Anonymous" constant is named for readability.

Not modified: `ProfileServiceTest.java` (per constraint), `UserClient.java`, `UserClientResponse.java`, `UserDto.java`. No dependencies added; `.class` artifacts from the verification run were removed.

**Verification result:** ran `javac *.java && java ProfileServiceTest` (JDK 17.0.14) from the case directory — compiled cleanly, program printed `PASS`, exit code 0.
