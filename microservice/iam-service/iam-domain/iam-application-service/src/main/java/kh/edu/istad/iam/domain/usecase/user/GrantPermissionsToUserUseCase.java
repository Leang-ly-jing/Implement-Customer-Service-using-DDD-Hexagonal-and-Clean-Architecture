package kh.edu.istad.iam.domain.usecase.user;

import kh.edu.istad.iam.domain.dto.command.user.GrantPermissionsToUserCommand;
import kh.edu.istad.iam.domain.dto.result.PermissionGrantResult;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserPermission;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class GrantPermissionsToUserUseCase {

    private final UserRepository userRepository;
    private final CurrentActorProvider currentActorProvider;
    private final PermissionRepository permissionRepository;


    public List<PermissionGrantResult> execute(GrantPermissionsToUserCommand command) {
        User user = userRepository.findByUuid(command.userUuid())
                .orElseThrow(() -> new AuthorizationDomainException("User not found"));

        Instant now = Instant.now();
        String actor = currentActorProvider.getCurrentActor();

        List<PermissionGrantResult> results = new ArrayList<>();
        List<Permission> permissions = permissionRepository.findByUuids(command.permissionUuids());
        for (Permission p : permissions) {
            p.validateCanAssign();
            UserPermission userPermission = UserPermission.builder()
                    .userId(user.getId())
                    .permissionId(p.getId())
                    .effect(command.effect())
                    .assignedAt(now)
                    .assignedBy(actor)
                    .expiresAt(command.expiresAt())
                    .reason(command.reason())
                    .build();
            userRepository.saveUserPermission(userPermission);
            results.add(new PermissionGrantResult(p.getUuid(), p.getName(), command.effect().name(), now, command.expiresAt(), command.reason()));
        }

        return results;
    }
}