package io.baselogic.springsecurity.authentication;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.EventUserDetails;
import io.baselogic.springsecurity.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


/**
 * EventUserAuthenticationProviderTests
 *
 * @since chapter03.05 Created Class
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class EventUserAuthenticationProviderTests {

    // Mockito:
    @MockBean
    private EventService eventService;


    @Autowired
    private EventUserAuthenticationProvider authenticationProvider;


    //-----------------------------------------------------------------------//

    private AppUser appUser1 = new AppUser();


    @BeforeEach
    void beforeEachTest() {
        appUser1 = TestUtils.user1;
        appUser1.setPassword("{noop}user1");
    }

    //-----------------------------------------------------------------------//


    @Test
    @DisplayName("supports - UsernamePasswordAuthenticationToken.class")
    void supports__TRUE() {

        boolean result = authenticationProvider.supports(UsernamePasswordAuthenticationToken.class);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("supports - SpringBootTest.class")
    void supports__FALSE() {

        boolean result = authenticationProvider.supports(SpringBootTest.class);

        assertThat(result).isFalse();
    }

    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("authenticate - null User")
    void authenticate__null_user() {

        // Expectation
        given(eventService.findUserByEmail(any(String.class)))
                .willReturn(null);

        Authentication authentication =
                new  UsernamePasswordAuthenticationToken("user1@baselogic.com",
                        "user1");

        assertThrows(UsernameNotFoundException.class, () -> {
            Authentication result = authenticationProvider.authenticate(authentication);
        });
    }

    @Test
    void authenticate_user(){
        given(eventService.findUserByEmail(any(String.class)))
                .willReturn(appUser1);

        String presentedUsername = appUser1.getName();
        String presentedPassword = "user1";

        Authentication authentication = new UsernamePasswordAuthenticationToken(presentedUsername, presentedPassword);
        Authentication authenticatedUser =  authenticationProvider.authenticate(authentication);

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)authenticatedUser;

        EventUserDetails eventUserDetails = (EventUserDetails) token.getPrincipal();

//        assertTrue(eventUserDetails.getUsername().equals(appUser1.getEmail()));
//        assertTrue(token.getCredentials().toString().equals(presentedPassword));

        Collection<GrantedAuthority> authorities = token.getAuthorities();

        authorities.forEach( auth -> assertTrue(auth.getAuthority().equals("ROLE_USER")));

    }

    @Test
    @DisplayName("authenticate - incorrect_authentication_credentials")
    void authenticate__incorrect_authentication_credentials() {

        // Expectation
        given(eventService.findUserByEmail(any(String.class)))
                .willReturn(appUser1);

        Authentication authentication =
                new  UsernamePasswordAuthenticationToken("test@baselogic.com",
                        "password");

        assertThrows(BadCredentialsException.class, () -> {
            Authentication result = authenticationProvider.authenticate(authentication);
        });
    }


} // The End...
