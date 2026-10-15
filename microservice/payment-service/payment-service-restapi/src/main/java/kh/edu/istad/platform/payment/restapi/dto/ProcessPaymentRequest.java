package kh.edu.istad.platform.payment.restapi.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcessPaymentRequest(
        UUID customerId,
        UUID orderId,
        BigDecimal price
) {
}
