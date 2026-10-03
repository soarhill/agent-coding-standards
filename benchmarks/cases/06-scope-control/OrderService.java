public class OrderService {
    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public void handleCreateMessage(String orderNo) {
        try {
            repository.insert(new Order(orderNo, OrderStatus.PENDING_PAYMENT));
        } catch (DuplicateKeyException e) {
            Order existing = repository.findByOrderNo(orderNo);
            if (existing == null) {
                throw new IllegalStateException("duplicate key but order not found");
            }

            repository.updateStatus(orderNo, OrderStatus.PENDING_PAYMENT);
        }
    }
}
