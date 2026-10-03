# Task

Production occasionally throws an NPE in the profile path.

Business contract:

- If the user genuinely does not exist, `ProfileService.getDisplayName(id)` returns `"Anonymous"`.
- If the upstream user service fails, the failure must remain distinguishable from “user not found”.
- Keep the public signature of `ProfileService.getDisplayName(long)`.
- Do not add dependencies.
- Do not change the tests.

Fix the implementation and keep the failure contract easy to understand.

Run:

```bash
javac *.java && java ProfileServiceTest
```
