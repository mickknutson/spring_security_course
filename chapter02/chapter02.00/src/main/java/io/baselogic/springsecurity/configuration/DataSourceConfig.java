package io.baselogic.springsecurity.configuration;

import io.baselogic.springsecurity.dao.EventRowMapper;
import io.baselogic.springsecurity.dao.UserRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Database Configuration
 *
 * @author mickknutson
 *
 * @since chapter01.00
 */

@Configuration
@EnableTransactionManagement
@Slf4j
public class DataSourceConfig {

    //-----------------------------------------------------------------------//

    @Bean
    @Description("Jdbc ResultSet RowMapper for 'appUsers'")
    public UserRowMapper userRowMapper(){
        return new UserRowMapper("appUsers.");
    }

    @Bean
    @Description("Jdbc ResultSet RowMapper for 'owner_'")
    public UserRowMapper ownerRowMapper(){
        return new UserRowMapper("owner_");
    }

    @Bean
    @Description("Jdbc ResultSet RowMapper for 'attendee_'")
    public UserRowMapper attendeeRowMapper(){
        return new UserRowMapper("attendee_");
    }

    @Bean
    @Description("Event RowMapper")
    public EventRowMapper eventRowMapper(){
        return new EventRowMapper(ownerRowMapper(),
                attendeeRowMapper());
    }

    @Bean
    @Description("Jdbc SQL Query for 'EVENT'")
    public String eventQuery(){
        return new StringBuilder(100)
                .append("SELECT e.id, e.summary, e.description, e.event_date, ")

                .append("owner.id as owner_id, owner.email as owner_email, owner.password as owner_password, owner.first_name as owner_first_name, owner.last_name as owner_last_name, ")
                .append("attendee.id as attendee_id, attendee.email as attendee_email, attendee.password as attendee_password, attendee.first_name as attendee_first_name, attendee.last_name as attendee_last_name ")
                .append("FROM events as e, appUsers as owner, appUsers as attendee ")
                .append("WHERE e.owner = owner.id and e.attendee = attendee.id")

                .toString();
    }

    @Bean
    @Description("Jdbc SQL Query for 'appUsers'")
    public String userQuery(){
        return new StringBuilder(100)
                .append("SELECT id, email, password, first_name, last_name ")
                .append("FROM appUsers ")
                .append("WHERE ")
                .toString();
    }


    @Bean
    @Description("Jdbc SQL Insert for 'appUsers'")
    public String userInsertQuery(){
        return new StringBuilder(100)
                .append("INSERT INTO appUsers (email, password, first_name, last_name) ")
                .append("VALUES(:email, :psswd, :first_name, :last_name)")
                .toString();
    }


    //-----------------------------------------------------------------------//


    /**
     * Access the H2 Console:
     * http://localhost:8080/admin/h2/
     *
     * @return {@link ServletRegistrationBean} for the H2 admin Servlet
     */
    @Bean
    @Description("H2 Database admin Servlet")
    public ServletRegistrationBean<WebServlet> h2servletRegistration(){
        ServletRegistrationBean<WebServlet> registrationBean = new ServletRegistrationBean<>(new WebServlet());
        registrationBean.addUrlMappings("/admin/h2/*");
        return registrationBean;

    }

} // The End...
