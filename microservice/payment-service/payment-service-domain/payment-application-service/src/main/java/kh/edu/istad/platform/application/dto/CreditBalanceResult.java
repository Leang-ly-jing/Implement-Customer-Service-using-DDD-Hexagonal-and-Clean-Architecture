package kh.edu.istad.platform.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreditBalanceResult(
        UUID customerId,
        BigDecimal totalCreditAmount
) {
}
