package kh.edu.istad.iam.restapi.dto.response;


import java.util.List;

public record PageResponse<T>(
        List<T> content,
        PageMetadata page
) {
    public record PageMetadata(
            int number,
            int size,
            long totalElements,
            int totalPages) {}
}
