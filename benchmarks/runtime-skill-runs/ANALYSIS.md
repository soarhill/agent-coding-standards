# Round 2 — Runtime Skill A/B Benchmark Analysis

> Human-facing synthesis of the blind-judged round-2 artifacts (`SUMMARY.md`, `judge/JUDGMENT.md`, per-run `metadata.md`).
> Question under test: does the full research standard compressed into a thin runtime Skill (`skill/SKILL.md` + `references/`) still improve code quality over a no-standards baseline, without causing scope creep or over-engineering?

## 1. Headline: effectively even, with zero harmful behavior

Skill 587 vs base 588 (−1 point over 600; −0.17 average). Every per-case and per-dimension delta is within ±1 point. Skill record: 1 win / 3 ties / 2 losses.

With six cases, one run per condition, and one judge pass, a ±1-point aggregate difference is noise. The defensible reading is **parity**: under this suite the thin Skill neither helped nor hurt measurable quality.

What the round does establish with reasonable confidence:

- **No scope creep.** Scope discipline 15/15 in all 12 runs; the case-06 `LegacyReportService` bait was untouched by both conditions (the skill worker explicitly reasoned "review trigger — inspect, not refactor", which is the Skill's REVIEW TRIGGER semantics applied correctly).
- **No over-engineering.** No new classes/layers/dependencies anywhere; abstraction was full-mark or +1 (case 02 helper consolidation).
- **Progressive disclosure works.** Reference loading matched task type exactly in all 6 treatment runs (SUMMARY table). No worker loaded irrelevant references; case 06 skipped anti-patterns.md with an explicitly correct justification.
- **Correctness floor held.** All runnable cases passed independently rerun verification in both conditions; case 05 recorded as unavailable, never fabricated.

## 2. Why the delta vanished (round 1 was +1.67 for the full ruleset)

Three forces, in probable order of importance:

1. **Ceiling effect.** This round's baseline averaged 98/100 (vs round-1 baseline ~89/95 ≈ 93.7% normalized). GLM-5.3 without any standards already produced near-rubric-maximal answers on these six small cases. When the baseline is at the ceiling, a standards document has no room to add points — it can only lose them. Cross-round score levels are NOT directly comparable (different judge pass, round 1 scored dimensions 1–6 of 95 while round 2 scored all 7 of 100), but the within-round pattern is what matters, and within round 2 the baseline was already excellent.
2. **Heavy convergence.** In 4/6 cases the two conditions produced structurally identical solutions (same helper decomposition in 01; same three-file contract fix in 03; functionally identical one-line-apart fixes in 04; byte-identical patch in 05). The model converges on the same canonical fix regardless of standards once the task is well-specified — these tasks have a small solution space at GLM-5.3's level.
3. **The round-1 wording gap was fixed before this round.** Round 1's only real negative signal was case 04 (standards run reported intentional aborts as UI errors). That was addressed in v1.1 and is baked into the current Skill — the round-2 skill worker explicitly cited "the Skill's cancellation rule" and treated abort as expected shutdown. Both conditions scored 99/99. In other words, the thin Skill successfully carried the one lesson round 1 taught, and the failure mode it was fixing is gone.

## 3. Case-specific behavior

### Case 02 — mapping/abstraction (the only substantive design divergence)

Round-1 baseline built a 17-field fluent builder (−13). This round's baseline instead produced the "named local per record component + one line per constructor argument + `empty()` factory + Javadoc convention" style — essentially the solution that won round 1 for standards. The skill run did the lighter thing: semantic blank-line grouping of the 17 args plus merging the two copy helpers into one generic `copy`.

The blind judge preferred the baseline's heavier, documented convention for this task's explicit "easier to review for future field additions" requirement (+1 readability, +1 contract, −1 abstraction vs skill).

Reading: this is solution-space variance, not a skill defect — round 1's *standards* condition won this case with the heavy style and round 2's *baseline* produced it unaided. But it is worth noting the Skill's "smallest coherent change / do not optimize for more patterns" framing may bias toward the lighter interpretation when a task explicitly asks for review-robustness machinery. One case, one run — an observation to watch, not a rule change.

### Case 04 — cancellation/error (round 1's pain point)

Both conditions: await the real promise, abort through the real `AbortController`, suppress `setError` when `signal.aborted` (with an explicit comment), identity-guard the controller cleanup. The skill run cited the Skill's cancellation rule as its reason. 99/99 tie. The v1.1→Skill transfer of the cancellation principle is validated in the intended direction.

### Case 06 — scope control

Three-line guarded fixes both sides; decoy untouched both sides. The skill run's allowlist (`== PENDING_PAYMENT`, "duplicate can only re-affirm, never change status") was called out by the judge as the stronger long-term invariant; the base run's blocklist got a documenting comment instead. Skill won this case +1 (also the base run left `.class` artifacts). Allowlist-vs-blocklist is again sampling variance, but note the Skill's contract emphasis ("truthful contracts", state semantics) is at least consistent with the direction the judge preferred.

### Hygiene note (`.class` artifacts)

The judge saw candidate "B" leave build artifacts twice and read it as a pattern. Unblinding shows one occurrence per condition (03: skill; 06: base). Not systematic; the two resulting −1s cancel across conditions. Judge conclusions on this point should be discounted accordingly.

## 4. Skill self-review: ambiguous, conflicting, or behavior-harmful rules?

- No rule was observed to cause wrong behavior, scope creep, or over-abstraction in any of the 6 treatment runs.
- Progressive disclosure instructions were followed literally and correctly in 6/6 runs.
- The only tension observed: the "smallest coherent change"/anti-ceremony framing vs tasks that explicitly request heavier review-robustness structure (case 02). SKILL.md's Boundaries already say repository-local conventions and task requirements win; no wording change is indicated by one tie-ish case.
- Treatment workers voluntarily ran extra out-of-band verification (01: 21 edge cases in a temp dir; 05: `node --check` + stale-identifier grep when no harness exists). This aligns with SKILL.md workflow step 7 ("run the closest relevant existing tests/checks when available and report the real result") and is a mild positive-signal behavioral difference that the rubric's diff-based scoring barely captures.

## 5. Comparison with first round (post-unblinding)

| | Round 1 (full `candidate-rules.md`) | Round 2 (thin runtime Skill) |
|---|---|---|
| Scoring | dims 1–6, max 95 | dims 1–7, max 100 |
| Baseline avg | 89.00 (93.7% normalized) | 98.00 |
| Treatment avg | 90.67 (95.4% normalized) | 97.83 |
| Delta | +1.67 | −0.17 |
| Record (treatment) | 3W / 2L / 1T | 1W / 3T / 2L |
| Scope creep / over-eng | none / none | none / none |

Cross-round score levels are not directly comparable (different judge passes and maxima); case-level patterns are:

- **Case 02:** round 1 standards +13 (baseline built a builder); round 2 skill −1 (baseline produced the round-1-winning style itself). The anti-builder guardrail didn't have a builder to prevent this time; variance dominated.
- **Case 04:** round 1 standards −8 (abort-as-error); round 2 skill tie 99/99 with the cancellation rule explicitly applied. The one concrete rule fix between rounds (v1.1 cancellation wording → Skill) demonstrably carried over.
- **Case 03:** round 1 −2 (judge preferred baseline's `Optional`); round 2 both conditions kept the nullable signature + documented contract (the Skill's compatibility framing), 98/99 split only by hygiene. The Skill did not reproduce round 1's contract-style penalty.
- **Case 05:** round 1 +3; round 2 byte-identical diffs. The canonical minimal fix is now reached unaided.
- **Case 01:** round 1 +4; round 2 tie — same decomposition independently.

Net: the thin Skill preserved the full ruleset's safety properties (no creep, no over-abstraction) and inherited its main fix (cancellation), while the measurable quality delta evaporated under a much stronger baseline. Whether the Skill adds value at the margin is now untestable with these six cases and this model — the suite is saturated at GLM-5.3's level.

## 6. Limitations

- Single model, single run per condition, six small cases: any |delta| < ~5 points is unresolvable.
- One blind judge pass (GLM-5.3); judge reasoning is evidence, not ground truth (its cross-candidate "pattern" for `.class` artifacts did not survive unblinding).
- Workers are prompt-isolated, not sandboxed (same user, shared disk). Audits found no violations, but adversarial isolation was never the threat model.
- Case 05's verification dimension is structurally capped (3/5 neutral) for both conditions.

## 7. What next (recommendation, not a rule change)

1. Do not modify `skill/` based on this round — no harmful behavior was observed and the one candidate tension (case 02 lightness) is single-run variance.
2. The suite is saturated for GLM-5.3: to keep measuring treatment effects, either add harder/larger cases (where baseline is not at ceiling), or run the planned second participant model, where the baseline floor is expected to be lower.
3. If a third round runs, consider 2–3 repetitions per condition per case to separate variance from effect — this round's only real lesson about methodology is that one run per cell cannot resolve ±1 point.
