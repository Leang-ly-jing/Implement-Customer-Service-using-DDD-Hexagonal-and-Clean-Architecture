package kh.edu.istad.platform.order.domain.event;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.platform.order.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderPaidEvent implements DomainEvent<Order> {
    private final Order order;
    private final ZonedDateTime createAt;

    public OrderPaidEvent(Order order, ZonedDateTime createAt) {
        this.order = order;
        this.createAt = createAt;
    }

    public Order getOrder() {
        return order;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }
}
