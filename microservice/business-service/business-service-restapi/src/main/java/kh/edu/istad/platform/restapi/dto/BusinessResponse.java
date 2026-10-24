package kh.edu.istad.platform.restapi.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record BusinessResponse(
        UUID id,
        String name,
        boolean active,

        List<ProductItemResponse> products
) {
    public record ProductItemResponse(UUID id, String name, BigDecimal price, int quantity, boolean available) {}
}