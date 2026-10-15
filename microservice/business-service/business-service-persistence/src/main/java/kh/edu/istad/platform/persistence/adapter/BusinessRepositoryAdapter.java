package kh.edu.istad.platform.persistence.adapter;


import kh.edu.istad.platform.application.port.out.BusinessRepository;
import kh.edu.istad.platform.domain.entity.Business;
import kh.edu.istad.platform.persistence.mapper.BusinessPersistenceMapper;
import kh.edu.istad.platform.persistence.repository.BusinessReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BusinessRepositoryAdapter implements BusinessRepository {

    private final BusinessReactiveMongoRepository mongoRepository;
    private final BusinessPersistenceMapper persistenceMapper;

    @Override
    public Mono<Business> save(Business business) {
        return mongoRepository.save(persistenceMapper.toDocument(business))
                .map(persistenceMapper::toDomain);
    }

    @Override
    public Mono<Business> findById(UUID businessId) {
        return mongoRepository.findById(businessId)
                .map(persistenceMapper::toDomain);
    }
}
