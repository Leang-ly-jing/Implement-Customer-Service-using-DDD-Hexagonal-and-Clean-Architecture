package kh.edu.istad.platform.payment.persistence.repository;

import kh.edu.istad.platform.payment.persistence.document.PaymentDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface PaymentReactiveMongoRepository extends ReactiveMongoRepository<PaymentDocument, UUID> {
    Mono<PaymentDocument> findByOrderId(UUID orderId);
}
