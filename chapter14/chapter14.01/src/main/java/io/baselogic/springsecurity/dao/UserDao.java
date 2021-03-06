package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import org.springframework.dao.EmptyResultDataAccessException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * An interface for managing {@link AppUser} instances.
 *
 * @author mickknutson
 *
 */
public interface UserDao {

    /**
     * Gets a {@link AppUser} for a specific {@link AppUser#getId()}.
     *
     * @param id
     *            the {@link AppUser#getId()} of the {@link AppUser} to find.
     * @return a {@link AppUser} for the given id. Cannot be null.
     * @throws EmptyResultDataAccessException
     *             if the {@link AppUser} cannot be found
     */
    Mono<AppUser> findById(@NotNull Integer id);

    /**
     * Finds a given {@link AppUser} by email address.
     *
     * @param email
     *            the email address to use to find a {@link AppUser}. Cannot be null.
     * @return a {@link AppUser} for the given email or null if one could not be found.
     * @throws IllegalArgumentException
     *             if email is null.
     */
    Mono<AppUser> findByEmail(@NotEmpty String email);


    /**
     * Finds any {@link AppUser} that has an email that starts with {@code partialEmail}.
     *
     * @param partialEmail
     *            the email address to use to find {@link AppUser}s. Cannot be null or empty String.
     * @return a List of {@link AppUser}s that have an email that starts with given partialEmail. The returned value
     *         will never be null. If no results are found an empty List will be returned.
     * @throws IllegalArgumentException
     *             if email is null or empty String.
     */
    Flux<AppUser> findAllByEmail(@NotEmpty String partialEmail);

    /**
     * Creates a new {@link AppUser}.
     *
     * @param appUser
     *            the new {@link AppUser} to create. The {@link AppUser} must be null.
     * @return the new {@link AppUser}.
     * @throws IllegalArgumentException
     *             if {@link AppUser} is non-null.
     */
    Mono<AppUser> save(@NotNull AppUser appUser);

} // The End...
