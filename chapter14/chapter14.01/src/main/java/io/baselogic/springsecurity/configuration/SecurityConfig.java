package io.baselogic.springsecurity.configuration;

import io.baselogic.springsecurity.service.DefaultEventService;
import io.baselogic.springsecurity.service.EventService;
import io.baselogic.springsecurity.userdetails.EventUserDetailsService;
import io.baselogic.springsecurity.web.handlers.CustomServerAuthenticationSuccessHandler;
import io.baselogic.springsecurity.web.handlers.RedirectingAccessDeniedHandler;
import io.baselogic.springsecurity.web.handlers.CustomServerLogoutHandler;
import io.baselogic.springsecurity.web.handlers.RedirectingServerLogoutSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Security Configuration  Class
 *
 * @since chapter02.01 created
 * @since chapter02.02 Added formLogin and logout configuration
 * @since chapter02.03 Added basic role-based authorization
 * @since chapter02.04 converted antMatchers to SPeL expressions
 * @since chapter02.05 Added .defaultSuccessUrl("/default")
 * @since chapter03.01 Converted configure(HttpSecurity) to use DSL
 * @since chapter03.01 Added PasswordEncoder passwordEncoder()
 * @since chapter03.02 Created userDetailsService() to return {@link UserDetailsManager}
 * @since chapter03.03 Removed userDetailsService() and configure(HttpSecurity) methods
 * @since chapter03.05 Added auth.authenticationProvider(EventUserAuthenticationProvider)
 * @since chapter03.06 Added .authenticationEntryPoint(loginUrlAuthenticationEntryPoint())
 * @since chapter04.00 removed .authenticationEntryPoint(loginUrlAuthenticationEntryPoint())
 * @since chapter04.01 Exposed 'JdbcUserDetailsManager' as 'UserDetailsManager' named 'userDetailsService'
 * @since chapter04.03 Added custom SQL Queries
 * @since chapter05.01 Removed 'UserDetailsManager'
 * @since chapter05.01 Removed custom SQL Queries
 * @since chapter05.01 Added auth.userDetailsService(userDetailsService)
 * @since chapter14.01 Refactored class for WebFlux
 */
@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {

    private static final String[] HASANYROLE_ANONYMOUS = {"ANONYMOUS", "USER"};
    private static final String HASROLE_USER = "USER";
    private static final String HASROLE_ADMIN = "ADMIN";


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
     * @see EventService  {@link DefaultEventService}
     *
     * @param userDetailsService ReactiveUserDetailsService
     * @param passwordEncoder BCrypt PasswordEncoder
     *
     */
    @Bean
    @Description("Configure AuthenticationManager")
    public ReactiveAuthenticationManager reactiveAuthenticationManager(final ReactiveUserDetailsService userDetailsService,
                                                                       final PasswordEncoder passwordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    @Autowired
    private CustomServerAuthenticationSuccessHandler customServerAuthenticationSuccessHandler;

    @Autowired
    private RedirectingAccessDeniedHandler redirectingAccessDeniedHandler;

    @Autowired
    private CustomServerLogoutHandler customServerLogoutHandler;

    @Autowired
    private RedirectingServerLogoutSuccessHandler redirectingServerLogoutSuccessHandler;


    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "/resources/**", "/css/**",
            "/favicon.ico", "/img/**", "/webjars/**"};


    @Description("Configure HTTP Security")
    @Bean
    public SecurityWebFilterChain configure(final ServerHttpSecurity http) {
        http.authorizeExchange(exchanges ->
                exchanges

                        // Allow anyone to use H2 (NOTE: NOT FOR PRODUCTION USE EVER !!! )
                        .pathMatchers("/admin/h2/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()

                        .pathMatchers(CLASSPATH_RESOURCE_LOCATIONS).permitAll()

                        .pathMatchers("/registration/**").permitAll()
                        .pathMatchers("/error/**").permitAll()

                        .pathMatchers("/").permitAll()

                        .pathMatchers("/login").permitAll()
                        .pathMatchers("/login*").permitAll()
                        .pathMatchers("/login/*").permitAll()
                        .pathMatchers("/login/**").permitAll()
                        .pathMatchers("/logout").permitAll()

                        .pathMatchers("/admin/*").hasRole(HASROLE_ADMIN)
                        .pathMatchers("/events/").hasRole(HASROLE_ADMIN)

                        .pathMatchers("/**").hasRole(HASROLE_USER)

                    .anyExchange().authenticated()
            );

            http.httpBasic().disable();

        // The default AccessDeniedException
        http.exceptionHandling().accessDeniedHandler(
                redirectingAccessDeniedHandler
                        .accessDeniedPage("/error/403")
        );

        // Login Configuration
        http.formLogin(form ->
                form.loginPage("/login")
                .authenticationSuccessHandler(
                        customServerAuthenticationSuccessHandler
                                .defaultSuccessUrl("/default")
                )
        );

        // Logout Configuration
        http.logout(logout -> {
            logout.logoutUrl("/logout")
                    // Allows for HTTP GET requests for logout:
                    .requiresLogout(new PathPatternParserServerWebExchangeMatcher("/logout"))
                    // FIXME: Cannot seem to have both:
//                    .logoutHandler(customServerLogoutHandler)
                    .logoutSuccessHandler(
                            redirectingServerLogoutSuccessHandler
                                    .logoutSuccessUrl("/login?logout")
                    )
            ;
        });

        // Allow anonymous users
        http.anonymous();

        // CSRF is enabled by default, with Java Config
        //NOSONAR
        http.csrf().disable();

        // Cross Origin Resource Sharing
        http.cors().disable();

        // HTTP Security Headers
        http.headers().disable();

        // Enable <frameset> in order to use H2 web console
        http.headers().frameOptions().disable();

        return http.build();
    }



    /**
     * Create a DelegatingPasswordEncoder
     *  see https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-encoding
     *
     *  Standard use, see {@link PasswordEncoderFactories}:
     *  <code>return PasswordEncoderFactories.createDelegatingPasswordEncoder();</code>
     *
     * Older implementations, such as SHAPasswordEncoder,
     * would require the client to pass in a salt value when encoding the password.
     *
     * BCrypt, however, will internally generate a random salt instead.
     * This is important to understand because it means that each call will have a different result,
     * and so we need to only encode the password once.
     *
     * Also be aware that the BCrypt algorithm generates a String of length 60,
     * so we need to make sure that the password will be stored in a column that can accommodate it.
     * A common mistake is to create a column of a different length and then get an Invalid Username
     * or Password error at authentication time.
     *
     * @return DelegatingPasswordEncoder
     * @since chapter02.01
     * @since chapter04.04 changed to BCrypt Password Encoder
     */
    @Bean
    @Description("Configure Password Encoder")
    public PasswordEncoder passwordEncoder() {

        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder(4));

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }


} // The End...
