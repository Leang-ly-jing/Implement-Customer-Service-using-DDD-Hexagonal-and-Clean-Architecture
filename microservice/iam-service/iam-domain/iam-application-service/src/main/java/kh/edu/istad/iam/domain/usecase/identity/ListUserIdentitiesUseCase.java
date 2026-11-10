package kh.edu.istad.iam.domain.usecase.identity;

import kh.edu.istad.iam.domain.dto.result.IdentityResult;
import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.mapper.IdentityDataMapper;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class ListUserIdentitiesUseCase {

    private final UserRepository userRepository;
    private final IdentityDataMapper identityDataMapper;

    public List<IdentityResult> execute(UUID userUuid) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() ->
                        new AuthorizationDomainException("User not found"));

        List<Identity> identities = userRepository.findIdentitiesByUserId(user.getId());

        return identities.stream()
                .filter(identity -> !identity.isDeleted())
                .map(identityDataMapper::identityToIdentityResult)
                .toList();
    }
}
