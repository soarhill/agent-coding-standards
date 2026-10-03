# Codex Runtime Skill A/B 分析

本轮 Treatment 平均比 Baseline 低 **0.83 分**，六组中四组平局、两组负向。差异集中在一个可读性偏好和一个条件性的 SSE failure 契约缺口。Scope 与 abstraction 两维的 12 个候选均满分，Coordinator 文件审查也未发现范围扩张、测试修改、新增依赖或架构层。

这是当前 Codex 配置的一轮独立配对实验。每个条件每个 case 只有一个样本，不是模型内部的多次重复采样实验；也没有读取其他模型或旧实验结果，因此不做跨模型排名。

## 1. 是否出现系统性质量退化？

**未被本轮证实，但确实观察到局部负向评分。** Baseline 为 596/600，Treatment 为 591/600；分差依次为 −1、0、0、−4、0、0。没有多数 case 同时退化，也没有 scope、abstraction、repository fit 或 verification 的一致退化。

Case 01 两份 refactor 都保持可见行为，指定测试通过。Judge 更喜欢 Baseline 的 `validate(input)` 后构造结果、字段独立具名限值和 priorities helper，给 Treatment readability 19/20、Baseline 20/20。Treatment 的语义验证 predicates 仍是合理局部抽取，不属于过度设计。这一分差主要是可读性判断，不能作为行为退化证据。

Case 04 的 −4 分更有实质性：Baseline 显式提供 `onerror(error) { throw error; }`，Treatment 仅依靠返回 promise 的 reject。Judge 对 Treatment correctness 和 contract 各扣 2 分。该缺口在 callback/retry 管理失败、promise 保持 pending 的 provider 下有意义；本轮没有 provider 实现，也没有该路径的失败测试，故不能将它描述为已复现的生产回归。

Baseline 五个 case 得 100，另一个因缺运行时验证得 96，存在明显评分上限效应。数据不能证明 Skill 有收益，也不能证明这个小负差必然由 Skill 稳定导致。

## 2. 是否出现 scope creep？

**未观察到。** 每个候选均只修改对应任务的实现文件。Case 03 的三个文件变化分别是 profile 空值处理、RPC absence/failure 分类和已有异常的 cause constructor，均直接服务于任务契约；其余每个 run 只有一个源码文件变化。

Coordinator 检查了完整 changed-file 集、受保护文件哈希和每份 diff；Judge 独立给所有候选 scope 15/15。没有测试、TASK、dependency manifest、其他 case 或 Skill/standards 改动。编译器生成的 `.class` 另行记录，未当作源码改动或混入 patch。

尤其 Case 06，没有顺手修整刻意无关的 `LegacyReportService.java`，也没有重写 repository、订单状态机或消息幂等架构。

## 3. 是否出现 over-engineering？

**未观察到。** Judge 对 12 个候选 abstraction 都给 10/10，Coordinator 逐 diff 审查结论一致。

Case 01 任务本身允许可读性重构，新增 helpers 分别对应 text、price、priorities、limit 等语义边界，没有新 class/interface、通用校验引擎或规则配置层。Case 03 扩展的是已有异常，不是新建结果类型体系。Case 04 使用已有 AbortController 与普通 try/catch/finally，没有引入 session framework。Case 05 共享一个现有状态显示逻辑，Case 06 直接删除错误写操作。

Treatment 修改行数更少并不自动代表质量更好。Case 01 的 Baseline patch 较大，Judge 仍认为其职责阶段更清晰；Case 02 两种显式注释风格也都得到满分。

## 4. progressive disclosure 是否正常？

**按领域和任务触发的 reference 选择正常；严格工作流顺序不是完全零偏差。**

以下为 Worker 报告的实际 reference 加载顺序；所有 Treatment 都先报告读取 `skill/SKILL.md`。表内省略共同路径前缀 `skill/references/`。

