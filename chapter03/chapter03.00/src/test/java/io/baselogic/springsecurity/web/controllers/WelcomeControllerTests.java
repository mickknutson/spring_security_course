package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class WelcomeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // HtmlUnit uses the Rhino Engine
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
    public void testHomePage() throws Exception {
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("Welcome to the EventManager!");
        assertThat(content).contains("Chapter 02.06");
        assertThat(content).contains("Customizing behavior after login");

    }

    @Test
    @DisplayName("HTML Unit Welcome Home Page")
    public void testHomePage_htmlUnit() throws Exception {

        HtmlPage welcomePage = webClient.getPage("http://localhost/");

        String id = welcomePage.getTitleText();
        assertThat(id).isEqualTo("Welcome to the EventManager!");

        String chapterHeading = welcomePage.getHtmlElementById("chapterHeading").getTextContent();
        assertThat(chapterHeading).contains("Chapter 02.06");

        String chapterTitle = welcomePage.getHtmlElementById("chapterTitle").getTextContent();
        assertThat(chapterTitle).contains("Each chapter will have a slightly different summary depending on what has been done.");

        String summary = welcomePage.getHtmlElementById("summary").getTextContent();
        assertThat(summary).contains("Customizing behavior after login");
    }

    //-------------------------------------------------------------------------


} // The End...
