# Task

A new `excludedLabels` field was added to both models, but the assembler was not updated correctly.

Fix the failing behavior and make the mapping easier to review for future field additions.

Constraints:

- Keep the public records unchanged.
- Do not add reflection, BeanUtils, code-generation libraries, or new dependencies.
- Do not change the tests.
- Explicit mapping is acceptable; reducing line count is not the goal.
- Run:
  ```bash
  javac *.java && java AvailableRoomCriteriaAssemblerTest
  ```
