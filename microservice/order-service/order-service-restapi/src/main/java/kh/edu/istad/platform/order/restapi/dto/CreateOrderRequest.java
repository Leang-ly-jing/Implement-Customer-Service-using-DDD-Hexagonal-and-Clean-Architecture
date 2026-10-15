package kh.edu.istad.platform.order.restapi.dto;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        UUID customerId,
        UUID businessId,
        BigDecimal price,
        List<OrderItemRequest> items,
        OrderAddressRequest address
) {}
