package io.baselogic.springsecurity.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * Here we leverage Spring's {@link EnableWebMvc} support. This allows more powerful configuration but still be
 * concise about it.
 * </p>
 *
 * @author Mick Knutson
 *
 * @since chapter01.00
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "io.baselogic.springsecurity.web.controllers",
        "io.baselogic.springsecurity.web.model"
})
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/"};


    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        // Add WebJars for Bootstrap & jQuery
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:webjars/")
        ;


        registry.addResourceHandler("/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS)
        ;
    }

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/error")
                .setViewName("/error");
        registry.addViewController("/errors/403")
                .setViewName("/errors/403");

        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
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
