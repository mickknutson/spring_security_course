package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

// Spring Test: -------------------------------------------------------------//
// Junit 5: -----------------------------------------------------------------//
// Assert-J: ----------------------------------------------------------------//
// --> assertThat(result.size()).isGreaterThan(0);
// http://joel-costigliola.github.io/assertj/index.html

/**
 * Functional and Mock tests for the EventController.
 */
@ExtendWith(SpringExtension.class)

//@WebMvcTest(ErrorController.class)
@AutoConfigureMockMvc

@SpringBootTest
@Slf4j
public class ErrorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private WebClient webClient;

    private static final String USER = "user";

    @BeforeEach
    void setup(WebApplicationContext context) {
    }



    //-----------------------------------------------------------------------//
    // exception_INTERNAL_SERVER_ERROR
    //-----------------------------------------------------------------------//

    /*
    @MockBean
    private UserVehicleService userVehicleService;

    @Test
    public void getVehicleShouldReturnMakeAndModel() {
        given(this.userVehicleService.getVehicleDetails("sboot"))
            .willReturn(new VehicleDetails("Honda", "Civic"));

        this.mvc.perform(get("/sboot/vehicle")
            .accept(MediaType.TEXT_PLAIN))
            .andExpect(status().isOk())
            .andExpect(content().string("Honda Civic"));
    }
    */

    /**
     * Testing the Error Controller throwing an HTTP 500 Exception.
     */
    @Test
    @DisplayName("exception_INTERNAL_SERVER_ERROR")
    public void exception_INTERNAL_SERVER_ERROR() throws Exception {



    }


    //-------------------------------------------------------------------------


} // The End...
