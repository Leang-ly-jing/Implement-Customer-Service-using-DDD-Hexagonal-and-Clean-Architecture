package kh.edu.istad.iam.domain.usecase.role;

import kh.edu.istad.iam.domain.dto.command.role.UpdateRoleCommand;
import kh.edu.istad.iam.domain.dto.result.RoleResult;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.mapper.RoleDataMapper;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
@Component
@RequiredArgsConstructor

public class UpdateRoleUseCase {

    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;
    private final RoleDataMapper roleDataMapper;

    public RoleResult execute(UpdateRoleCommand command) {
        Role role = roleRepository.findByUuid(command.roleUuid())
                .orElseThrow(() ->
                        new AuthorizationDomainException("Role not found"));

        role.validateCanModify();
        role.setDescription(command.description());
        role.setUpdatedAt(Instant.now());
        role.setUpdatedBy(currentActorProvider.getCurrentActor());
        Role updated = roleRepository.save(role);
        return roleDataMapper.roleToRoleResult(updated, List.of());
    }
}
