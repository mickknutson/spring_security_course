package io.baselogic.springsecurity.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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


} // The end...
