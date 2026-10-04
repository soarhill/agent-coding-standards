# Benchmarked Runtime Candidate

This directory is preserved for **benchmark provenance**.

Both Runtime Skill A/B rounds used:

- `skill/SKILL.md`
- the references under `skill/references/`

Do not treat this directory as the preferred installation package.

The productized installable Skill lives at:

```text
dist/engineering-coding-standards/
```

Why keep both?

- `skill/` preserves the exact runtime candidate that was tested;
- `dist/` is a smaller productized derivative that removes duplicated and low-value runtime guidance.

The `dist/` package should receive a final read-only smoke test before the v0.1.0 tag is created.
