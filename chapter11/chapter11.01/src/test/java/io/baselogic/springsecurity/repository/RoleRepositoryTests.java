package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Slf4j
class RoleRepositoryTests {

    @Autowired
    private RoleRepository repository;

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
