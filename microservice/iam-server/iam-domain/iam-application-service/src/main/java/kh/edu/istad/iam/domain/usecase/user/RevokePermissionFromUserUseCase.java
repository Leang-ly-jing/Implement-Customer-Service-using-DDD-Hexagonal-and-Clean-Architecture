package kh.edu.istad.iam.domain.usecase.user;

import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserPermission;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
@RequiredArgsConstructor
public class RevokePermissionFromUserUseCase {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    public void execute(UUID userUuid, UUID permissionUuid, String reason) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new AuthorizationDomainException("User not found"));
        Permission permission = permissionRepository.findByUuid(permissionUuid)
                .orElseThrow(() -> new AuthorizationDomainException("Permission not found"));

        UserPermission userPermission = userRepository.findActiveUserPermission(user.getId(), permission.getId())
                .orElseThrow(() -> new AuthorizationDomainException("Active direct permission assignment not found"));

        userPermission.revoke(currentActorProvider.getCurrentActor(), reason);
        userRepository.saveUserPermission(userPermission);
    }
}