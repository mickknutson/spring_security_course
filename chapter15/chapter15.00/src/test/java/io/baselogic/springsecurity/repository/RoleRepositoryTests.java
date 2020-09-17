package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository repository;

	@Test
    @DisplayName("RoleRepository - validateUser_User")
    public void validateUser_User() {
        Role user = repository.findById(0).orElseThrow(RuntimeException::new);
        assertThat(user.getId()).isEqualTo(0);
        assertThat(user.getName()).isEqualTo("ROLE_USER");
	}

	@Test
    @DisplayName("RoleRepository - validateUser_Admin")
	public void validateUser_Admin() {
        Role user = repository.findById(1).orElseThrow(RuntimeException::new);
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getName()).isEqualTo("ROLE_ADMIN");
	}

} // The End...
