package kh.edu.istad.platform.customer.domain.service;

import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.event.CustomerDeactivatedEvent;
import kh.edu.istad.platform.customer.domain.event.CustomerIntiatedEvent;
import kh.edu.istad.platform.customer.domain.event.CustomerUpdateEvent;

public interface CustomerDomainService {

    CustomerIntiatedEvent customerIntiate(Customer customer);
    CustomerUpdateEvent customerUpdate(Customer customer);
    CustomerDeactivatedEvent customerDeactivate(Customer customer);

}
