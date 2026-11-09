package kh.edu.istad.iam.domain.dto.query;

public record PermissionFilterQuery
        (  int page,
           int size,
           String sort,
           String search,
           Boolean enabled){
    public PermissionFilterQuery{
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

    }
}
