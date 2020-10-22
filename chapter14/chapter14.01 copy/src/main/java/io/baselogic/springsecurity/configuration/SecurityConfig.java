package io.baselogic.springsecurity.configuration;

import io.baselogic.springsecurity.service.DefaultEventService;
import io.baselogic.springsecurity.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Security Configuration  Class
 *
 * @see WebSecurityConfigurerAdapter
 * @since chapter02.01 created
 */
@Configuration
//@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {

    private static final String HASANYROLE_ANONYMOUS = "hasAnyRole('ANONYMOUS', 'USER')";
    private static final String HASROLE_USER = "hasRole('USER')";
    private static final String HASROLE_ADMIN = "hasRole('ADMIN')";


    @Autowired
    private UserDetailsService userDetailsService;



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
