package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import io.baselogic.springsecurity.service.DefaultEventServiceTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Spring Test: -------------------------------------------------------------//
// Junit 5: -----------------------------------------------------------------//
// Assert-J: ----------------------------------------------------------------//
// --> assertThat(result.size()).isGreaterThan(0);
// http://joel-costigliola.github.io/assertj/index.html


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class LoginTests {

    private static final Logger log = LoggerFactory.getLogger(DefaultEventServiceTests.class);

    @Autowired
    private MockMvc mockMvc;

    // HtmlUnit --> Rhino Engine
    private WebClient webClient;

    @BeforeEach
    void setup(WebApplicationContext context) {
        webClient = MockMvcWebClientBuilder
                .webAppContextSetup(context)
                .build();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
    }

    //-------------------------------------------------------------------------

    @Test
    @DisplayName("Custom Login Page")
    @WithAnonymousUser
    public void testHomePage() throws Exception {

        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isUnauthorized())
//                .andExpect(view().name("index"))
                .andReturn();
    }


    //-------------------------------------------------------------------------


} // The End...