| Case / Treatment 标签 | 实际 references | 触发理由 |
|---|---|---|
| 01 / A | core → java-spring 与 anti-patterns 同 batch → review-checklist | Java、可读性 refactor |
| 02 / A | core → java-spring → anti-patterns → review-checklist | Java、字段 mapping |
| 03 / B | core → java-spring → review-checklist | Java absence/failure 契约 |
| 04 / B | core → vue-js-ts → review-checklist | JavaScript promise/cancellation |
| 05 / B | core → vue-js-ts → anti-patterns → review-checklist | Vue identity、重复状态 mapping |
| 06 / B | core → java-spring → review-checklist | Java 单点状态修复 |

core/review 各被 6 个 Treatment 加载；java-spring 4 次、vue-js-ts 2 次、anti-patterns 3 次，共 21 次 reference 文件加载。没有报告 Java 任务加载 Vue reference、Vue/JS 任务加载 Java reference，或不分任务读取全部 references。Baseline 均报告没有 Skill/standards/research 读取。

**05-B 主动报告了顺序偏差**：组件与 core、Vue reference 同 batch 并行读取。Skill 的 numbered workflow 是先读任务和附近代码、识别授权边界，再加载 core 和按需领域 reference。该 batch 的路径和领域选择合规，但不完全符合顺序。它不是禁止材料或另一条件结果污染，所以依照 runbook 的污染重跑条件没有重跑；保留全部 12 次原始采样及该偏差，避免选择性替换结果。若将读序也视为验收硬门槛，本轮应标记为 **11 次无报告偏差，1 次程序性偏差**，不能宣称所有 run 严格零偏差通过。

该偏差不进入匿名 Judge 输入，因为 reference 记录会揭示条件身份。它在揭盲后的独立过程分析中公开。Case 05 两份代码 diff 完全相同，但代码相同并不能消除流程偏差。

实际读取记录来自 Worker 完成报告，而非系统调用审计。参考文件的选择正确也不等于所有规则都必然落实到实现；Case 04 的条件性缺口说明这两件事需要分别评价。

## 5. Case 02 是否诱发不必要抽象？

**没有。** Treatment A 直接把缺失的 `excludedLabels` 映射为 `copyStrings(query.excludedLabels())`，沿用现有 nullable-list/defensive-copy helper，并给 null-query 的 positional defaults 添加字段注释。

没有新增 mapper、generic copy helper、reflection、BeanUtils、配置对象、model 改造或新依赖。Baseline B 也沿用同一 helper，并给两个构造分支的每个参数加目的字段标注。Judge 判 100/100 平局，认为注释风格差异无实质优劣。大 mapping 触发的是查看和显式校对，没有变成自动抽象重构。

证据：[Treatment patch](runs/02-A/change.diff)、[Baseline patch](runs/02-B/change.diff)、[Judge 原始评价](blind/JUDGE.md)。

## 6. Case 04 是否正确区分 cancellation 和 failure？

**返回 promise 的 failure 与主动 cancellation 在代码中被正确区分；SSE callback failure 的完整策略仍有差异。**

两份实现都 await transport promise，并把实际传入 request 的 AbortController 保存在 active handle 中。`closeSSE()` 调用该 controller 的 `abort()`。catch 中检查本次 `controller.signal.aborted`，非取消错误才调用 `setError`；finally 释放当前 handle 时做 controller identity 检查。因此它们没有把主动取消一律显示成网络故障，也没有把普通 promise rejection 伪装成成功。

Coordinator 独立重跑的 3 个现有测试覆盖等待 promise 完成、返回 promise 的异步 failure、关闭时真实 abort。取消测试在 abort 后 resolve，未专门断言 abort 导致 reject 时不显示错误；这一性质来自源码 guard 的审查，不能扩大成该分支已被运行时测试验证。

Treatment B 未提供 `onerror`，Baseline A 显式 throw callback error。若 provider 默认 retry 并保持 promise pending，Treatment 不能保证该 callback failure 及时进入 outer error。Judge 明确把它标为条件性 contract/coverage limitation，而非已观察到的失败。这是本轮最值得关注的局部弱点，但没有足够重复样本将其归因于 Skill。

