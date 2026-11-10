package kh.edu.istad.iam.domain.usecase.role;

import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.RolePermission;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RevokePermissionFromRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    public void execute(UUID roleUuid, UUID permissionUuid, String reason) {
        Role role = roleRepository.findByUuid(roleUuid)
                .orElseThrow(() ->
                        new AuthorizationDomainException("Role not found"));

        Permission permission = permissionRepository.findByUuid(permissionUuid)
                .orElseThrow(() ->
                        new AuthorizationDomainException("Permission not found"));

        RolePermission rolePermission = roleRepository.findActiveRolePermission(role.getId(), permission.getId())
                .orElseThrow(() -> new AuthorizationDomainException("Active permission assignment not found on role"));


        rolePermission.revoke(currentActorProvider.getCurrentActor(), reason);

        roleRepository.saveRolePermission(rolePermission);
    }
}
