package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.domain.User;
import io.baselogic.springsecurity.domain.Event;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

/**
 * A service for managing {@link Event}'s.
 *
 * @author mickknutson
 *
 */
public interface EventService {

    /**
     * Given an id gets an {@link Event}.
     *
     * @param eventId
     *            the {@link Event#getId()}
     * @return the {@link Event}. Cannot be null.
     * @throws RuntimeException
     *             if the {@link Event} cannot be found.
     */
    Event findEventById(Integer eventId);

    /**
     * Creates a {@link Event} and returns the new id for that {@link Event}.
     *
     * @param event
     *            the {@link Event} to create. Note that the {@link Event#getId()} should be null.
     * @return the new id for the {@link Event}
     * @throws RuntimeException
     *             if {@link Event#getId()} is non-null.
     */
    Integer createEvent(Event event);

    /**
     * Finds the {@link Event}'s that are intended for the {@link User}.
     *
     * @param userId
     *            the {@link User#getId()} to obtain {@link Event}'s for.
     * @return a non-null {@link List} of {@link Event}'s intended for the specified {@link User}. If the
     *         {@link User} does not exist an empty List will be returned.
     */
    List<Event> findEventByUser(Integer userId);

    /**
     * Gets all the available {@link Event}'s.
     *
     * @return a non-null {@link List} of {@link Event}'s
     */
    List<Event> findAllEvents();





    /**
     * Gets a {@link User} for a specific {@link User#getId()}.
     *
     * @param id
     *            the {@link User#getId()} of the {@link User} to find.
     * @return a {@link User} for the given id. Cannot be null.
     * @throws EmptyResultDataAccessException
     *             if the {@link User} cannot be found
     */
    User findUserById(Integer id);

    /**
     * Finds a given {@link User} by email address.
     *
     * @param email
     *            the email address to use to find a {@link User}. Cannot be null.
     * @return a {@link User} for the given email or null if one could not be found.
     * @throws IllegalArgumentException
     *             if email is null.
     */
    User findUserByEmail(String email);

    /**
     * Finds any {@link User} that has an email that starts with {@code partialEmail}.
     *
     * @param partialEmail
     *            the email address to use to find {@link User}s. Cannot be null or empty String.
     * @return a List of {@link User}s that have an email that starts with given partialEmail. The returned
     *         value will never be null. If no results are found an empty List will be returned.
     * @throws IllegalArgumentException
     *             if email is null or empty String.
     */
    List<User> findUsersByEmail(String partialEmail);

    /**
     * Creates a new {@link User}.
     *
     * @param user
     *            the new {@link User} to create. The {@link User#getId()} must be null.
     * @return the new {@link User#getId()}.
     * @throws IllegalArgumentException
     *             if {@link User#getId()} is non-null.
     */
    Integer createUser(User user);

} // The End...
