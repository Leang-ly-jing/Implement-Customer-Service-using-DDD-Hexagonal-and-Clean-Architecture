package kh.edu.istad.iam.infrastructure.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.edu.istad.iam.domain.dto.result.AuthUserResult;
import kh.edu.istad.iam.domain.usecase.user.LoadUserForAuthenticationUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtEncoder jwtEncoder;
    private final LoadUserForAuthenticationUseCase loadUserForAuthenticationUseCase;
    private final JdbcTemplate jdbcTemplate;

    @Value("${jwt.access-token-expiration:18000}")
    private long expiresIn;

    @Value("${jwt.issuer:http://localhost:9000}")
    private String issuer;

    public OAuth2AuthenticationSuccessHandler(@org.springframework.context.annotation.Lazy JwtEncoder jwtEncoder,
                                             LoadUserForAuthenticationUseCase loadUserForAuthenticationUseCase,
                                             JdbcTemplate jdbcTemplate) {
        this.jwtEncoder = jwtEncoder;
        this.loadUserForAuthenticationUseCase = loadUserForAuthenticationUseCase;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String username;
        if (authentication.getPrincipal() instanceof CustomOAuth2User oAuth2User) {
            username = oAuth2User.getUsername();
        } else if (authentication.getPrincipal() instanceof CustomOidcUser oidcUser) {
            username = oidcUser.getUsername();
        } else {
            username = authentication.getName();
        }

        AuthUserResult authUser = loadUserForAuthenticationUseCase.execute(username);
        Instant now = Instant.now();

        Set<String> authorities = authUser.authorities();
        Set<String> roles = authorities.stream()
                .filter(auth -> auth.startsWith("ROLE_"))
                .collect(Collectors.toSet());

        Set<String> permissions = authorities.stream()
                .filter(auth -> !auth.startsWith("ROLE_"))
                .collect(Collectors.toSet());

        UUID sessionId = UUID.randomUUID();
        try {
            String userAgent = request.getHeader("User-Agent");
            String ip = request.getRemoteAddr();
            String browser = parseBrowser(userAgent);
            String device = parseDevice(userAgent);
            jdbcTemplate.update(
                    "INSERT INTO user_sessions (id, user_uuid, device_type, browser, ip_address, created_at, last_active_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    sessionId, authUser.uuid(), device, browser, ip, Timestamp.from(now), Timestamp.from(now)
            );
            jdbcTemplate.update(
                    "INSERT INTO login_history (user_uuid, username, status, ip_address, device, device_type, login_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    authUser.uuid(), authUser.username(), "SUCCESS", ip, browser + " (" + device + ")", device.toLowerCase(), Timestamp.from(now)
            );
        } catch (Exception ignored) {
        }

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .subject(authUser.uuid().toString())
                .claim("preferred_username", authUser.username())
                .claim("roles", roles)
                .claim("permissions", permissions)
                .claim("authorities", authorities)
                .claim("sid", sessionId.toString())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        String redirectUrl = UriComponentsBuilder.fromUriString("/account/profile")
                .queryParam("token", token)
                .queryParam("username", authUser.username())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String parseBrowser(String userAgent) {
        if (userAgent == null) return "Browser";
        if (userAgent.contains("Chrome")) return "Chrome";
        if (userAgent.contains("Firefox")) return "Firefox";
        if (userAgent.contains("Safari")) return "Safari";
        if (userAgent.contains("Edge")) return "Edge";
        return "Browser";
    }

    private String parseDevice(String userAgent) {
        if (userAgent == null) return "DESKTOP";
        String lower = userAgent.toLowerCase();
        if (lower.contains("mobile") || lower.contains("android") || lower.contains("iphone")) {
            return "MOBILE";
        }
        if (lower.contains("tablet") || lower.contains("ipad")) {
            return "TABLET";
        }
        return "DESKTOP";
    }
}
