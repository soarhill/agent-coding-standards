# Codex Runtime Skill A/B 验收结果

日期：2026-10-03，Asia/Shanghai。起始 commit：`2f1007632c29d74bc4e385d9f2d94de3790a2cbc`。

本轮完成 6 个 case × 2 个条件，共 12 次独立 Worker run；全部使用 fresh context、相同继承模型配置和独立 worktree。12 次结束后才启动一个全新的 Blind Judge；六个 case 全部评分并保存原始输出后才揭盲。

**Baseline 均分 99.33，Treatment 均分 98.50，平均分差 −0.83。Treatment 0 胜、4 平、2 负。没有观察到 scope creep 或 over-engineering；系统性质量退化未被本轮小样本证实。** 分差集中在 Case 01 的可读性（−1）和 Case 04 的 SSE error callback 契约覆盖（−4）。

## 每个 case 的揭盲成绩

| Case | Baseline 标签 / 分数 | Treatment 标签 / 分数 | Treatment − Baseline | 结论 |
|---|---|---|---:|---|
| 01 complex validation | B / 100 | A / 99 | −1 | Baseline 的 validate/construct 分离与具名限值略清晰 |
| 02 mechanical mapping | B / 100 | A / 100 | 0 | 均修复 excludedLabels，显式映射，无额外抽象 |
| 03 error/null contract | A / 100 | B / 100 | 0 | 均区分不存在与上游失败，并保留异常原因 |
| 04 promise/SSE | A / 100 | B / 96 | −4 | 均修复 await 和真实取消；Treatment 缺显式 onerror 策略 |
| 05 Vue state | A / 96 | B / 96 | 0 | diff 完全一致；无运行时 harness |
| 06 scope control | A / 100 | B / 100 | 0 | 均只移除重复消息路径的状态重置 |
| **平均** | **99.33** | **98.50** | **−0.83** | **0 胜 / 4 平 / 2 负** |

Case 04 的扣分依据是条件性契约缺口：若 SSE provider 在 callback 中重试且不 reject promise，Treatment 的外层 error 不会因此更新。provider 实现未作为证据提供，不能据此宣称已复现生产失败。两份候选的现有 3 个测试都通过。

## 各维度平均分

| 维度 | 满分 | Baseline | Treatment | 平均分差 |
|---|---:|---:|---:|---:|
| Correctness | 30 | 30.00 | 29.67 | −0.33 |
| Readability | 20 | 20.00 | 19.83 | −0.17 |
| Contract | 15 | 15.00 | 14.67 | −0.33 |
| Scope | 15 | 15.00 | 15.00 | 0.00 |
| Abstraction | 10 | 10.00 | 10.00 | 0.00 |
| Repository fit | 5 | 5.00 | 5.00 | 0.00 |
| Verification | 5 | 4.33 | 4.33 | 0.00 |

维度数值独立四舍五入；精确值与逐 case 维度分差见 [AGGREGATE.json](AGGREGATE.json)。

## 验证与流程完整性

- Coordinator 独立重跑全部 TASK 指定命令：10 个 run 通过；Case 05 两个 run 未提供自动 harness，记录为 `not_provided`。
- 12 份 diff 均通过对应最终 worktree 的反向应用检查。
- 12 个 run 均无范围外源码改动、测试/TASK 修改、依赖 manifest 修改或受保护文件哈希变化。
- Coordinator 与 Judge 均未发现不必要抽象；Case 06 的 `LegacyReportService.java` 未修改。
- 主仓库 `skill/`、`standards/`、原有 benchmark cases/tests/rubric/runbook 哈希保持不变。Worker 实现仅存在于独立 worktree 和结果 patch。
- Treatment reference 选择均与任务相关，未报告无关领域或全量加载。**05-B 报告了读序偏差**：组件、core、Vue reference 在同一并行 batch 加载，未严格完成附近代码阅读后再加载 reference。该 run 保留原样，未选择性重采样；不能将本轮表述为零流程偏差。
- 读路径遵守情况依据 Worker 报告；隔离沿用 runbook 的合作式约束，没有 OS 级读审计。Judge 报告未读取 mapping 或其他禁止材料。
- 模型/推理配置全部继承当前 Coordinator、未指定 override；工具不暴露精确 runtime model ID 或 reasoning setting，未猜测填写。见 [PREFLIGHT.json](PREFLIGHT.json) 与 [RUNS.json](RUNS.json)。

## 可复核材料

- [ANALYSIS.md](ANALYSIS.md)：八项重点问题、证据边界与采样解释。
- [blind/JUDGE.md](blind/JUDGE.md)：Judge 原始逐维度评价，未改写。
- [blind/scores.json](blind/scores.json)：Judge 原始分数。
- [REVEAL.json](REVEAL.json)：六个 case 的 A/B 映射，全部评分后发布。
- [BLINDING.json](BLINDING.json)：Judge 输入 manifest、原始输出哈希与揭盲门槛记录。
- [PATCH-INTEGRITY.json](PATCH-INTEGRITY.json)：12 份 patch 的哈希与反向应用结果。
- [runs/](runs/)：每个 run 的 `change.diff`、factual `metadata.json`、验证输出、git status 和 `record.json`；后者含 Worker 报告、实际 reference 加载、流程偏差及 Coordinator 审查。
- [PROTOCOL.md](PROTOCOL.md)：隔离方法与限制；[capture-run.ps1](capture-run.ps1) 和 [aggregate.py](aggregate.py) 保存收集/计算流程。

每个条件每个 case 仅一次采样，只有一位 Judge，Baseline 评分接近上限。本轮可报告轻微负向评分与局部差异，不能证明稳定因果效应、跨模型优劣或 Skill 必然有害/有益。本轮未根据结果修改 Skill。
