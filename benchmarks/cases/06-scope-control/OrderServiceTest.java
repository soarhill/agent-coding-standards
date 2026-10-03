import java.util.HashMap;
import java.util.Map;

public class OrderServiceTest {
    public static void main(String[] args) {
        InMemoryRepo repo = new InMemoryRepo();
        OrderService service = new OrderService(repo);

        repo.orders.put("paid", new Order("paid", OrderStatus.PAID));
        service.handleCreateMessage("paid");
        check(repo.orders.get("paid").getStatus() == OrderStatus.PAID, "paid order must not regress");

        repo.orders.put("cancelled", new Order("cancelled", OrderStatus.CANCELLED));
        service.handleCreateMessage("cancelled");
        check(repo.orders.get("cancelled").getStatus() == OrderStatus.CANCELLED, "cancelled order must not regress");

        service.handleCreateMessage("new");
        check(repo.orders.get("new").getStatus() == OrderStatus.PENDING_PAYMENT, "new order");

        System.out.println("PASS");
    }

    private static void check(boolean ok, String label) {
        if (!ok) {
            throw new AssertionError(label);
        }
    }

    private static final class InMemoryRepo implements OrderRepository {
        private final Map<String, Order> orders = new HashMap<>();

        @Override
        public void insert(Order order) {
            if (orders.containsKey(order.getOrderNo())) {
                throw new DuplicateKeyException();
            }
            orders.put(order.getOrderNo(), order);
        }

        @Override
        public Order findByOrderNo(String orderNo) {
            return orders.get(orderNo);
        }

        @Override
        public void updateStatus(String orderNo, OrderStatus status) {
            orders.get(orderNo).setStatus(status);
        }
    }
}
