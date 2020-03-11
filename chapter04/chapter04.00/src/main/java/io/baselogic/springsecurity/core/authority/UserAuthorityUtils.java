package io.baselogic.springsecurity.core.authority;

import io.baselogic.springsecurity.domain.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * A utility class used for creating the {@link GrantedAuthority}'s given a {@link AppUser}. In a real solution
 * this would be looked up in the existing system, but for simplicity our original system had no notion of authorities.
 *
 * @since chapter03.03
 * @author Mick Knutson
 *
 */
public interface UserAuthorityUtils {

    public static Collection<GrantedAuthority> createAuthorities(final @NotNull AppUser appUser) {
        String username = appUser.getEmail();
        if (username.startsWith("admin")) {
            return AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
        }
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

} // The End...
