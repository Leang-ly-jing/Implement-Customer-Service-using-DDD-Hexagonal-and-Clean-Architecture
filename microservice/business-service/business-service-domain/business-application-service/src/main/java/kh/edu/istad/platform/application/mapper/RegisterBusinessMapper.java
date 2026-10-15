package kh.edu.istad.platform.application.mapper;

import kh.edu.istad.common.domain.valueobject.BusinessContact;
import kh.edu.istad.common.domain.valueobject.BusinessName;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.platform.application.dto.ProductCommand;
import kh.edu.istad.platform.application.dto.RegisterBusinessCommand;
import kh.edu.istad.platform.application.dto.RegisterBusinessResult;
import kh.edu.istad.platform.domain.entity.Business;
import kh.edu.istad.platform.domain.entity.Product;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegisterBusinessMapper {

    public Business toBusiness(RegisterBusinessCommand command) {
        return Business.Builder.builder()
                .businessName(new BusinessName(command.legalName(), command.displayName()))
                .businessContact(new BusinessContact(command.email(), command.phoneNumber()))
                .products(command.products() != null ? command.products().stream().map(this::toProduct).toList() : null)
                .build();
    }

    public RegisterBusinessResult toResult(Business business, String message) {
        return new RegisterBusinessResult(
                business.getId().value(),
                business.getBusinessName().legalName(),
                business.getBusinessName().displayName(),
                business.isActive(),
                message
        );
    }

    private Product toProduct(ProductCommand command) {
        return Product.Builder.builder()
                .productId(new ProductId(UUID.randomUUID()))
                .name(command.name())
                .price(new Money(command.price()))
                .quantity(command.quantity())
                .available(command.available())
                .build();
    }
}
