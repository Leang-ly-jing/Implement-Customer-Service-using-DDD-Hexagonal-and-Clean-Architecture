package kh.edu.istad.iam.domain.usecase.user;


import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserRole;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
@RequiredArgsConstructor
public class RevokeRoleFromUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;



    public void execute(UUID userUuid, UUID roleUuid, String reason) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new AuthorizationDomainException("User not found"));

        Role role = roleRepository.findByUuid(roleUuid)
                .orElseThrow(() ->
                        new AuthorizationDomainException("Role not found"));

        UserRole userRole = userRepository.findActiveUserRole(user.getId(), role.getId())
                .orElseThrow(() -> new AuthorizationDomainException("Active role assignment not found"));

        userRole.revoke(currentActorProvider.getCurrentActor(), reason);
        userRepository.saveUserRole(userRole);
    }
}