package io.baselogic.springsecurity.authentication;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


/**
 * EventUserAuthenticationProviderTests
 *
 * @since chapter03.05
 */
@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class EventUserAuthenticationProviderTests {

    // Mockito:
    @MockBean
    private EventService eventService;


    @Autowired
    private EventUserAuthenticationProvider authenticationProvider;


    //-----------------------------------------------------------------------//

    private AppUser appUser1 = new AppUser();


    @BeforeEach
    public void beforeEachTest() {
        appUser1 = TestUtils.user1;
        appUser1.setPassword("{noop}user1");
    }

    //-----------------------------------------------------------------------//
    // Supports
    //-----------------------------------------------------------------------//


    @Test
    @DisplayName("supports - UsernamePasswordAuthenticationToken.class")
    public void supports__TRUE() {

        boolean result = authenticationProvider.supports(DomainUsernamePasswordAuthenticationToken.class);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("supports - SpringBootTest.class")
    public void supports__FALSE() {

        boolean result = authenticationProvider.supports(SpringBootTest.class);

        assertThat(result).isFalse();
    }

    //-----------------------------------------------------------------------//
    // authenticate
    //-----------------------------------------------------------------------//


    @Test
    @DisplayName("authenticate - valid User")
    public void authenticate__valid_user() {

        Authentication authentication =
                new  DomainUsernamePasswordAuthenticationToken("user1",
                        "user1", "baselogic.com");

        // Expectation
        given(eventService.findUserByEmail(any(String.class)))
                .willReturn(TestUtils.user1);

        Authentication result = authenticationProvider.authenticate(authentication);

        assertThat(result.isAuthenticated()).isTrue();
    }

    @Test
    @DisplayName("authenticate - null User")
    public void authenticate__null_user() {

        // Expectation
        given(eventService.findUserByEmail(any(String.class)))
                .willReturn(null);

        Authentication authentication =
                new  DomainUsernamePasswordAuthenticationToken("user1",
                        "user1", "baselogic.com");

        assertThrows(UsernameNotFoundException.class, () -> {
            Authentication result = authenticationProvider.authenticate(authentication);
        });
    }


    @Test
    @DisplayName("authenticate - incorrect_authentication_credentials")
    public void authenticate__incorrect_authentication_credentials() {

        // Expectation
        given(eventService.findUserByEmail(any(String.class)))
                .willReturn(appUser1);

        Authentication authentication =
                new  DomainUsernamePasswordAuthenticationToken("test",
                        "password", "baselogic.com");

        assertThrows(BadCredentialsException.class, () -> {
            Authentication result = authenticationProvider.authenticate(authentication);
        });
    }


} // The End...
