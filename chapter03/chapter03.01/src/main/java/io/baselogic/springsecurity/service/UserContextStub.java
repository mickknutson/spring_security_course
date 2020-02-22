package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.UserDao;
import io.baselogic.springsecurity.domain.AppUser;
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
@Deprecated
//@Service
public class UserContextStub implements UserContext {


    @Autowired
    public UserContextStub() {
    }

    @Override
    public AppUser getCurrentUser() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void setCurrentUser(@NotNull(message="user.notNull.key") AppUser appUser) {

        throw new UnsupportedOperationException();
    }

} // The End...
