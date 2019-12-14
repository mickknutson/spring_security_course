package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


/**
 * UserContextTests
 *
 * @since chapter1.00
 */
@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class UserContextTests {

    @Autowired
    private UserContext userContext;

    // Mockito:
    @MockBean
    private SecurityContext securityContext;

    @MockBean
    private Authentication authentication;

    @MockBean
    private EventService eventService;


    //-----------------------------------------------------------------------//

    private User user1 = new User();
    private User testUser1 = new User();


    @BeforeEach
    public void beforeEachTest() {
        user1 = TestUtils.user1;
        testUser1 = TestUtils.testUser1;
    }


    @Test
    public void initJdbcOperations() {
        assertThat(userContext).isNotNull();
    }

    //-----------------------------------------------------------------------//


    @Test
    @DisplayName("getCurrentUser - null Authentication returns null")
    public void getCurrentUser__null_authentication() {

        // Expectation
        // SecurityContext:
        given(this.securityContext.getAuthentication())
                .willReturn(authentication);

        // Authentication:
        given(this.authentication.getPrincipal())
                .willReturn(new User());

        User user = userContext.getCurrentUser();

        assertThat(user).isNull();
//        verify(this.securityContext).getAuthentication();
//        verify(this.authentication).getPrincipal();
    }

//    @Test
    @DisplayName("getCurrentUser - null User email - returns null")
    @WithMockUser("user1@example.com")
    public void getCurrentUser__null_user_email() {

        // Expectation
        // SecurityContext:
        given(this.securityContext.getAuthentication())
                .willReturn(authentication);

        // Authentication:
        given(this.authentication.getPrincipal())
                .willReturn(new User());

        User user = userContext.getCurrentUser();

        assertThat(user).isNull();
    }

    @Test
    @DisplayName("getCurrentUser - throws IllegalStateException")
    @WithMockUser("user1@example.com")
    public void getCurrentUser__throws_IllegalStateException() {

        User user = new User();
        user.setEmail("test@foobar.com");


        // Expectation
        // SecurityContext:
        given(this.securityContext.getAuthentication())
                .willReturn(authentication);

        // Authentication:
        given(this.authentication.getPrincipal())
                .willReturn(new User());

        // Authentication:
        given(this.eventService.findUserByEmail(any(String.class)))
                .willReturn(null);

        assertThrows(IllegalStateException.class, () -> {
            userContext.getCurrentUser();
        });
    }


    //-----------------------------------------------------------------------//

    @Test
    public void setCurrentUser() {
        userContext.setCurrentUser(user1);
    }

    @Test
    public void setCurrentUser__UsernameNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userContext.setCurrentUser(testUser1);
        });
    }

    @Test
    public void setCurrentUser_null_User() {
        assertThrows(IllegalArgumentException.class, () -> {
            userContext.setCurrentUser(null);
        });
    }

    @Test
    public void setCurrentUser_invalid_User() {
        assertThrows(NullPointerException.class, () -> {
            userContext.setCurrentUser(new User());
        });
    }

    //-----------------------------------------------------------------------//

} // The End...
