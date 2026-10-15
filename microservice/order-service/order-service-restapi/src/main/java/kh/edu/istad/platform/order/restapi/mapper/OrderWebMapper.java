package kh.edu.istad.platform.order.restapi.mapper;

import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.dto.OrderAddressCommand;
import kh.edu.istad.platform.order.domain.dto.OrderItemCommand;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderRequest;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderResponse;
import kh.edu.istad.platform.order.restapi.dto.OrderAddressRequest;
import kh.edu.istad.platform.order.restapi.dto.OrderItemRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderWebMapper {

    public CreateOrderCommand toCommand(CreateOrderRequest request) {
        return new CreateOrderCommand(
                request.customerId(),
                request.businessId(),
                request.price(),
                request.items().stream().map(this::toItemCommand).toList(),
                toAddressCommand(request.address())
        );
    }

    public CreateOrderResponse toResponse(CreateOrderResult result) {
        return new CreateOrderResponse(
                result.trackingId(),
                result.orderStatus(),
                result.message()
        );
    }

    private OrderItemCommand toItemCommand(OrderItemRequest itemRequest) {
        return new OrderItemCommand(
                itemRequest.productId(),
                itemRequest.quantity(),
                itemRequest.price(),
                itemRequest.subTotal()
        );
    }

    private OrderAddressCommand toAddressCommand(OrderAddressRequest addressRequest) {
        return new OrderAddressCommand(
                addressRequest.street(),
                addressRequest.postalCode(),
                addressRequest.city()
        );
    }
}
