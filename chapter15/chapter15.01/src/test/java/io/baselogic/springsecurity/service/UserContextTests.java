package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * UserContextTests
 *
 * @since chapter1.00
 * @since chapter4.02 Can only setCurrentUser() with a user that exist in the db.
 */
    
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class UserContextTests {

    @Autowired
    private UserContext userContext;

    private AppUser owner = new AppUser();


    @BeforeEach
    void beforeEachTest() {
        owner.setId(1);
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

        AppUser appUser = userContext.getCurrentUser();

        assertThat(appUser).isNotNull();
        assertThat(appUser.getId()).isZero();
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
        SecurityContextHolder.clearContext();
        AppUser result = userContext.getCurrentUser();
        assertThat(result).isNull();
    }

} // The End...
