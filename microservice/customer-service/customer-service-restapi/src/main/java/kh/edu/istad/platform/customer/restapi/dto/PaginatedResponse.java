package kh.edu.istad.platform.customer.restapi.dto;

import lombok.Builder;

import java.util.List;
@Builder
public record PaginatedResponse<T>(
        int currentPage,
        int pageSize,
        long totalItems,
        int totalPages,
        List<T> items,
        boolean hasNext
) {
}
