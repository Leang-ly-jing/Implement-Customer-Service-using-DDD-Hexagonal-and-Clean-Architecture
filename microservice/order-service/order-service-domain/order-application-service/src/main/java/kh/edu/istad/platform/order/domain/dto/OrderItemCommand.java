package kh.edu.istad.platform.order.domain.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemCommand(
        UUID productId,
        Integer quantity,
        BigDecimal price,
        BigDecimal subTotal
) {
}
