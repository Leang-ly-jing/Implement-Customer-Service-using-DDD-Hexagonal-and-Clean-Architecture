package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.ProductId;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Business extends AggregateRoot<BusinessId> {
    private final List<Product> products;
    private final Boolean active;

    private Business(Builder builder) {
        super.setId(builder.businessId);
        products = builder.products == null ? List.of() : List.copyOf(builder.products);
        active = builder.active;
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(active);   // null-safe: null ត្រូវបានចាត់ទុកថា inactive
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public Optional<Product> findProduct(ProductId productId) {
        if (productId == null) {
            return Optional.empty();
        }
        return products.stream()
                .filter(product -> productId.equals(product.getId()))
                .findFirst();
    }

    public static final class Builder {
        private BusinessId businessId;
        private List<Product> products;
        private Boolean active;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder businessId(BusinessId val) {
            businessId = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Builder active(Boolean val) {
            active = val;
            return this;
        }

        public Business build() {
            return new Business(this);
        }
    }
}