两份实现都只保留一个 module-level controller，且 loading cleanup 未全面处理重叠请求。TASK 和现有测试没有要求 concurrent sessions；这个共同的覆盖边界没有被当作相对退化，也没有在本轮新增功能或修改测试来扩展任务。

证据：[Baseline patch](runs/04-A/change.diff)、[Treatment patch](runs/04-B/change.diff)、[各 run metadata](runs/04-B/metadata.json)。

## 7. Case 06 是否保持最小修改范围？

**是。** Baseline A 为 +1/−1，Treatment B 为 +1/−2（后者还删除一个相邻空行）。两份都只删除 catch 分支的 `repository.updateStatus(orderNo, PENDING_PAYMENT)`，替换为保存原状态的说明注释。

新单插入 PENDING_PAYMENT、duplicate 之后的查询以及 duplicate-but-not-found 异常均保留。删除状态写操作使已支付、已取消及其他既有状态都不被 duplicate create 重置。没有额外状态条件、helper、repository 修改或 `LegacyReportService` 清理。两份独立测试都 PASS；Judge 均给 scope 15/15、abstraction 10/10、总分 100。

证据：[Baseline patch](runs/06-A/change.diff)、[Treatment patch](runs/06-B/change.diff)。

## 8. 哪些现象可能只是单次采样随机性？

- **Case 01 的 helper/constant 选择和 1 分差**：存在多种同样合理的 refactor，Judge 的偏好也会影响这类细小分差。
- **Case 02 的注释风格、Case 03 的异常消息风格、Case 06 的注释和空行**：可见实现差异均未产生评分差，不宜解释为 Skill 特征。
- **Case 04 是否顺手写出显式 onerror**：这是具体、值得复核的契约差异，但一次采样不能区分稳定 Skill 效应与模型随机选择。缺口是条件性的，不能因它暂时未被现有测试捕获就忽略，也不能夸大为已复现回归。
- **05-B 的读序偏差**：可能是工具并行习惯与本次调度导致；只有一个样本，不能推广为全部 Codex Treatment 必然违背 disclosure 顺序。
- **Case 05 diff 相同与 Case 06 行为相同**：可能说明小任务有明显最小解且 Baseline 已受共同系统指令约束，不能据此认为 Skill 无作用于其他复杂任务。

六个 case 异质，不能当作同一个任务的六次独立重复采样来做稳定效应估计。本轮没有第二次采样、多个 Judge、一致性检验、token/耗时对照或 provider 集成测试。报告仅保留描述性统计，不声称统计显著，也不读取旧结果补充“跨模型”结论。

## 实验约束与结论边界

模型和推理配置使用当前 Coordinator 的同一继承值；API 不暴露精确版本，无法提供已验证的具体 model ID。共享的系统/developer 编码约束及 skill catalog 是两条件共同静态输入，Baseline 并非完全没有工程约束。没有仓库/祖先 AGENTS 内容作为额外标准注入。

隔离是 AUTOPILOT 定义的 fresh context + 独立 worktree + 合作式文件访问约束，不是安全沙箱。冻结文件和源码范围有 Coordinator 的 git/hash 证据；读取合规和实际 reference 加载来自 Worker 报告。新 Judge 只获得 31 个匿名输入文件；其原始输出保存后验证六个 case 及所有分数总和，再发布 REVEAL。见 [PROTOCOL.md](PROTOCOL.md)、[BLINDING.json](BLINDING.json) 和 [REVEAL.json](REVEAL.json)。

这轮结果支持的结论是：**未观察到 scope creep、over-engineering 或 Case 02/06 的边界失控；存在两处局部负向评分及一个读序偏差，尚不足以证明系统性质量退化或稳定收益。** Skill、standards 和 benchmark cases/tests 保持冻结，本轮只提交实验材料，没有根据结果修改 Skill。
