package kh.edu.istad.platform.order.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.*;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.entity.OrderItem;
import kh.edu.istad.platform.order.domain.entity.Product;
import kh.edu.istad.platform.order.persistence.entity.OrderAddressEntity;
import kh.edu.istad.platform.order.persistence.entity.OrderEntity;
import kh.edu.istad.platform.order.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Component
public class OrderPersistenceMapper {
    public OrderEntity toEntity(Order order) {
        OrderAddressEntity addressEntity = OrderAddressEntity.builder()
                .id(order.getDeliveryAddress().id())
                .street(order.getDeliveryAddress().street())
                .postalCode(order.getDeliveryAddress().postalCode())
                .city(order.getDeliveryAddress().city())
                .build();
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().value())
                .customerId(order.getCustomerId().value())
                .businessId(order.getBusinessId().value())
                .trackingId(order.getTrackingId().value())
                .address(addressEntity)
                .price(order.getPrice().amount())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessage() != null ?
                        String.join(",", order.getFailureMessage()) : "")
                .build();
        List<OrderItemEntity> itemEntities = order.getItems().stream()
                .map(item -> OrderItemEntity.builder()
                        .id(item.getId().value())
                        .order(orderEntity)
                        .productId(item.getProduct().getId().value())
                        .price(item.getPrice().amount())
                        .quantity(item.getQuantity())
                        .subTotal(item.getSubTotal().amount())
                        .build())
                .toList();
        orderEntity.setItems(itemEntities);
        return orderEntity;
    }
    public Order toDomain(OrderEntity entity) {
        StreetAddress deliveryAddress = new StreetAddress(
                entity.getAddress().getId(),
                entity.getAddress().getStreet(),
                entity.getAddress().getPostalCode(),
                entity.getAddress().getCity()
        );
        List<OrderItem> items = entity.getItems().stream()
                .map(item -> OrderItem.Builder.builder()
                        .orderItemId(new OrderItemId(item.getId()))
                        .product(new Product(new ProductId(item.getProductId())))
                        .price(new Money(item.getPrice()))
                        .quantity(item.getQuantity())
                        .subTotal(new Money(item.getSubTotal()))
                        .build())
                .toList();
        return Order.Builder.builder()
                .orderId(new OrderId(entity.getId()))
                .customerId(new CustomerId(entity.getCustomerId()))
                .businessId(new BusinessId(entity.getBusinessId()))
                .deliveryAddress(deliveryAddress)
                .price(new Money(entity.getPrice()))
                .items(items)
                .trackingId(new TrackingId(entity.getTrackingId()))
                .orderStatus(entity.getOrderStatus())
                .failureMessage(entity.getFailureMessages() == null || entity.getFailureMessages().isEmpty() ?
                        new ArrayList<>() : new ArrayList<>(Arrays.asList(entity.getFailureMessages().split(","))))
                .build();
    }
}