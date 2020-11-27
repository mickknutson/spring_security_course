package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * EventNumberGenerator
 *
 * @author mickknutson
 *
 * @since chapter14.01 Created
 */
@Repository
@DependsOn("mongoDataInitializer")
@Slf4j
public class EventNumberGenerator {

    private AtomicInteger lastGivenNumber;

    private ReactiveMongoTemplate template;

    @Autowired
    public EventNumberGenerator(final ReactiveMongoTemplate template) {
        Assert.notNull(template, "template cannot be null");
        this.template = template;
    }

    @PostConstruct
    protected void init() {
        lastGivenNumber = new AtomicInteger(1_000);

        Query query = new Query();

        query.with(Sort.by(Sort.Direction.DESC, "id")).limit(1);
        Mono<Event> mono = template.findOne(query, Event.class);

        mono.subscribe(event -> {
            lastGivenNumber.set(event.getId());
            log.debug("lastGivenNumber.set({})", event.getId());
        });
    }

    public Integer getNextGivenNumber() {
        return lastGivenNumber.addAndGet(1);
    }

    public Integer getLastGivenNumber() {
        return lastGivenNumber.get();
    }

} // The End...
