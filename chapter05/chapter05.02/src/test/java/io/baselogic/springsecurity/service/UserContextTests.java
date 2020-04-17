package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * UserContextTests
 *
 * @since chapter1.00
 * @since chapter4.02 Can only setCurrentUser() with a user that exist in the db.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class UserContextTests {

    @Autowired
    private UserContext userContext;

    private AppUser owner = new AppUser();


    @BeforeEach
    public void beforeEachTest() {
        owner.setId(1);
    }


    @Test
    public void initJdbcOperations() {
        assertThat(userContext).isNotNull();
    }


    @Test
    public void setCurrentUser() {
        // Not in the database:
//        userContext.setCurrentUser(TestUtils.TEST_APP_USER_1);
        userContext.setCurrentUser(TestUtils.user1);

        AppUser appUser = userContext.getCurrentUser();

        assertThat(appUser).isNotNull();
        assertThat(appUser.getId()).isEqualTo(0);
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

} // The End...
