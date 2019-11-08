package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * An interface for managing {@link User} instances.
 *
 * @author mickknutson
 *
 */
public interface UserDao {

    /**
     * Gets a {@link User} for a specific {@link User#getId()}.
     *
     * @param id
     *            the {@link User#getId()} of the {@link User} to find.
     * @return a {@link User} for the given id. Cannot be null.
     * @throws EmptyResultDataAccessException
     *             if the {@link User} cannot be found
     */
    User findById(@NotNull Integer id);

    /**
     * Finds a given {@link User} by email address.
     *
     * @param email
     *            the email address to use to find a {@link User}. Cannot be null.
     * @return a {@link User} for the given email or null if one could not be found.
     * @throws IllegalArgumentException
     *             if email is null.
     */
    User findByEmail(@NotEmpty String email);


    /**
     * Finds any {@link User} that has an email that starts with {@code partialEmail}.
     *
     * @param partialEmail
     *            the email address to use to find {@link User}s. Cannot be null or empty String.
     * @return a List of {@link User}s that have an email that starts with given partialEmail. The returned value
     *         will never be null. If no results are found an empty List will be returned.
     * @throws IllegalArgumentException
     *             if email is null or empty String.
     */
    List<User> findAllByEmail(@NotEmpty String partialEmail);

    /**
     * Creates a new {@link User}.
     *
     * @param user
     *            the new {@link User} to create. The {@link User#getId()} must be null.
     * @return the new {@link User#getId()}.
     * @throws IllegalArgumentException
     *             if {@link User#getId()} is non-null.
     */
    Integer save(@NotNull User user);

} // The End...
