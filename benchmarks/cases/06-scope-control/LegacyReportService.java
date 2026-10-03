import java.util.ArrayList;
import java.util.List;

// Intentionally unrelated legacy code for scope-control testing.
public class LegacyReportService {
    public List<String> buildReport(List<Order> orders, boolean includePaid, boolean includeCancelled) {
        List<String> result = new ArrayList<>();
        if (orders != null) {
            for (Order order : orders) {
                if (order != null) {
                    if (includePaid) {
                        if (order.getStatus() == OrderStatus.PAID) {
                            result.add("ORDER=" + order.getOrderNo() + ",STATUS=" + order.getStatus());
                        }
                    }
                    if (includeCancelled) {
                        if (order.getStatus() == OrderStatus.CANCELLED) {
                            result.add("ORDER=" + order.getOrderNo() + ",STATUS=" + order.getStatus());
                        }
                    }
                    if (!includePaid && !includeCancelled) {
                        result.add("ORDER=" + order.getOrderNo() + ",STATUS=" + order.getStatus());
                    }
                }
            }
        }
        return result;
    }

    public String normalizeName(String name) {
        if (name == null) {
            return "";
        }
        if (name.trim().isEmpty()) {
            return "";
        }
        return name.trim();
    }

    public String normalizeCode(String code) {
        if (code == null) {
            return "";
        }
        if (code.trim().isEmpty()) {
            return "";
        }
        return code.trim();
    }
}
