package kh.edu.istad.iam.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kh.edu.istad.iam.domain.dto.command.identity.FederatedIdentityCommand;
import kh.edu.istad.iam.domain.dto.command.user.RegisterUserCommand;
import kh.edu.istad.iam.domain.dto.result.AuthUserResult;
import kh.edu.istad.iam.domain.usecase.identity.FindOrProvisionFederatedUserUseCase;
import kh.edu.istad.iam.domain.usecase.user.RegisterUserUseCase;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import kh.edu.istad.iam.web.dto.RegisterForm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebAuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Value("${telegram.bot-token:}")
    private String telegramBotToken;

    @Value("${telegram.bot-username:}")
    private String telegramBotUsername;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", true);
        }
        model.addAttribute("telegramBotUsername", telegramBotUsername);
        return "login";
    }

    @GetMapping("/login/telegram")
    public String handleTelegramLogin(@RequestParam Map<String, String> params,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        String id = params.get("id");
        String hash = params.get("hash");
        if (id == null || id.isBlank()) {
            return "redirect:/login?error=true";
        }

        if (hash != null && !hash.isBlank() && telegramBotToken != null && !telegramBotToken.isBlank()) {
            if (!verifyTelegramHash(params, hash, telegramBotToken)) {
                return "redirect:/login?error=true";
            }
        }

        try {
            String firstName = params.getOrDefault("first_name", "Telegram");
            String lastName = params.get("last_name");
            String username = params.get("username");
            String photoUrl = params.get("photo_url");

            String displayName = (firstName + (lastName != null && !lastName.isBlank() ? " " + lastName : "")).trim();
            String loginUsername = username != null && !username.isBlank() ? username : "tg_" + id;
            String email = loginUsername + "@telegram.istad.co";

            String attributesJson = "{}";
            try {
                attributesJson = objectMapper.writeValueAsString(params);
            } catch (Exception ignored) {
            }

            FederatedIdentityCommand command = new FederatedIdentityCommand(
                    IdentityProvider.TELEGRAM,
                    id,
                    email,
                    true,
                    displayName,
                    loginUsername,
                    photoUrl,
                    attributesJson
            );

            AuthUserResult authUser = findOrProvisionFederatedUserUseCase.execute(command);

            List<SimpleGrantedAuthority> authorities = authUser.authorities() != null
                    ? authUser.authorities().stream().map(SimpleGrantedAuthority::new).toList()
                    : List.of(new SimpleGrantedAuthority("ROLE_USER"));

            org.springframework.security.core.userdetails.User principal =
                    new org.springframework.security.core.userdetails.User(
                            authUser.username(),
                            "",
                            authUser.enabled(),
                            authUser.accountNonExpired(),
                            authUser.credentialsNonExpired(),
                            authUser.accountNonLocked(),
                            authorities
                    );

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    authorities
            );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            securityContextRepository.saveContext(context, request, response);

            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/login?error=true";
        }
    }

    private boolean verifyTelegramHash(Map<String, String> allParams, String hash, String botToken) {
        if (botToken == null || botToken.isBlank()) {
            return true;
        }
        if (hash == null || hash.isBlank()) {
            return false;
        }
        try {
            List<String> keys = new ArrayList<>(allParams.keySet());
            keys.remove("hash");
            Collections.sort(keys);

            StringBuilder dataCheckString = new StringBuilder();
            for (String key : keys) {
                if (dataCheckString.length() > 0) {
                    dataCheckString.append("\n");
                }
                dataCheckString.append(key).append("=").append(allParams.get(key));
            }

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] secretKey = md.digest(botToken.getBytes(StandardCharsets.UTF_8));

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));
            byte[] hmac = mac.doFinal(dataCheckString.toString().getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hmac) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString().equalsIgnoreCase(hash);
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("request", new RegisterForm("", "", "", "", "", "", "km"));
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("request") RegisterForm request,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            RegisterUserCommand command = new RegisterUserCommand(
                    request.userName(),
                    request.email(),
                    request.password(),
                    request.confirmPassword(),
                    request.givenName(),
                    request.familyName(),
                    request.locale() != null ? request.locale() : "km"
            );
            registerUserUseCase.execute(command);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}
