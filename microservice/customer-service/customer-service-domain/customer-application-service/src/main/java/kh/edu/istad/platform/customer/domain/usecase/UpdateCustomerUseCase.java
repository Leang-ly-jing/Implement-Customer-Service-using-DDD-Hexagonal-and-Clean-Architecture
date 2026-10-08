package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.event.CustomerUpdateEvent;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateCustomerUseCase {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;

    public UpdateCustomerResult execute(UpdateCustomerCommand command) {
        Customer customer = customerRepository.findById(command.id())
                .orElseThrow(() -> new CustomerDomainException("Customer not found with id: " + command.id()));

        customer.setFamilyName(command.familyName());
        customer.setGivenName(command.givenName());

        CustomerUpdateEvent event = customerDomainService.customerUpdate(customer);
        Customer updatedCustomer = customerRepository.save(event.getCustomer());

        log.info("update success : {}", command);
        return new UpdateCustomerResult(
                updatedCustomer.getId().value(),
                updatedCustomer.getFamilyName(),
                updatedCustomer.getGivenName()
        );
    }
}
