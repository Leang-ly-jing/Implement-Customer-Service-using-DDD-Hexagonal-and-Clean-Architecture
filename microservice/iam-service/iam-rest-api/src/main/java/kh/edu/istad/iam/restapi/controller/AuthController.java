package kh.edu.istad.iam.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.iam.domain.dto.command.user.RegisterUserCommand;
import kh.edu.istad.iam.domain.dto.result.UserResult;
import kh.edu.istad.iam.domain.usecase.user.RegisterUserUseCase;
import kh.edu.istad.iam.restapi.dto.request.RegisterUserRequest;
import kh.edu.istad.iam.restapi.dto.response.UserResponse;
import kh.edu.istad.iam.restapi.mapper.UserWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final UserWebMapper userWebMapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody
                                     RegisterUserRequest request) {
        RegisterUserCommand command = userWebMapper.toCommand(request);

        UserResult result = registerUserUseCase.execute(command);
        return userWebMapper.toResponse(result);
    }
}