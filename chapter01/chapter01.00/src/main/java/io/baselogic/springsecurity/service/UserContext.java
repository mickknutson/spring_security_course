package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.domain.AppUser;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

/**
 * Manages the current {@link AppUser}. This demonstrates how in larger applications it is good to abstract out
 * accessing the current user to return the application specific user rather than interacting with Spring Security
 * classes directly.
 *
 * @author mickknutson
 *
 */
public interface UserContext {

    /**
     * Gets the currently logged in {@link AppUser} or null if there is no authenticated user.
     *
     * @return the current authenticated {@link AppUser}.
     */
    AppUser getCurrentUser();

    /**
     * Sets the currently logged in {@link AppUser}.
     *
     * @param appUser the logged in {@link AppUser}. Cannot be null.
     * @throws {@link ConstraintViolationException} if the {@link AppUser} is null.
     */
    void setCurrentUser(@NotNull(message="user.notNull.key") AppUser appUser);


} // The End...
