package kh.edu.istad.platform.order.domain.port.out;

import kh.edu.istad.platform.order.domain.entity.Customer;


import java.util.Optional;
import java.util.UUID;
public interface CustomerRepository {
    Optional<Customer> findCustomer(UUID customerId);
}
