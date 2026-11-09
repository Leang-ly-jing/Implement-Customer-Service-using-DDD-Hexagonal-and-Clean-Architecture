package kh.edu.istad.iam.infrastructure.config;

import kh.edu.istad.iam.domain.dto.result.AuthUserResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class TokenConfig {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            Authentication principal = context.getPrincipal();
            if (principal == null) {
                return;
            }

            Object userPrincipal = principal.getPrincipal();
            AuthUserResult authUser = null;

            if (userPrincipal instanceof CustomUserDetails userDetails) {
                authUser = userDetails.getAuthUser();
            } else if (userPrincipal instanceof CustomOAuth2User oAuth2User) {
                authUser = oAuth2User.getAuthUser();
            }

            if (authUser != null) {
                String userUuid = authUser.uuid().toString();
                context.getClaims().subject(userUuid);

                Set<String> authorities = authUser.authorities();
                Set<String> roles = authorities.stream()
                        .filter(auth -> auth.startsWith("ROLE_"))
                        .collect(Collectors.toSet());

                Set<String> permissions = authorities.stream()
                        .filter(auth -> !auth.startsWith("ROLE_"))
                        .collect(Collectors.toSet());

                if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                    context.getClaims().claim("preferred_username", authUser.username());
                    context.getClaims().claim("roles", roles);
                    context.getClaims().claim("permissions", permissions);
                    context.getClaims().claim("authorities", authorities);
                }

                if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                    context.getClaims().claim("preferred_username", authUser.username());
                    context.getClaims().claim("email", authUser.email());
                    context.getClaims().claim("roles", roles);
                }
            }
        };
    }
}
