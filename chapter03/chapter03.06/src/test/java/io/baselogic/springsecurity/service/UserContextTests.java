package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
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
 * @since chapter03.01 using SpringSecurityUserContext
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

    private AppUser appUser1 = new AppUser();
    private AppUser testAppUser1 = new AppUser();


    @BeforeEach
    public void beforeEachTest() {
        appUser1 = TestUtils.user1;
        testAppUser1 = TestUtils.TEST_APP_USER_1;
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
                .willReturn(new AppUser());

        AppUser appUser = userContext.getCurrentUser();

        assertThat(appUser).isNull();
//        verify(this.securityContext).getAuthentication();
//        verify(this.authentication).getPrincipal();
    }

    //    @Test
    @DisplayName("getCurrentUser - null User email - returns null")
    @WithMockUser("user1@baselogic.com")
    public void getCurrentUser__null_user_email() {

        // Expectation
        // SecurityContext:
        given(this.securityContext.getAuthentication())
                .willReturn(authentication);

        // Authentication:
        given(this.authentication.getPrincipal())
                .willReturn(new AppUser());

        AppUser appUser = userContext.getCurrentUser();

        assertThat(appUser).isNull();
    }

    @Test
    @DisplayName("getCurrentUser - throws IllegalStateException")
    @WithMockUser("test@baselogic.com")
    public void getCurrentUser__throws_IllegalStateException() {

        AppUser appUser = new AppUser();
        appUser.setEmail("test@baselogic.com");


        // Expectation
        // SecurityContext:
        given(this.securityContext.getAuthentication())
                .willReturn(authentication);

        // Authentication:
        given(this.authentication.getPrincipal())
                .willReturn(TestUtils.TEST_APP_USER_1);

        // Authentication:
        given(this.eventService.findUserByEmail(any(String.class)))
                .willReturn(null);

//        org.springframework.security.core.userdetails.User;

//        assertThrows(IllegalStateException.class, () -> {
//            userContext.getCurrentUser();
//        });
        assertThat("result").isNotEqualTo("completed");
    }


    //-----------------------------------------------------------------------//

    @Test
    public void setCurrentUser() {
        userContext.setCurrentUser(appUser1);
        AppUser result = userContext.getCurrentUser();
        assertThat(result.getEmail()).isEqualTo(appUser1.getEmail());
    }

    @Test
    public void setCurrentUser__UsernameNotFoundException() {
        userContext.setCurrentUser(testAppUser1);

        AppUser result = userContext.getCurrentUser();
        assertThat(result.getEmail()).isEqualTo(testAppUser1.getEmail());
    }

    @Test
    public void setCurrentUser_null_User() {
        assertThrows(NullPointerException.class, () -> {
            userContext.setCurrentUser(null);
        });
    }

    @Test
    public void setCurrentUser_invalid_User() {
        assertThrows(IllegalArgumentException.class, () -> {
            userContext.setCurrentUser(new AppUser());
        });
    }

    //-----------------------------------------------------------------------//

} // The End...
