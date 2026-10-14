package kh.edu.istad.platform.customer.domain.mapper;

import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class UpdateCustomerMapper {

    public void toUpdateCustomer(Customer customer, UpdateCustomerCommand command) {
        if (customer == null || command == null) return;
        customer.setFamilyName(command.familyName());
        customer.setGivenName(command.givenName());
    }

    public UpdateCustomerResult toResult(Customer customer) {
        if (customer == null) return null;
        return new UpdateCustomerResult(
                customer.getId() != null ? customer.getId().value() : null,
                customer.getFamilyName(),
                customer.getGivenName()
        );
    }
}
