package kh.edu.istad.iam.domain.usecase.role;

import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class DeactivateRoleUseCase {

    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;

    public void execute(UUID roleUuid) {
        Role role = roleRepository.findByUuid(roleUuid)
                .orElseThrow(() ->
                        new AuthorizationDomainException("Role not found!!"));

        role.validateCanModify();
        role.setActive(false);
        role.setUpdatedAt(Instant.now());
        role.setUpdatedBy(currentActorProvider.getCurrentActor());

        roleRepository.save(role);
    }
}
