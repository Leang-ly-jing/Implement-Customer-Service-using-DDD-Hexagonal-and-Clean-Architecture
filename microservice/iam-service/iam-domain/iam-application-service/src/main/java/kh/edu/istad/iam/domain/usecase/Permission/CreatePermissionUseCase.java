package kh.edu.istad.iam.domain.usecase.Permission;

import kh.edu.istad.iam.domain.dto.command.permission.CreatePermissionCommand;
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
public class CreatePermissionUseCase {

    private final PermissionDataMapper permissionDataMapper;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    public PermissionResult execute(CreatePermissionCommand command) {
        String resource = command.resource().trim().toLowerCase();
        String actor = command.action().trim().toLowerCase();
        if (permissionRepository.existsByResourceAndAction(resource, actor)) {
            throw new AuthorizationDomainException("Permission already exists: " + resource + ":" + actor);
        }
        Instant now = Instant.now();
        String actorProviderCurrentActor = currentActorProvider.getCurrentActor();
        Permission permission = permissionDataMapper.createCommandToPermission(command, actorProviderCurrentActor, now);
        Permission saved = permissionRepository.save(permission);
        return permissionDataMapper.permissionToPermissionResult(saved);
    }
}
