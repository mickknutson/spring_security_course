package io.baselogic.springsecurity.userdetails;

import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.EventUserDetails;
import io.baselogic.springsecurity.service.DefaultEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * Integrates with Spring Security using our existing {@link UserDao} by looking up
 * a {@link User} and
 * converting it into a {@link EventUserDetails} so that Spring Security can do the
 * username/password comparison for us.
 *
 *  This replaces the manual UserDetailsService code in
 *  {@link DefaultEventService#createUser(AppUser)}
 *
 * @author mickknutson
 *
 * @since chapter03.03 Created Class
 * @since chapter03.04 added support for custom EventUserDetails
 *
 */
@Service
@Slf4j
public class EventUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public EventUserDetailsService(final @NotNull UserDao userDao) {
        this.userDao = userDao;
    }


    /**
     * Lookup a {@link AppUser} by the username representing
     * the email address. Then, convert the {@link AppUser}
     * into a {@link EventUserDetails} to conform to the {@link UserDetails} interface.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("*** Executing eventUserDetailsService.loadUserByUsername('{}')", username);
        AppUser appUser = userDao.findByEmail(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("Invalid username/password.");
        }
        return new EventUserDetails(appUser);
    }

} // The End...
