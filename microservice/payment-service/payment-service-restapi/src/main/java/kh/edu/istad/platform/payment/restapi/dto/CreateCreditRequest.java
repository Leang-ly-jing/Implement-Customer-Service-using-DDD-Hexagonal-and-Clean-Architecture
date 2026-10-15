package kh.edu.istad.platform.payment.restapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateCreditRequest(
        @NotNull(message = "Customer ID is required")
        UUID customerId,
        @NotNull(message = "Total credit amount is required")
        @Positive(message = "Total credit amount must be greater than zero")
        BigDecimal totalCreditAmount
) {
}
