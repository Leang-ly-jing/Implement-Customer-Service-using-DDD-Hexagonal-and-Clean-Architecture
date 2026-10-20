package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentRepository;
import kh.edu.istad.platform.customer.persistence.mapper.CustomerPersistenceMapper;
import kh.edu.istad.platform.customer.persistence.repository.CustomerSegmentMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Component
@RequiredArgsConstructor
public class CustomerSegmentRepositoryAdapter implements CustomerSegmentRepository {
    private final CustomerSegmentMongoRepository customerSegmentMongoRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;

    @Override
    public Mono<CustomerSegment> save(CustomerSegment customerSegment) {
        return customerSegmentMongoRepository.save(customerPersistenceMapper.toSegmentDocument(customerSegment))
                .map(customerPersistenceMapper::toSegmentDomain);

    }

    @Override
    public Flux<CustomerSegment> findAll() {
        return customerSegmentMongoRepository.findAll()
                .map(customerPersistenceMapper::toSegmentDomain);
    }

    @Override
    public Mono<CustomerSegment> findById(CustomerSegmentId segmentId) {
        return customerSegmentMongoRepository.findById(segmentId.value().toString())
                .map(customerPersistenceMapper::toSegmentDomain);
    }

    @Override
    public Mono<Void> deleteById(CustomerSegmentId segmentId) {
        return customerSegmentMongoRepository.deleteById(segmentId.value().toString());
    }
}
