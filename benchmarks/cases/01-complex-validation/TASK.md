# Task

The behavior of `RecommendationCriteriaNormalizer.normalize` is correct, but the method has become difficult to read and modify.

Improve its readability and maintainability **without changing observable behavior**.

Constraints:

- Do not change the public API of `RecommendationCriteria` or `RecommendationCriteriaNormalizer.normalize`.
- Do not add dependencies.
- Do not change the tests.
- Avoid creating classes/interfaces unless they provide a real semantic boundary.
- Run:
  ```bash
  javac *.java && java RecommendationCriteriaNormalizerTest
  ```
