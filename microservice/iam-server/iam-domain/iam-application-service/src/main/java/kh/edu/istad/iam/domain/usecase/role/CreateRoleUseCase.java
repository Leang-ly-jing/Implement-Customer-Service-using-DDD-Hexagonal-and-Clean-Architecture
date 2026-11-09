package kh.edu.istad.iam.domain.usecase.role;

import kh.edu.istad.iam.domain.dto.command.role.CreateRoleCommand;
import kh.edu.istad.iam.domain.dto.result.PermissionResult;
import kh.edu.istad.iam.domain.dto.result.RoleResult;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.RolePermission;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.mapper.PermissionDataMapper;
import kh.edu.istad.iam.domain.mapper.RoleDataMapper;
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
public class CreateRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;
    private final RoleDataMapper roleDataMapper;
    private final PermissionDataMapper permissionDataMapper;

    public RoleResult execute(CreateRoleCommand command) {
        String roleName = command.name().toUpperCase().trim();
        if (roleRepository.existsByName(roleName)) {
            throw new AuthorizationDomainException("Role name already exists");
        }

        Instant now = Instant.now();
        String actor = currentActorProvider.getCurrentActor();
        Role role = roleDataMapper.createCommandToRole(command, actor, now);
        Role savedRole = roleRepository.save(role);

        List<PermissionResult> permissionResults = new ArrayList<>();
        if (command.permissionUuids() != null && !command.permissionUuids().isEmpty()) {
            List<Permission> permissions = permissionRepository.findByUuids(command.permissionUuids());
            for (Permission p : permissions) {
                p.validateCanAssign();
                RolePermission rp = RolePermission.builder()
                        .roleId(savedRole.getId())
                        .permissionId(p.getId())
                        .assignedAt(now)
                        .assignedBy(actor)
                        .reason("Assigned at role creation")
                        .build();
                roleRepository.saveRolePermission(rp);
                permissionResults.add(permissionDataMapper.permissionToPermissionResult(p));
            }
        }

        return roleDataMapper.roleToRoleResult(savedRole, permissionResults);
    }
}