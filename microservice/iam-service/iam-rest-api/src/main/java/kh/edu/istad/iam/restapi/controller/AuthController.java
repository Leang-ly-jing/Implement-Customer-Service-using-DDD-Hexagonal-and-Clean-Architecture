package kh.edu.istad.iam.restapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kh.edu.istad.iam.domain.dto.command.user.RegisterUserCommand;
import kh.edu.istad.iam.domain.dto.result.UserResult;
import kh.edu.istad.iam.domain.usecase.user.RegisterUserUseCase;
import kh.edu.istad.iam.domain.valueobject.LoginFailureReason;
import kh.edu.istad.iam.restapi.dto.request.LoginRequest;
import kh.edu.istad.iam.restapi.dto.request.RegisterUserRequest;
import kh.edu.istad.iam.restapi.dto.response.AuthResponse;
import kh.edu.istad.iam.restapi.dto.response.UserResponse;
import kh.edu.istad.iam.restapi.mapper.UserWebMapper;
import kh.edu.istad.iam.restapi.service.LoginAttemptService;
import kh.edu.istad.iam.restapi.service.LoginHistoryService;
import kh.edu.istad.iam.restapi.service.TokenService;
import kh.edu.istad.iam.restapi.service.UserSessionService;
import kh.edu.istad.iam.restapi.validator.PasswordPolicyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final UserWebMapper userWebMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserSessionService userSessionService;
    private final LoginHistoryService loginHistoryService;
    private final LoginAttemptService loginAttemptService;
    private final PasswordPolicyValidator passwordPolicyValidator;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterUserRequest request) {
        passwordPolicyValidator.validate(request.password());
        RegisterUserCommand command = userWebMapper.toCommand(request);
        UserResult result = registerUserUseCase.execute(command);
        return userWebMapper.toResponse(result);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request,
                              HttpServletRequest servletRequest) {
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
        } catch (AuthenticationException ex) {
            loginAttemptService.onFailure(request.username());
            loginHistoryService.recordLoginFailure(
                    request.username(), resolveFailureReason(ex), servletRequest);
            throw ex;
        }

        loginAttemptService.onSuccess(request.username());
        UUID sessionId = userSessionService.createSession(request.username(), servletRequest);
        loginHistoryService.recordLoginSuccess(request.username(), servletRequest);
        return tokenService.issueTokens(auth, sessionId);
    }

    private LoginFailureReason resolveFailureReason(AuthenticationException ex) {
        if (ex instanceof LockedException) return LoginFailureReason.ACCOUNT_LOCKED;
        if (ex instanceof DisabledException) return LoginFailureReason.ACCOUNT_DISABLED;
        if (ex instanceof BadCredentialsException) return LoginFailureReason.BAD_CREDENTIALS;
        return LoginFailureReason.UNKNOWN;
    }
}
