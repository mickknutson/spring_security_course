package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andReturn();
    }

    @Test
    @DisplayName("HTML Unit Welcome Home Page")
    public void testHomePage_htmlUnit() throws Exception {

        HtmlPage welcomePage = webClient.getPage("http://localhost/");

//        assertThat(welcomePage.getUrl().toString()).endsWith("/messages/123");

        String id = welcomePage.getTitleText();
        assertThat(id).isEqualTo("Welcome to the blincEventManager!");

        String summary = welcomePage.getHtmlElementById("chapterTitle").getTextContent();
        assertThat(summary).contains("Chapter 01.00: ");
    }

//    @Test
//    @DisplayName("MockMvc Welcome Home Page")
    public void testMessagePage() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(xpath("//title[@id='pageTitle']").exists()
//                .andExpect(xpath("//p[@id='pageTitle']").exists())
//                .andExpect(content().string("Chapter 01.00")
                );
    }


    //-------------------------------------------------------------------------


} // The End...
