package io.baselogic.springsecurity.web.controllers;

import io.baselogic.springsecurity.annotations.WithMockAdmin1;
import io.baselogic.springsecurity.annotations.WithMockUser1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller to handle after-login behavior
 *
 * @author mickknutson
 *
 * @since chapter02.06 Created class
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class DefaultControllerTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Customize the WebClient to work with HtmlUnit
     *
     * @param context WebApplicationContext
     */
    @BeforeEach
    void setup(WebApplicationContext context) {

        // https://docs.spring.io/spring-security/site/docs/current/reference/html5/#test-mockmvc-setup
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Default Controller - user1")
    @WithMockUser1
    public void defaultRedirect__user1() throws Exception {
        MvcResult result = mockMvc.perform(get("/default"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"))
                .andExpect(header().string("Location", "/"))
                .andReturn();

    }

    /**
     * This test does not fail if the @@WithMockAdmin1 is used.
     * Nor does it fail if this is used:
     * [authorities = {"ADMIN"}]
     * or
     * [roles = {"ADMIN"}]
     * or
     * [roles = {"USER", "ADMIN"}]
     *
     * @throws Exception if MockMvc throws exception
     */
    @Test
    @DisplayName("Default Controller - admin1 - ADMIN role only")
    @WithMockUser(username = "admin1@baselogic.com", password = "admin1", roles = {"ADMIN"})
//    @WithMockAdmin1
    public void defaultRedirect__admin1() throws Exception {
        MvcResult result = mockMvc.perform(get("/default"))
                .andExpect(status().isForbidden())
                .andReturn();

    }

    @Test
    @DisplayName("Default Controller - admin1 - ADMIN and USER role")
    @WithMockAdmin1
    public void defaultRedirect__admin1_roles() throws Exception {
        MvcResult result = mockMvc.perform(get("/default"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/events/"))
                .andExpect(header().string("Location", "/events/"))
                .andReturn();

    }

    //-----------------------------------------------------------------------//


} // The End...
