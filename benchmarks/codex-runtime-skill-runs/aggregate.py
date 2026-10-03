"""Aggregate saved blind scores only after the Coordinator publishes REVEAL.json."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parent
DIMENSIONS = {
    'correctness': 30,
    'readability': 20,
    'contract': 15,
    'scope': 15,
    'abstraction': 10,
    'repository_fit': 5,
    'verification': 5,
}


def load(path):
    return json.loads(path.read_text(encoding='utf-8-sig'))


def main():
    reveal = load(ROOT / 'REVEAL.json')
    judge = load(ROOT / 'blind' / 'scores.json')
    by_case = {item['case']: item for item in judge['cases']}
    assert set(by_case) == {'01', '02', '03', '04', '05', '06'}
    rows = []
    for mapping in reveal['cases']:
        case_id = mapping['case'][:2]
        item = by_case[case_id]
        for label in ('A', 'B'):
            for key, maximum in DIMENSIONS.items():
                assert 0 <= item[label][key] <= maximum, (case_id, label, key)
            assert item[label]['total'] == sum(item[label][key] for key in DIMENSIONS)
        baseline_label = next(label for label, condition in mapping['labels'].items() if condition == 'baseline')
        treatment_label = next(label for label, condition in mapping['labels'].items() if condition == 'treatment')
        baseline = item[baseline_label]
        treatment = item[treatment_label]
        rows.append({
            'case': mapping['case'],
            'baseline_label': baseline_label,
            'treatment_label': treatment_label,
            'baseline': baseline,
            'treatment': treatment,
            'dimension_deltas': {key: treatment[key] - baseline[key] for key in DIMENSIONS},
            'total_delta': treatment['total'] - baseline['total'],
            'blind_winner': item['winner'],
            'blind_reason': item['reason'],
        })
    aggregate = {
        'cases': rows,
        'baseline_mean': sum(row['baseline']['total'] for row in rows) / 6,
        'treatment_mean': sum(row['treatment']['total'] for row in rows) / 6,
        'mean_delta': sum(row['total_delta'] for row in rows) / 6,
        'treatment_score_wins': sum(row['total_delta'] > 0 for row in rows),
        'score_ties': sum(row['total_delta'] == 0 for row in rows),
        'treatment_score_losses': sum(row['total_delta'] < 0 for row in rows),
        'dimension_means': {
            key: {
                'maximum': maximum,
                'baseline': sum(row['baseline'][key] for row in rows) / 6,
                'treatment': sum(row['treatment'][key] for row in rows) / 6,
                'delta': sum(row['dimension_deltas'][key] for row in rows) / 6,
            }
            for key, maximum in DIMENSIONS.items()
        },
        'interpretation': 'Descriptive single-sample paired comparison; no statistical significance or cross-model causal claim.',
    }
    (ROOT / 'AGGREGATE.json').write_text(json.dumps(aggregate, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')
    print(json.dumps(aggregate, ensure_ascii=False, indent=2))


if __name__ == '__main__':
    main()
