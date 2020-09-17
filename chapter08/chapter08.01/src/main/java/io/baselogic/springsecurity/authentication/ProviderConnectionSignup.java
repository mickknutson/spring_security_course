package io.baselogic.springsecurity.authentication;

import io.baselogic.springsecurity.dataaccess.EventUserDao;
import io.baselogic.springsecurity.domain.EventUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

/**
 * This ConnectionSignUp can be used to put the Social
 * User into your local database.
 */
@Service
public class ProviderConnectionSignup implements ConnectionSignUp {

    @Autowired
    private EventUserDao eventUserDao;

    @Override
    public String execute(Connection<?> connection) {

        EventUser user = SocialAuthenticationUtils.createEventUserFromProvider(connection);

        // TODO: FIXME: Need to put this into a Utility:
        /*UserProfile profile = connection.fetchUserProfile();

        EventUser user = new EventUser();

        if(profile.getEmail() != null){
            user.setEmail(profile.getEmail());
        }
        else if(profile.getUsername() != null){
            user.setEmail(profile.getUsername());
        }
        else {
            user.setEmail(connection.getDisplayName());
        }

        user.setFirstName(profile.getFirstName());
        user.setLastName(profile.getLastName());

        user.setPassword(randomAlphabetic(32));*/

        eventUserDao.createUser(user);

        return user.getEmail();
    }

} // The End...
