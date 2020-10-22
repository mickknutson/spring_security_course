package io.baselogic.springsecurity.configuration;

import io.baselogic.springsecurity.domain.Role;
import io.baselogic.springsecurity.repository.AppUserRepository;
import io.baselogic.springsecurity.repository.EventRepository;
import io.baselogic.springsecurity.repository.RoleRepository;
import io.netty.util.internal.StringUtil;
import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.h2.H2ConnectionOption;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;
import java.util.Arrays;

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
@EnableR2dbcRepositories("io.baselogic.springsecurity.repository")
//@EnableTransactionManagement
@Slf4j
public class DataSourceConfig extends AbstractR2dbcConfiguration {


    //-----------------------------------------------------------------------//

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        log.info("*** connectionFactory...");
//        ConnectionFactoryOptions baseOptions = ConnectionFactoryOptions.parse(properties.getUrl());
//        ConnectionFactoryOptions.Builder ob = ConnectionFactoryOptions.builder().from(baseOptions);
//
//        if (!StringUtil.isNullOrEmpty(properties.getUser())) {
//            ob = ob.option(USER, properties.getUser());
//        }
//        if (!StringUtil.isNullOrEmpty(properties.getPassword())) {
//            ob = ob.option(PASSWORD, properties.getPassword());
//        }
//        return ConnectionFactories.get(ob.build());
//
//        ConnectionFactoryOptions options = builder()
//                .option(DRIVER, "h2")
//                .option(PROTOCOL, "...")  // file, mem
//                .option(HOST, "…")
//                .option(USER, "…")
//                .option(PASSWORD, "…")
//                .option(DATABASE, "…")
//                .build();

        H2ConnectionFactory connectionFactory = new H2ConnectionFactory(
                H2ConnectionConfiguration.builder()
                        .inMemory("jdbc:h2:mem:dataSource")
//                        .inMemory("r2dbc:h2:mem:///dataSource")
//                        .option(H2ConnectionOption.DB_CLOSE_DELAY, "-1")
                        .option("DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                        .username("sa")
                        .build());

        return connectionFactory;
    }

    /**
     * Seed the database with our schema.sql
     * @return
     */
    @Bean
    public ConnectionFactoryInitializer initializer() {
        log.info("*** ConnectionFactoryInitializer...");

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();

        initializer.setConnectionFactory(connectionFactory());

        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(true, true, null, new ClassPathResource("schema.sql")));
        populator.addPopulators(new ResourceDatabasePopulator(true, true, null, new ClassPathResource("data.sql")));
        initializer.setDatabasePopulator(populator);

        return initializer;
    }


    @Bean
    public CommandLineRunner seedRoleRepository(AppUserRepository appUserRepository,
                                                EventRepository eventRepository,
                                                RoleRepository roleRepository) {
        log.info("*** seedRoleRepository...");

        return (args) -> {

            log.info("-------------------------------");
            log.info("--> save a few roles");

            roleRepository.save(new Role(Integer.valueOf(0), "ROLE_USER", null));

            // save a few roles
//            repository.saveAll(Arrays.asList(
//                    new Role(Integer.valueOf(0), "ROLE_USER", null),
//                    new Role(Integer.valueOf(1), "ROLE_USER", null)
//                    )
//            )
//                    .blockLast(Duration.ofSeconds(10));

            log.info("-------------------------------");
            log.info("appUserRepository.findAll():");
            log.info("-------------------------------");
            appUserRepository.findAll().doOnNext(role -> {
                log.info(role.toString());
            }).blockLast(Duration.ofSeconds(10));;


            log.info("-------------------------------");
            log.info("eventRepository.findAll():");
            log.info("-------------------------------");
            eventRepository.findAll().doOnNext(event -> {
                log.info(event.toString());
            }).blockLast(Duration.ofSeconds(10));;


            log.info("-------------------------------");
            log.info("roleRepository.findAll():");
            log.info("-------------------------------");
            roleRepository.findAll().doOnNext(role -> {
                log.info(role.toString());
            }).blockLast(Duration.ofSeconds(10));;
            log.info("-------------------------------");
            log.info("The End...");
            log.info("-------------------------------");


            // fetch all roles
            /*log.info("roles found with findAll():");
            log.info("-------------------------------");
            repository.findAll().doOnNext(role -> {
                log.info(role.toString());
            }).blockLast(Duration.ofSeconds(10));

            log.info("");

            // fetch an individual role by ID
            repository.findById(1).doOnNext(role -> {
                log.info("role found with findById(1):");
                log.info("--------------------------------");
                log.info(role.toString());
                log.info("");
            }).block(Duration.ofSeconds(10));*/


            // fetch roles by last name
            /*log.info("role found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            repository.findById(0).doOnNext(role -> {
                log.info(role.toString());
            }).blockLast(Duration.ofSeconds(10));;
            log.info("");*/
        };
    }


    /*@Bean
    ReactiveTransactionManager transactionManager() {
        return new R2dbcTransactionManager(connectionFactory());
    }*/

} // The End...
