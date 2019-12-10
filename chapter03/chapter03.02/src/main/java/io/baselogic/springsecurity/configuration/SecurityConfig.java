package io.baselogic.springsecurity.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * Spring Security Configuration  Class
 * @see WebSecurityConfigurerAdapter
 * @since chapter02.01
 */
@Configuration
@EnableWebSecurity//(debug = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Configure AuthenticationManager with inMemory credentials.
     *
     * Note: Prior to Spring Security 5.0
     * the default PasswordEncoder was NoOpPasswordEncoder which required plain text passwords.
     * In Spring Security 5, the default is DelegatingPasswordEncoder, which required Password Storage Format.
     *
     * @param am       AuthenticationManagerBuilder
     * @throws Exception Authentication exception
     */
    @Override
    public void configure(final AuthenticationManagerBuilder am) throws Exception {

        am.userDetailsService(userDetailsService());

        am.inMemoryAuthentication()
                .withUser("user").password("{noop}user").roles("USER")
                .and().withUser("admin").password("{noop}admin").roles("ADMIN")
                .and().withUser("user1@example.com").password("{noop}user1").roles("USER")
                .and().withUser("admin1@example.com").password("{noop}admin1").roles("USER", "ADMIN")
        ;

        log.debug("***** Password for user 'user1@example.com' is 'user1'");
        log.debug("***** Password for admin 'admin1@example.com' is 'admin1'");
    }

    /**
     * The parent method from {@link WebSecurityConfigurerAdapter} (public UserDetailsService userDetailsService())
     * originally returns a {@link org.springframework.security.core.userdetails.UserDetailsService}, but this needs to be a {@link UserDetailsManager}
     * see: UserDetailsManager vs UserDetailsService
     * @since chapter03.02
     */
    @Bean
    @Override
    public UserDetailsManager userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user").password("{noop}password").roles("USER").build());
        manager.createUser(User.withUsername("admin").password("{noop}admin").roles("USER", "ADMIN").build());
        manager.createUser(User.withUsername("user1@example.com").password("{noop}user1").roles("USER").build());
        manager.createUser(User.withUsername("admin1@example.com").password("{noop}admin1").roles("USER", "ADMIN").build());
        return manager;
    }


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
     * @param http HttpSecurity configuration.
     * @throws Exception Authentication configuration exception
     *
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http.authorizeRequests()
                // FIXME: TODO: Allow anyone to use H2 (NOTE: NOT FOR PRODUCTION USE EVER !!! )
                .antMatchers("/admin/h2/**").permitAll()

                .antMatchers("/").access("hasAnyRole('ANONYMOUS', 'USER')")
                .antMatchers("/registration/*").permitAll()
                .antMatchers("/login/*").access("hasAnyRole('ANONYMOUS', 'USER')")
                .antMatchers("/logout/*").access("hasAnyRole('ANONYMOUS', 'USER')")
                .antMatchers("/admin/*").access("hasRole('ADMIN')")
                .antMatchers("/events/").access("hasRole('ADMIN')")
                .antMatchers("/**").access("hasRole('USER')")

                // The default AccessDeniedException
                .and().exceptionHandling().accessDeniedPage("/errors/403")

                .and().formLogin()
                .loginPage("/login/form")
                .loginProcessingUrl("/login")
                .failureUrl("/login/form?error")
                .usernameParameter("username") // redundant
                .passwordParameter("password") // redundant
                .defaultSuccessUrl("/default", true)
                .permitAll()

                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login/form?logout")
                .permitAll()

                .and().anonymous()

                // CSRF is enabled by default, with Java Config
                .and().csrf().disable()

                // Cross Origin Resource Sharing
                .cors().disable()

                // HTTP Security Headers
                .headers().disable()
        ;
    }

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
                .antMatchers("/css/**")
                .antMatchers("/img/**")
                .antMatchers("/webjars/**")
        ;
    }


} // The End...
