package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QueryCustomerSegmentUseCase {
    private final CustomerSegmentRepository customerSegmentRepository;

    public Flux<CustomerSegment> findAll() {
        return customerSegmentRepository.findAll();
    }

    public Mono<CustomerSegment> findById(CustomerSegmentId id) {
        return customerSegmentRepository.findById(id);
    }
}
