package io.baselogic.springsecurity.core.authority;

import io.baselogic.springsecurity.domain.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * A utility class used for creating the {@link GrantedAuthority}'s given a {@link AppUser}. In a real solution
 * this would be looked up in the existing system, but for simplicity our original system had no notion of authorities.
 *
 * @since chapter03.00
 * @author Mick Knutson
 *
 */
public interface UserAuthorityUtils {

    static final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN",
            "ROLE_USER");
    static final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER");

    public static Collection<GrantedAuthority> createAuthorities(final @NotNull AppUser appUser) {
        String username = appUser.getEmail();
        if (username.startsWith("admin")) {
            return ADMIN_ROLES;
        }
        return USER_ROLES;
    }

} // The End...
