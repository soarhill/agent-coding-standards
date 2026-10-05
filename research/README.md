# Research provenance

This directory documents the evidence base used to derive the coding standards.

## Source material

The first research round independently reviewed four local tutorial projects:

- `D:\software\IDEA\weblog`
- `D:\software\IDEA\xiaoha-ai-robot\project`
- `D:\software\IDEA\xiaohashu`
- `D:\software\IDEA\xiaoha-seckill`

Two different coding models independently produced research reports:

1. **Z Code** — `phase1-research-report.md`
2. **Codex** — `四项目代码工程质量研究报告.md`

They were then cross-reviewed.

Later targeted validation notes are kept here when a real-project output exposes a gap that needs external evidence. See:

- `naming-and-organization-validation.md` — Alibaba P3C, Google Java Style, Oracle, Maven, Gradle, Vue, and npm guidance used to calibrate naming/package/module rules.

## Evidence policy

The four projects share the same tutorial lineage. Therefore:

- repeated patterns are **not** four independent votes;
- a recurring pattern may be a good practice, a teaching shortcut, a personal style, or a repeated defect;
- project code is treated as evidence for a problem or a local solution, not as an authority;
- rules that prescribe a specific implementation need stronger support than rules that identify a concrete defect.

The core rule for this repository is:

> **Observed ≠ Recommended.**

## What the cross-review established

The cross-review showed that the two reports are complementary:

- Z Code is stronger at turning observations into explicit, agent-executable rules and at surfacing repeated code, configuration leakage, dead code, and AI-coding failure patterns.
- Codex is stronger at preserving applicability boundaries and challenging rules that jump from “this is a problem” to “therefore every project must use this one implementation.”

The cross-review also corrected factual mistakes in the first-pass research. Examples called out during review include:

- `weblog` does contain a constructor-injection example in `TagServiceImpl`;
- `xiaoha-seckill` uses Spring AMQP / RabbitMQ rather than RocketMQ;
- several additional findings from the Codex report were independently rechecked against source and confirmed, including a conditional-argument `trim()` null hazard, a missing seckill result-state branch, a double-serialization path, and inconsistent exact-end-time semantics.

These corrections are a reminder that **no single model report is itself a standard**.

## Current status

The raw reports and cross-review text are research artifacts. The actionable synthesis lives in:

- `../standards/candidate-rules.md`
- the productized runtime package under `../dist/engineering-coding-standards/`

Rules are kept narrow and are promoted only when supported by concrete failures, repeated evidence, or verified language/framework/tooling guidance.
