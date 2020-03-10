package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

} // The End...
