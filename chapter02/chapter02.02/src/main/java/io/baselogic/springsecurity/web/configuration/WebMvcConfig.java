package io.baselogic.springsecurity.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Here we leverage Spring's {@link EnableWebMvc} support. This allows more powerful configuration but still be
 * concise about it. Specifically it allows overriding {@link WebMvcConfigurerAdapter#requestMappingHandlerMapping()}.
 * Note that this class is loaded via the WebAppInitializer
 * </p>
 *
 * @author Mick Knutson
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "io.baselogic.springsecurity.web.controllers",
        "io.baselogic.springsecurity.web.model"
})
public class WebMvcConfig extends WebMvcConfigurerAdapter
{

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/" };

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/")
                .setCachePeriod(1)
        ;

        // Add WebJars for Bootstrap & jQuery
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("/webjars/");
        }


        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations(
                    CLASSPATH_RESOURCE_LOCATIONS);

        }

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
