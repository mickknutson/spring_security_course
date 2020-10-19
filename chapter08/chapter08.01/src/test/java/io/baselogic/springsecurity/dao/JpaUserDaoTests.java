package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.repository.AppUserRepository;
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

/**
 * JpaEventDaoTests
 *
 * @since chapter5.01
 */

@Transactional
@SpringBootTest//(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class JpaUserDaoTests {

    // Mockito:
//    @MockBean
    private AppUserRepository appUserRepository;


    @Autowired
    private UserDao userDao;

    private AppUser owner = new AppUser();
    private AppUser attendee = new AppUser();


    @BeforeEach
    void beforeEachTest() {
        owner.setId(1);
        attendee.setId(0);
    }

    //-----------------------------------------------------------------------//

    @Test
    void initJdbcOperations() {
        assertThat(userDao).isNotNull();
    }

    @Test
    void findById() {
        AppUser appUser = userDao.findById(1);

        assertThat(appUser).isNotNull()
                .isNotEqualTo(new Object())
                .hasFieldOrPropertyWithValue("id", 1);
        assertThat(appUser.hashCode()).isNotZero();

    }

    @Test
    void findByEmail() {
        AppUser appUser = userDao.findByEmail("user1@baselogic.com");
        assertThat(appUser.getEmail()).isEqualTo("user1@baselogic.com");
    }


    @Test
    void findByEmail_no_results() {
        AppUser appUser = userDao.findByEmail("foo@baselogic.com");
        assertThat(appUser).isNull();
    }


    /*@Test
    void findByEmail_EmptyResultDataAccessException() {

        // Expectation
        given(appUserRepository.findByEmail(any(String.class)))
                .willThrow(EmptyResultDataAccessException.class);

        assertThrows(EmptyResultDataAccessException.class, () -> {
            userDao.save(owner);
        });

    }*/

    //-----------------------------------------------------------------------//

    @Test
    void findAllByEmail() {
        List<AppUser> appUsers = userDao.findAllByEmail("@baselogic");
        assertThat(appUsers.size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    void findAllByEmail_no_results() {
        List<AppUser> appUsers = userDao.findAllByEmail("@baselogic.io");
        assertThat(appUsers.size()).isZero();
    }

    //-----------------------------------------------------------------------//


    @Test
    void createUser() {
        List<AppUser> appUsers = userDao.findAllByEmail("@baselogic.com");
        assertThat(appUsers.size()).isGreaterThanOrEqualTo(3);

        AppUser appUser = TestUtils.createMockUser("test@baselogic.com", "test", "example");
        int userId = userDao.save(appUser);
        assertThat(userId).isGreaterThanOrEqualTo(3);

        appUsers = userDao.findAllByEmail("baselogic.com");
        assertThat(appUsers.size()).isGreaterThanOrEqualTo(4);
    }


} // The End...
