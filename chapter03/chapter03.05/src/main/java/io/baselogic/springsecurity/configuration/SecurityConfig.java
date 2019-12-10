package io.baselogic.springsecurity.configuration;

import io.baselogic.springsecurity.authentication.EventUserAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * Spring Security Configuration  Class
 * @see WebSecurityConfigurerAdapter
 * @since chapter02.01
 * @since chapter03.05 Added
 */
@Configuration
@EnableWebSecurity//(debug = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private EventUserAuthenticationProvider euap;


    /**
     * Configure AuthenticationManager with inMemory credentials.
     *
     * NOTE:
     * Due to a known limitation with JavaConfig:
     * <a href="https://jira.spring.io/browse/SPR-13779">
     *     https://jira.spring.io/browse/SPR-13779</a>
     *
     * We cannot use the following to expose a {@link UserDetailsManager}
     * <pre>
     *     http.authorizeRequests()
     * </pre>
     *
     * In order to expose {@link UserDetailsManager} as a bean, we must create  @Bean
     *
     * @param auth       AuthenticationManagerBuilder
     * @throws Exception Authentication exception
     */
    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(euap);
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
