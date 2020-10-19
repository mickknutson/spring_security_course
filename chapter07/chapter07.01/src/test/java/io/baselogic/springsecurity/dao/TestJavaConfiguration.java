package io.baselogic.springsecurity.dao;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages =
        {
                "io.baselogic.springsecurity.dao",
                "io.baselogic.springsecurity.repository"
        }
)
public class TestJavaConfiguration {


}
