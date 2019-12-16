package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.domain.User;

import javax.validation.ConstraintViolationException;

/**
 * Manages the current {@link User}. This demonstrates how in larger applications it is good to abstract out
 * accessing the current user to return the application specific user rather than interacting with Spring Security
 * classes directly.
 *
 * @since chapter01.00
 * @author mickknutson
 *
 */
public interface UserContext {

    /**
     * Gets the currently logged in {@link User} or null if there is no authenticated user.
     *
     * @return the current authenticated {@link User}.
     */
    User getCurrentUser();

    /**
     * Sets the currently logged in {@link User}.
     *
     * @param user the logged in {@link User}. Cannot be null.
     * @throws {@link ConstraintViolationException} if the {@link User} is null.
     */
    void setCurrentUser(User user);

} // The End...
