package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.order.domain.entity.Customer;
import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
@Component
public class CustomerRepositoryAdapter implements CustomerRepository {
    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return  Optional.of(
                new Customer(
                        new CustomerId(customerId)
                )
        );
    }
}
