package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.User;
import io.baselogic.springsecurity.service.DefaultEventServiceTests;
import io.baselogic.springsecurity.service.EventService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


// Spring Test: -------------------------------------------------------------//

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


// Junit 5: -----------------------------------------------------------------//
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;


// Assert-J: ----------------------------------------------------------------//
// --> assertThat(result.size()).isGreaterThan(0);
// http://joel-costigliola.github.io/assertj/index.html
import static org.assertj.core.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class WelcomeControllerTests {

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
    @DisplayName("Mock Mvc Welcome Home Page")
    @WithMockUser
    public void testHomePage() throws Exception {

        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andReturn();
    }

    @Test
    @DisplayName("HTML Unit Welcome Home Page")
    @WithMockUser
    public void testHomePage_htmlUnit() throws Exception {

        HtmlPage welcomePage = webClient.getPage("http://localhost/");

//        assertThat(welcomePage.getUrl().toString()).endsWith("/messages/123");

        String id = welcomePage.getTitleText();
        assertThat(id).isEqualTo("Welcome to the blincEventManager!");

        String summary = welcomePage.getHtmlElementById("chapterTitle").getTextContent();
        assertThat(summary).contains("Chapter 02.01: ");
    }

    //-------------------------------------------------------------------------


} // The End...
