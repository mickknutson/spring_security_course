package io.baselogic.springsecurity.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * General Spring Configuration.
 * See Other Configs: {@link DataSourceConfig}
 * @since chapter01.00
 * @since chapter03.03 added .userdetails package
 * @since chapter03.05 added .authentication package
 */
@Configuration
@ComponentScan(basePackages =
        {
                "io.baselogic.springsecurity"
        }
)
@Slf4j
public class JavaConfig {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }


} // The end...
