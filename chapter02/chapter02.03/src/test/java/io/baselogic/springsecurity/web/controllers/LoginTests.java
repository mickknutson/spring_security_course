package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.baselogic.springsecurity.service.DefaultEventServiceTests;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
@Slf4j
public class LoginTests {

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

    //-----------------------------------------------------------------------//
    // User Login
    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Custom Login Page")
    public void login() throws Exception {

        MvcResult result = mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andReturn();
    }

    @Test
    @DisplayName("Custom Login Page - AnonymousUser")
    @WithAnonymousUser
    public void login_With_AnonymousUser() throws Exception {

        MvcResult result = mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andReturn();
    }

    @Test
    @DisplayName("Custom Login Page - user1")
    @WithMockUser("user1@example.com")
    public void login_with_user1() throws Exception {

        MvcResult result = mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andReturn();
    }

    @Test
    @DisplayName("Custom Login Page - user1")
    @WithMockUser(username="user1@example.com", roles={"USER"})
    public void login_with_user1_and_roles() throws Exception {

        MvcResult result = mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andReturn();
    }

    @Test
    @DisplayName("Custom Login Page - admin1")
    @WithMockUser("admin1@example.com")
    public void login_admin1() throws Exception {

        MvcResult result = mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andReturn();
    }

    @Test
    @DisplayName("Custom Login Page - admin1")
    @WithMockUser(username="admin1@example.com", roles={"USER","ADMIN"})
    public void login_admin1_and_roles() throws Exception {

        MvcResult result = mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andReturn();
    }

    //-----------------------------------------------------------------------//
    // Login Variation
    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Custom Login Page - with error param")
    public void login_with_error_param() throws Exception {

        MvcResult result = mockMvc.perform(get("/login")
                .param("error", "testError")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andReturn();
    }

    @Test
    @DisplayName("Custom Login Page - with error param")
    public void login_with_null_error_param() throws Exception {
        String value = null;

        MvcResult result = mockMvc.perform(get("/login")
                .param("error", value)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andReturn();
    }


    @Test
    @DisplayName("Custom Login Page - with logout param")
    public void login_with_logout_param() throws Exception {

        MvcResult result = mockMvc.perform(get("/login")
                .param("logout", "logout")
        )
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    @DisplayName("Custom Login Page - with logout param")
    public void login_with_null_logout_param() throws Exception {

        String value = null;

        MvcResult result = mockMvc.perform(get("/login")
                .param("logout", value)
        )
                .andExpect(status().isOk())
                .andReturn();
    }


    //-------------------------------------------------------------------------


} // The End...
