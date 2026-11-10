package kh.edu.istad.iam.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kh.edu.istad.iam.domain.dto.command.identity.FederatedIdentityCommand;
import kh.edu.istad.iam.domain.dto.result.AuthUserResult;
import kh.edu.istad.iam.domain.usecase.identity.FindOrProvisionFederatedUserUseCase;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomOidcUserService(FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase) {
        this.findOrProvisionFederatedUserUseCase = findOrProvisionFederatedUserUseCase;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId().toLowerCase();
        Map<String, Object> attributes = oidcUser.getAttributes();

        IdentityProvider provider;
        String providerSubject;
        String email = null;
        String displayName = null;
        String pictureUrl = null;
        String username = null;

        if ("google".equals(registrationId)) {
            provider = IdentityProvider.GOOGLE;
            providerSubject = oidcUser.getSubject();
            email = oidcUser.getEmail();
            displayName = oidcUser.getFullName();
            pictureUrl = oidcUser.getPicture();
            username = email != null ? email.split("@")[0] : null;
        } else if ("telegram".equals(registrationId)) {
            provider = IdentityProvider.TELEGRAM;
            providerSubject = attributes.get("id") != null ? String.valueOf(attributes.get("id")) : oidcUser.getSubject();
            displayName = (String) attributes.get("name");
            if (displayName == null) {
                displayName = (String) attributes.get("first_name");
            }
            username = (String) attributes.get("username");
            if (username == null) {
                username = (String) attributes.get("preferred_username");
            }
            if (username == null || username.isBlank()) {
                username = "tg_" + providerSubject;
            }
            pictureUrl = (String) attributes.get("picture");
            email = (String) attributes.get("email");
            if (email == null || email.isBlank()) {
                email = username + "@telegram.istad.co";
            }
        } else {
            provider = IdentityProvider.GOOGLE;
            providerSubject = oidcUser.getSubject();
            email = oidcUser.getEmail();
            displayName = oidcUser.getFullName();
            username = email != null ? email.split("@")[0] : oidcUser.getSubject();
        }

        String attributesJson = "{}";
        try {
            attributesJson = objectMapper.writeValueAsString(attributes);
        } catch (Exception ignored) {
        }

        FederatedIdentityCommand command = new FederatedIdentityCommand(
                provider,
                providerSubject,
                email,
                email != null,
                displayName != null ? displayName : username,
                username,
                pictureUrl,
                attributesJson
        );

        AuthUserResult authUser = findOrProvisionFederatedUserUseCase.execute(command);

        Set<GrantedAuthority> authorities = new HashSet<>(oidcUser.getAuthorities());
        if (authUser.authorities() != null) {
            authorities.addAll(authUser.authorities().stream().map(SimpleGrantedAuthority::new).toList());
        }

        String nameAttributeKey = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        if (nameAttributeKey == null || nameAttributeKey.isBlank()) {
            nameAttributeKey = "sub";
        }

        if (oidcUser.getUserInfo() != null) {
            return new CustomOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo(), nameAttributeKey, authUser);
        } else {
            return new CustomOidcUser(authorities, oidcUser.getIdToken(), nameAttributeKey, authUser);
        }
    }
}

