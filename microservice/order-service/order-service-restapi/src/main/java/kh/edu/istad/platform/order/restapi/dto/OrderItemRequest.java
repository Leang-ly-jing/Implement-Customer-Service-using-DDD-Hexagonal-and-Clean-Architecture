package kh.edu.istad.platform.order.restapi.dto;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.UUID;

public record OrderItemRequest(
        UUID productId,
        Integer quantity,
        BigDecimal price,
        BigDecimal subTotal
) {
}
