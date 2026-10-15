package kh.edu.istad.platform.payment.persistence.adapter;

import kh.edu.istad.platform.application.port.out.CreditEntryRepository;
import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.payment.persistence.mapper.PaymentPersistenceMapper;
import kh.edu.istad.platform.payment.persistence.repository.CreditEntryReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreditEntryRepositoryAdapter implements CreditEntryRepository {

    private final CreditEntryReactiveMongoRepository mongoRepository;
    private final PaymentPersistenceMapper mapper;

    @Override
    public Mono<CreditEntry> findByCustomerId(UUID customerId) {
        return mongoRepository.findByCustomerId(customerId)
                .map(mapper::toCreditEntryDomain);
    }

    @Override
    public Mono<CreditEntry> save(CreditEntry creditEntry) {
        return mongoRepository.save(mapper.toCreditEntryDocument(creditEntry))
                .map(mapper::toCreditEntryDomain);
    }
}
