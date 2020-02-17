package io.baselogic.springsecurity.configuration;

import io.baselogic.springsecurity.dao.EventRowMapper;
import io.baselogic.springsecurity.dao.UserRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Database Configuration
 *
 * @author mickknutson
 *
 * @since chapter01.00
 * @since chapter04.01 added DataSource config to manually add additional SQL files to the init.
 */
@Configuration
@EnableTransactionManagement
@Slf4j
public class DataSourceConfig {


    //-------------------------------------------------------------------------

    /**
     * Custom H2 implementation for our {@link EmbeddedDatabase}
     * @return DataSource for our embedded database.
     */
    @Bean
    public DataSource dataSource() {

        // no need shutdown, EmbeddedDatabaseFactoryBean will take care of this
        return new EmbeddedDatabaseBuilder()
                //Starting embedded database: url='jdbc:h2:mem:dataSource;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false', username='sa'
                .setName("dataSource")
                // Lets not get upset as we are only debugging ;-)
                .ignoreFailedDrops(true)
                .continueOnError(true)
                // DB Details:
                .setType(EmbeddedDatabaseType.H2)
                .addScript("/schema.sql")
                .addScript("/data.sql")
                .addScript("/database/h2/security-schema.sql")
                .addScript("/database/h2/security-users.sql")
                .addScript("/database/h2/security-user-authorities.sql")
                .build();
    }

    //-------------------------------------------------------------------------

    @Bean
    public UserRowMapper userRowMapper(){
        return new UserRowMapper("appUsers.");
    }

    @Bean
    public UserRowMapper ownerRowMapper(){
        return new UserRowMapper("owner_");
    }

    @Bean
    public UserRowMapper attendeeRowMapper(){
        return new UserRowMapper("attendee_");
    }

    @Bean
    public EventRowMapper eventRowMapper(){
        return new EventRowMapper(ownerRowMapper(),
                attendeeRowMapper());
    }

    @Bean
    public String eventQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e.id, e.summary, e.description, e.event_date, ");

        sb.append("owner.id as owner_id, owner.email as owner_email, owner.password as owner_password, owner.first_name as owner_first_name, owner.last_name as owner_last_name, ");
        sb.append("attendee.id as attendee_id, attendee.email as attendee_email, attendee.password as attendee_password, attendee.first_name as attendee_first_name, attendee.last_name as attendee_last_name ");

        sb.append("FROM events as e, appUsers as owner, appUsers as attendee ");
        sb.append("WHERE e.owner = owner.id and e.attendee = attendee.id");

        return sb.toString();
    }

    @Bean
    public String userQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id, email, password, first_name, last_name ");
        sb.append("FROM appUsers ");
        sb.append("WHERE ");

        return sb.toString();
    }


    @Bean
    public String userInsertQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO appUsers (email, password, first_name, last_name) ");
        sb.append("VALUES(:email, :psswd, :first_name, :last_name)");

        return sb.toString();
    }


    //-------------------------------------------------------------------------


    /**
     * Access the H2 Console:
     * http://localhost:8080/admin/h2/
     *
     * @return {@link ServletRegistrationBean} for the H2 admin Servlet
     */
    @Bean
    public ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/admin/h2/*");
        return registrationBean;

    }

} // The End...
