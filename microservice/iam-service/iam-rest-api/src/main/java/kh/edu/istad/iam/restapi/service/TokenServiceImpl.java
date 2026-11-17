package kh.edu.istad.iam.restapi.service;

import kh.edu.istad.iam.domain.dto.result.AuthUserResult;
import kh.edu.istad.iam.domain.dto.result.SystemConfigResult;
import kh.edu.istad.iam.domain.usecase.user.LoadUserForAuthenticationUseCase;
import kh.edu.istad.iam.restapi.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtEncoder jwtEncoder;
    private final LoadUserForAuthenticationUseCase loadUserForAuthenticationUseCase;
    private final SystemConfigService configService;

    @Value("${jwt.access-token-expiration:18000}")
    private long defaultExpiresIn;

    @Value("${jwt.issuer:http://localhost:9000}")
    private String issuer;

    @Override
    public AuthResponse issueTokens(Authentication authentication) {
        return issueTokens(authentication, null);
    }

    @Override
    public AuthResponse issueTokens(Authentication authentication, UUID sessionId) {
        String username = authentication.getName();
        AuthUserResult authUser = loadUserForAuthenticationUseCase.execute(username);

        Instant now = Instant.now();
        SystemConfigResult config = configService.get();
        long tokenTtlSeconds = (config != null && config.accessTokenTtlMinutes() > 0)
                ? Duration.ofMinutes(config.accessTokenTtlMinutes()).toSeconds()
                : defaultExpiresIn;

        Set<String> authorities = authUser.authorities();
        Set<String> roles = authorities.stream()
                .filter(auth -> auth.startsWith("ROLE_"))
                .collect(Collectors.toSet());

        Set<String> permissions = authorities.stream()
                .filter(auth -> !auth.startsWith("ROLE_"))
                .collect(Collectors.toSet());

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(tokenTtlSeconds))
                .subject(authUser.uuid().toString())
                .claim("preferred_username", authUser.username())
                .claim("roles", roles)
                .claim("permissions", permissions)
                .claim("authorities", authorities);

        if (sessionId != null) {
            claimsBuilder.claim("sid", sessionId.toString());
        }

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claimsBuilder.build())).getTokenValue();

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(tokenTtlSeconds)
                .username(authUser.username())
                .roles(roles)
                .build();
    }
}
