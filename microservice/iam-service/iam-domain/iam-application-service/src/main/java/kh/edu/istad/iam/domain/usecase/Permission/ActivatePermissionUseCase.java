package kh.edu.istad.iam.domain.usecase.Permission;

import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ActivatePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;


    public void execute(UUID permissionUuid) {
        Permission permission = permissionRepository.findByUuid(permissionUuid)
                .orElseThrow(() -> new AuthorizationDomainException(
                        "Permission not found"));

        permission.setActive(true);
        permission.setUpdatedAt(Instant.now());
        permission.setUpdatedBy(currentActorProvider.getCurrentActor());
        permissionRepository.save(permission);
    }
}
