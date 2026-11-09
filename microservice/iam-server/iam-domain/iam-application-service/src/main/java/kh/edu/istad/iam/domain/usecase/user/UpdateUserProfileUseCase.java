package kh.edu.istad.iam.domain.usecase.user;

import kh.edu.istad.iam.domain.dto.command.user.UpdateUserProfileCommand;
import kh.edu.istad.iam.domain.dto.result.UserResult;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.mapper.UserDataMapper;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
@Component
@RequiredArgsConstructor
public class UpdateUserProfileUseCase {

    private final UserRepository userRepository;
    private final CurrentActorProvider currentActorProvider;
    private final UserDataMapper userDataMapper;

    public UserResult execute(UpdateUserProfileCommand command) {
        User user = userRepository.findByUuid(command.userUuid())
                .orElseThrow(() -> new AuthorizationDomainException("User not found"));

        if (command.givenName() != null) user.setGivenName(command.givenName());
        if (command.familyName() != null) user.setFamilyName(command.familyName());
        if (command.middleName() != null) user.setMiddleName(command.middleName());
        if (command.nickname() != null) user.setNickname(command.nickname());
        if (command.locale() != null) user.setLocale(command.locale());
        if (command.phoneNumber() != null) user.setPhoneNumber(command.phoneNumber());

        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(currentActorProvider.getCurrentActor());

        User updatedUser = userRepository.save(user);
        return userDataMapper.userToUserResult(updatedUser, List.of());
    }
}