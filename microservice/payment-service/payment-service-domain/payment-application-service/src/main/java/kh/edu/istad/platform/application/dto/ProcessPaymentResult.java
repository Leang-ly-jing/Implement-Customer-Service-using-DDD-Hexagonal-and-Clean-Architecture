package kh.edu.istad.platform.application.dto;

import kh.edu.istad.common.domain.valueobject.PaymentStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record ProcessPaymentResult(
        UUID paymentId,
        UUID customerId,
        UUID orderId,
        BigDecimal price,
        PaymentStatus paymentStatus,
        ZonedDateTime createdAt,
        List<String> failureMessages
) {
}
