package kh.edu.istad.iam.domain.usecase.user;

import kh.edu.istad.iam.domain.dto.result.RoleSummary;
import kh.edu.istad.iam.domain.dto.result.UserResult;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserRole;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.mapper.UserDataMapper;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDataMapper userDataMapper;

    public UserResult execute(UUID uuid){
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(()-> new AuthorizationDomainException("User not found"));


        List<UserRole> userRoles = userRepository.findRolesByUserId(user.getId());
        List<RoleSummary> roleSummaries = new ArrayList<>();
        for (UserRole ur : userRoles) {
            if (ur.isActive()) {
                roleRepository.findById(ur.getRoleId()).ifPresent(role ->
                        roleSummaries.add(new RoleSummary(role.getUuid(), role.getName(), role.isActive())));
            }
        }
        return userDataMapper.userToUserResult(user,roleSummaries);
    }


}
