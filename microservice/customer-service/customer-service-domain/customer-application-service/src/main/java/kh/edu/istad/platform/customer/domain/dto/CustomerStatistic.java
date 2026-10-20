package kh.edu.istad.platform.customer.domain.dto;

public record CustomerStatistic(
        String status,
        String segmentId,
        Long total
) {
}
