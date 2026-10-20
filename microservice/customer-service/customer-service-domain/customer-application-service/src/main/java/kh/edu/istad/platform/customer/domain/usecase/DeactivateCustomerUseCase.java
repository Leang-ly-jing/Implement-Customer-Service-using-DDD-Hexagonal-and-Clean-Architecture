package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeactivateCustomerUseCase {

    private final CustomerRepository customerRepository;

    public Mono<Customer> deactivate(UUID id) {
        return customerRepository.findById(new CustomerId(id))
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer not found with id: " + id)))
                .flatMap(customer -> {
                    customer.deactiveCustomer();
                    return customerRepository.save(customer);
                });
    }

    public Mono<Void> deleteById(UUID id) {
        return customerRepository.deleteById(new CustomerId(id));
    }
}
