package kh.edu.istad.iam.restapi.controller;

import jakarta.validation.Valid;

import kh.edu.istad.iam.domain.dto.command.user.ChangePasswordCommand;
import kh.edu.istad.iam.domain.dto.command.user.UpdateUserProfileCommand;
import kh.edu.istad.iam.domain.dto.result.UserResult;
import kh.edu.istad.iam.domain.usecase.identity.ListUserIdentitiesUseCase;
import kh.edu.istad.iam.domain.usecase.identity.UnlinkIdentityUseCase;
import kh.edu.istad.iam.domain.usecase.user.ChangePasswordUseCase;
import kh.edu.istad.iam.domain.usecase.user.GetUserUseCase;
import kh.edu.istad.iam.domain.usecase.user.UpdateUserProfileUseCase;
import kh.edu.istad.iam.restapi.dto.request.ChangePasswordRequest;
import kh.edu.istad.iam.restapi.dto.request.UpdateUserProfileRequest;
import kh.edu.istad.iam.restapi.dto.response.IdentityResponse;
import kh.edu.istad.iam.restapi.dto.response.UserResponse;
import kh.edu.istad.iam.restapi.mapper.UserWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController {

    private final GetUserUseCase getUserUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ListUserIdentitiesUseCase listUserIdentitiesUseCase;
    private final UnlinkIdentityUseCase unlinkIdentityUseCase;
    private final UserWebMapper userWebMapper;

    @GetMapping
    public UserResponse getProfile(@AuthenticationPrincipal Jwt jwt) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        UserResult result = getUserUseCase.execute(userUuid);
        return userWebMapper.toResponse(result);
    }

    @PatchMapping
    public UserResponse updateProfile(@AuthenticationPrincipal Jwt jwt,
                                      @Valid @RequestBody UpdateUserProfileRequest request) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        UpdateUserProfileCommand command = userWebMapper.toCommand(userUuid, request);
        UserResult result = updateUserProfileUseCase.execute(command);
        return userWebMapper.toResponse(result);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@AuthenticationPrincipal Jwt jwt,
                               @Valid @RequestBody ChangePasswordRequest request) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        ChangePasswordCommand command = new ChangePasswordCommand(
                userUuid,
                request.currentPassword(),
                request.newPassword(),
                request.confirmPassword()
        );
        changePasswordUseCase.execute(command);
    }

    @GetMapping("/identities")
    public List<IdentityResponse> getIdentities(@AuthenticationPrincipal Jwt jwt) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        return listUserIdentitiesUseCase.execute(userUuid).stream()
                .map(userWebMapper::toResponse)
                .toList();
    }

    @DeleteMapping("/identities/{identityUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlinkIdentity(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID identityUuid) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        unlinkIdentityUseCase.execute(userUuid, identityUuid);
    }
}
