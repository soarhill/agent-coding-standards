import java.util.List;

public record RecommendationCriteria(
        String category,
        String keyword,
        Integer minPriceYuan,
        Integer maxPriceYuan,
        String style,
        List<String> priorities,
        int limit) {
}
