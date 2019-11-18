package io.baselogic.springsecurity.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Security Configuration  Class
 * @see WebSecurityConfigurerAdapter
 */
@Configuration
@EnableWebSecurity
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

        am.inMemoryAuthentication()
                .withUser("user").password("{noop}user").roles("USER")
                .and().withUser("admin").password("{noop}admin").roles("ADMIN")
                .and().withUser("user1@example.com").password("{noop}user1").roles("USER")
                .and().withUser("admin1@example.com").password("{noop}admin1").roles("USER", "ADMIN")
        ;

        log.info("***** Password for user 'user1@example.com' is 'user1'");
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
                .antMatchers("/**").hasRole("USER")

                .and().formLogin()
                    .loginPage("/login/form")
                    .loginProcessingUrl("/login")
                    .failureUrl("/login/form?error")
                    .usernameParameter("username")
                    .passwordParameter("password")

                .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")

                .and().httpBasic()

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
     * @param web {@link WebSecurity} is created by {@link WebSecurityConfiguration}
     * <p>
     * The {@link WebSecurity} is created by {@link WebSecurityConfiguration} to create the
     * {@link FilterChainProxy} known as the Spring Security Filter Chain
     * (springSecurityFilterChain). The springSecurityFilterChain is the {@link Filter} that
     * the {@link DelegatingFilterProxy} delegates to.
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
