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
        AppUser appUser = userDao.findById(1);
        log.info(appUser.toString());

        assertThat(appUser).isNotNull();
        assertThat(appUser.equals(appUser)).isTrue();
        assertThat(appUser.equals(new Object())).isFalse();
        assertThat(appUser.equals(new AppUser())).isFalse();
        assertThat(appUser.hashCode()).isNotEqualTo(0);
    }

    @Test
    public void findByEmail() {
        AppUser appUser = userDao.findByEmail("user1@baselogic.com");
        assertThat(appUser.getEmail()).isEqualTo("user1@baselogic.com");
    }


    @Test
    public void findByEmail_no_results() {
        AppUser appUser = userDao.findByEmail("foo@baselogic.com");
        assertThat(appUser).isNull();
    }



    @Test
    public void findAllByEmail() {
        List<AppUser> appUsers = userDao.findAllByEmail("@baselogic");
        assertThat(appUsers.size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    public void findAllByEmail_no_results() {
        List<AppUser> appUsers = userDao.findAllByEmail("@baselogic.io");
        assertThat(appUsers.size()).isEqualTo(0);
    }


    @Test
    public void createUser() {
        List<AppUser> appUsers = userDao.findAllByEmail("@baselogic.com");
        assertThat(appUsers.size()).isGreaterThanOrEqualTo(3);

        AppUser appUser = TestUtils.createMockUser("test@baselogic.com", "test", "example");
        int userId = userDao.save(appUser);
        assertThat(userId).isGreaterThanOrEqualTo(3);

        appUsers = userDao.findAllByEmail("baselogic.com");
        assertThat(appUsers.size()).isGreaterThanOrEqualTo(4);
    }

    @Test
    public void createUser_with_id() {
        assertThrows(IllegalArgumentException.class, () -> {
            AppUser appUser = TestUtils.createMockUser("test@baselogic.com", "test", "example");
            appUser.setId(12345);
            int userId = userDao.save(appUser);
        });
    }


} // The End...
