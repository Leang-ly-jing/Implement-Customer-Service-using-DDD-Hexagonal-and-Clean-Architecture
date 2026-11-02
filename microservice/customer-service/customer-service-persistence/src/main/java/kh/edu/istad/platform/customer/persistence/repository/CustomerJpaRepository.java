package kh.edu.istad.platform.customer.persistence.repository;

import kh.edu.istad.platform.customer.persistence.entity.CustomerEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


import java.util.UUID;

public interface CustomerJpaRepository extends ReactiveMongoRepository<CustomerEntity, UUID> {
}
