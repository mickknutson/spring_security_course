package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

// Assert-J
// --> assertThat(result.size()).isGreaterThan(0);
// http://joel-costigliola.github.io/assertj/index.html
import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintViolationException;
import static org.assertj.core.api.Assertions.*;


@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class UserContextTests {

    @Autowired
    private UserContext userContext;

    private User owner = new User();


    @Before
    public void beforeEachTest() {
        owner.setId(1);
    }


    @Test
    public void initJdbcOperations() {
        assertThat(userContext).isNotNull();
    }


    @Test
    public void setCurrentUser() {
        userContext.setCurrentUser(owner);

        User user = userContext.getCurrentUser();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
    }

    @Test(expected = NullPointerException.class)
    public void setCurrentUser_null_User() {
        userContext.setCurrentUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCurrentUser_invalid_User() {
        userContext.setCurrentUser(new User());
    }

} // The End...
