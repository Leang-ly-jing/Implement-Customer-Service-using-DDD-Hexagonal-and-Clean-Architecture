package kh.edu.istad.iam.domain.usecase.Permission;

import kh.edu.istad.iam.domain.dto.command.permission.UpdatePermissionCommand;
import kh.edu.istad.iam.domain.dto.result.PermissionResult;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.mapper.PermissionDataMapper;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class UpdatePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;
    private final PermissionDataMapper permissionDataMapper;


    public PermissionResult execute(UpdatePermissionCommand command) {
        Permission permission = permissionRepository.findByUuid(command.permissionUuid())
                .orElseThrow(() ->
                        new AuthorizationDomainException("Permission not found"));

        permission.validateCanModify();
        permission.setDescription(command.description());
        permission.setUpdatedAt(Instant.now());
        permission.setUpdatedBy(currentActorProvider.getCurrentActor());
        Permission updated = permissionRepository.save(permission);

        return permissionDataMapper.permissionToPermissionResult(updated);
    }
}
