package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AppUserNumberGenerator
 *
 * @author mickknutson
 *
 * @since chapter05.02 Created
 */
@Repository
@DependsOn("mongoDataInitializer")
@Slf4j
public class AppUserNumberGenerator {

    private AtomicInteger lastGivenNumber;

    private MongoTemplate template;

    @Autowired
    public AppUserNumberGenerator(final MongoTemplate template) {
        Assert.notNull(template, "template cannot be null");
        this.template = template;
    }

    @PostConstruct
    protected void init() {
        lastGivenNumber = new AtomicInteger(10);

        Query query = new Query();

        query.with(Sort.by(Sort.Direction.DESC, "id")).limit(1);
        AppUser result = template.findOne(query, AppUser.class);

        lastGivenNumber.set(result.getId());
    }

    public Integer getNextGivenNumber() {
        return lastGivenNumber.addAndGet(1);
    }

    public Integer getLastGivenNumber() {
        return lastGivenNumber.get();
    }

} // The End...
