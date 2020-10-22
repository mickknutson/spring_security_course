package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataR2dbcTest
@Slf4j
class AppUserRepositoryTests {

    @Autowired
    private AppUserRepository repository;


    @BeforeEach
    void beforeEachTest() {
    }


    /*@Test
    @DisplayName("AppUserRepository - validateUser_User")
	void validateUser_User() {
        String username = "user1@baselogic.com";
        Mono<AppUser> result = repository.findByEmail(username);
        AppUser appUser = result.block();

        assertThat(appUser.getEmail()).isEqualTo(username);
        assertThat(appUser.getRoles().size()).isEqualTo(1);
//        assertThat(user.getFirstName()).as("check %s's username", user.getEmail()).isEqualTo("foo@bar.com");
	}

	@Test
    @DisplayName("AppUserRepository - validateUser_Admin")
	void validateUser_Admin() {
	    String username = "admin1@baselogic.com";
        Mono<AppUser> result = repository.findByEmail(username);
        AppUser appUser = result.block();

        assertThat(appUser.getEmail()).isEqualTo(username);
        assertThat(appUser.getRoles().size()).isEqualTo(2);
	}


    @Test
    @DisplayName("AppUserRepository - Initialize Repository")
    void initJpaOperations() {
        assertThat(repository).isNotNull();
    }


    @Test
    @DisplayName("AppUserRepository - findById")
    void test_AppUserRepository_findById() {
        Mono<AppUser> result = repository.findById(1);
        AppUser appUser = result.block();

        assertThat(appUser).isNotNull()
                .isNotEqualTo(new Object())
                .hasFieldOrPropertyWithValue("id", 1);
        assertThat(appUser.hashCode()).isNotZero();
    }


    @Test
    @DisplayName("AppUserRepository - findByEmail")
    void findByEmail() {
        Mono<AppUser> result = repository.findByEmail("user1@baselogic.com");
        AppUser appUser = result.block();

        assertThat(appUser.getEmail()).isEqualTo("user1@baselogic.com");
    }


    @Test
    @DisplayName("AppUserRepository - findByEmail - email not found")
    void findByEmail_no_results() {
        Mono<AppUser> result = repository.findByEmail("foo@baselogic.com");
        AppUser appUser = result.block();

        assertThat(appUser).isNull();
    }


    @Test
    @DisplayName("AppUserRepository - findAllByEmail")
    void findAllByEmail() {
        Flux<AppUser> result = repository.findAllByEmailContaining("@baselogic");

        StepVerifier
                .create(result)
                .expectNext(TestUtils.user1)
                .expectNextMatches(user1 -> (user1.getEmail()).equals(TestUtils.user1))
//                .expectNext("CLOE", "CATE")

                .expectComplete()
                .verify();

//        assertThat(result.count().block()).isGreaterThanOrEqualTo(3);
    }

    @Test
    @DisplayName("AppUserRepository - findAllByEmail - not found")
    void findAllByEmail_no_results() {
        Flux<AppUser> result = repository.findAllByEmailContaining("@baselogic.io");

        assertThat(result.count().block()).isZero();
    }


    @Test
    @DisplayName("AppUserRepository - create User")
    void createUser() {
        Flux<AppUser> result = repository.findAllByEmailContaining("@baselogic.com");

        assertThat(result.count().block()).isGreaterThanOrEqualTo(3);

        AppUser appUser = TestUtils.createMockUser("test@baselogic.com", "test", "example");
        Mono<Integer> userId = repository.save(appUser).map( u -> u.getId());

//        assertThat(userId).isGreaterThanOrEqualTo(3);

        result = repository.findAllByEmailContaining("baselogic.com");
        assertThat(result.count().block()).isGreaterThanOrEqualTo(4);
    }

    @Test
    @DisplayName("AppUserRepository - create User - with ID")
    void createUser_with_id() {
        Flux<AppUser> result = repository.findAll();

        assertThat(result.count().block()).isGreaterThanOrEqualTo(3);

        AppUser appUser = TestUtils.createMockUser("test@baselogic.com", "test", "example");
        appUser.setId(12345);

        Mono<Integer> userId = repository.save(appUser).map( u -> u.getId());

        result = repository.findAll();
        assertThat(result.count().block()).isGreaterThanOrEqualTo(3);
    }*/


} // The End...
