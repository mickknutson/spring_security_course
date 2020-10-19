package io.baselogic.springsecurity.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing to ensure Security Headers are enabled.
 *
 * @author mickknutson
 *
 * @since chapter13.02 created
 */
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("tls")
@Slf4j
public class HeaderTests {

    @Autowired
    private MockMvc mockMvc;


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
    }

    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Testing Enabled Headers ContentType")
    @WithAnonymousUser
    void test_http_headers_contentTypeOptions() throws Exception {

        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andReturn();
    }

    @Test
    @DisplayName("Testing Enabled Headers XSS Protection")
    @WithAnonymousUser
    void test_http_headers_xssProtection() throws Exception {

        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-XSS-Protection", "1; mode=block"))
                .andReturn();
    }

    @Test
    @DisplayName("Testing Enabled Headers cache Control")
    @WithAnonymousUser
    void test_http_headers_cacheControl() throws Exception {

        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate"))
                .andExpect(header().string("Pragma", "no-cache"))
                .andExpect(header().string("Expires", "0"))
                .andReturn();
    }

    @Test
    @DisplayName("Testing Enabled Headers httpStrictTransportSecurity HTS")
    @WithAnonymousUser
    void test_http_headers_httpStrictTransportSecurity() throws Exception {

        MvcResult result = mockMvc.perform(get("https://localhost:8443"))
                .andExpect(status().isFound())
                .andExpect(header().string("Strict-Transport-Security", "max-age=31536000 ; includeSubDomains"))
                .andReturn();
    }

    @Test
    @DisplayName("Testing Enabled Headers <frame> Options")
    @WithAnonymousUser
    void test_http_headers_frameOptions() throws Exception {

        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                // Frames are needed for H2 Admin Console:
                // .andExpect(header().string("X-Frame-Options", "DENY"))
                .andReturn();
    }


} // The End...
