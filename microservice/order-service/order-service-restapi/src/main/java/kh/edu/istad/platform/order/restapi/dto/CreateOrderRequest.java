package kh.edu.istad.platform.order.restapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull(message = "Customer ID is required")
        UUID customerId,
        @NotNull(message = "Business ID is required")
        UUID businessId,
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,
        @NotEmpty(message = "Order items must not be empty")
        List<@Valid OrderItemRequest> items,
        @NotNull(message = "Delivery address is required")
        @Valid OrderAddressRequest address
) {}
