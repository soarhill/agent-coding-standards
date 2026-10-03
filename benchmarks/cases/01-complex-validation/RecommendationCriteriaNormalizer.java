import java.util.List;

public final class RecommendationCriteriaNormalizer {
    private static final int MAX_RESULTS = 20;

    private RecommendationCriteriaNormalizer() {
    }

    public static RecommendationCriteria normalize(RecommendationCriteria input) {
        if (input == null
                || !hasText(input.category())
                || input.category().length() > 80
                || tooLong(input.keyword(), 160)
                || tooLong(input.style(), 80)
                || (input.minPriceYuan() != null && input.minPriceYuan() < 0)
                || (input.maxPriceYuan() != null && input.maxPriceYuan() < 0)
                || (input.minPriceYuan() != null && input.maxPriceYuan() != null
                    && input.minPriceYuan() > input.maxPriceYuan())
                || (input.priorities() != null && (input.priorities().size() > 5
                    || input.priorities().stream().anyMatch(value -> tooLong(value, 80))))) {
            throw new BusinessException("INVALID_CRITERIA");
        }

        List<String> priorities = input.priorities() == null
                ? List.of()
                : input.priorities().stream()
                    .filter(RecommendationCriteriaNormalizer::hasText)
                    .map(String::trim)
                    .toList();

        return new RecommendationCriteria(
                input.category().trim(),
                trimToNull(input.keyword()),
                input.minPriceYuan(),
                input.maxPriceYuan(),
                trimToNull(input.style()),
                priorities,
                Math.max(1, Math.min(MAX_RESULTS, input.limit() <= 0 ? 4 : input.limit())));
    }

    private static boolean tooLong(String value, int max) {
        return value != null && value.length() > max;
    }

    private static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static String trimToNull(String value) {
        if (!hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
