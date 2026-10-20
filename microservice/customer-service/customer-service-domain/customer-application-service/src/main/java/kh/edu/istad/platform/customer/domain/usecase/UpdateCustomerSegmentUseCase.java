package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.common.domain.valueobject.CustomerSegmentLevel;
import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateCustomerSegmentUseCase {
    private final CustomerSegmentRepository customerSegmentRepository;

    public Mono<CustomerSegment> update(UUID id, CustomerSegmentLevel segmentLevel) {
        return customerSegmentRepository.findById(new CustomerSegmentId(id))
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer segment not found with id: " + id)))
                .flatMap(segment -> {
                    segment.updateCustomerSegment(segmentLevel);
                    return customerSegmentRepository.save(segment);
                });
    }
}
