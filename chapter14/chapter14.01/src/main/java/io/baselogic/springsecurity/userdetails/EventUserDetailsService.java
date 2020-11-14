package io.baselogic.springsecurity.userdetails;

import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.EventUserDetails;
import io.baselogic.springsecurity.service.DefaultEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
 * @since chapter14.01 Refactored class for WebFlux
 *
 */
@Service("userDetailsService")
@Slf4j
public class EventUserDetailsService implements ReactiveUserDetailsService {

    private final UserDao userDao;

    public EventUserDetailsService(final @NotNull UserDao userDao) {
        this.userDao = userDao;
    }


    /**
     * Lookup a {@link AppUser} by the username representing
     * the email address. Then, convert the {@link AppUser}
     * into a {@link EventUserDetails} to conform to the {@link UserDetails} interface.
     */
    @Override
    public Mono<UserDetails> findByUsername(final String username) throws UsernameNotFoundException {
        log.debug("* eventUserDetailsService.findByUsername('{}')", username);

        return userDao.findByEmail(username)
                // Need to figure out how to return a (UserDetails) instead of (EventUserDetails)
//                .map(EventUserDetails::new)
                .map(appUser -> {
                    UserDetails ud = new EventUserDetails(appUser);
                    log.debug("UserDetails: {}", ud);
                    return ud;
                })
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Invalid username/password.")));
    }

} // The End...
