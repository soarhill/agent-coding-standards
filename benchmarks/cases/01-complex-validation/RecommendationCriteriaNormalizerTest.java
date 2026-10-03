import java.util.List;

public class RecommendationCriteriaNormalizerTest {
    public static void main(String[] args) {
        RecommendationCriteria normalized = RecommendationCriteriaNormalizer.normalize(
                new RecommendationCriteria(
                        "  phone  ",
                        "  camera ",
                        1000,
                        5000,
                        "  minimal ",
                        List.of(" battery ", "", "camera"),
                        0));

        check("phone".equals(normalized.category()), "category trim");
        check("camera".equals(normalized.keyword()), "keyword trim");
        check("minimal".equals(normalized.style()), "style trim");
        check(normalized.priorities().equals(List.of("battery", "camera")), "priorities normalize");
        check(normalized.limit() == 4, "default limit");

        RecommendationCriteria maxed = RecommendationCriteriaNormalizer.normalize(
                new RecommendationCriteria("phone", null, null, null, null, null, 99));
        check(maxed.limit() == 20, "limit clamp");

        expectInvalid(null);
        expectInvalid(new RecommendationCriteria(" ", null, null, null, null, null, 1));
        expectInvalid(new RecommendationCriteria("phone", null, -1, null, null, null, 1));
        expectInvalid(new RecommendationCriteria("phone", null, 100, 10, null, null, 1));
        expectInvalid(new RecommendationCriteria(
                "phone", null, null, null, null,
                List.of("a", "b", "c", "d", "e", "f"), 1));

        System.out.println("PASS");
    }

    private static void expectInvalid(RecommendationCriteria input) {
        try {
            RecommendationCriteriaNormalizer.normalize(input);
            throw new AssertionError("expected invalid criteria");
        } catch (BusinessException expected) {
            check("INVALID_CRITERIA".equals(expected.getMessage()), "error message");
        }
    }

    private static void check(boolean ok, String label) {
        if (!ok) {
            throw new AssertionError(label);
        }
    }
}
