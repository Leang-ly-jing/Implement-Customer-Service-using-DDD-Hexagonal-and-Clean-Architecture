package kh.edu.istad.iam.domain.usecase.user;

import kh.edu.istad.iam.domain.dto.command.user.CreateUserCommand;
import kh.edu.istad.iam.domain.dto.result.RoleSummary;
import kh.edu.istad.iam.domain.dto.result.UserResult;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserRole;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.mapper.UserDataMapper;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PasswordHasher;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;
    private final UserDataMapper userDataMapper;

    public UserResult execute(CreateUserCommand command){
        if (userRepository.existsByUsername(command.username()) || userRepository.existsByEmail(command.email())){
            throw new AuthorizationDomainException("Username or Email already exists");
        }
        if (command.phoneNumber() != null && userRepository.existsByPhoneNumber(command.phoneNumber())){
            throw new AuthorizationDomainException("Phone number already exists");
        }

        Instant now = Instant.now();
        String actor = currentActorProvider.getCurrentActor();
        String password = passwordHasher.hash(command.password());
        User user = userDataMapper.createCommandToUser(command, password, actor, now);
        User savedUser = userRepository.save(user);

        List<RoleSummary> roleSummaries = new ArrayList<>();
        if (command.roleUuids() != null && !command.roleUuids().isEmpty()) {
            List<Role> roles = roleRepository.findByUuids(command.roleUuids());
            for (Role role : roles) {
                role.validateCanAssign();
                UserRole userRole = UserRole.builder()
                        .userId(savedUser.getId())
                        .roleId(role.getId())
                        .assignedBy(actor)
                        .assignedAt(now)
                        .reason("CREATE")
                        .build();

                userRepository.saveUserRole(userRole);
                roleSummaries.add(new RoleSummary(role.getUuid(),role.getName(),role.isActive()));
            }
        }
        return userDataMapper.userToUserResult(savedUser, roleSummaries);

    }



}
