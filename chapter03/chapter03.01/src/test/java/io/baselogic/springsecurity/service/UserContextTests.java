package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


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
    private SecurityContext context;

    @MockBean
    private Authentication authentication;

    @MockBean
    private EventService eventService;


    //-----------------------------------------------------------------------//

    private User user = new User();


    @BeforeEach
    public void beforeEachTest() {
        user = TestUtils.testUser1;
    }


    @Test
    public void initJdbcOperations() {
        assertThat(userContext).isNotNull();
    }

    //-----------------------------------------------------------------------//

//    @Test
    @DisplayName("getCurrentUser - null Authentication")
    public void getCurrentUser__null_authentication() {

        // Expectation
        // SecurityContext:
        given(context.getAuthentication())
                .willReturn((Authentication) new Object());

        // Authentication:
        given(authentication.getPrincipal())
                .willReturn(new User());

        User user = userContext.getCurrentUser();

        assertThat(user).isNull();
    }

//    @Test
    @DisplayName("getCurrentUser - null User email")
    public void getCurrentUser__null_user_email() {

        // Expectation
        // SecurityContext:
        Mockito.when(context.getAuthentication())
                .thenReturn(((Authentication) new Object()));

        // Authentication:
        given(authentication.getPrincipal())
                .willReturn(new User());

        User user = userContext.getCurrentUser();

        assertThat(user).isNull();
    }

//    @Test
    @DisplayName("getCurrentUser - null User result")
    public void getCurrentUser__null_user_result() {

        User user = new User();
        user.setEmail("test@foobar.com");

        // Expectation
        // SecurityContext:
        when(context.getAuthentication())
                .thenReturn((Authentication) new Object());

        // Authentication:
        when(authentication.getPrincipal())
                .thenReturn(user);

        // Authentication:
        when(eventService.findUserByEmail(any(String.class)))
                .thenReturn(null);

        assertThrows(IllegalStateException.class, () -> {
            userContext.getCurrentUser();
        });
    }


    //-----------------------------------------------------------------------//

    @Test
    public void setCurrentUser() {
        assertThrows(UnsupportedOperationException.class, () -> {
            userContext.setCurrentUser(user);
        });
    }

    @Test
    public void setCurrentUser_null_User() {
        assertThrows(UnsupportedOperationException.class, () -> {
            userContext.setCurrentUser(null);
        });
    }

    @Test
    public void setCurrentUser_invalid_User() {
        assertThrows(UnsupportedOperationException.class, () -> {
            userContext.setCurrentUser(new User());
        });
    }

    //-----------------------------------------------------------------------//

} // The End...
