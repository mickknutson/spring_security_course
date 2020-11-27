package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RoleRepository extends ReactiveMongoRepository<Role, Integer> {

} // The End...
