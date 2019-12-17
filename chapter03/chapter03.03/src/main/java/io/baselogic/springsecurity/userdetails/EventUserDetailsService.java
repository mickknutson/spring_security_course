package io.baselogic.springsecurity.userdetails;

import io.baselogic.springsecurity.core.authority.UserAuthorityUtils;
import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.service.DefaultEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Integrates with Spring Security using our existing {@link UserDao} by looking up
 * a {@link User} and
 * converting it into a {@link UserDetails} so that Spring Security can do the
 * username/password comparison for us.
 *
 *  This replaces the manual UserDetailsService code in
 *  {@link DefaultEventService#createUser(io.baselogic.springsecurity.domain.User)}
 *
 * @author mickknutson
 *
 * @since chapter03.03
 *
 *
 */
@Component
@Slf4j
public class EventUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public EventUserDetailsService(final @NotNull UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Lookup a {@link User} by the username representing the email address. Then, convert the
     * {@link User} into a {@link UserDetails} to conform to the {@link UserDetails} interface.
     */
    @Override
    public UserDetails loadUserByUsername(final @NotEmpty String username) throws UsernameNotFoundException {
        io.baselogic.springsecurity.domain.User user = userDao.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username/password.");
        }
        Collection<GrantedAuthority> authorities = UserAuthorityUtils.createAuthorities(user);
        return new User(user.getEmail(), user.getPassword(), authorities);
    }

} // The End...
