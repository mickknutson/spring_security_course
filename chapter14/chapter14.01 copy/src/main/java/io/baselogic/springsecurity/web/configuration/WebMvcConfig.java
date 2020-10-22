package io.baselogic.springsecurity.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * <p>
 * Here we leverage Spring's {@link EnableWebFlux} support.
 * This allows more powerful configuration but still be concise about it.
 * </p>
 *
 * @author Mick Knutson
 *
 * @since chapter14.01 Refactored from WebMvcConfigurer to {@link WebFluxConfigurer}
 */
@Configuration

@EnableWebFlux

@ComponentScan(basePackages = {
        "io.baselogic.springsecurity.web.endpoints",
        "io.baselogic.springsecurity.web.model"
})
public class WebMvcConfig implements WebFluxConfigurer {

    // i18N support
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
        resource.setBasename("classpath:locales/messages");
        resource.setDefaultEncoding("UTF-8");
        resource.setFallbackToSystemLocale(Boolean.TRUE);
        return resource;
    }

} // The End...
