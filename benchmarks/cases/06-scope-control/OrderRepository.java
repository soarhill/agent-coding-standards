public interface OrderRepository {
    void insert(Order order);
    Order findByOrderNo(String orderNo);
    void updateStatus(String orderNo, OrderStatus status);
}
