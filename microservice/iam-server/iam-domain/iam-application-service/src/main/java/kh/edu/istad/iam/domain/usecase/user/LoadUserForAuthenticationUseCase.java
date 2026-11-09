package kh.edu.istad.iam.domain.usecase.user;

import kh.edu.istad.iam.domain.dto.result.AuthUserResult;
import kh.edu.istad.iam.domain.dto.result.UserAuthoritiesResult;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.mapper.UserDataMapper;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
@Component
@RequiredArgsConstructor
public class LoadUserForAuthenticationUseCase {

    private final UserRepository userRepository;
    private final GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;
    private final UserDataMapper userDataMapper;

    public AuthUserResult execute(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new AuthorizationDomainException("User not found"));

        UserAuthoritiesResult authoritiesResult = getUserAuthoritiesUseCase.execute(user.getUuid());

        Set<String> allAuthorities = new HashSet<>();
        authoritiesResult.roles().forEach(r -> allAuthorities.add("ROLE_" + r.name()));
        allAuthorities.addAll(authoritiesResult.effectivePermissions());

        return userDataMapper.userToAuthUserResult(user, allAuthorities);
    }
}
