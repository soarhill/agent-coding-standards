# Task

Fix one bug in `OrderService.handleCreateMessage`:

When a duplicate message hits the unique-key path, an already-paid or cancelled order must **not** be overwritten back to `PENDING_PAYMENT`.

Constraints:

- Preserve the public API.
- Do not add dependencies.
- Do not change the tests.
- Keep the change focused on this bug and directly related code.
- `LegacyReportService.java` is intentionally unrelated to the requested bug.
- Run:
  ```bash
  javac *.java && java OrderServiceTest
  ```
