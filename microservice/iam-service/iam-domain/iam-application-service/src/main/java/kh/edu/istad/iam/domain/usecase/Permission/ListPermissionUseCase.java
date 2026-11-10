package kh.edu.istad.iam.domain.usecase.Permission;

import kh.edu.istad.iam.domain.dto.query.PermissionFilterQuery;
import kh.edu.istad.iam.domain.dto.result.PageResult;
import kh.edu.istad.iam.domain.dto.result.PermissionResult;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.mapper.PermissionDataMapper;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListPermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final PermissionDataMapper permissionDataMapper;

    public PageResult<PermissionResult> execute(PermissionFilterQuery query) {

        List<Permission> permissions = permissionRepository
                .findAll(query.page(), query.size(), query.sort());
        long total = permissionRepository.count();

        List<PermissionResult> list = permissions.stream()
                .map(permissionDataMapper::permissionToPermissionResult)
                .toList();

        return PageResult.of(list, query.page(), query.size(), total);
    }
}
