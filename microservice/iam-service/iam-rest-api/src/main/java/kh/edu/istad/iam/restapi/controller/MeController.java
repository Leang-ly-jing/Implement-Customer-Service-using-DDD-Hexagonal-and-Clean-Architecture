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
import kh.edu.istad.iam.domain.valueobject.AccountAction;
import kh.edu.istad.iam.restapi.dto.request.ChangePasswordRequest;
import kh.edu.istad.iam.restapi.dto.request.ClientInfo;
import kh.edu.istad.iam.restapi.dto.request.DeactivateAccountRequest;
import kh.edu.istad.iam.restapi.dto.request.UpdateUserProfileRequest;
import kh.edu.istad.iam.restapi.dto.response.*;
import kh.edu.istad.iam.restapi.mapper.UserWebMapper;
import kh.edu.istad.iam.restapi.service.UserSessionService;
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
    private final UserSessionService userSessionService;
    private final kh.edu.istad.iam.domain.usecase.user.DisableUserUseCase disableUserUseCase;
    private final kh.edu.istad.iam.restapi.service.LoginHistoryService loginHistoryService;
    private final kh.edu.istad.iam.restapi.service.AccountActivityService accountActivityService;

    @GetMapping("/activities")
    public List<AccountActivityResponse> getActivities(@AuthenticationPrincipal Jwt jwt) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        return accountActivityService.getActivities(userUuid);
    }

    @GetMapping("/login-history")
    public List<LoginHistoryResponse> getLoginHistory(@AuthenticationPrincipal Jwt jwt) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        return loginHistoryService.getLoginHistory(userUuid);
    }

    @GetMapping("/sessions")
    public List<UserSessionResponse> getSessions(@AuthenticationPrincipal Jwt jwt) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        String sid = jwt.getClaimAsString("sid");
        UUID currentSid = sid != null ? UUID.fromString(sid) : null;
        return userSessionService.getSessions(userUuid, currentSid);
    }

    @DeleteMapping("/sessions/{sessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeSession(@AuthenticationPrincipal Jwt jwt,
                              @PathVariable("sessionId") UUID sessionId,
                              jakarta.servlet.http.HttpServletRequest request) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        userSessionService.revokeSession(userUuid, sessionId);
        accountActivityService.record(userUuid,  AccountAction.SESSION_TERMINATED, sessionId.toString(), ClientInfo.from(request));
    }

    @DeleteMapping("/sessions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeOthers(@AuthenticationPrincipal Jwt jwt,
                             jakarta.servlet.http.HttpServletRequest request) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        String sid = jwt.getClaimAsString("sid");
        if (sid == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Token does not contain a session id");
        }
        userSessionService.revokeAllOtherSessions(userUuid, UUID.fromString(sid));
        accountActivityService.record(userUuid, AccountAction.ALL_SESSIONS_REVOKED, null, ClientInfo.from(request));
    }

    @GetMapping
    public UserResponse getProfile(@AuthenticationPrincipal Jwt jwt) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        UserResult result = getUserUseCase.execute(userUuid);
        return userWebMapper.toResponse(result);
    }

    @PatchMapping
    public UserResponse updateProfile(@AuthenticationPrincipal Jwt jwt,
                                      @Valid @RequestBody UpdateUserProfileRequest request,
                                      jakarta.servlet.http.HttpServletRequest servletRequest) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        UpdateUserProfileCommand command = userWebMapper.toCommand(userUuid, request);
        UserResult result = updateUserProfileUseCase.execute(command);
        accountActivityService.record(userUuid, AccountAction.PROFILE_UPDATED, null, ClientInfo.from(servletRequest));
        return userWebMapper.toResponse(result);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@AuthenticationPrincipal Jwt jwt,
                               @Valid @RequestBody ChangePasswordRequest request,
                               jakarta.servlet.http.HttpServletRequest servletRequest) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        ChangePasswordCommand command = new ChangePasswordCommand(
                userUuid,
                request.currentPassword(),
                request.newPassword(),
                request.confirmPassword()
        );
        changePasswordUseCase.execute(command);
        accountActivityService.record(userUuid, AccountAction.PASSWORD_CHANGED, null, ClientInfo.from(servletRequest));
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
    public void unlinkIdentity(@AuthenticationPrincipal Jwt jwt,
                               @PathVariable("identityUuid") UUID identityUuid,
                               jakarta.servlet.http.HttpServletRequest request) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        unlinkIdentityUseCase.execute(userUuid, identityUuid);
        accountActivityService.record(userUuid, AccountAction.IDENTITY_UNLINKED, identityUuid.toString(), ClientInfo.from(request));
    }

    @PostMapping("/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateAccount(@AuthenticationPrincipal Jwt jwt,
                                  @RequestBody(required = false) DeactivateAccountRequest request,
                                  jakarta.servlet.http.HttpServletRequest servletRequest) {
        UUID userUuid = UUID.fromString(jwt.getSubject());
        String reason = request != null ? request.reason() : null;

        disableUserUseCase.execute(userUuid);
        userSessionService.revokeAllSessions(userUuid);
        accountActivityService.record(
                userUuid, AccountAction.ACCOUNT_DEACTIVATED,
                reason, ClientInfo.from(servletRequest)
        );
    }
}
