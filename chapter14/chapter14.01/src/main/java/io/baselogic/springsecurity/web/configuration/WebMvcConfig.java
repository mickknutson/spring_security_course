package io.baselogic.springsecurity.web.configuration;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;

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
@EnableWebFlux
@ComponentScan(basePackages = {
        "io.baselogic.springsecurity.web"
})
public class WebMvcConfig implements WebFluxConfigurer {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/"};

    @Autowired
    private ThymeleafReactiveViewResolver thymeleafReactiveViewResolver;


    @Description("addResourceHandlers")
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {

        // Add WebJars for Bootstrap & jQuery
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(false);

        registry.addResourceHandler("/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS)
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS));

        registry.setOrder(1);
    }


    @Override
    public void configureViewResolvers(final ViewResolverRegistry registry) {
        registry.viewResolver(thymeleafReactiveViewResolver);

        registry.order(1);
    }


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
