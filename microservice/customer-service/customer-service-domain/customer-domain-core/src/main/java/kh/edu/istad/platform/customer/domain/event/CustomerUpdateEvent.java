package kh.edu.istad.platform.customer.domain.event;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.platform.customer.domain.entity.Customer;

import java.time.ZonedDateTime;

public class CustomerUpdateEvent implements DomainEvent<Customer> {
    private final Customer customer;
    private final ZonedDateTime intiatedAt;

    public Customer getCustomer() {
        return customer;
    }

    public ZonedDateTime getIntiatedAt() {
        return intiatedAt;
    }

    public CustomerUpdateEvent(Customer customer, ZonedDateTime intiatedAt) {
        this.customer = customer;
        this.intiatedAt = intiatedAt;
    }
}
