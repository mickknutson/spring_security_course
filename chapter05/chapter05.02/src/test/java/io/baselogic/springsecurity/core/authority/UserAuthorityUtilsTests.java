package io.baselogic.springsecurity.core.authority;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class UserAuthorityUtilsTests {

    @BeforeEach
    public void beforeEachTest() {
        UserAuthorityUtilsTests utils = new UserAuthorityUtilsTests();
    }


    @Test
    @DisplayName("UserAuthorityUtilsTests - createAuthorities")
    public void createAuthorities() {
        UserAuthorityUtils.createAuthorities(TestUtils.APP_USER_1);
    }


    @Test
    @DisplayName("UserAuthorityUtilsTests - getUserEmail - AppUser")
    public void getUserEmail_AppUser() {
        String result = UserAuthorityUtils.getUserEmail(TestUtils.APP_USER_1);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("UserAuthorityUtilsTests - getUserEmail - UserDetails_User")
    public void getUserEmail_UserDetails_User() {
        String result = UserAuthorityUtils.getUserEmail(TestUtils.user1UserDetails);

        assertThat(result).isEqualTo(TestUtils.APP_USER_1.getEmail());
    }

    @Test
    @DisplayName("UserAuthorityUtilsTests - getUserEmail - Non-user user")
    public void getUserEmail_non_user_user() {
        String result = UserAuthorityUtils.getUserEmail("Chuck Norris");

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("UserAuthorityUtilsTests - getUserEmail - null user")
    public void getUserEmail_null_user() {
        String result = UserAuthorityUtils.getUserEmail(null);

        assertThat(result).isNull();
    }

} // The End...
