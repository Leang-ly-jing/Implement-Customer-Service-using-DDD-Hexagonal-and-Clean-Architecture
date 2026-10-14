package kh.edu.istad.platform.customer.domain.mapper;

import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerCommad;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import org.springframework.stereotype.Component;

@Component
public class InitiateCustomerMapper {
    public Customer toCustomer(InitiateCustomerCommad commad){
        return Customer.Builder.builder()
                .userName(commad.userName())
                .familyName(commad.familyName())
                .givenName(commad.givenName())
                .email(new Email(commad.email()))
                .phoneNumber(new PhoneNumber(commad.phoneNumber()))
                .build();
    }

    public InitiateCustomerResult roResult(Customer customer){
        return new InitiateCustomerResult(
                customer.getId().value(),
                customer.getUserName(),
                customer.getFamilyName(),
                customer.getGivenName(),
                customer.getEmail().value(),
                customer.getPhoneNumber().value()
        );
    }
}
