package io.baselogic.springsecurity.authority;

import io.baselogic.springsecurity.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.List;

/**
 * A utility class used for creating the {@link GrantedAuthority}'s given a {@link User}. In a real solution
 * this would be looked up in the existing system, but for simplicity our original system had no notion of authorities.
 *
 * @since chapter03.00
 * @author Mick Knutson
 *
 */
public final class UserAuthorityUtils {

    private static final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN",
            "ROLE_USER");
    private static final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER");

    public static Collection<? extends GrantedAuthority> createAuthorities(User user) {
        String username = user.getEmail();
        if (username != null && username.startsWith("admin")) {
            return ADMIN_ROLES;
        }
        return USER_ROLES;
    }

    private UserAuthorityUtils() {
    }

} // The End...
