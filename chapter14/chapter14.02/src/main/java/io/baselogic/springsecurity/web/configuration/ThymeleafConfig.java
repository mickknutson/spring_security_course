package io.baselogic.springsecurity.web.configuration;

import lombok.extern.slf4j.Slf4j;
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
 * @since chapter14.01 Refactored for WebFLux
 */
@Configuration
@EnableConfigurationProperties(ThymeleafProperties.class)
@Slf4j
public class ThymeleafConfig {

    private final ISpringWebFluxTemplateEngine templateEngine;


    public ThymeleafConfig(ISpringWebFluxTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

//    @Bean
//    @Description("Thymeleaf template resolver serving HTML 5")
//    public SpringResourceTemplateResolver templateResolver(final ApplicationContext applicationContext) {
//        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
//        templateResolver.setApplicationContext(applicationContext);
//        templateResolver.setPrefix("classpath:templates/");
//        templateResolver.setSuffix(".html");
//        templateResolver.setTemplateMode(TemplateMode.HTML);
//        templateResolver.setCharacterEncoding("UTF-8");
//        templateResolver.setOrder(1);
//        templateResolver.setCacheable(false);
//        return templateResolver;
//    }

    @Bean
    @Description("Thymeleaf view resolver")
    public ThymeleafReactiveViewResolver thymeleafChunkedAndDataDrivenViewResolver(final ApplicationContext applicationContext) {
        final ThymeleafReactiveViewResolver resolver = new ThymeleafReactiveViewResolver();
        resolver.setTemplateEngine(templateEngine);
        resolver.setOrder(1);
        resolver.setResponseMaxChunkSizeBytes(8192); // OUTPUT BUFFER size limit
        resolver.setApplicationContext(applicationContext);
        return resolver;
    }

} // The End...