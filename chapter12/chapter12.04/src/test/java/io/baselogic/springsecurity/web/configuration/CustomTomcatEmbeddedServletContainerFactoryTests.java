package io.baselogic.springsecurity.web.configuration;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("tls")
@Slf4j
class CustomTomcatEmbeddedServletContainerFactoryTests {

    int localServerPort = 8080;

    int localRedirectPort = 8443;


    String url = "http://localhost:8080/";
    String redirectUrl = "https://localhost:8443/";


    @Autowired
    private MockMvc mockMvc;

    private WebClient webClient;


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

        webClient = MockMvcWebClientBuilder
                .webAppContextSetup(context, springSecurity())
                .build();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(true);
    }

    @AfterEach
    void afterEachTest() {}



    //-----------------------------------------------------------------------//

    String expectedPageTitle = "Welcome to the EventManager!";
    String expectedChapterHeading = "Chapter 07.01";
    String expectedChapterTitle = "Each chapter will have a slightly different summary depending on what has been done.";
    String expectedChapterSummary = "Advanced Certificate Authentication";


    @Test
    @DisplayName("Test TLS Port Redirect with Mock Mvc")
    void testHomePage() throws Exception {

//        URI uri = new URI(redirectUrl);
        URI uri = new URI(url);

        MvcResult result = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
//                .andExpect(forwardedUrlPattern(redirectUrl))
//                .andExpect(redirectedUrl(redirectUrl))
                .andReturn();

        log.info("getForwardedUrl: {}", result.getResponse().getForwardedUrl());
        log.info("getIncludedUrl: {}", result.getResponse().getIncludedUrl());
        log.info("getRedirectedUrl: {}", result.getResponse().getRedirectedUrl());
        String content = result.getResponse().getContentAsString();
        log.info("Resulting page: {}", content);

    }

//    @Test
    @DisplayName("Test TLS Port Redirect with WebClient")
    @WithMockEventUserDetailsUser1
    void default_port_Redirect() throws Exception {
        log.info("Executing tests on port {}", localServerPort);
        log.info("Redirecting from [{}] to [{}]", url, redirectUrl);

//        String targetUri = url+"/";
        String targetUri = redirectUrl+"/";

        HtmlPage page = webClient.getPage(targetUri);

        log.debug("page.asText(): {}", page.asXml());

//        log.info("page.getDocumentURI(): {}", page.getDocumentURI());
//        assertThat(page.getDocumentURI()).contains(redirectUrl);

        String id = page.getTitleText();
        assertThat(id).isEqualTo(expectedPageTitle);

        String chapterHeading = page.getHtmlElementById("chapterHeading").getTextContent();
        assertThat(chapterHeading).contains(expectedChapterHeading);

        String chapterTitle = page.getHtmlElementById("chapterTitle").getTextContent();
        assertThat(chapterTitle).contains(expectedChapterTitle);

        String summary = page.getHtmlElementById("summary").getTextContent();
        assertThat(summary).contains(expectedChapterSummary);


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
