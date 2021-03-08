package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.configuration.MongoDataInitializer;
import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RoleRepositoryTests
 *
 * @author mickknutson
 *
 * @since chapter05.02 Updated for Mongo
 */
@DataMongoTest
@Import({MongoDataInitializer.class})
@Slf4j
class RoleRepositoryTests {

    @Autowired
    private RoleRepository repository;

    @Test
    @DisplayName("Initialize Repository")
    @Order(1)
    void initRepositoryOperations() {
        assertThat(repository).isNotNull();
    }

    @Test
    @DisplayName("test_findById_user1")
    @Order(2)
    void test_findById_USER() {
        Role result = repository.findById(0).orElseThrow(RuntimeException::new);
        assertThat(result.getId()).isZero();
        assertThat(result.isNew()).isTrue();
        assertThat(result.getName()).isEqualTo("ROLE_USER");

	}

	@Test
    @DisplayName("findAll")
    @Order(3)
	void test_findAll() {

        List<Role> results = repository.findAll();

        assertThat(results).containsExactlyInAnyOrder(
                Role.builder().id(0).name("ROLE_USER").persisted(false).build(),
                Role.builder().id(1).name("ROLE_ADMIN").persisted(false).build()
        );

	}

    @Test
    @DisplayName("test_saveRole_with_id")
    @Order(4)
    void test_saveRole_with_id() {

        Role role = Role.builder()
                .id(42)
                .name("ROLE_TEST")
                .persisted(true)
                .build();

        Long pre = repository.count();
        assertThat(pre).isEqualTo(2);

        Role r = repository.save(role);
        assertThat(r).isNotNull()
                .isNotEqualTo(new Object())
                .hasFieldOrPropertyWithValue("id", 42)
                .hasFieldOrPropertyWithValue("name", "ROLE_TEST");
        assertThat(r.isNew()).isFalse();

        Long post = repository.count();
        assertThat(post).isEqualTo(3);
    }


} // The End...
