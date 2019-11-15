package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Junit 5: -----------------------------------------------------------------//

// Assert-J
// --> assertThat(result.size()).isGreaterThan(0);
// http://joel-costigliola.github.io/assertj/index.html


@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JdbcUserDaoTests {

    private static final Logger log = LoggerFactory.getLogger(JdbcUserDaoTests.class);

    @Autowired
    private UserDao userDao;

    private User owner = new User();
    private User attendee = new User();


    @BeforeEach
    public void beforeEachTest() {
        owner.setId(1);
        attendee.setId(0);
    }


    @Test
    public void initJdbcOperations() {
        assertThat(userDao).isNotNull();
    }

    @Test
    public void findById() {
        User user = userDao.findById(1);
        log.info(user.toString());

        assertThat(user).isNotNull();
        assertThat(user.equals(user)).isTrue();
        assertThat(user.equals(new Object())).isFalse();
        assertThat(user.equals(new User())).isFalse();
        assertThat(user.hashCode()).isNotEqualTo(0);
    }

    @Test
    public void findByEmail() {
        User user = userDao.findByEmail("user1@example.com");
        assertThat(user.getEmail()).isEqualTo("user1@example.com");
    }


    @Test
    public void findByEmail_no_results() {
        User user = userDao.findByEmail("foo@example.com");
        assertThat(user).isNull();
    }



    @Test
    public void findAllByEmail() {
        List<User> users = userDao.findAllByEmail("@example");
        assertThat(users.size()).isEqualTo(3);
    }

    @Test
    public void findAllByEmail_no_results() {
        List<User> users = userDao.findAllByEmail("@baselogic.io");
        assertThat(users.size()).isEqualTo(0);
    }


    @Test
    public void createUser() {
        List<User> users = userDao.findAllByEmail("@example.com");
        assertThat(users.size()).isEqualTo(3);

        User user = TestUtils.createMockUser("test@example.com", "test", "example");
        int userId = userDao.save(user);
        assertThat(userId).isGreaterThanOrEqualTo(3);

        users = userDao.findAllByEmail("example.com");
        assertThat(users.size()).isEqualTo(4);
    }

    @Test
    public void createUser_with_id() {
        assertThrows(IllegalArgumentException.class, () -> {
            User user = TestUtils.createMockUser("test@example.com", "test", "example");
            user.setId(12345);
            int userId = userDao.save(user);
        });
    }


} // The End...
