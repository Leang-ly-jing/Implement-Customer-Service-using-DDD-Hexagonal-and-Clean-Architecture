package kh.edu.istad.platform.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.OrderStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDetail extends BaseEntity<OrderId> {
    private final OrderStatus orderStatus;
    private final Money totalAmount;
    private final List<Product> products;

    private OrderDetail(Builder builder) {
        super.setId(builder.orderId);
        this.orderStatus = builder.orderStatus;
        this.totalAmount = builder.totalAmount;
        this.products = builder.products == null ? new ArrayList<>() : new ArrayList<>(builder.products);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public static final class Builder {
        private OrderId orderId;
        private OrderStatus orderStatus;
        private Money totalAmount;
        private List<Product> products = new ArrayList<>();

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder totalAmount(Money val) {
            totalAmount = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public OrderDetail build() {
            return new OrderDetail(this);
        }
    }
}
