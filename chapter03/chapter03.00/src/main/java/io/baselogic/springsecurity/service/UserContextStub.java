package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * Returns the same user for every call to {@link #getCurrentUser()}.
 * This is used prior to adding security, so that the rest of the application can be used.
 *
 * @see UserContext {@link SpringSecurityUserContext} for new UserContext moving forward.
 *
 * @since chapter01.00
 * @author mickknutson
 */
@Service
public class UserContextStub implements UserContext {

    private final UserDao userService;

    /**
     * The {@link User#getId()} for the user that is representing the currently logged in user. This can be
     * modified using {@link #setCurrentUser(User)}
     */
    private int currentUserId = 0;

    @Autowired
    public UserContextStub(@NotNull UserDao userService) {
        this.userService = userService;
    }

    @Override
    public User getCurrentUser() {
        return userService.findById(currentUserId);
    }

    @Override
    public final void setCurrentUser(@NotNull(message="user.notNull.key") User user) {

        Integer currentId = user.getId();

        if(currentId == null) {
            throw new IllegalArgumentException("user.getId() cannot be null");
        }
        this.currentUserId = currentId;
    }

} // The End...
