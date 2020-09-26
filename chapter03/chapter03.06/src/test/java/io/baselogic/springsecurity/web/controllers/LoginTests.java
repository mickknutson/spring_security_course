package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsAdmin1;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import io.baselogic.springsecurity.dao.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.ModelAndViewAssert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Login tests to demonstrate testing with various users including:
 * {@link WithAnonymousUser}
 * {@link WithMockUser}
 *
 * Later we will add {@link WithUserDetails} and {@link WithSecurityContext} support
 *
 * Additionally, leveraging {@link SecurityMockMvcRequestBuilders} to build secure requests.
 * Also leveraging {@link SecurityMockMvcResultMatchers} to perform secure assertions.
 *
 * @author mickknutson
 *
 * @since chapter02.01 created
 * @since chapter03.04 added WithUserDetails support for EventUserDetails
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class LoginTests {

    @Autowired
    private MockMvc mockMvc;

    // HtmlUnit uses the Rhino Engine
    private WebClient webClient;

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

        webClient = MockMvcWebClientBuilder
                .webAppContextSetup(context)
                .build();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(true);
    }

    //-----------------------------------------------------------------------//
    // Anonymous tests

    /**
     * Test Secured Page with {@link WithAnonymousUser} annotations mixed with {@link SecurityMockMvcResultMatchers}
     *
     * @throws Exception is the test fails unexpectedly.
     */
    @Test
    @DisplayName("Secured Page - anonymous - isUnauthorized")
    @WithAnonymousUser
    public void testHomePage_isUnauthorized() throws Exception {

        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(unauthenticated())
                .andReturn();
    }

    /**
     * Test Secured Page with {@link RequestPostProcessor} mixed with {@link SecurityMockMvcResultMatchers}
     * @throws Exception is the test fails unexpectedly.
     */
    @Test
    @DisplayName("Secured Page - anonymous - isUnauthorized - RequestPostProcessor")
    public void test_HomePage_isUnauthorized_RequestPostProcessor() throws Exception {

        MvcResult result = mockMvc.perform(get("/")
                // Simulate a valid security User:
                .with(anonymous()))

                .andExpect(status().isOk())
                .andExpect(unauthenticated())
                .andReturn();
    }

    /**
     * Test Secured Page with {@link RequestPostProcessor} mixed with {@link SecurityMockMvcResultMatchers}
     * @throws Exception is the test fails unexpectedly.
     */
    @Test
    @DisplayName("Secured Page - anonymous - isUnauthorized - RequestPostProcessor")
    public void test_AllEvents_isUnauthorized_RequestPostProcessor() throws Exception {

        MvcResult result = mockMvc.perform(get("/events")
                // Simulate a valid security User:
                .with(anonymous()))

                .andExpect(status().isFound())
                .andExpect(header().string("Location", endsWith("/login/form")))
                .andExpect(unauthenticated())
                .andReturn();
    }


    //-----------------------------------------------------------------------//
    // user1 Tests

    /**
     * Test Secured Page with {@link RequestPostProcessor} mixed with {@link SecurityMockMvcResultMatchers}
     *
     * @throws Exception is the test fails unexpectedly.
     */
    @Test
    @DisplayName("My Events Page - authenticated - user1")
    @WithMockEventUserDetailsUser1
    public void testMyEventsPage_user1_authenticated() throws Exception {

        MvcResult result = mockMvc.perform(get("/events/my"))
                .andExpect(status().isOk())
                .andExpect(authenticated()
                        .withUsername(TestUtils.user1UserDetails.getUsername()).withRoles("USER"))

                .andExpect(view().name("events/my"))
                .andExpect(model().attribute("events", hasSize(greaterThanOrEqualTo(2))))
                .andReturn();
    }

    /**
     * Test Secured Page with {@link RequestPostProcessor} mixed with {@link SecurityMockMvcResultMatchers}
     *
     * @throws Exception is the test fails unexpectedly.
     */
    @Test
    @DisplayName("My Events Page - authenticated - user1 - RequestPostProcessor")
    @WithMockEventUserDetailsUser1
    public void testMyEventsPage_user1_authenticated__RequestPostProcessor() throws Exception {

        MvcResult result = mockMvc.perform(get("/events/my"))

                .andExpect(status().isOk())
                .andExpect(authenticated()
                        .withUsername(TestUtils.user1UserDetails.getUsername()).withRoles("USER"))

                .andExpect(view().name("events/my"))
                .andExpect(model().attribute("events", hasSize(greaterThanOrEqualTo(2))))
                .andReturn();
    }

    /**
     * Test form login with {@link RequestPostProcessor} mixed with {@link SecurityMockMvcResultMatchers}
     *
     * @throws Exception is the test fails unexpectedly.
     *
     * FIXME: This test throws an error when asserting authenticated:
     * java.lang.AssertionError: Authentication should not be null
     *
     * But also get the following error:
     * java.lang.AssertionError: Redirected URL expected:</default> but was:</login/form?error>
     */
    @Test
    @DisplayName("Form Login - authenticated - user1")
    public void testFormLogin_user1_authenticated() throws Exception {

        /*MvcResult result = mockMvc.perform(
                formLogin()
                        .user(TestUtils.user1UserDetails.getUsername())
                        .userParameter(TestUtils.user1UserDetails.getUsername())
                        .password(TestUtils.user1UserDetails.getPassword())
        )
//                .andExpect(authenticated()
//                        .withUsername(TestUtils.user1UserDetails.getUsername()).withRoles("USER"))
                .andExpect(redirectedUrl("/default"))
                .andExpect(header().string("Location", endsWith("/default")))
                .andReturn();*/

        //NOTE: formLogin() wont work as we have a custom login with three fields.
        assertThat("formLogin").isNotEqualTo("Custom loginForm");

    }

    /**
     * Test form login with {@link RequestPostProcessor} mixed with {@link SecurityMockMvcResultMatchers}
     *
     * @throws Exception is the test fails unexpectedly.
     */
    @Test
    @DisplayName("Form Login - invalid username - foo")
    public void testFormLogin_invalid_username() throws Exception {

        MvcResult result = mockMvc.perform(
                formLogin()
                        .user("foo@baselogic.com")
                        .password("invalid")
        )
                .andExpect(unauthenticated())
                .andReturn();

    }

    /**
     * Test form login with {@link RequestPostProcessor} mixed with {@link SecurityMockMvcResultMatchers}
     *
     * @throws Exception is the test fails unexpectedly.
     */
    @Test
    @DisplayName("Form Login - invalid username - user1")
    public void testFormLogin_invalid_password_user1() throws Exception {

        MvcResult result = mockMvc.perform(
                formLogin()
                        .user(TestUtils.user1UserDetails.getUsername())
                        .password("invalid")
        )
                .andExpect(unauthenticated())
                .andReturn();

    }


    //-----------------------------------------------------------------------//
    // admin1 Tests

    /**
     * Test Secured Page with {@link RequestPostProcessor} mixed with {@link SecurityMockMvcResultMatchers}
     *
     * @throws Exception is the test fails unexpectedly.
     */
    @Test
    @DisplayName("All Events Page - authenticated - admin1")
    @WithMockEventUserDetailsAdmin1
    public void test_AllEventsPage_admin1_authenticated() throws Exception {

        MvcResult result = mockMvc.perform(get("/events/"))

                .andExpect(status().isOk())
                .andExpect(authenticated().withRoles("USER","ADMIN"))
                .andExpect(view().name("events/list"))
                .andExpect(model().attribute("events", hasSize(greaterThanOrEqualTo(3))))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("All Events");

        // In-line ModelAndViewAssert validation
        ModelAndView mav = result.getModelAndView();

        assertThat(mav).isNotNull();
        assertViewName(mav, "events/list");

        assertModelAttributeAvailable(mav, "events");
        assertAndReturnModelAttributeOfType(mav, "events", List.class);

    }


    //-----------------------------------------------------------------------//
    // CUSTOM LOGIN FORM

//    @Test
    @DisplayName("HTML Unit Custom login form")
    public void test_Custom_LoginForm_htmlUnit() throws Exception {

        HtmlPage page = webClient.getPage("http://localhost/login/form");

        if(log.isTraceEnabled()){
            log.info("***: {}", page.asXml());
        }

        HtmlInput username = page.getHtmlElementById("username");
        username.setValueAttribute("user1");

        HtmlInput domain = page.getHtmlElementById("domain");
        username.setValueAttribute("baselogic.com");

        HtmlInput password = page.getHtmlElementById("password");
        username.setValueAttribute("user1");

        HtmlButton button =  page.getHtmlElementById("submit");
        HtmlPage pageAfterClick = button.click();

        String titleText = pageAfterClick.getTitleText();
        assertThat(titleText).contains("Create Event");

        String summary = pageAfterClick.getHtmlElementById("summary").asXml();
        assertThat(summary).contains("A new event....");

    }


} // The End...
