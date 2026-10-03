# Task

This Vue component has two user-visible bugs:

1. the same backend file status can show different text in the table and edit dialog;
2. after inserting/removing/reordering rows, the text input inside a row can stay attached to the wrong logical file.

Fix those two issues.

Constraints:

- Do not change the backend status codes.
- Do not change API calls.
- Do not change styling or unrelated markup.
- Keep the component in Vue 3 Composition API.
- Do not add dependencies.
