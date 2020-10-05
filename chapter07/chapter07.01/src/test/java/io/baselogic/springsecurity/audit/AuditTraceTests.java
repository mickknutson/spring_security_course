package io.baselogic.springsecurity.audit;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import io.baselogic.springsecurity.web.configuration.CustomTomcatEmbeddedServletContainerFactory;
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
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("tls")
@Slf4j
public class AuditTraceTests {

    @Autowired
    TraceMonitor traceMonitor;

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
                .webAppContextSetup(context)
                .build();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(true);
    }

    @AfterEach
    void afterEachTest() {}



    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Test AOP Audit Trace")
    @WithMockEventUserDetailsUser1
    public void test_audit_trace() throws Exception {

        MvcResult result = mockMvc.perform(get("/events/my"))

                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        log.info("TEST: traceMonitor: {}", traceMonitor.toString());
//        log.info("content: {}", content);

    }

} // The End...
