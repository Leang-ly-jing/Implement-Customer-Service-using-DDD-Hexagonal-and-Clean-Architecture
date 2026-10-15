package kh.edu.istad.platform.application.port.out;

import kh.edu.istad.platform.domain.entity.Payment;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentRepository {
    Mono<Payment> save(Payment payment);
    Mono<Payment> findByOrderId(UUID orderId);
}
