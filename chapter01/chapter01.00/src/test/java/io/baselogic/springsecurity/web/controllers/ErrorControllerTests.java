package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;



/**
 * Functional and Mock tests for the EventController.
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc

@SpringBootTest
@Slf4j
public class ErrorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private WebClient webClient;

    private static final String USER = "user";

    @Autowired
    private ErrorController controller;

    @BeforeEach
    void setup(WebApplicationContext context) {
    }



    //-----------------------------------------------------------------------//
    // exception_INTERNAL_SERVER_ERROR
    //-----------------------------------------------------------------------//

    /**
     * Testing the Error Controller throwing an HTTP 500 Exception.
     */
    @Test
    @DisplayName("exception_INTERNAL_SERVER_ERROR")
    public void handleInternalServerError() throws Exception {

        Throwable throwable = new RuntimeException("Foo Bar Exception");
        ModelAndView mav = new ModelAndView("error", "error", "Foo Bar Exception");

        ModelAndView response = controller.handleInternalServerError(throwable);

        assertThat(response.getViewName()).isEqualTo("error");
        assertThat((String)response.getModel().get("error"))
                .contains("<h2>Unknown error</h2><br />Exception during execution of SpringSecurity application:<br />Foo Bar Exception<br />");
    }

    @Test
    @DisplayName("exception_INTERNAL_SERVER_ERROR with null exception")
    public void exception_INTERNAL_SERVER_ERROR__null_exception() throws Exception {

        ModelAndView response = controller.handleInternalServerError(null);

        assertThat(response.getViewName()).isEqualTo("error");
        assertThat((String)response.getModel().get("error"))
                .contains("<h2>Unknown error</h2><br />Exception during execution of SpringSecurity application:<br />");
    }


    //-------------------------------------------------------------------------


} // The End...
