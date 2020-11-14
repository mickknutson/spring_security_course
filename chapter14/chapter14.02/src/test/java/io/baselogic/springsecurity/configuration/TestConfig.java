package io.baselogic.springsecurity.configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import io.baselogic.springsecurity.repository.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

/**
 * General Spring Configuration.
 * See Other Configs: {@link DataSourceConfig}
 *
 * @since chapter14.01
 *
 */
@Configuration
//@ComponentScan(basePackages =
//        {
//                "io.baselogic.springsecurity"
//        }
//)
public class TestConfig {

//    @Autowired
//    MongoTemplate template;
//    ReactiveMongoTemplate template;

//    @Bean
//    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
//        return new MongoTemplate(mongoClient, "dataSource");
//    }

//    @Bean
//    public SequenceGeneratorService sequenceGeneratorService() {
//        return new SequenceGeneratorService(template);
//    }


} // The end...
