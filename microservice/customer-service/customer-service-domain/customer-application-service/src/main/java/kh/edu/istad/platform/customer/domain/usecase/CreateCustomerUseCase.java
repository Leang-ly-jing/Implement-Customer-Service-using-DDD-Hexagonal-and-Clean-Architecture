package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.event.CustomerIntiatedEvent;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentRepository;
import kh.edu.istad.platform.customer.domain.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CreateCustomerUseCase {
    private final CustomerRepository customerRepository;
    private final CustomerSegmentRepository customerSegmentRepository;
    private final CustomerDomainService customerDomainService;

    public Mono<Customer> create(Customer customer, CustomerSegmentId segmentId) {
        return customerSegmentRepository.findById(segmentId)
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer segment not found with id: " + segmentId.value())))
                .flatMap(segment -> {
                    customer.assignSegment(segment);
                    CustomerIntiatedEvent event = customerDomainService.customerIntiate(customer);
                    return customerRepository.save(event.getCustomer());
                });
    }
}
