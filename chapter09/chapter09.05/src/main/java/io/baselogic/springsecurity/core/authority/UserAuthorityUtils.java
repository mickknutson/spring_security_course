package io.baselogic.springsecurity.core.authority;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.EventUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * A utility class used for creating the {@link GrantedAuthority}'s given a {@link AppUser}. In a real solution
 * this would be looked up in the existing system, but for simplicity our original system had no notion of authorities.
 *
 * @author Mick Knutson

 * @since chapter03.03
 * @since chapter04.01 added {@link UserAuthorityUtils#getUserEmail(Object)} method
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

    public static String getUserEmail(final @NotNull Object user) {
        if(user instanceof User) {
            return ((User) user).getUsername();
        } else if(user instanceof EventUserDetails){
            return ((EventUserDetails) user).getUsername();
        } else{
            return null;
        }
    }

} // The End...
