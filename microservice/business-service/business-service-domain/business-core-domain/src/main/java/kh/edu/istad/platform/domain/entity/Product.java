package kh.edu.istad.platform.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.platform.domain.exception.BusinessDomainException;

import java.math.BigDecimal;
import java.util.UUID;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;
    private final int quantity;
    private boolean available;

    private Product(Builder builder) {
        super.setId(builder.productId);
        name = builder.name;
        price = builder.price;
        quantity = builder.quantity;
        available = builder.available;
    }

    public void updateWithConfirmedNamePriceAndAvailability(String name, Money price, boolean available) {
        this.name = name;
        this.price = price;
        this.available = available;
    }
    void initializeProduct() {
        super.setId(new ProductId(UUID.randomUUID()));
    }

    void validate() {
        if (name == null || name.isBlank()) {
            throw new BusinessDomainException("Product name must not be blank!");
        }
        if (price == null || price.amount() == null
                || price.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessDomainException("Product '" + name + "' price must be greater than zero!");
        }
        if (quantity < 0) {
            throw new BusinessDomainException("Product '" + name + "' quantity must not be negative!");
        }
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return available;
    }

    public static final class Builder {
        private ProductId productId;
        private String name;
        private Money price;
        private int quantity;
        private boolean available;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder productId(ProductId val) {
            productId = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder available(boolean val) {
            available = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
