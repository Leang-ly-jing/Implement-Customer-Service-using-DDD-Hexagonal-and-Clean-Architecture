package kh.edu.istad.iam.domain.dto.result;

import java.util.List;

public record PageResult<T>(
        List<T> content,
        PageMetadata page
) {
    public record PageMetadata(
            int number,
            int size,
            long totalElements,
            int totalPages
    ) {}
    public static <T> PageResult<T> of(List<T> content, int number, int size, long totalElements) {
        int totalPages = size == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) size);
        return new PageResult<>(content, new PageMetadata(number, size, totalElements, totalPages));
    }
}
