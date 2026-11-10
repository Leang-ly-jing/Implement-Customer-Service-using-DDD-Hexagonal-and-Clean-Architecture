package kh.edu.istad.iam.domain.usecase.user;

import kh.edu.istad.iam.domain.dto.command.user.RegisterUserCommand;
import kh.edu.istad.iam.domain.dto.result.RoleSummary;
import kh.edu.istad.iam.domain.dto.result.UserResult;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserRole;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.mapper.UserDataMapper;
import kh.edu.istad.iam.domain.port.out.PasswordHasher;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordHasher passwordHasher;
    private final UserDataMapper userDataMapper;


    public UserResult execute(RegisterUserCommand command){
        if (!command.password().equals(command.confirmPassword())) {
            throw new AuthorizationDomainException("Your passwords do not match with your confirm password");
        }
        if (userRepository.existsByUsername(command.username()) || userRepository.existsByEmail(command.email())) {
            throw new AuthorizationDomainException("Username or Email is already in use");
        }
        Instant now = Instant.now();
        String hashedPassword = passwordHasher.hash(command.password());
        User user = userDataMapper.registerCommandToUser(command, hashedPassword, now);

        User savedUser = userRepository.save(user);
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(()-> new AuthorizationDomainException("Role not found"));

        UserRole assignment = UserRole.builder()
                .userId(savedUser.getId())
                .roleId(userRole.getId())
                .assignedAt(now)
                .assignedBy("SYSTEM ADMIN")
                .reason("DEFAULT")
                .build();
        userRepository.saveUserRole(assignment);

        List<RoleSummary> roles = List.of(new RoleSummary(userRole.getUuid(), userRole.getName(), userRole.isActive()));
        return userDataMapper.userToUserResult(savedUser, roles);

    }
}
