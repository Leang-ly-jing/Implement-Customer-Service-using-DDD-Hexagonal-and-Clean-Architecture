package kh.edu.istad.platform.customer.restapi.mapper;

import kh.edu.istad.platform.customer.domain.dto.DeactivateCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerCommad;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import kh.edu.istad.platform.customer.restapi.dto.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CustomerWebMapper {

    Customer toDomain(CustomerCreateRequest request);
    default Email toEmail(String email){
        return new Email(email);
    }
    default PhoneNumber toPhoneNumber(String phoneNumber){
        return new PhoneNumber(phoneNumber);
    }

    @Mapping(source = "id", target = "id")
    @Mapping(source = "request.familyName", target = "familyName")
    @Mapping(source = "request.givenName", target = "givenName")
    UpdateCustomerCommand toCommand(UUID id, CustomerUpdateRequest request);
    CustomerUpdateResponse toResponse(UpdateCustomerResult result);

}
