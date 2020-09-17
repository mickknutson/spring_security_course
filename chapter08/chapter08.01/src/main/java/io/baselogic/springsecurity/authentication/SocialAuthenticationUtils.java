package io.baselogic.springsecurity.authentication;

import io.baselogic.springsecurity.core.authority.EventUserAuthorityUtils;
import io.baselogic.springsecurity.domain.EventUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * Social Authentication
 *
 * <pre>SocialAuthenticationUtils.authenticate(connection)</pre>
 */
public class SocialAuthenticationUtils {

    public static void authenticate(Connection<?> connection) {

        EventUser user = createEventUserFromProvider(connection);

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
        user.setLastName(profile.getLastName());*/


        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        EventUserAuthorityUtils.createAuthorities(user));

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    public static EventUser createEventUserFromProvider(Connection<?> connection){
        // TODO: FIXME: Need to put this into a Utility:
        UserProfile profile = connection.fetchUserProfile();

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

        user.setPassword(randomAlphabetic(32));

        return user;

    }

} // The End...
