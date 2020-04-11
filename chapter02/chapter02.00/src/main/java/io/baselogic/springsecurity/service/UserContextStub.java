package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Returns the same user for every call to {@link #getCurrentUser()}.
 * This is used prior to adding security, so that the rest of the application can be used.
 *
 * @since chapter01.00
 * @author mickknutson
 */
@Service
@Validated
public class UserContextStub implements UserContext {

    private final UserDao userService;

    /**
     * The {@link AppUser#getId()} for the user that is representing the currently logged in user. This can be
     * modified using {@link #setCurrentUser(AppUser)}
     */
    private int currentUserId = 0;

    @Autowired
    public UserContextStub(@NotNull UserDao userService) {
        this.userService = userService;
    }

    @Override
    public AppUser getCurrentUser() {
        return userService.findById(currentUserId);
    }

    @Override
    public final void setCurrentUser(@NotNull(message="user.notNull.key") AppUser appUser) {

        Integer currentId = appUser.getId();

        if(currentId == null) {
            throw new IllegalArgumentException("user.getId() cannot be null");
        }
        this.currentUserId = currentId;
    }

} // The End...
