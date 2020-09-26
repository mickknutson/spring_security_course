package io.baselogic.springsecurity.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * @author Mick Knutson
 *
 * @since chapter 12.02 Created
 */
@Configuration
public class SessionConfig {

    /**
     * {@link HttpSessionEventPublisher} Publishes <code>HttpSessionApplicationEvent</code>s to the Spring Root
     * used in {@link SessionManagementConfigurer}
     *
     * @return HttpSessionEventPublisher
     * @since chapter12.02
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

} // The End...
