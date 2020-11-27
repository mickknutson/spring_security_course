package io.baselogic.springsecurity.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.reactive.config.*;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.concurrent.TimeUnit;

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
// Issue with turning this on:
//@EnableWebFlux
@ComponentScan(basePackages = {
        "io.baselogic.springsecurity.web.controllers",
        "io.baselogic.springsecurity.web.handlers",
        "io.baselogic.springsecurity.web.model"
})
public class WebMvcConfig implements WebFluxConfigurer {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/"};

    @Description("addResourceHandlers")
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS)
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS));

        registry.setOrder(1);
    }


    /*@Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ViewResolver resolver = ... ;
        registry.viewResolver(resolver);
    }*/


    /*@Description("configurePathMatching")
    @Override
    public void configurePathMatching(final PathMatchConfigurer configurer) {
        configurer
                .setUseCaseSensitiveMatch(true)
                .setUseTrailingSlashMatch(false)
                .addPathPrefix("/api",
                        HandlerTypePredicate.forAnnotation(RestController.class));
    }*/


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
