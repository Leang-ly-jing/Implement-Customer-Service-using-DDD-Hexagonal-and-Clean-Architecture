package kh.edu.istad.iam.domain.usecase.role;

import kh.edu.istad.iam.domain.dto.command.role.AssignPermissionsToRoleCommand;
import kh.edu.istad.iam.domain.dto.result.PermissionAssignmentResult;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.RolePermission;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class AssignPermissionsToRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;


    public List<PermissionAssignmentResult> execute(AssignPermissionsToRoleCommand command) {
        Role role = roleRepository.findByUuid(command.roleUuid())
                .orElseThrow(() ->
                        new AuthorizationDomainException("Role not found"));

        Instant now = Instant.now();
        String actor = currentActorProvider.getCurrentActor();
        List<PermissionAssignmentResult> results = new ArrayList<>();

        List<Permission> permissions = permissionRepository.findByUuids(command.permissionUuids());
        for (Permission p : permissions) {
            p.validateCanAssign();
            RolePermission rolePermission = RolePermission.builder()
                    .roleId(role.getId())
                    .permissionId(p.getId())
                    .assignedAt(now)
                    .assignedBy(actor)
                    .expiresAt(command.expiresAt())
                    .reason(command.reason())
                    .build();
            roleRepository.saveRolePermission(rolePermission);
            results.add(new PermissionAssignmentResult(p.getUuid(), p.getName(), now, command.expiresAt(), command.reason()));
        }

        return results;
    }
}