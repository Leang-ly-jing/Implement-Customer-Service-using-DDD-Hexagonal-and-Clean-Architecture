package kh.edu.istad.iam.domain.usecase.Permission;

import kh.edu.istad.iam.domain.dto.result.PermissionResult;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.mapper.PermissionDataMapper;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetPermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final PermissionDataMapper permissionDataMapper;

    public PermissionResult execute(UUID permissionUuid) {

        Permission permission = permissionRepository.findByUuid(permissionUuid)
                .orElseThrow(() ->
                        new AuthorizationDomainException("Permission not found"));

        return permissionDataMapper.permissionToPermissionResult(permission);
    }

}

