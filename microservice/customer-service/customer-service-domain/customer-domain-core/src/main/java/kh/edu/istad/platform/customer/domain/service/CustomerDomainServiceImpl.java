package kh.edu.istad.platform.customer.domain.service;

import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.event.CustomerDeactivatedEvent;
import kh.edu.istad.platform.customer.domain.event.CustomerIntiatedEvent;
import kh.edu.istad.platform.customer.domain.event.CustomerUpdateEvent;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CustomerDomainServiceImpl implements CustomerDomainService{
    @Override
    public CustomerIntiatedEvent customerIntiate(Customer customer) {
        customer.initiateCustomer();
        return new CustomerIntiatedEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public CustomerUpdateEvent customerUpdate(Customer customer) {
        customer.updateCustomer(customer.getFamilyName(), customer.getGivenName());
        return new CustomerUpdateEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public CustomerDeactivatedEvent customerDeactivate(Customer customer) {
        customer.deactiveCustomer();
        return new CustomerDeactivatedEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")) );
    }
}
