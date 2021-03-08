package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.configuration.MongoDataInitializer;
import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author mickknutson
 *
 * @since chapter14.01 Updated for Reactive WebFlux
 */
@DataMongoTest
@Import({MongoDataInitializer.class})
@Slf4j
class AppUserRepositoryTests {

    @Autowired
    private AppUserRepository repository;

    @BeforeEach
    void beforeEachTest() {}

    @Test
    @DisplayName("AppUserRepository - Initialize Repository")
    void initJpaOperations() {
        assertThat(repository).isNotNull();
    }


    @Test
    @DisplayName("AppUserRepository - validateUser_User")
    void test_validateUser_User() {
        String username = "user1@baselogic.com";
        AppUser user = repository.findByEmail(username);
        assertThat(user.getEmail()).isEqualTo(username);
        assertThat(user.getRoles().size()).isOne();
    }

    @Test
    @DisplayName("AppUserRepository - validateUser_Admin")
    void test_validateUser_Admin() {
        String username = "admin1@baselogic.com";
        AppUser user = repository.findByEmail(username);
        assertThat(user.getEmail()).isEqualTo(username);
        assertThat(user.getRoles().size()).isEqualTo(2);
    }



    @Test
    @DisplayName("AppUserRepository - findById")
    void test_AppUserRepository_findById() {
        AppUser appUser = repository.findById(1).orElseThrow(RuntimeException::new);

        assertThat(appUser).isNotNull()
                .isNotEqualTo(new Object())
                .hasFieldOrPropertyWithValue("id", 1);
        assertThat(appUser.hashCode()).isNotZero();
        assertThat(appUser.isNew()).isTrue();
    }


    @Test
    @DisplayName("AppUserRepository - findByEmail")
    void test_findByEmail() {
        AppUser appUser = repository.findByEmail("user1@baselogic.com");
        assertThat(appUser.getEmail()).isEqualTo("user1@baselogic.com");
    }


    @Test
    @DisplayName("AppUserRepository - findByEmail - email not found")
    void test_findByEmail_no_results() {
        AppUser appUser = repository.findByEmail("foo@baselogic.com");
        assertThat(appUser).isNull();
    }


    @Test
    @DisplayName("AppUserRepository - findAllByEmail")
    void test_findAllByEmail() {
        List<AppUser> appUsers = repository.findAllByEmailContaining("@baselogic");
        assertThat(appUsers.size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    @DisplayName("AppUserRepository - findAllByEmail - not found")
    void test_findAllByEmail_no_results() {
        List<AppUser> appUsers = repository.findAllByEmailContaining("@baselogic.io");
        assertThat(appUsers.size()).isZero();
    }


    @Test
    @DisplayName("AppUserRepository - create User")
    void test_createUser() {
        List<AppUser> appUsers = repository.findAllByEmailContaining("@baselogic.com");
        assertThat(appUsers.size()).isPositive();

        AppUser appUser = TestUtils.createMockUser("test@baselogic.com", "test", "example");
        appUser.setId(42);

        int userId = repository.save(appUser).getId();
        assertThat(userId).isEqualTo(42);

        List<AppUser> newAppUsers = repository.findAllByEmailContaining("baselogic.com");
        assertThat(newAppUsers.size()).isGreaterThan(appUsers.size());
    }

    @Test
    @DisplayName("AppUserRepository - create User - with ID")
    void test_createUser_with_id() {
        List<AppUser> appUsers = repository.findAll();
        assertThat(appUsers.size()).isPositive();

        AppUser appUser = TestUtils.createMockUser("test@baselogic.com", "test", "example");
        appUser.setId(12345);
        appUser.setPersisted(true);
        int userId = repository.save(appUser).getId();

        List<AppUser> newAppUsers = repository.findAll();
        assertThat(newAppUsers.size()).isGreaterThan(appUsers.size());
    }


} // The End...
