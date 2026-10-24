package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;

import java.util.UUID;

public class CustomerRepositoryAdapter implements CustomerRepository {
    @Override
    public boolean exists(UUID customerId) {
        return true;
    }
}
