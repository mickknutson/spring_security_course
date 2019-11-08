package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.baselogic.springsecurity.service.DefaultEventServiceTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Spring Test: -------------------------------------------------------------//
// Junit 5: -----------------------------------------------------------------//
// Assert-J: ----------------------------------------------------------------//
// --> assertThat(result.size()).isGreaterThan(0);
// http://joel-costigliola.github.io/assertj/index.html


//@ExtendWith(SpringExtension.class)
//@AutoConfigureMockMvc
//@SpringBootTest
@Slf4j
public class LoginWebDriverTests {

    private WebDriver driver;

    @BeforeEach
    void setup(WebApplicationContext context) {
        driver = MockMvcHtmlUnitDriverBuilder
                .webAppContextSetup(context)
                .build();
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

//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("index"))
//                .andReturn();
    }



    //-------------------------------------------------------------------------


} // The End...
