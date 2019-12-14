package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * RegistrationControllerTests
 *
 * @since chapter03.00
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class RegistrationControllerTests {

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


    //-----------------------------------------------------------------------//
    // Registration Form
    //-----------------------------------------------------------------------//

    /**
     * Test the URI for creating a new Registration with MockMvc.
     */
    @Test
    @DisplayName("Show Registration Form - WithAnonymousUser")
    @WithAnonymousUser
    public void showRegistrationForm__WithUser() throws Exception {
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
    public void registerNewUser() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/registration/form");

        assertThat(page.getTitleText())
                .contains("User Registration");

        HtmlInput firstName = page.getHtmlElementById("firstName");
        firstName.setValueAttribute("Chuck");

        HtmlInput lastName = page.getHtmlElementById("lastName");
        lastName.setValueAttribute("Norris");

        HtmlInput email = page.getHtmlElementById("email");
        email.setValueAttribute("chuck@example.com");

        HtmlInput password = page.getHtmlElementById("password");
        password.setValueAttribute("some password");

        HtmlButton button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

//        assertThat(pageAfterClick.getTitleText())
//                .contains("Welcome to the EventManager!");

    }

    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("Submit Registration Form - null first name")
    @WithAnonymousUser
    public void registerNewUser__null__first_name() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/registration/form");

        assertThat(page.getTitleText())
                .contains("User Registration");

//        HtmlInput firstName = page.getHtmlElementById("firstName");
//        firstName.setValueAttribute("Chuck");

        HtmlInput lastName = page.getHtmlElementById("lastName");
        lastName.setValueAttribute("Norris");

        HtmlInput email = page.getHtmlElementById("email");
        email.setValueAttribute("chuck@example.com");

        HtmlInput password = page.getHtmlElementById("password");
        password.setValueAttribute("some password");

        HtmlButton button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("User Registration");

        if(log.isTraceEnabled()){
            log.info("***: {}", pageAfterClick.asXml());
        }

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("First Name is required");

    }


    @Test
    @DisplayName("Submit Registration Form - null last name")
    @WithAnonymousUser
    public void registerNewUser__null__last_name() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/registration/form");

        assertThat(page.getTitleText())
                .contains("User Registration");

        HtmlInput firstName = page.getHtmlElementById("firstName");
        firstName.setValueAttribute("Chuck");

//        HtmlInput lastName = page.getHtmlElementById("lastName");
//        lastName.setValueAttribute("Norris");

        HtmlInput email = page.getHtmlElementById("email");
        email.setValueAttribute(TestUtils.testUser1.getEmail());

        HtmlInput password = page.getHtmlElementById("password");
        password.setValueAttribute("some password");

        HtmlButton button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("User Registration");

        if(log.isTraceEnabled()){
            log.info("***: {}", pageAfterClick.asXml());
        }

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Last Name is required");

    }


    @Test
    @DisplayName("Submit Registration Form - null email")
    @WithAnonymousUser
    public void registerNewUser__null__email() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/registration/form");

        assertThat(page.getTitleText())
                .contains("User Registration");

        HtmlInput firstName = page.getHtmlElementById("firstName");
        firstName.setValueAttribute("Chuck");

        HtmlInput lastName = page.getHtmlElementById("lastName");
        lastName.setValueAttribute("Norris");

//        HtmlInput email = page.getHtmlElementById("email");
//        email.setValueAttribute(TestUtils.testTest1.getEmail());

        HtmlInput password = page.getHtmlElementById("password");
        password.setValueAttribute("some password");

        HtmlButton button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("User Registration");

        if(log.isTraceEnabled()){
            log.info("***: {}", pageAfterClick.asXml());
        }

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Email is required");

    }


    @Test
    @DisplayName("Submit Registration Form - duplicate email")
    @WithAnonymousUser
    public void registerNewUser__duplicate__email() throws Exception {
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
        password.setValueAttribute("some password");

        HtmlButton button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("User Registration");

        if(log.isTraceEnabled()){
            log.info("***: {}", pageAfterClick.asXml());
        }

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Email address is already in use.");

    }


    @Test
    @DisplayName("Submit Registration Form - null password")
    @WithAnonymousUser
    public void registerNewUser__null__password() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost/registration/form");

        assertThat(page.getTitleText())
                .contains("User Registration");

        HtmlInput firstName = page.getHtmlElementById("firstName");
        firstName.setValueAttribute("Chuck");

        HtmlInput lastName = page.getHtmlElementById("lastName");
        lastName.setValueAttribute("Norris");

        HtmlInput email = page.getHtmlElementById("email");
        email.setValueAttribute("test@example.com");

//        HtmlInput password = page.getHtmlElementById("password");
//        password.setValueAttribute("some password");

        HtmlButton button =  page.getHtmlElementById("submit");


        HtmlPage pageAfterClick = button.click();

        assertThat(pageAfterClick.getTitleText())
                .contains("User Registration");

        if(log.isTraceEnabled()){
            log.info("***: {}", pageAfterClick.asXml());
        }

        String errors = pageAfterClick.getHtmlElementById("fieldsErrors").getTextContent();
        assertThat(errors).contains("Password is required");

    }


    //-------------------------------------------------------------------------


} // The End...
