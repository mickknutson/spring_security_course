package io.baselogic.springsecurity.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.session.SimpleRedirectSessionInformationExpiredStrategy;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import java.util.Arrays;

/**
 * @author Mick Knutson
 *
 * @since chapter 12.02 Created
 * @since chapter 12.05 Created @Bean hiddenHttpMethodFilter()
 * @since chapter 12.05 Created @Bean sessionRegistry()
 */
@Configuration
public class SessionConfig {

    /**
     * {@link HttpSessionEventPublisher} Publishes <code>HttpSessionApplicationEvent</code>s to the Spring Root
     * used in {@link SessionManagementConfigurer}
     *
     * @return HttpSessionEventPublisher
     * @since chapter12.05
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


    /**
     * Default SessionRegistry
     *
     * @return SessionRegistry
     */
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    /**
     * HiddenHttpMethodFilter to handle FORM submissions from DELETE
     * Uses hidden parameter called _method, then is translater to @DeleteMapping()
     * NOTE: The property 'spring.mvc.hiddenmethod.filter.enabled: true' does not work.
     *       Had to create this @Bean to get the HiddenHttpMethodFilter to register.
     *
     * @return FilterRegistrationBean for HiddenHttpMethodFilter
     *
     * @since chapter12.05
     */
    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter(){
        FilterRegistrationBean<HiddenHttpMethodFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new HiddenHttpMethodFilter());
        registrationBean.setUrlPatterns(Arrays.asList("/*"));

        return registrationBean;
    }

} // The End...
