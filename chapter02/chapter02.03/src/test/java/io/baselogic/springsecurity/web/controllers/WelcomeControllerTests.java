package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.User;
import io.baselogic.springsecurity.service.DefaultEventServiceTests;
import io.baselogic.springsecurity.service.EventService;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@Slf4j
public class WelcomeControllerTests {

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

//    @BeforeEach
//    void setup(WebApplicationContext context) {
//        webClient = MockMvcWebClientBuilder
//                // demonstrates applying a MockMvcConfigurer (Spring Security)
//                .webAppContextSetup(context, springSecurity())
//                // for illustration only - defaults to ""
//                .contextPath("")
//                // By default MockMvc is used for localhost only;
//                // the following will use MockMvc for example.com and example.org as well
//                // .useMockMvcForHosts("baselogic.io","baselogic.com")
//                .build();
//    }


    //-------------------------------------------------------------------------

    @Test
    @DisplayName("Mock Mvc Welcome Home Page")
    public void testHomePage() throws Exception {

        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isFound())
                .andReturn();
    }

    @Test
    @DisplayName("HTML Unit Welcome Home Page")
    public void testHomePage_htmlUnit() throws Exception {

        HtmlPage welcomePage = webClient.getPage("http://localhost/");

        String id = welcomePage.getTitleText();
        assertThat(id).isEqualTo("Welcome to the blincEventManager!");

        String chapterHeading = welcomePage.getHtmlElementById("chapterHeading").getTextContent();
        assertThat(chapterHeading).contains("Chapter 02.03");

        String chapterTitle = welcomePage.getHtmlElementById("chapterTitle").getTextContent();
        assertThat(chapterTitle).contains("Each chapter will have a slightly different summary depending on what has been done.");

        String summary = welcomePage.getHtmlElementById("summary").getTextContent();
        assertThat(summary).contains("Basic role based authorization.");
    }

    //-------------------------------------------------------------------------


} // The End...
