package kh.edu.istad.platform.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateCreditEntryCommand(
        UUID customerId,
        BigDecimal totalCreditAmount
) {
}
