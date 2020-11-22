package io.baselogic.springsecurity.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.web.model.RegistrationDto;
import io.florianlopes.spring.test.web.servlet.request.MockMvcRequestBuilderUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * RegistrationControllerTests
 *
 * @since chapter03.00
 */

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
class RegistrationControllerTests {

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
    void beforeEachTest(WebApplicationContext context) {
        webClient = MockMvcWebClientBuilder
                .webAppContextSetup(context)
                .build();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(true);
    }


    //-----------------------------------------------------------------------//
    // Registration Form
    //-----------------------------------------------------------------------//

    /**
     * Test the URI for creating a new Registration with MockMvc.
     */
    @Test
    @DisplayName("Show Registration Form - WithAnonymousUser")
    @WithAnonymousUser
    void showRegistrationForm__WithUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/registration/form")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("registration/register"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("User Registration");
    }

    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Submit Registration Form")
    @WithAnonymousUser
    void registrationForm() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/registration/form");

        assertThat(page.getTitleText())
                .contains("User Registration");

        HtmlInput firstName = page.getHtmlElementById("firstName");
        firstName.setValueAttribute("Chuck");

        HtmlInput lastName = page.getHtmlElementById("lastName");
        lastName.setValueAttribute("Norris");

        HtmlInput email = page.getHtmlElementById("email");
        email.setValueAttribute("chuck@baselogic.com");

        HtmlInput password = page.getHtmlElementById("password");
        password.setValueAttribute("chucknorris");

        HtmlButton button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("Welcome to the EventManager!");

//        assertThat(pageAfterClick.getTitleText())
//                .contains("TODO we will implement registration later in the chapter");
    }

    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Submit Registration Form - null first name")
    @WithAnonymousUser
    void registrationFormt__null__first_name() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/registration/form");

        assertThat(page.getTitleText())
                .contains("User Registration");

//        HtmlInput firstName = page.getHtmlElementById("firstName");
//        firstName.setValueAttribute("Chuck");

        HtmlInput lastName = page.getHtmlElementById("lastName");
        lastName.setValueAttribute("Norris");

        HtmlInput email = page.getHtmlElementById("email");
        email.setValueAttribute("chuck@baselogic.com");

        HtmlInput password = page.getHtmlElementById("password");
        password.setValueAttribute("chucknorris");

        HtmlButton button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("User Registration");

        if(log.isTraceEnabled()){
            log.debug("***: {}", pageAfterClick.asXml());
        }

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("First Name is required");

    }


    @Test
    @DisplayName("Submit Registration Form - null last name")
    @WithAnonymousUser
    void registrationForm__null__last_name() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/registration/form");

        assertThat(page.getTitleText())
                .contains("User Registration");

        HtmlInput firstName = page.getHtmlElementById("firstName");
        firstName.setValueAttribute("Chuck");

//        HtmlInput lastName = page.getHtmlElementById("lastName");
//        lastName.setValueAttribute("Norris");

        HtmlInput email = page.getHtmlElementById("email");
        email.setValueAttribute(TestUtils.TEST_APP_USER_1.getEmail());

        HtmlInput password = page.getHtmlElementById("password");
        password.setValueAttribute("chucknorris");

        HtmlButton button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("User Registration");

        if(log.isTraceEnabled()){
            log.debug("***: {}", pageAfterClick.asXml());
        }

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Last Name is required");

    }


    @Test
    @DisplayName("Submit Registration Form - null email")
    @WithAnonymousUser
    void registrationForm__null__email() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/registration/form");

        assertThat(page.getTitleText())
                .contains("User Registration");

        HtmlInput firstName = page.getHtmlElementById("firstName");
        firstName.setValueAttribute("Chuck");

        HtmlInput lastName = page.getHtmlElementById("lastName");
        lastName.setValueAttribute("Norris");

//        HtmlInput email = page.getHtmlElementById("email");
//        email.setValueAttribute("chuck@baselogic.com");

        HtmlInput password = page.getHtmlElementById("password");
        password.setValueAttribute("chucknorris");

        HtmlButton button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("User Registration");

        if(log.isTraceEnabled()){
            log.debug("***: {}", pageAfterClick.asXml());
        }

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Email is required");

    }


    @Test
    @DisplayName("Submit Registration Form - duplicate email")
    @WithAnonymousUser
    void registrationForm__duplicate__email() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/registration/form");

        assertThat(page.getTitleText())
                .contains("User Registration");

        HtmlInput firstName = page.getHtmlElementById("firstName");
        firstName.setValueAttribute("Chuck");

        HtmlInput lastName = page.getHtmlElementById("lastName");
        lastName.setValueAttribute("Norris");

        HtmlInput email = page.getHtmlElementById("email");
        email.setValueAttribute(TestUtils.user1.getEmail());

        HtmlInput password = page.getHtmlElementById("password");
        password.setValueAttribute("chucknorris");

        HtmlButton button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("User Registration");

        if(log.isTraceEnabled()){
            log.debug("***: {}", pageAfterClick.asXml());
        }

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Email address is already in use.");

    }


    @Test
    @DisplayName("Submit Registration Form - null password")
    @WithAnonymousUser
    void registrationForm__null__password() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/registration/form");

        assertThat(page.getTitleText())
                .contains("User Registration");

        HtmlInput firstName = page.getHtmlElementById("firstName");
        firstName.setValueAttribute("Chuck");

        HtmlInput lastName = page.getHtmlElementById("lastName");
        lastName.setValueAttribute("Norris");

        HtmlInput email = page.getHtmlElementById("email");
        email.setValueAttribute("chuck@baselogic.com");

//        HtmlInput password = page.getHtmlElementById("password");
//        password.setValueAttribute("chucknorris");

        HtmlButton button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("User Registration");

        if(log.isTraceEnabled()){
            log.debug("***: {}", pageAfterClick.asXml());
        }

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Password is required");

    }


    //-----------------------------------------------------------------------//

   @Test
   @WithAnonymousUser
   @DisplayName("Test register/new by itself")
    void registrationTest() throws Exception{
         RegistrationDto registrationDto = new RegistrationDto();
         registrationDto.setFirstName("Helen");
         registrationDto.setLastName("Ma");
         registrationDto.setEmail("helenma@baselogic.com");
         registrationDto.setPassword("helen");

         mockMvc.perform(MockMvcRequestBuilderUtils.postForm("/registration/new",registrationDto))
                                 .andExpect(status().is3xxRedirection())
                                 .andExpect(redirectedUrl("/"))
                                 .andExpect(flash()
                                         .attribute("message", "Registration Successful. Account created for 'helenma@baselogic.com'."));
         }

    @Test
    @WithAnonymousUser
    @DisplayName("Test register/new with invalid data")
    void registrationTest_notValidated() throws Exception{
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setFirstName(" ");
        registrationDto.setLastName(" ");
        registrationDto.setEmail(" ");
        registrationDto.setPassword(" ");

        mockMvc.perform(MockMvcRequestBuilderUtils.postForm("/registration/new",registrationDto))
                .andExpect(status().isOk())
                .andExpect(view().name("registration/register"));

    }

    @Test
    @WithAnonymousUser
    @DisplayName("Test register/new with an existing user email. It will not register this new user.")
    void registrationTest_emailInUse() throws Exception{
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setFirstName("helen");
        registrationDto.setLastName("ma");
        registrationDto.setEmail("user1@baselogic.com");
        registrationDto.setPassword("helen");

        mockMvc.perform(MockMvcRequestBuilderUtils.postForm("/registration/new",registrationDto))
                .andExpect(status().isOk())
                .andExpect(view().name("registration/register"));
     }

} // The End...
