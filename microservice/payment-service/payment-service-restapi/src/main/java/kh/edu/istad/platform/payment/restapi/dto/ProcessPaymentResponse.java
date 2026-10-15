package kh.edu.istad.platform.payment.restapi.dto;

import kh.edu.istad.common.domain.valueobject.PaymentStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record ProcessPaymentResponse(
        UUID paymentId,
        UUID customerId,
        UUID orderId,
        BigDecimal price,
        PaymentStatus paymentStatus,
        ZonedDateTime createdAt,
        List<String> failureMessages
) {
}
