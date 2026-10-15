package kh.edu.istad.platform.application.port.out;

import kh.edu.istad.platform.domain.entity.CreditHistory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CreditHistoryRepository {
    Flux<CreditHistory> findByCustomerId(UUID customerId);
    Mono<CreditHistory> save(CreditHistory creditHistory);
}
