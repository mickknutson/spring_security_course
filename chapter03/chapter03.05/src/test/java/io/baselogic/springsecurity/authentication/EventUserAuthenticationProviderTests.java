package io.baselogic.springsecurity.authentication;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.User;
import io.baselogic.springsecurity.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

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
    private UsernamePasswordAuthenticationToken token;

    @MockBean
    private EventService eventService;


    @Autowired
    private EventUserAuthenticationProvider authenticationProvider;


    //-----------------------------------------------------------------------//

    private User user1 = new User();
    private User testUser1 = new User();


    @BeforeEach
    public void beforeEachTest() {
        user1 = TestUtils.user1;
        testUser1 = TestUtils.testUser1;
    }

    //-------------------------------------------------------------------------


    @Test
    @DisplayName("supports - UsernamePasswordAuthenticationToken.class")
    public void supports__TRUE() {

        boolean result = authenticationProvider.supports(UsernamePasswordAuthenticationToken.class);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("supports - SpringBootTest.class")
    public void supports__FALSE() {

        boolean result = authenticationProvider.supports(SpringBootTest.class);

        assertThat(result).isFalse();
    }

    //-----------------------------------------------------------------------//

    /*@Test
    @DisplayName("loadUserByUsername - user1")
    public void loadUserByUsername_user1() {

        // Expectation
        given(token.getName())
                .willReturn(TestUtils.user1.getEmail());

        given(eventService.findUserByEmail(any(String.class)))
                .willReturn(TestUtils.user1);

        UserDetails result = authenticationProvider.authenticate(TestUtils.user1.getEmail());

        assertThat(result).isNotNull();
    }*/

    /*@Test
    @DisplayName("loadUserByUsername - admin1")
    public void loadUserByUsername_admin1() {

        // Expectation
        given(userDao.findByEmail(any(String.class)))
                .willReturn(TestUtils.admin1);

        UserDetails result = eventUserDetailsService.loadUserByUsername(TestUtils.admin1.getEmail());

        assertThat(result).isNotNull();
    }*/


    /*@Test
    @DisplayName("loadUserByUsername - no User found")
    public void loadUserByUsername__null_credentials() {

        assertThrows(UsernameNotFoundException.class, () -> {
            UserDetails result = eventUserDetailsService.loadUserByUsername("foobar");
        });
    }*/


} // The End...
