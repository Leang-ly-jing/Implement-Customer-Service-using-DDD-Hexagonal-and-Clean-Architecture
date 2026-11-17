package kh.edu.istad.iam.restapi.validator;

import kh.edu.istad.iam.domain.dto.result.SystemConfigResult;
import kh.edu.istad.iam.restapi.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PasswordPolicyValidator {

    private final SystemConfigService configService;

    public void validate(String password) {
        SystemConfigResult c = configService.get();
        List<String> errors = new ArrayList<>();

        if (password == null || password.length() < c.passwordMinLength()) {
            errors.add("at least " + c.passwordMinLength() + " characters");
        }
        if (c.requireUppercase() && (password == null || !password.matches(".*[A-Z].*"))) {
            errors.add("an uppercase letter");
        }
        if (c.requireDigit() && (password == null || !password.matches(".*\\d.*"))) {
            errors.add("a digit");
        }
        if (c.requireSpecial() && (password == null || !password.matches(".*[^A-Za-z0-9].*"))) {
            errors.add("a special character");
        }

        if (!errors.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password must contain " + String.join(", ", errors)
            );
        }
    }
}
