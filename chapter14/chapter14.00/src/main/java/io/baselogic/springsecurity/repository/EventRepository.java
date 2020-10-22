package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, Integer> {

    List<Event> findByOwner(AppUser appUser);





    @Query("{'owner.id' : ?0}")
    List<Event> findByUser(Integer id);


} // The End...
