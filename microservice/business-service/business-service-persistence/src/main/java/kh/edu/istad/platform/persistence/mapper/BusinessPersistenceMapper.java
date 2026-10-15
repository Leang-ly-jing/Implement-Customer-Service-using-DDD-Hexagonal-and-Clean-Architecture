package kh.edu.istad.platform.persistence.mapper;


import kh.edu.istad.common.domain.valueobject.*;
import kh.edu.istad.platform.domain.entity.Business;
import kh.edu.istad.platform.domain.entity.Product;
import kh.edu.istad.platform.persistence.document.BusinessDocument;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BusinessPersistenceMapper {

    public BusinessDocument toDocument(Business business) {
        List<BusinessDocument.ProductDocument> productDocs = business.getProducts().stream()
                .map(product -> BusinessDocument.ProductDocument.builder()
                        .productId(product.getId().value())
                        .name(product.getName())
                        .price(product.getPrice().amount())
                        .quantity(product.getQuantity())
                        .available(product.isAvailable())
                        .build())
                .toList();

        return BusinessDocument.builder()
                .id(business.getId().value())
                .legalName(business.getBusinessName().legalName())
                .displayName(business.getBusinessName().displayName())
                .email(business.getBusinessContact().email())
                .phoneNumber(business.getBusinessContact().phoneNumber())
                .active(business.isActive())
                .products(productDocs)
                .build();
    }

    public Business toDomain(BusinessDocument document) {
        List<Product> products = document.getProducts() != null ? document.getProducts().stream()
                .map(prodDoc -> Product.Builder.builder()
                        .productId(new ProductId(prodDoc.getProductId()))
                        .name(prodDoc.getName())
                        .price(new Money(prodDoc.getPrice()))
                        .quantity(prodDoc.getQuantity())
                        .available(prodDoc.isAvailable())
                        .build())
                .toList() : new ArrayList<>();

        return Business.Builder.builder()
                .businessId(new BusinessId(document.getId()))
                .businessName(new BusinessName(document.getLegalName(), document.getDisplayName()))
                .businessContact(new BusinessContact(document.getEmail(), document.getPhoneNumber()))
                .active(document.isActive())
                .products(products)
                .build();
    }
}
