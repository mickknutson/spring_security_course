package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * A service for managing {@link Event}'s.
 *
 * @author mickknutson
 *
 * @since chapter01.00
 * @since chapter09.04 added @PreAuthorize("hasRole('ADMIN')") to findAllEvents()
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
     * Finds the {@link Event}'s that are intended for the {@link AppUser}.
     *
     * @param userId
     *            the {@link AppUser#getId()} to obtain {@link Event}'s for.
     * @return a non-null {@link List} of {@link Event}'s intended for the specified {@link AppUser}. If the
     *         {@link AppUser} does not exist an empty List will be returned.
     */
    List<Event> findEventByUser(Integer userId);

    /**
     * Gets all the available {@link Event}'s.
     *
     * @return a non-null {@link List} of {@link Event}'s
     */
    @PreAuthorize("hasRole('ADMIN')")
    List<Event> findAllEvents();





    /**
     * Gets a {@link AppUser} for a specific {@link AppUser#getId()}.
     *
     * @param id
     *            the {@link AppUser#getId()} of the {@link AppUser} to find.
     * @return a {@link AppUser} for the given id. Cannot be null.
     * @throws EmptyResultDataAccessException
     *             if the {@link AppUser} cannot be found
     */
    AppUser findUserById(Integer id);

    /**
     * Finds a given {@link AppUser} by email address.
     *
     * @param email
     *            the email address to use to find a {@link AppUser}. Cannot be null.
     * @return a {@link AppUser} for the given email or null if one could not be found.
     * @throws IllegalArgumentException
     *             if email is null.
     */
    AppUser findUserByEmail(String email);

    /**
     * Finds any {@link AppUser} that has an email that starts with {@code partialEmail}.
     *
     * @param partialEmail
     *            the email address to use to find {@link AppUser}s. Cannot be null or empty String.
     * @return a List of {@link AppUser}s that have an email that starts with given partialEmail. The returned
     *         value will never be null. If no results are found an empty List will be returned.
     * @throws IllegalArgumentException
     *             if email is null or empty String.
     */
    List<AppUser> findUsersByEmail(String partialEmail);

    /**
     * Creates a new {@link AppUser}.
     *
     * @param appUser
     *            the new {@link AppUser} to create. The {@link AppUser#getId()} must be null.
     * @return the new {@link AppUser#getId()}.
     * @throws IllegalArgumentException
     *             if {@link AppUser#getId()} is non-null.
     */
    Integer createUser(AppUser appUser);

} // The End...
