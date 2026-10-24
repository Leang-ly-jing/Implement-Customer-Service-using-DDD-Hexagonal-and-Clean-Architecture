package kh.edu.istad.platform.restapi.mapper;

import kh.edu.istad.platform.application.dto.ProductCommand;
import kh.edu.istad.platform.application.dto.RegisterBusinessCommand;
import kh.edu.istad.platform.application.dto.RegisterBusinessResult;
import kh.edu.istad.platform.domain.entity.Business;
import kh.edu.istad.platform.domain.entity.Product;
import kh.edu.istad.platform.restapi.dto.BusinessResponse;
import kh.edu.istad.platform.restapi.dto.ProductRequest;
import kh.edu.istad.platform.restapi.dto.RegisterBusinessRequest;
import kh.edu.istad.platform.restapi.dto.RegisterBusinessResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BusinessWebMapper {

    public RegisterBusinessCommand toCommand(RegisterBusinessRequest request) {
        return new RegisterBusinessCommand(
                request.legalName(),
                request.displayName(),
                request.email(),
                request.phoneNumber(),
                request.products() != null ? request.products().stream().map(this::toProductCommand).toList() : null
        );
    }

    public RegisterBusinessResponse toResponse(RegisterBusinessResult result) {
        return new RegisterBusinessResponse(
                result.businessId(),
                result.legalName(),
                result.displayName(),
                result.active(),
                result.message()
        );
    }

    private ProductCommand toProductCommand(ProductRequest request) {
        return new ProductCommand(
                request.name(),
                request.price(),
                request.quantity(),
                request.available()
        );
    }

    public BusinessResponse toDetailResponse(Business business) {
        return new BusinessResponse(
                business.getId() != null ? business.getId().value() : null,
                business.getBusinessName() != null ? business.getBusinessName().displayName() : null,
                business.isActive(),
                business.getProducts() != null ? business.getProducts().stream()
                        .map(this::toProductItem)
                        .toList() : List.of()
        );
    }
    private BusinessResponse.ProductItemResponse toProductItem(Product product) {
        return new BusinessResponse.ProductItemResponse(
                product.getId() != null ? product.getId().value() : null,
                product.getName(),
                product.getPrice() != null ? product.getPrice().amount() : null,
                product.getQuantity(),
                product.isAvailable()
        );
    }

}
