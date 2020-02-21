package io.baselogic.springsecurity.configuration;

import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Security Configuration  Class
 * @see WebSecurityConfigurerAdapter
 * @since chapter02.01
 * @since chapter03.05 Added .authenticationEntryPoint(loginUrlAuthenticationEntryPoint())
 * @since chapter04.00 removed .authenticationEntryPoint(loginUrlAuthenticationEntryPoint())
 * @since chapter04.01 Exposed 'JdbcUserDetailsManager' as 'UserDetailsManager' named 'userDetailsService'
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ROLE_ANONYMOUS = "ANONYMOUS";
    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";



    @Autowired
    private DataSource dataSource;

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
     * @see {userDetailsService()}
     * @see {@link io.baselogic.springsecurity.service.DefaultEventService}
     *
     * @param auth       AuthenticationManagerBuilder
     * @throws Exception Authentication exception
     */
    @Description("Configure AuthenticationManager with inMemory credentials")
    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
//                .passwordEncoder(passwordEncoder())
        ;
    }

    /**
     * The parent method from {@link WebSecurityConfigurerAdapter} (public UserDetailsService userDetailsService())
     * originally returns a {@link org.springframework.security.core.userdetails.UserDetailsService},
     * but this needs to be a {@link UserDetailsManager}
     * UserDetailsManager vs UserDetailsService
     */
    @Bean
    @Description("Expose 'JdbcUserDetailsManager' as 'UserDetailsManager' named 'userDetailsService'")
    @Override
    public UserDetailsManager userDetailsService() {
        return new JdbcUserDetailsManager() {{
            setDataSource(dataSource);
        }};
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
                // Allow anyone to use H2 (NOTE: NOT FOR PRODUCTION USE EVER !!! )
                .antMatchers("/admin/h2/**").permitAll()

                .antMatchers("/").hasAnyRole(ROLE_ANONYMOUS, ROLE_USER)
                .antMatchers("/registration/*").permitAll()
                .antMatchers("/login/*").hasAnyRole(ROLE_ANONYMOUS, ROLE_USER)
                .antMatchers("/logout/*").hasAnyRole(ROLE_ANONYMOUS, ROLE_USER)
                .antMatchers("/admin/*").hasRole(ROLE_ADMIN)
                .antMatchers("/events/").hasRole(ROLE_ADMIN)
                .antMatchers("/**").hasRole(ROLE_USER)

                // The default AccessDeniedException
                .and().exceptionHandling()
                .accessDeniedPage("/errors/403")

                // Login Configuration
                .and().formLogin()
                .loginPage("/login/form")
                .loginProcessingUrl("/login")
                .failureUrl("/login/form?error")
                .usernameParameter("username") // redundant
                .passwordParameter("password") // redundant
                .defaultSuccessUrl("/default", true)
                .permitAll()

                // Logout Configuration
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login/form?logout")
                .permitAll()
        ;


        // Allow anonymous users
        http.anonymous();

        // CSRF is enabled by default, with Java Config
        http.csrf().disable();

        // Cross Origin Resource Sharing
        http.cors().disable();

        // HTTP Security Headers
        http.headers().disable();

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
    @Description("Configure Web Security")
    @Override
    public void configure(final WebSecurity web) {
        web.ignoring()
                .antMatchers("/css/**")
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
    @Description("Configure Password Encoder")
    public PasswordEncoder passwordEncoder() {

        String idForEncode = "noop";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, NoOpPasswordEncoder.getInstance());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }


} // The End...
