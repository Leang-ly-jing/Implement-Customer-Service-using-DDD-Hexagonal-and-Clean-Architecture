package kh.edu.istad.iam.infrastructure.config;

import kh.edu.istad.iam.domain.dto.result.AuthUserResult;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User, UserDetails {

    private final AuthUserResult authUser;
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;

    public CustomOAuth2User(AuthUserResult authUser, Map<String, Object> attributes, String nameAttributeKey) {
        this.authUser = authUser;
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authUser.authorities().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getName() {
        Object name = attributes.get(nameAttributeKey);
        return name != null ? name.toString() : authUser.username();
    }

    @Override
    public String getPassword() {
        return authUser.passwordHash();
    }

    @Override
    public String getUsername() {
        return authUser.username();
    }

    @Override
    public boolean isAccountNonExpired() {
        return authUser.accountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return authUser.accountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return authUser.credentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return authUser.enabled();
    }
}