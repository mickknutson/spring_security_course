package io.baselogic.springsecurity.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Database Configuration
 *
 * @author mickknutson
 *
 * @since chapter01.00
 * @since chapter04.02 Added customGroupAuthoritiesByUsernameQuery() for GBAC support
 * @since chapter04.03 Added Support for JdbcUserDetailsManager SQL
 * @since chapter05.01 REMOVED DataSource config to manually add additional SQL files to the init.
 */
@Configuration
@EnableMongoRepositories(basePackages="io.baselogic.springsecurity.repository")
@Slf4j
public class DataSourceConfig {


    //-----------------------------------------------------------------------//
    // Mongo
//    @Bean
//    @Autowired
//    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
//        return new MongoTransactionManager(dbFactory);
//    }

} // The End...
