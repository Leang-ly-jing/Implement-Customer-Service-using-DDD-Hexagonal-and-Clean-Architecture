package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.DeactivateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.DeactivateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.event.CustomerDeactivatedEvent;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeactivateCustomerUseCase {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;

    public DeactivateCustomerResult execute(DeactivateCustomerCommand command) {
        Customer customer = customerRepository.findById(command.id())
                .orElseThrow(() -> new CustomerDomainException("Customer not found with id: " + command.id()));

        CustomerDeactivatedEvent event = customerDomainService.customerDeactivate(customer);
        Customer deactivatedCustomer = customerRepository.save(event.getCustomer());

        log.info("deactivate success : {}", command);
        return new DeactivateCustomerResult(
                deactivatedCustomer.getId().value(),
                deactivatedCustomer.getCustomerStatus().name()
        );
    }
}
