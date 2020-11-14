package io.baselogic.springsecurity.configuration;

import io.baselogic.springsecurity.service.DefaultEventService;
import io.baselogic.springsecurity.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

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
@EnableReactiveMethodSecurity
@Slf4j
public class SecurityConfig {

    private static final String HASANYROLE_ANONYMOUS = "hasAnyRole('ANONYMOUS', 'USER')";
    private static final String HASROLE_USER = "hasRole('USER')";
    private static final String HASROLE_ADMIN = "hasRole('ADMIN')";


    @Autowired
    private UserDetailsService userDetailsService;


    @Description("Configure HTTP Security")
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .formLogin().and()
                .httpBasic().disable()
                .authorizeExchange()
                .pathMatchers("/**").permitAll()
                .pathMatchers("/controller").hasRole("ADMIN")
                .anyExchange().authenticated()
                .and()
                .build();

        /*
        http
            .authorizeExchange(exchanges ->
                exchanges
                    .pathMatchers("/**").permitAll()
                    .anyExchange().authenticated()
            )
            .httpBasic().disable()
//            .httpBasic(withDefaults())
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login")
            );
        return http.build();
         */
    }

    /*@Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges ->
                        exchanges
                                .anyExchange().authenticated()
                )
                .httpBasic(withDefaults())
                .formLogin(withDefaults());
        return http.build();
    }*/


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
    /*@Description("Configure Web Security")
    @Bean
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**")
                .antMatchers("/css/**")
                .antMatchers("/favicon.ico")
                .antMatchers("/img/**")
                .antMatchers("/webjars/**")
        ;

        // Thymeleaf needs to use the Thymeleaf configured FilterSecurityInterceptor
        // and not the default Filter from AutoConfiguration.
        final HttpSecurity http = getHttp();
        web.postBuildAction(() -> {
            web.securityInterceptor(http.getSharedObject(FilterSecurityInterceptor.class));
        });

    }*/


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
