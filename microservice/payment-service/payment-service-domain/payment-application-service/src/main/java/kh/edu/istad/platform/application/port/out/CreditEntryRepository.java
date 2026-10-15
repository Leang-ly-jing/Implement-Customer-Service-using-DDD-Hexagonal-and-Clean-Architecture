package kh.edu.istad.platform.application.port.out;

import kh.edu.istad.platform.domain.entity.CreditEntry;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CreditEntryRepository {
    Mono<CreditEntry> findByCustomerId(UUID customerId);
    Mono<CreditEntry> save(CreditEntry creditEntry);
}
