package kh.edu.istad.iam.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kh.edu.istad.iam.domain.dto.command.identity.FederatedIdentityCommand;
import kh.edu.istad.iam.domain.dto.result.AuthUserResult;
import kh.edu.istad.iam.domain.usecase.identity.FindOrProvisionFederatedUserUseCase;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomOAuth2UserService(FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase) {
        this.findOrProvisionFederatedUserUseCase = findOrProvisionFederatedUserUseCase;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId().toLowerCase();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        IdentityProvider provider;
        String providerSubject;
        String email = null;
        String displayName = null;
        String pictureUrl = null;
        String username = null;
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        if ("google".equals(registrationId)) {
            provider = IdentityProvider.GOOGLE;
            providerSubject = (String) attributes.get("sub");
            email = (String) attributes.get("email");
            displayName = (String) attributes.get("name");
            pictureUrl = (String) attributes.get("picture");
            username = email != null ? email.split("@")[0] : null;
        } else if ("github".equals(registrationId)) {
            provider = IdentityProvider.GITHUB;
            providerSubject = String.valueOf(attributes.get("id"));
            email = (String) attributes.get("email");
            displayName = (String) attributes.get("name");
            pictureUrl = (String) attributes.get("avatar_url");
            username = (String) attributes.get("login");
        } else if ("facebook".equals(registrationId)) {
            provider = IdentityProvider.FACEBOOK;
            providerSubject = (String) attributes.get("id");
            email = (String) attributes.get("email");
            displayName = (String) attributes.get("name");
            username = (String) attributes.get("name");
        } else {
            provider = IdentityProvider.TELEGRAM;
            providerSubject = attributes.get("id") != null ? String.valueOf(attributes.get("id")) : String.valueOf(attributes.get("sub"));
            displayName = (String) attributes.get("name");
            if (displayName == null) {
                displayName = (String) attributes.get("first_name");
            }
            username = (String) attributes.get("username");
            if (username == null) {
                username = (String) attributes.get("preferred_username");
            }
            if (pictureUrl == null) {
                pictureUrl = (String) attributes.get("picture");
            }
            if (email == null) {
                email = (String) attributes.get("email");
            }
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
        return new CustomOAuth2User(authUser, attributes, userNameAttributeName != null ? userNameAttributeName : "sub");
    }
}
