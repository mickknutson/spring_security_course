package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * UserContextTests
 *
 * @since chapter01.00
 * @since chapter04.02 Can only setCurrentUser() with a user that exist in the db.
 * @since chapter14.01 Updated for Reactive WebFlux
 */
@SpringBootTest
@Slf4j
class UserContextTests {

    @Autowired
    private UserContext userContext;

    @BeforeEach
    void beforeEachTest() {
    }


    @Test
    void initJdbcOperations() {
        assertThat(userContext).isNotNull();
    }


    @Test
    void test_setCurrentUser() {
        // Not in the database:
//        userContext.setCurrentUser(TestUtils.TEST_APP_USER_1);
        userContext.setCurrentUser(TestUtils.user1);

//        AppUser appUser = userContext.getCurrentUser();
//
//        assertThat(appUser).isNotNull();
//        assertThat(appUser.getId()).isZero();
    }

    @Test
    void test_setCurrentUser_null_User() {
        assertThrows(NullPointerException.class, () -> {
            userContext.setCurrentUser(null);
        });

    }

    @Test
    void test_setCurrentUser_invalid_User() {
        AppUser user = new AppUser();

        assertThrows(IllegalArgumentException.class, () -> {
            userContext.setCurrentUser(user);
        });
    }

    @Test
    @DisplayName("getCurrentUser with a null authentication from SecurityContext")
    void test_getCurrentUser_null_authentication() {
//        ReactiveSecurityContextHolder.clearContext();
//        AppUser result = userContext.getCurrentUser();
//        assertThat(result).isNull();
    }

} // The End...
