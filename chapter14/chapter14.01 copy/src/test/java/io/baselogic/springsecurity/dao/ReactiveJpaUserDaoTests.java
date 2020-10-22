package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JpaEventDaoTests
 *
 * @since chapter5.01
 */
    
//@Transactional
@SpringBootTest//(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class ReactiveJpaUserDaoTests {

    // Mockito:
//    @MockBean
    private AppUserRepository appUserRepository;


    @Autowired
    private UserDao userDao;



    @BeforeEach
    void beforeEachTest() {
    }

    //-----------------------------------------------------------------------//

    @Test
    void initJdbcOperations() {
        assertThat(userDao).isNotNull();
    }

    @Test
    void findById() {
        Mono<AppUser> result = userDao.findById(1);
        AppUser appUser = result.block();

        assertThat(appUser).isNotNull()
                .isNotEqualTo(new Object())
                .hasFieldOrPropertyWithValue("id", 1);
        assertThat(appUser.hashCode()).isNotZero();
    }

    @Test
    void findByEmail() {
        Mono<AppUser> result = userDao.findByEmail("user1@baselogic.com");
        assertThat(result.block().getEmail()).isEqualTo("user1@baselogic.com");
    }


    @Test
    void findByEmail_no_results() {
        Mono<AppUser> result = userDao.findByEmail("foo@baselogic.com");
        assertThat(result.block()).isNull();
    }


    //-----------------------------------------------------------------------//

    @Test
    void findAllByEmail() {
        Flux<AppUser> result = userDao.findAllByEmail("@baselogic");
        assertThat(result.count().block()).isGreaterThanOrEqualTo(3);
    }

    @Test
    void findAllByEmail_no_results() {
        Flux<AppUser> result = userDao.findAllByEmail("@baselogic.io");
        assertThat(result.count().block()).isZero();
    }

    //-----------------------------------------------------------------------//


    @Test
    void createUser() {
        Flux<AppUser> result = userDao.findAllByEmail("@baselogic.com");
        assertThat(result.count().block()).isGreaterThanOrEqualTo(3);

        AppUser appUser = TestUtils.createMockUser("test@baselogic.com", "test", "example");
        Mono<Integer> userId = userDao.save(appUser);
        assertThat(userId.block()).isGreaterThanOrEqualTo(3);

        result = userDao.findAllByEmail("baselogic.com");
        assertThat(result.count().block()).isGreaterThanOrEqualTo(4);
    }


} // The End...
