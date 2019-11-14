package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * An interface for managing {@link Event}'s.
 *
 * @author mickknutson
 *
 */
public interface EventDao {

    /**
     * Given an id gets an {@link Event}.
     *
     * @param eventId
     *            the {@link Event#getId()}
     * @return the {@link Event}. Cannot be null.
     * @throws RuntimeException
     *             if the {@link Event} cannot be found.
     */
    Event findById(@NotNull Integer eventId);

    /**
     * Finds the {@link Event}'s that are intended for the {@link User}.
     *
     * @param userId
     *            the {@link User#getId()} to obtain {@link Event}'s for.
     * @return a non-null {@link List} of {@link Event}'s intended for the specified {@link User}. If the
     *         {@link User} does not exist an empty List will be returned.
     */
    List<Event> findByUser(@NotNull Integer userId);

    /**
     * Gets all the available {@link Event}'s.
     *
     * @return a non-null {@link List} of {@link Event}'s
     */
    List<Event> findAll();

    /**
     * Creates a {@link Event} and returns the new id for that {@link Event}.
     *
     * @param event
     *            the {@link Event} to create. Note that the {@link Event#getId()} should be null.
     * @return the new id for the {@link Event}
     * @throws RuntimeException
     *             if {@link Event#getId()} is non-null.
     */
    Integer save(@NotNull @Valid Event event);


} // The End...
