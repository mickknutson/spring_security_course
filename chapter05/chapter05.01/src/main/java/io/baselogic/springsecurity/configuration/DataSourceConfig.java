package io.baselogic.springsecurity.configuration;

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
 * @since chapter04.02 Added customGroupAuthoritiesByUsernameQuery() for GBAC support
 * @since chapter04.03 Added Support for JdbcUserDetailsManager SQL
 * @since chapter05.01 REMOVED DataSource config to manually add additional SQL files to the init.
 */
@Configuration
@EnableTransactionManagement
@Slf4j
public class DataSourceConfig {


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
