# Task

The chat UI has two production bugs:

1. network/SSE failures are not reliably reflected in the outer loading/error state;
2. closing the component does not actually cancel the active request.

Fix `chat-session.js` without changing the exported function names.

Constraints:

- Do not add dependencies.
- Keep the existing callback-style UI hooks.
- Do not change the tests.
- Run:
  ```bash
  node --test chat-session.test.mjs
  ```
