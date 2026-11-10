package kh.edu.istad.iam.infrastructure.config;

import kh.edu.istad.iam.domain.dto.result.AuthUserResult;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;

@Getter
public class CustomOidcUser extends DefaultOidcUser implements UserDetails {

    private final AuthUserResult authUser;

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities,
                          OidcIdToken idToken,
                          OidcUserInfo userInfo,
                          String nameAttributeKey,
                          AuthUserResult authUser) {
        super(authorities, idToken, userInfo, nameAttributeKey);
        this.authUser = authUser;
    }

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities,
                          OidcIdToken idToken,
                          String nameAttributeKey,
                          AuthUserResult authUser) {
        super(authorities, idToken, nameAttributeKey);
        this.authUser = authUser;
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

