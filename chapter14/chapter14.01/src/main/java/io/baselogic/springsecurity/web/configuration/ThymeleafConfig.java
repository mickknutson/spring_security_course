package io.baselogic.springsecurity.web.configuration;

import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;

/**
 * Thymeleaf Configuration
 * The first chapter this is required in is chapter02.05
 *
 * @since chapter02.05 Class created.
 * @since chapter14.01 Refactored for WebFlux
 */
@Configuration
@EnableConfigurationProperties(ThymeleafProperties.class)
public class ThymeleafConfig {

    private final ISpringWebFluxTemplateEngine templateEngine;


    public ThymeleafConfig(ISpringWebFluxTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    @Bean
    @Description("Thymeleaf view resolver")
    public ThymeleafReactiveViewResolver thymeleafReactiveViewResolver(final ApplicationContext applicationContext) {
        final ThymeleafReactiveViewResolver resolver = new ThymeleafReactiveViewResolver();
        resolver.setTemplateEngine(templateEngine);
        resolver.setResponseMaxChunkSizeBytes(8192); // OUTPUT BUFFER size limit
        resolver.setApplicationContext(applicationContext);

        resolver.setOrder(1);

        return resolver;
    }

} // The End...