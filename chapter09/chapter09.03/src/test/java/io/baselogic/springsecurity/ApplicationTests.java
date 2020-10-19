package io.baselogic.springsecurity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


    
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("trace")
@Slf4j
class ApplicationTests {

    @Autowired
    private Environment environment;

    @Test
    void contextLoads() {

        Application.main(new String[0]);

        assertThat(environment.getActiveProfiles()).containsOnly("trace");
    }

} // The End...
