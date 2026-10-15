package kh.edu.istad.platform.payment.persistence.adapter;

import kh.edu.istad.platform.application.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.persistence.mapper.PaymentPersistenceMapper;
import kh.edu.istad.platform.payment.persistence.repository.CreditHistoryReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreditHistoryRepositoryAdapter implements CreditHistoryRepository {

    private final CreditHistoryReactiveMongoRepository mongoRepository;
    private final PaymentPersistenceMapper mapper;

    @Override
    public Flux<CreditHistory> findByCustomerId(UUID customerId) {
        return mongoRepository.findByCustomerId(customerId)
                .map(mapper::toCreditHistoryDomain);
    }

    @Override
    public Mono<CreditHistory> save(CreditHistory creditHistory) {
        return mongoRepository.save(mapper.toCreditHistoryDocument(creditHistory))
                .map(mapper::toCreditHistoryDomain);
    }
}
