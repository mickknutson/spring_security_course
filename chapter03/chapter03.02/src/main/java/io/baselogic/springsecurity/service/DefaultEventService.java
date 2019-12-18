package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.EventDao;
import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A default implementation of {@link EventService} that delegates to {@link EventDao} and {@link UserDao}.
 *
 * @author mickknutson

 * @since chapter01.00
 * @since chapter03.02 adding {@link UserDetailsManager} userDetailsManager
 *
 */
@Service
public class DefaultEventService implements EventService {

    private final EventDao eventDao;
    private final UserDao userDao;
    private final UserDetailsManager userDetailsManager;

    @Autowired
    public DefaultEventService(final @NotNull EventDao eventDao,
                               final @NotNull UserDao userDao,
                               @Qualifier("userDetailsService") final UserDetailsManager userDetailsManager) {
        this.eventDao = eventDao;
        this.userDao = userDao;
        this.userDetailsManager = userDetailsManager;
    }

    public Event findEventById(Integer eventId) {
        return eventDao.findById(eventId);
    }

    public List<Event> findEventByUser(Integer userId) {
        return eventDao.findByUser(userId);
    }

    public List<Event> findAllEvents() {
        return eventDao.findAll();
    }

    public Integer createEvent(Event event) {
        return eventDao.save(event);
    }



    public AppUser findUserById(Integer id) {
        return userDao.findById(id);
    }

    public AppUser findUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public List<AppUser> findUsersByEmail(String partialEmail) {
        return userDao.findAllByEmail(partialEmail);
    }

    public Integer createUser(final AppUser appUser) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                appUser.getEmail(),
                appUser.getPassword(),
                authorities);

        userDetailsManager.createUser(userDetails);

        return userDao.save(appUser);
    }

} // The End...