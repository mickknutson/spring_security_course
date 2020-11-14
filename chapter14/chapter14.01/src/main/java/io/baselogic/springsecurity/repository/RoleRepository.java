package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends ReactiveMongoRepository<Role, Integer> {

} // The End...
