package kh.edu.istad.iam.domain.dto.query;

public record UserFilterQuery(
        int page,
        int size,
        String sort,
        String search,
        Boolean enabled
) {
    public UserFilterQuery {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
    }
}