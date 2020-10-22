package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;


@DataR2dbcTest
@Slf4j
class RoleRepositoryTests {

    @Autowired
    private RoleRepository repository;

	@Test
    @DisplayName("RoleRepository - validateUser_User")
    void validateUser_User() {
        Mono<Role> result = repository.findById(0);

        StepVerifier
                .create(result)
                .assertNext(role -> {
                    assertThat(role.getId()).isZero();
                    assertThat(role.getName()).isEqualTo("ROLE_USER");
                })
                .expectComplete()
                .verify();
	}

	@Test
    @DisplayName("RoleRepository - validateUser_Admin")
	void validateUser_Admin() {
        Mono<Role> result = repository.findById(1);

        StepVerifier
                .create(result)
                .assertNext(role -> {
                    assertThat(role.getId()).isZero();
                    assertThat(role.getName()).isEqualTo("ROLE_ADMIN");
                })
                .expectComplete()
                .verify();
	}

} // The End...
