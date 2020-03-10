package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
public class AppUserRepositoryTests {

    @Autowired
    private AppUserRepository repository;

    private AppUser owner = new AppUser();
    private AppUser attendee = new AppUser();


    @BeforeEach
    public void beforeEachTest() {
        owner.setId(1);
        attendee.setId(0);
    }


    @Test
	public void validateUser_User() {
        String username = "user1@example.com";
        AppUser user = repository.findByEmail(username);
        assertThat(user.getEmail()).isEqualTo(username);
        assertThat(user.getRoles().size()).isEqualTo(1);
//        assertThat(user.getFirstName()).as("check %s's username", user.getEmail()).isEqualTo("foo@bar.com");
	}

	@Test
	public void validateUser_Admin() {
	    String username = "admin1@example.com";
        AppUser user = repository.findByEmail(username);
        assertThat(user.getEmail()).isEqualTo(username);
        assertThat(user.getRoles().size()).isEqualTo(2);
	}


    @Test
    @DisplayName("AppUserRepository - Initialize Repository")
    public void initJpaOperations() {
        assertThat(repository).isNotNull();
    }


    @Test
    @DisplayName("AppUserRepository - findById")
    public void findById() {
        AppUser appUser = repository.findById(1).orElseThrow(RuntimeException::new);
        log.info(appUser.toString());

        assertThat(appUser).isNotNull();
        assertThat(appUser.equals(appUser)).isTrue();
        assertThat(appUser.equals(new Object())).isFalse();
        assertThat(appUser.equals(new AppUser())).isFalse();
        assertThat(appUser.hashCode()).isNotEqualTo(0);
    }


    @Test
    @DisplayName("AppUserRepository - findByEmail")
    public void findByEmail() {
        AppUser appUser = repository.findByEmail("user1@example.com");
        assertThat(appUser.getEmail()).isEqualTo("user1@example.com");
    }


    @Test
    @DisplayName("AppUserRepository - findByEmail - email not found")
    public void findByEmail_no_results() {
        AppUser appUser = repository.findByEmail("foo@example.com");
        assertThat(appUser).isNull();
    }


    @Test
    @DisplayName("AppUserRepository - findAllByEmail")
    public void findAllByEmail() {
        List<AppUser> appUsers = repository.findAllByEmailContaining("@example");
        assertThat(appUsers.size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    @DisplayName("AppUserRepository - findAllByEmail - not found")
    public void findAllByEmail_no_results() {
        List<AppUser> appUsers = repository.findAllByEmailContaining("@baselogic.io");
        assertThat(appUsers.size()).isEqualTo(0);
    }


    @Test
    @DisplayName("AppUserRepository - create User")
    public void createUser() {
        List<AppUser> appUsers = repository.findAllByEmailContaining("@example.com");
        assertThat(appUsers.size()).isGreaterThanOrEqualTo(3);

        AppUser appUser = TestUtils.createMockUser("test@example.com", "test", "example");
        int userId = repository.save(appUser).getId();
        assertThat(userId).isGreaterThanOrEqualTo(3);

        appUsers = repository.findAllByEmailContaining("example.com");
        assertThat(appUsers.size()).isGreaterThanOrEqualTo(4);
    }

    @Test
    @DisplayName("AppUserRepository - create User - with ID")
    public void createUser_with_id() {
        List<AppUser> appUsers = repository.findAll();
        assertThat(appUsers.size()).isGreaterThanOrEqualTo(3);

//        assertThrows(IllegalArgumentException.class, () -> {
            AppUser appUser = TestUtils.createMockUser("test@example.com", "test", "example");
            appUser.setId(12345);
            int userId = repository.save(appUser).getId();
//        });

        appUsers = repository.findAll();
        assertThat(appUsers.size()).isGreaterThanOrEqualTo(3);
    }


} // The End...
