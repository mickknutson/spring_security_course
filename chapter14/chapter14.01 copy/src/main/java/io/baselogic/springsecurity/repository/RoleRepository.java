package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.Role;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RoleRepository extends ReactiveCrudRepository<Role, Integer> {

} // The End...
