package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * UserContextTests
 *
 * @since chapter01.00
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
        userContext.setCurrentUser(owner);

        AppUser appUser = userContext.getCurrentUser();

        assertThat(appUser).isNotNull();
        assertThat(appUser.getId()).isEqualTo(1);
    }

    @Test
    void test_setCurrentUser_null_User() {
        assertThrows(ConstraintViolationException.class, () -> {
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
//        assertThat(result).isNull();
        assertThat(result).isNotNull();
    }

} // The End...
