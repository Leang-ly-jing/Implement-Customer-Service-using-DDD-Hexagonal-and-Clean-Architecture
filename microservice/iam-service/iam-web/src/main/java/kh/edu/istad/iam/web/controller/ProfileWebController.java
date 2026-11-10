package kh.edu.istad.iam.web.controller;

import jakarta.validation.Valid;
import kh.edu.istad.iam.domain.dto.command.user.ChangePasswordCommand;
import kh.edu.istad.iam.domain.dto.command.user.UpdateUserProfileCommand;
import kh.edu.istad.iam.domain.dto.result.IdentityResult;
import kh.edu.istad.iam.domain.dto.result.UserAuthoritiesResult;
import kh.edu.istad.iam.domain.dto.result.UserResult;
import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.usecase.identity.ListUserIdentitiesUseCase;
import kh.edu.istad.iam.domain.usecase.identity.UnlinkIdentityUseCase;
import kh.edu.istad.iam.domain.usecase.user.ChangePasswordUseCase;
import kh.edu.istad.iam.domain.usecase.user.GetUserAuthoritiesUseCase;
import kh.edu.istad.iam.domain.usecase.user.GetUserUseCase;
import kh.edu.istad.iam.domain.usecase.user.UpdateUserProfileUseCase;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import kh.edu.istad.iam.web.dto.ChangePasswordForm;
import kh.edu.istad.iam.web.dto.ConnectedAppDto;
import kh.edu.istad.iam.web.dto.ProfileEditForm;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ProfileWebController {

    private final UserRepository userRepository;
    private final GetUserUseCase getUserUseCase;
    private final GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ListUserIdentitiesUseCase listUserIdentitiesUseCase;
    private final UnlinkIdentityUseCase unlinkIdentityUseCase;
    private final RegisteredClientRepository registeredClientRepository;
    private final JdbcTemplate jdbcTemplate;

    private User resolveCurrentUser(Authentication authentication) {
        if (authentication == null) {
            throw new AuthorizationDomainException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> byUsername = userRepository.findByUsernameOrEmail(username);
            if (byUsername.isPresent()) {
                return byUsername.get();
            }
        }

        if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            if (email != null && !email.isBlank()) {
                Optional<User> byEmail = userRepository.findByEmail(email);
                if (byEmail.isPresent()) {
                    return byEmail.get();
                }
            }
            String sub = oauth2User.getAttribute("sub");
            if (sub != null && !sub.isBlank()) {
                for (IdentityProvider provider : IdentityProvider.values()) {
                    Optional<Identity> identityOpt = userRepository.findIdentityByProviderAndSubject(provider, sub);
                    if (identityOpt.isPresent()) {
                        return userRepository.findById(identityOpt.get().getUserId())
                                .orElseThrow(() -> new AuthorizationDomainException("User not found for identity: " + sub));
                    }
                }
            }
        }

        String principalName = authentication.getName();
        Optional<User> byName = userRepository.findByUsernameOrEmail(principalName);
        if (byName.isPresent()) {
            return byName.get();
        }

        for (IdentityProvider provider : IdentityProvider.values()) {
            Optional<Identity> identityOpt = userRepository.findIdentityByProviderAndSubject(provider, principalName);
            if (identityOpt.isPresent()) {
                return userRepository.findById(identityOpt.get().getUserId())
                        .orElseThrow(() -> new AuthorizationDomainException("User not found for identity: " + principalName));
            }
        }

        try {
            UUID uuid = UUID.fromString(principalName);
            return userRepository.findByUuid(uuid)
                    .orElseThrow(() -> new AuthorizationDomainException("User not found: " + principalName));
        } catch (IllegalArgumentException e) {
            throw new AuthorizationDomainException("User not found: " + principalName);
        }
    }

    @GetMapping({"/", "/profile"})
    public String index(Authentication authentication, Model model) {
        User user = resolveCurrentUser(authentication);
        if ("User".equalsIgnoreCase(user.getFamilyName())) {
            user.setFamilyName(null);
            userRepository.save(user);
        }
        UserResult userResult = getUserUseCase.execute(user.getUuid());
        UserAuthoritiesResult authorities = getUserAuthoritiesUseCase.execute(user.getUuid());

        model.addAttribute("user", userResult);
        model.addAttribute("currentUser", user);
        model.addAttribute("authorities", authorities);
        return "index";
    }

    @GetMapping("/profile/edit")
    public String editPage(Authentication authentication, Model model) {
        User user = resolveCurrentUser(authentication);
        UserResult userResult = getUserUseCase.execute(user.getUuid());

        model.addAttribute("user", userResult);
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String handleEdit(Authentication authentication,
                             @Valid @ModelAttribute ProfileEditForm form,
                             org.springframework.validation.BindingResult bindingResult,
                             Model model) {
        User user = resolveCurrentUser(authentication);
        if (bindingResult.hasErrors()) {
            UserResult userResult = getUserUseCase.execute(user.getUuid());
            model.addAttribute("user", userResult);
            model.addAttribute("errorMessage", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "profile-edit";
        }
        try {
            UpdateUserProfileCommand command = new UpdateUserProfileCommand(
                    user.getUuid(),
                    form.givenName(),
                    form.familyName(),
                    null,
                    null,
                    null,
                    form.locale()
            );
            updateUserProfileUseCase.execute(command);
            return "redirect:/profile";
        } catch (Exception e) {
            UserResult userResult = getUserUseCase.execute(user.getUuid());
            model.addAttribute("user", userResult);
            model.addAttribute("errorMessage", e.getMessage());
            return "profile-edit";
        }
    }

    @GetMapping("/profile/password")
    public String passwordPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        return "change-password";
    }

    @PostMapping("/profile/password")
    public String handlePassword(Authentication authentication,
                                 @Valid @ModelAttribute ChangePasswordForm form,
                                 org.springframework.validation.BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "change-password";
        }
        User user = resolveCurrentUser(authentication);
        try {
            ChangePasswordCommand command = new ChangePasswordCommand(
                    user.getUuid(),
                    form.currentPassword(),
                    form.newPassword(),
                    form.confirmPassword()
            );
            changePasswordUseCase.execute(command);
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "change-password";
        }
    }

    @GetMapping("/profile/accounts")
    public String linkedAccounts(Authentication authentication, Model model) {
        User user = resolveCurrentUser(authentication);
        List<IdentityResult> identities = listUserIdentitiesUseCase.execute(user.getUuid());

        model.addAttribute("identities", identities);
        return "linked-accounts";
    }

    @PostMapping("/profile/accounts/{id}/unlink")
    public String unlinkAccount(Authentication authentication,
                                @PathVariable("id") UUID identityUuid) {
        User user = resolveCurrentUser(authentication);
        unlinkIdentityUseCase.execute(user.getUuid(), identityUuid);
        return "redirect:/profile/accounts";
    }

    @GetMapping("/profile/apps")
    public String connectedApps(Authentication authentication, Model model) {
        User user = resolveCurrentUser(authentication);
        List<ConnectedAppDto> apps = new ArrayList<>();

        try {
            List<String> registeredClientIds = jdbcTemplate.query(
                    "SELECT registered_client_id, authorities FROM oauth2_authorization_consent WHERE principal_name = ?",
                    (rs, rowNum) -> {
                        String regId = rs.getString("registered_client_id");
                        String authoritiesStr = rs.getString("authorities");
                        RegisteredClient client = registeredClientRepository.findById(regId);
                        String clientName = client != null && client.getClientName() != null ? client.getClientName() : regId;
                        String clientId = client != null ? client.getClientId() : regId;
                        Set<String> scopes = StringUtils.hasText(authoritiesStr)
                                ? StringUtils.commaDelimitedListToSet(authoritiesStr)
                                : Set.of();
                        apps.add(new ConnectedAppDto(clientId, clientName, scopes));
                        return regId;
                    },
                    user.getUsernameValue()
            );
        } catch (Exception ignored) {
        }

        model.addAttribute("apps", apps);
        return "connected-apps";
    }

    @PostMapping("/profile/apps/{clientId}/revoke")
    public String revokeApp(Authentication authentication,
                            @PathVariable("clientId") String clientId) {
        User user = resolveCurrentUser(authentication);
        RegisteredClient client = registeredClientRepository.findByClientId(clientId);
        String regId = client != null ? client.getId() : clientId;

        try {
            jdbcTemplate.update(
                    "DELETE FROM oauth2_authorization_consent WHERE (registered_client_id = ? OR registered_client_id = ?) AND principal_name = ?",
                    regId, clientId, user.getUsernameValue()
            );
        } catch (Exception ignored) {
        }

        return "redirect:/profile/apps";
    }
}
