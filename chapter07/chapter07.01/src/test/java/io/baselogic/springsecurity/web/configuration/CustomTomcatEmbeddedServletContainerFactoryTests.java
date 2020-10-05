package io.baselogic.springsecurity.web.configuration;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.web.functional.objects.IndexPage;
import io.baselogic.springsecurity.web.functional.objects.LoginPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("tls")
@Slf4j
public class CustomTomcatEmbeddedServletContainerFactoryTests {

    @Value("${server.port: 8080}")
    private int serverPort;

    int localServerPort = 8080;


    @Autowired
    private MockMvc mockMvc;

    private WebClient webClient;

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

        webClient = MockMvcWebClientBuilder
                .webAppContextSetup(context)
                .build();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(true);
    }

    @AfterEach
    void afterEachTest() {
        if (driver != null) {
            driver.close();
        }
    }



    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Test TLS Port Redirect")
    @WithMockEventUserDetailsUser1
    public void default_port_Redirect() throws Exception {
        log.info("Executing tests on port {}", localServerPort);

        HtmlPage page = webClient.getPage("http://localhost:"+localServerPort+"/events/form");

        String titleText = page.getTitleText();
        log.info("page.uri: {}", page.getBaseURI());
        log.info("page.URL: {}", page.getUrl());

        assertThat(titleText).contains("Create Event");
    }



    /**
     * Test Secured Page with {@link WithAnonymousUser} annotations mixed with {@link SecurityMockMvcResultMatchers}
     *
     * @throws Exception is the test fails unexpectedly.
     */
    @Test
    @DisplayName("Testing that TLS is working with 'tls' profile")
    @WithAnonymousUser
    void testing_tls_index() throws Exception {

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

    @Test
    @DisplayName("Validate the servletContainer.init()")
    void unit_test_servletContainer_init() throws Exception {

        CustomTomcatEmbeddedServletContainerFactory container = new CustomTomcatEmbeddedServletContainerFactory();
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        TomcatServletWebServerFactory result = (TomcatServletWebServerFactory) container.init(factory);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Validate the servletContainer.createSecurityConstraints()")
    void unit_test_servletContainer_createSecurityConstraints() throws Exception {

        CustomTomcatEmbeddedServletContainerFactory container = new CustomTomcatEmbeddedServletContainerFactory();
        SecurityConstraint result = container.createSecurityConstraints();

        assertThat(result.getUserConstraint()).isEqualTo("CONFIDENTIAL");
        assertThat(result.included("/*", "GET")).isTrue();
    }


} // The End...
