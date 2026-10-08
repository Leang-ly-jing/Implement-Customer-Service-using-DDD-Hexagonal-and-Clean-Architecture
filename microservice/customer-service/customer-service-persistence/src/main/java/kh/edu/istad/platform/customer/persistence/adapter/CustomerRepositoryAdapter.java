package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import kh.edu.istad.platform.customer.persistence.entity.CustomerEntity;
import kh.edu.istad.platform.customer.persistence.repository.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = CustomerEntity.builder()
                .customerId(customer.getId() != null ? customer.getId().value() : null)
                .userName(customer.getUserName())
                .familyName(customer.getFamilyName())
                .givenName(customer.getGivenName())
                .email(customer.getEmail() != null ? customer.getEmail().value() : null)
                .phoneNumber(customer.getPhoneNumber() != null ? customer.getPhoneNumber().value() : null)
                .customerStatus(customer.getCustomerStatus())
                .build();

        CustomerEntity savedEntity = customerJpaRepository.save(entity);

        return Customer.Builder.builder()
                .id(savedEntity.getCustomerId() != null ? new CustomerId(savedEntity.getCustomerId()) : null)
                .userName(savedEntity.getUserName())
                .familyName(savedEntity.getFamilyName())
                .givenName(savedEntity.getGivenName())
                .email(savedEntity.getEmail() != null ? new Email(savedEntity.getEmail()) : null)
                .phoneNumber(savedEntity.getPhoneNumber() != null ? new PhoneNumber(savedEntity.getPhoneNumber()) : null)
                .customerStatus(savedEntity.getCustomerStatus())
                .build();
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return customerJpaRepository.findById(id)
                .map(entity -> Customer.Builder.builder()
                        .id(new CustomerId(entity.getCustomerId()))
                        .userName(entity.getUserName())
                        .familyName(entity.getFamilyName())
                        .givenName(entity.getGivenName())
                        .email(entity.getEmail() != null ? new Email(entity.getEmail()) : null)
                        .phoneNumber(entity.getPhoneNumber() != null ? new PhoneNumber(entity.getPhoneNumber()) : null)
                        .customerStatus(entity.getCustomerStatus())
                        .build());
    }
}
