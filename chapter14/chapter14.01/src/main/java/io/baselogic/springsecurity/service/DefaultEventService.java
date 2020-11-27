package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.EventDao;
import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

/**
 * A default implementation of {@link EventService} that delegates to {@link EventDao} and {@link UserDao}.
 *
 * @author mickknutson
 *
 * @since chapter01.00 Created
 * @since chapter14.01 Refactored class for WebFlux
 */
@Service
@Validated
@Slf4j
public class DefaultEventService implements EventService {

    private final EventDao eventDao;
    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;


    public DefaultEventService(final EventDao eventDao,
                               final UserDao userDao,
                               final PasswordEncoder passwordEncoder) {
        Assert.notNull(eventDao, "eventDao cannot be null");
        Assert.notNull(userDao, "UserDao cannot be null");
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

        this.eventDao = eventDao;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<Event> findEventById(final Integer eventId) {
        return eventDao.findById(eventId);
    }

    @Override
    public Flux<Event> findEventByUser(final Integer userId) {
        return eventDao.findByUser(userId);
    }

    @Override
    public Flux<Event> findAllEvents() {
        return eventDao.findAll();
    }

    @Override
    public Mono<Integer> createEvent(final Event event) {
        return eventDao.save(event);
    }

    @Override
    public Mono<AppUser> findUserById(final Integer id) {
        return userDao.findById(id);
    }

    @Override
    public Mono<AppUser> findUserByEmail(final String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public Flux<AppUser> findUsersByEmail(final String partialEmail) {
        return userDao.findAllByEmail(partialEmail);
    }

    @Override
    public Mono<AppUser> createUser(final AppUser appUser) {
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        return userDao.save(appUser);
    }

} // The End...
