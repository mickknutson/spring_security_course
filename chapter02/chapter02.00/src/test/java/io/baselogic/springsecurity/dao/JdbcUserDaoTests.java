package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
@Slf4j
public class JdbcUserDaoTests {

    @Autowired
    private UserDao userDao;

    private AppUser owner = new AppUser();
    private AppUser attendee = new AppUser();


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
        AppUser user = userDao.findById(1);
        log.info(user.toString());

        assertThat(user).isNotNull();
        assertThat(user.equals(user)).isTrue();
        assertThat(user.equals(new Object())).isFalse();
        assertThat(user.equals(new AppUser())).isFalse();
        assertThat(user.hashCode()).isNotEqualTo(0);
    }

    @Test
    public void findByEmail() {
        AppUser user = userDao.findByEmail("user1@baselogic.com");
        assertThat(user.getEmail()).isEqualTo("user1@baselogic.com");
    }


    @Test
    public void findByEmail_no_results() {
        AppUser user = userDao.findByEmail("foo@baselogic.com");
        assertThat(user).isNull();
    }



    @Test
    public void findAllByEmail() {
        List<AppUser> users = userDao.findAllByEmail("@baselogic");
        assertThat(users.size()).isEqualTo(3);
    }

    @Test
    public void findAllByEmail_no_results() {
        List<AppUser> users = userDao.findAllByEmail("@baselogic.io");
        assertThat(users.size()).isEqualTo(0);
    }


    @Test
    public void createUser() {
        List<AppUser> users = userDao.findAllByEmail("@baselogic.com");
        assertThat(users.size()).isEqualTo(3);

        AppUser user = TestUtils.createMockUser("test@baselogic.com", "test", "example");
        int userId = userDao.save(user);
        assertThat(userId).isGreaterThanOrEqualTo(3);

        users = userDao.findAllByEmail("baselogic.com");
        assertThat(users.size()).isEqualTo(4);
    }

    @Test
    public void createUser_with_id() {
        assertThrows(IllegalArgumentException.class, () -> {
            AppUser user = TestUtils.createMockUser("test@baselogic.com", "test", "example");
            user.setId(12345);
            int userId = userDao.save(user);
        });
    }


} // The End...
