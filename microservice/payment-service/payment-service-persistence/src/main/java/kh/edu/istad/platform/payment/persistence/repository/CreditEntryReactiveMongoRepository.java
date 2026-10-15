package kh.edu.istad.platform.payment.persistence.repository;

import kh.edu.istad.platform.payment.persistence.document.CreditEntryDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CreditEntryReactiveMongoRepository extends ReactiveMongoRepository<CreditEntryDocument, UUID> {
    Mono<CreditEntryDocument> findByCustomerId(UUID customerId);
}
