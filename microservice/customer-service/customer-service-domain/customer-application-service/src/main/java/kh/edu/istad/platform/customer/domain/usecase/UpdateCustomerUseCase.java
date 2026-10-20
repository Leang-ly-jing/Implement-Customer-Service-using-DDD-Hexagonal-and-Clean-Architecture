package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.event.CustomerUpdateEvent;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.mapper.UpdateCustomerMapper;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateCustomerUseCase {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;
    private final UpdateCustomerMapper updateCustomerMapper;

    public Mono<UpdateCustomerResult> execute(UpdateCustomerCommand command) {
        return customerRepository.findById(new CustomerId(command.id()))
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer not found with id: " + command.id())))
                .flatMap(customer -> {
                    updateCustomerMapper.toUpdateCustomer(customer, command);
                    CustomerUpdateEvent event = customerDomainService.customerUpdate(customer);
                    return customerRepository.save(event.getCustomer());
                })
                .map(updatedCustomer -> {
                    log.info("Update successful for: {}", command);
                    return updateCustomerMapper.toResult(updatedCustomer);
                });
    }
}
