package io.baselogic.springsecurity.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * CustomGlobalMethodSecurityConfiguration
 *
 * @author mickknutson
 *
 * @since chapter07.01 Created for non-web related security configuration
 */
@Configuration
@EnableGlobalAuthentication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

} // The End...
