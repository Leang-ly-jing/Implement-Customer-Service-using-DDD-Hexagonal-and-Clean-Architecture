package kh.edu.istad.platform.payment.persistence.repository;

import kh.edu.istad.platform.payment.persistence.document.CreditHistoryDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface CreditHistoryReactiveMongoRepository extends ReactiveMongoRepository<CreditHistoryDocument, UUID> {
    Flux<CreditHistoryDocument> findByCustomerId(UUID customerId);
}
