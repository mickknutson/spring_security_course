package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Slf4j
class RoleRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository repository;


    @BeforeEach
    void beforeEachTest() {
        Role test = new Role();
        test.setId(42);
        test.setName("TEST");

//        entityManager.persist(test);
    }

	@Test
    @DisplayName("RoleRepository - validateUser_User")
    void validateUser_User() {
        Role user = repository.findById(0).orElseThrow(RuntimeException::new);
        assertThat(user.getId()).isZero();
        assertThat(user.getName()).isEqualTo("ROLE_USER");
	}

	@Test
    @DisplayName("RoleRepository - validateUser_Admin")
	void validateUser_Admin() {
        Role user = repository.findById(1).orElseThrow(RuntimeException::new);
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getName()).isEqualTo("ROLE_ADMIN");
	}

} // The End...
