package io.baselogic.springsecurity.web.configuration;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.web.driver.IndexPage;
import io.baselogic.springsecurity.web.driver.LoginPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("tls")
@Slf4j
public class CustomTomcatEmbeddedServletContainerFactoryTests {

    @Value("${server.port: 8080}")
    private int serverPort;


    @Autowired
    private MockMvc mockMvc;

    private WebDriver driver;

    /**
     * Customize the WebClient to work with HtmlUnit
     *
     * @param context WebApplicationContext
     */
    @BeforeEach
    void beforeEachTest(WebApplicationContext context) {

        // https://docs.spring.io/spring-security/site/docs/current/reference/html5/#test-mockmvc-setup
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/testing.html#spring-mvc-test-server-htmlunit-webdriver
        driver = MockMvcHtmlUnitDriverBuilder
                .mockMvcSetup(mockMvc)
                .build();
    }

    @AfterEach
    void afterEachTest() {
        if (driver != null) {
            driver.close();
        }
    }

    /**
     * Test Secured Page with {@link WithAnonymousUser} annotations mixed with {@link SecurityMockMvcResultMatchers}
     *
     * @throws Exception is the test fails unexpectedly.
     */
    @Test
    @DisplayName("Testing that TLS is working with 'tls' profile")
    @WithAnonymousUser
    public void testing_tls_index() throws Exception {

//        String URL = "https://localhost:8443/";
    String URL = "http://localhost:8080/";

        IndexPage indexPage = IndexPage.to(driver);
        indexPage.navigateTo();

        assertThat(indexPage.validate()).isTrue();

        LoginPage loginPage = indexPage.clickLogin();
        assertThat(loginPage.validate()).isTrue();

        indexPage = loginPage.formLogin(TestUtils.user1);

        assertThat(indexPage.validate()).isTrue();

    }

} // The End...
