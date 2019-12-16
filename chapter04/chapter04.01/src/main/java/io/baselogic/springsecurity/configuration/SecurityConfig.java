package io.baselogic.springsecurity.configuration;

import io.baselogic.springsecurity.authentication.EventUserAuthenticationProvider;
import io.baselogic.springsecurity.web.authentication.DomainUsernamePasswordAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

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
                .and().exceptionHandling()
                .accessDeniedPage("/errors/403")

                // We overrode defaultAuthenticationEntryPoint and added a reference to
                // o.s.s.web.authentication.LoginUrlAuthenticationEntryPoint, which
                // determines what happens when a request for a protected resource occurs and the
                // user is not authenticated. In our case, we are redirected to a login page.
                .authenticationEntryPoint(loginUrlAuthenticationEntryPoint())

                // Login Configuration
                //NOTE: We remove this in favor of the {@link LoginUrlAuthenticationEntryPoint}
                /*.and().formLogin()
                .loginPage("/login/form")
                .loginProcessingUrl("/login")
                .failureUrl("/login/form?error")
                .usernameParameter("username") // redundant
                .passwordParameter("password") // redundant
                .defaultSuccessUrl("/default", true)
                .permitAll()*/

                // Logout Configuration
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login/form?logout")
                .permitAll()


                // Add custom DomainUsernamePasswordAuthenticationFilter
                .and().addFilterAt(domainUsernamePasswordAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
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
    @Override
    public void configure(final WebSecurity web) {
        web.ignoring()
                .antMatchers("/css/**")
                .antMatchers("/img/**")
                .antMatchers("/webjars/**")
        ;
    }

    /**
     * Expose AuthenticationManager
     * @return AuthenticationManager for use in other Bean's
     * @throws Exception on Bean creation
     * @since chapter03.06
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()
            throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * DomainUsernamePasswordAuthenticationFilter
     * @return DomainUsernamePasswordAuthenticationFilter for Login
     * @throws Exception registering Bean
     *
     * @since chapter03.06
     */
    @Bean
    public DomainUsernamePasswordAuthenticationFilter domainUsernamePasswordAuthenticationFilter()
            throws Exception {

        DomainUsernamePasswordAuthenticationFilter dupaf
                = new DomainUsernamePasswordAuthenticationFilter(super.authenticationManagerBean());
        dupaf.setFilterProcessesUrl("/login");
        dupaf.setUsernameParameter("username");
        dupaf.setPasswordParameter("password");

        dupaf.setAuthenticationSuccessHandler(
                new SavedRequestAwareAuthenticationSuccessHandler(){{
                    setDefaultTargetUrl("/default");
                }}
        );

        dupaf.setAuthenticationFailureHandler(
                new SimpleUrlAuthenticationFailureHandler(){{
                    setDefaultFailureUrl("/login/form?error");
                }}
        );

        dupaf.afterPropertiesSet();

        return dupaf;
    }

    /**
     * LoginUrlAuthenticationEntryPoint
     * @return LoginUrlAuthenticationEntryPoint
     * @since chapter03.06
     */
    @Bean
    public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint(){
        return new LoginUrlAuthenticationEntryPoint("/login/form");
    }


    /**
     * Create a DelegatingPasswordEncoder
     *  see https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-encoding
     *
     * @return DelegatingPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        String idForEncode = "noop";
        Map encoders = new HashMap<>();
        encoders.put("noop", NoOpPasswordEncoder.getInstance());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


} // The End...
