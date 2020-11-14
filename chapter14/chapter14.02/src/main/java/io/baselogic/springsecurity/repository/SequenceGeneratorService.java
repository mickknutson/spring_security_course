package io.baselogic.springsecurity.repository;

import io.baselogic.springsecurity.domain.DatabaseSequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

//@Service
@Slf4j
public class SequenceGeneratorService {

    private MongoOperations mongoOperations;

    private MongoTemplate mongoTemplate;

    @Autowired
    public SequenceGeneratorService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Integer generateSequence(String sequenceName) {
        log.info("generateSequence: {}", sequenceName);

        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(sequenceName)),
                new Update().inc("sequence",1),
                options().returnNew(true).upsert(true),
                DatabaseSequence.class);

        return !Objects.isNull(counter) ? counter.getSequence() : 1;

    }

} // The End...
