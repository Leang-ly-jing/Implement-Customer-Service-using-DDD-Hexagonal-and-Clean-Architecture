package kh.edu.istad.platform.application.dto;

import java.math.BigDecimal;

public record ProductCommand(
        String name,
        BigDecimal price,
        int quantity,
        boolean available
) {
}
