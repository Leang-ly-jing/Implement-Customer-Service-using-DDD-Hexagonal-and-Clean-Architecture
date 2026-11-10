package kh.edu.istad.iam.domain.dto.query;

public record RoleFilterQuery(
        int page,
        int size,
        String sort,
        String search,
        Boolean enabled
) {
    public RoleFilterQuery {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
    }
}