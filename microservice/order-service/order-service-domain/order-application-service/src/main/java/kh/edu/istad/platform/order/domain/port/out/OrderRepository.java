package kh.edu.istad.platform.order.domain.port.out;

import kh.edu.istad.common.domain.valueobject.TrackingId;
import kh.edu.istad.platform.order.domain.entity.Order;
import reactor.core.publisher.Mono;


import java.util.Optional;
import java.util.UUID;
public interface OrderRepository {
    Mono<Order> save(Order order);
    Mono<Order> findById(UUID orderId);
    Mono<Order> findByTrackingId(TrackingId trackingId);
}
