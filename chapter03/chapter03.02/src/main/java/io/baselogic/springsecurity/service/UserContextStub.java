package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

/**
 * Returns the same user for every call to {@link #getCurrentUser()}.
 * This is used prior to adding security, so that the rest of the application can be used.
 *
 * @see UserContext {@link SpringSecurityUserContext} for new UserContext moving forward.
 *
 * @since chapter01.00
 * @author mickknutson
 *
 * @deprecated To be removed after chapter03.01
 */
//@Service
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
        throw new UnsupportedOperationException();
    }

    @Override
    public final void setCurrentUser(@NotNull(message="user.notNull.key") User user) {

        throw new UnsupportedOperationException();
    }

} // The End...