package io.baselogic.springsecurity.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Security Configuration  Class
 *
 * @see WebSecurityConfigurerAdapter
 * @since chapter02.01 created
 * @since chapter02.02 Added formLogin and logout configuration
 * @since chapter02.03 Added basic role-based authorization
 * @since chapter02.04 converted antMatchers to SPeL expressions
 * @since chapter02.05 Added .defaultSuccessUrl("/default")
 * @since chapter03.01 Converted configure(HttpSecurity) to use DSL
 * @since chapter03.01 Added PasswordEncoder passwordEncoder()
 * @since chapter03.02 Created userDetailsService() to return {@link UserDetailsManager}
 * @since chapter03.03 Removed userDetailsService() and configure(HttpSecurity) methods
 * @since chapter03.05 Added EventUserAuthenticationProvider @Bean
 * @since chapter03.05 Added auth.authenticationProvider(EventUserAuthenticationProvider)
 */
@Configuration
@EnableWebSecurity(debug = false)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String HASANYROLE_ANONYMOUS = "hasAnyRole('ANONYMOUS', 'USER')";
    private static final String HASROLE_USER = "hasRole('USER')";
    private static final String HASROLE_ADMIN = "hasRole('ADMIN')";

    /**
     * HTTP Security configuration
     *
     * <http auto-config="true"> is equivalent to:
     * <pre>
     *  <http>
     *      <form-login />
     *      <http-basic />
     *      <logout />
     *  </http>
     * </pre>
     *
     * Which is equivalent to the following JavaConfig:
     *
     * <pre>
     *     http.formLogin()
     *          .and().httpBasic()
     *          .and().logout();
     * </pre>
     *
     * @see  org.springframework.security.access.expression.SecurityExpressionRoot
     * @param http HttpSecurity configuration.
     * @throws Exception Authentication configuration exception
     *
     */
    @Description("Configure HTTP Security")
    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http.authorizeRequests(authorizeRequests -> authorizeRequests

                // Allow anyone to use H2 (NOTE: NOT FOR PRODUCTION USE EVER !!! )
                .antMatchers("/admin/h2/**").permitAll()
                .antMatchers("/actuator/**").permitAll()

                .antMatchers("/").access(HASANYROLE_ANONYMOUS)
                .antMatchers("/registration/*").permitAll()
                .antMatchers("/login/*").access(HASANYROLE_ANONYMOUS)
                .antMatchers("/logout/*").access(HASANYROLE_ANONYMOUS)
                .antMatchers("/admin/*").access(HASROLE_ADMIN)
                .antMatchers("/events/").access(HASROLE_ADMIN)
                .antMatchers("/**").access(HASROLE_USER)

        );

        // The default AccessDeniedException
        http.exceptionHandling(handler -> handler
                .accessDeniedPage("/errors/403")
        );

        // Login Configuration
        http.formLogin(form -> form
                .loginPage("/login/form")
                .loginProcessingUrl("/login")
                .failureUrl("/login/form?error")
                .usernameParameter("username") // redundant
                .passwordParameter("password") // redundant
                .defaultSuccessUrl("/default", true)
                .permitAll()
        );

        // Logout Configuration
        http.logout(form -> form
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login/form?logout")
                .permitAll()
        );


        // Allow anonymous users
        http.anonymous();

        // CSRF is enabled by default, with Java Config
        http.csrf().disable();

        // Cross Origin Resource Sharing
        http.cors().disable();

        // HTTP Security Headers
        http.headers().disable();
        http.headers().xssProtection().disable();
        http.headers().contentTypeOptions().disable();

        // Enable <frameset> in order to use H2 web console
        http.headers().frameOptions().disable();

    } // end configure


    /**
     * This is the equivalent to:
     * <pre><http pattern="/css/**" security="none"/></pre>
     *
     *
     * @param web {@link WebSecurity} is created by {@link org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration}
     * <p>
     * The {@link WebSecurity} is created by {@link org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration} to create the
     * {@link org.springframework.security.web.FilterChainProxy} known as the Spring Security Filter Chain
     * (springSecurityFilterChain). The springSecurityFilterChain is the {@link javax.servlet.Filter} that
     * the {@link org.springframework.web.filter.DelegatingFilterProxy} delegates to.
     * </p>
     */
    @Override
    public void configure(final WebSecurity web) {
        web.ignoring()
                .antMatchers("/resources/**")
                .antMatchers("/css/**")
                .antMatchers("/favicon.ico")
                .antMatchers("/img/**")
                .antMatchers("/webjars/**")
        ;
    }


    /**
     * Create a DelegatingPasswordEncoder
     *  see https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-encoding
     *
     *  Standard use, see {@link PasswordEncoderFactories}:
     *  <code>return PasswordEncoderFactories.createDelegatingPasswordEncoder();</code>
     *
     * @return DelegatingPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        String idForEncode = "noop";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, NoOpPasswordEncoder.getInstance());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }


} // The End...
