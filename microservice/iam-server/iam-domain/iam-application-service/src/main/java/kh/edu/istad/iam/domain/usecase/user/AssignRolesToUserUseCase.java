package kh.edu.istad.iam.domain.usecase.user;

import kh.edu.istad.iam.domain.dto.command.user.AssignRolesToUserCommand;
import kh.edu.istad.iam.domain.dto.result.RoleAssignmentResult;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserRole;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class AssignRolesToUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;


    public List<RoleAssignmentResult> execute(AssignRolesToUserCommand command) {
        User user = userRepository.findByUuid(command.userUuid())
                .orElseThrow(() -> new AuthorizationDomainException("User not found"));

        Instant now = Instant.now();
        String actor = currentActorProvider.getCurrentActor();
        List<RoleAssignmentResult> results = new ArrayList<>();

        List<Role> roles = roleRepository.findByUuids(command.roleUuids());
        for (Role role : roles) {
            role.validateCanAssign();
            UserRole userRole = UserRole.builder()
                    .userId(user.getId())
                    .roleId(role.getId())
                    .assignedAt(now)
                    .assignedBy(actor)
                    .expiresAt(command.expiresAt())
                    .reason(command.reason())
                    .build();
            userRepository.saveUserRole(userRole);
            results.add(new RoleAssignmentResult(role.getUuid(), role.getName(), now, command.expiresAt(), command.reason()));
        }

        return results;
    }
}