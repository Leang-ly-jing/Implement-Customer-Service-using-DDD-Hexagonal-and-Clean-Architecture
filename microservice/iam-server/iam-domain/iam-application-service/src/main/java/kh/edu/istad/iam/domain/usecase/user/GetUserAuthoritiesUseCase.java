package kh.edu.istad.iam.domain.usecase.user;

import kh.edu.istad.iam.domain.dto.result.UserAuthoritiesResult;
import kh.edu.istad.iam.domain.entity.*;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GetUserAuthoritiesUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public UserAuthoritiesResult execute(UUID userUuid) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new AuthorizationDomainException("User not found"));

        List<UserRole> userRoles = userRepository.findRolesByUserId(user.getId());
        List<UserAuthoritiesResult.AssignedRoleSummary> roleSummaries = new ArrayList<>();
        Set<Role> activeRoles = new HashSet<>();

        for (UserRole ur : userRoles) {
            if (ur.isActive()) {
                roleRepository.findById(ur.getRoleId()).ifPresent(role -> {
                    roleSummaries.add(new UserAuthoritiesResult.AssignedRoleSummary(role.getUuid(), role.getName(), ur.getExpiresAt()));

                    List<RolePermission> rps = roleRepository.findPermissionsByRoleId(role.getId());
                    Set<Permission> rolePerms = new HashSet<>();
                    for (RolePermission rp : rps) {
                        if (rp.isActive()) {
                            permissionRepository.findById(rp.getPermissionId()).ifPresent(rolePerms::add);
                        }
                    }
                    role.setPermissions(rolePerms);
                    activeRoles.add(role);
                });
            }
        }
        user.setRoles(activeRoles);

        List<UserPermission> directPermissions = userRepository.findPermissionsByUserId(user.getId());
        List<UserAuthoritiesResult.DirectPermissionSummary> directSummaries = new ArrayList<>();
        for (UserPermission up : directPermissions) {
            if (up.isActive()) {
                permissionRepository.findById(up.getPermissionId()).ifPresent(p -> {
                    up.setPermission(p);
                    directSummaries.add
                            (new UserAuthoritiesResult.DirectPermissionSummary(p.getName(), up.getEffect().name()));
                });
            }
        }

        Set<String> effectivePermissions = user.getEffectivePermissions(directPermissions);
        return new UserAuthoritiesResult(roleSummaries, directSummaries, effectivePermissions);
    }
}