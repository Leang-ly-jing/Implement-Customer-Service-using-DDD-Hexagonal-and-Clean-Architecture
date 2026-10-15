package kh.edu.istad.platform.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.common.domain.valueobject.*;
import kh.edu.istad.platform.domain.exception.BusinessDomainException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Business extends AggregateRoot<BusinessId> {
    private final BusinessName businessName;
    private final BusinessContact businessContact;
    private OrderApproval orderApproval;
    private boolean active;
    private OrderDetail orderDetail;
    private final List<Product> products;

    private Business(Builder builder) {
        super.setId(builder.businessId);
        businessName = builder.businessName;
        businessContact = builder.businessContact;
        orderApproval = builder.orderApproval;
        active = builder.active;
        orderDetail = builder.orderDetail;
        products = builder.products;
    }

    public void validateBusiness() {
        if (getId() != null) {
            throw new BusinessDomainException("Business Id must be null!");
        }
        if (businessName == null || businessName.legalName() == null || businessName.legalName().isBlank()) {
            throw new BusinessDomainException("Business legal name must not be blank!");
        }
        if (businessName.displayName() == null || businessName.displayName().isBlank()) {
            throw new BusinessDomainException("Business display name must not be blank!");
        }
        if (businessContact == null || businessContact.email() == null || businessContact.email().isBlank()) {
            throw new BusinessDomainException("Business email must not be blank!");
        }
        if (businessContact.phoneNumber() == null || businessContact.phoneNumber().isBlank()) {
            throw new BusinessDomainException("Business phone number must not be blank!");
        }
        products.forEach(Product::validate);
    }

    public void initializeBusiness() {
        super.setId(new BusinessId(UUID.randomUUID()));
        this.active = true;
    }

    public void validateOrder(List<String> failureMessages) {
        if (orderDetail.getOrderStatus() != OrderStatus.PAID) {
            failureMessages.add("Payment is not finished yet!");
        }
        BigDecimal total = orderDetail.getProducts().stream().map(orderProduct -> {
            Product currentProduct = products.stream()
                    .filter(p -> p.getId().equals(orderProduct.getId()))
                    .findFirst()
                    .orElse(null);
            if (currentProduct == null) {
                failureMessages.add("Product with id " + orderProduct.getId().value() + " is not in business catalog!");
                return BigDecimal.ZERO;
            }
            if (!currentProduct.isAvailable()) {
                failureMessages.add("Product with id " + orderProduct.getId().value() + " is currently not available!");
            }
            if (currentProduct.getQuantity() < orderProduct.getQuantity()) {
                failureMessages.add("Product " + currentProduct.getName() + " has insufficient stock!");
            }
            return currentProduct.getPrice().amount().multiply(BigDecimal.valueOf(orderProduct.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (orderDetail.getTotalAmount().amount().compareTo(total) != 0) {
            failureMessages.add("Price total is not correct for order " + orderDetail.getId().value());
        }
    }

    public void constructOrderApproval(OrderApprovalStatus orderApprovalStatus) {
        this.orderApproval = OrderApproval.Builder.builder()
                .orderApprovalId(new OrderApprovalId(UUID.randomUUID()))
                .businessId(this.getId())
                .orderId(this.orderDetail.getId())
                .approvalStatus(orderApprovalStatus)
                .build();
    }

    public BusinessName getBusinessName() {
        return businessName;
    }

    public BusinessContact getBusinessContact() {
        return businessContact;
    }

    public OrderApproval getOrderApproval() {
        return orderApproval;
    }

    public boolean isActive() {
        return active;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public List<Product> getProducts() {
        return products;
    }

    public static final class Builder {
        private BusinessId businessId;
        private BusinessName businessName;
        private BusinessContact businessContact;
        private OrderApproval orderApproval;
        private boolean active;
        private OrderDetail orderDetail;
        private List<Product> products;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder businessId(BusinessId val) {
            businessId = val;
            return this;
        }

        public Builder businessName(BusinessName val) {
            businessName = val;
            return this;
        }

        public Builder businessContact(BusinessContact val) {
            businessContact = val;
            return this;
        }

        public Builder orderApproval(OrderApproval val) {
            orderApproval = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Builder orderDetail(OrderDetail val) {
            orderDetail = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Business build() {
            return new Business(this);
        }
    }
}
