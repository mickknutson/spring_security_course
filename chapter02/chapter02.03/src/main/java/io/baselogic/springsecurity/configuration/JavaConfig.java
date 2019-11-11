package io.baselogic.springsecurity.configuration;

import org.springframework.context.annotation.*;

@Configuration
@Import({DataSourceConfig.class})
@ComponentScan(basePackages =
        {
                "io.baselogic.springsecurity.dao",
                "io.baselogic.springsecurity.domain",
                "io.baselogic.springsecurity.service"
        }
)
public class JavaConfig {


} // The end...
