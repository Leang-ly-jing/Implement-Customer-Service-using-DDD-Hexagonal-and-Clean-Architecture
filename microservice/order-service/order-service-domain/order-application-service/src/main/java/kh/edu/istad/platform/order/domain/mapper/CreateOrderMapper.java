package kh.edu.istad.platform.order.domain.mapper;

import kh.edu.istad.common.domain.valueobject.*;
import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.dto.OrderAddressCommand;
import kh.edu.istad.platform.order.domain.dto.OrderItemCommand;
import kh.edu.istad.platform.order.domain.entity.Business;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.entity.OrderItem;
import kh.edu.istad.platform.order.domain.entity.Product;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CreateOrderMapper {

    public Business toBusiness(CreateOrderCommand command) {
        return Business.Builder.builder()
                .businessId(new BusinessId(command.businessId()))
                .products(command.items().stream()
                        .map(item -> new Product(new ProductId(item.productId()), "", new Money(item.price())))
                        .toList())
                .build();
    }

    public Order toOrder(CreateOrderCommand command) {
        return Order.Builder.builder()
                .customerId(new CustomerId(command.customerId()))
                .businessId(new BusinessId(command.businessId()))
                .deliveryAddress(toStreetAddress(command.address()))
                .price(new Money(command.price()))
                .items(toOrderItems(command.items()))
                .build();
    }

    public CreateOrderResult toResult(Order order, String message) {
        return new CreateOrderResult(
                order.getTrackingId().value(),
                order.getOrderStatus(),
                message
        );
    }

    private StreetAddress toStreetAddress(OrderAddressCommand address) {
        return new StreetAddress(
                UUID.randomUUID(),
                address.street(),
                address.postalCode(),
                address.city()
        );
    }

    private List<OrderItem> toOrderItems(List<OrderItemCommand> itemCommands) {
        return itemCommands.stream().map(item -> OrderItem.Builder.builder()
                .product(new Product(new ProductId(item.productId()), "", new Money(item.price())))
                .price(new Money(item.price()))
                .quantity(item.quantity())
                .subTotal(new Money(item.subTotal()))
                .build()
        ).toList();
    }
}
