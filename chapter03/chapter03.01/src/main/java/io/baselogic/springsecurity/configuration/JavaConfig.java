package io.baselogic.springsecurity.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * General Spring Configuration.
 * See Other Configs: {@link io.baselogic.springsecurity.configuration.DataSourceConfig}
 */
@Configuration
@ComponentScan(basePackages =
        {
                "io.baselogic.springsecurity"
        }
)
public class JavaConfig {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }


} // The end...
