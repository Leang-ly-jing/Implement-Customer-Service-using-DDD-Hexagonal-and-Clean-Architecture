package kh.edu.istad.platform.restapi.dto;

import java.math.BigDecimal;
public record ProductRequest(
        String name,
        BigDecimal price,
        int quantity,
        boolean available
) {}
