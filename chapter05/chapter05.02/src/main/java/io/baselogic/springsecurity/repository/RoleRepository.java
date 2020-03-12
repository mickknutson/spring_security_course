package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, Integer> {

} // The End...
