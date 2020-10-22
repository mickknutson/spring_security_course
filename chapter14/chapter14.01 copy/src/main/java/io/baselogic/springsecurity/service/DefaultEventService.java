package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.EventDao;
import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A default implementation of {@link EventService} that delegates to {@link EventDao} and {@link UserDao}.
 *
 * @author mickknutson
 *
 * @since chapter14.01 Refactored class for WebFlux
 *
 */
@Service
@Validated
public class DefaultEventService implements EventService {

    private final EventDao eventDao;
    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public DefaultEventService(final @NotNull EventDao eventDao,
                               final @NotNull UserDao userDao,
                               final PasswordEncoder passwordEncoder) {
        this.eventDao = eventDao;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<Event> findEventById(Integer eventId) {
        return eventDao.findById(eventId);
    }

    @Override
    public Flux<Event> findEventByUser(Integer userId) {
        return eventDao.findByUser(userId);
    }

    @Override
    public Flux<Event> findAllEvents() {
        return eventDao.findAll();
    }

    @Override
    public Mono<Integer> createEvent(Event event) {
        return eventDao.save(event);
    }

    @Override
    public Mono<AppUser> findUserById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public Mono<AppUser> findUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public Flux<AppUser> findUsersByEmail(String partialEmail) {
        return userDao.findAllByEmail(partialEmail);
    }

    @Override
    public Mono<Integer> createUser(final AppUser appUser) {
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        return userDao.save(appUser);
    }

} // The End...