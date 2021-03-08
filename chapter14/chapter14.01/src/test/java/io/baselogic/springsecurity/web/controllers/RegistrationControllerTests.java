package io.baselogic.springsecurity.web.controllers;


import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * RegistrationControllerTests
 *
 * @since chapter03.00 Created
 * @since chapter14.01 Refactored for WebFlux
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class RegistrationControllerTests {

    private WebTestClient client;

    /**
     * Customize the WebClient
     *
     */
    @BeforeEach
    void beforeEachTest(ApplicationContext context) {
        this.client = WebTestClient
                .bindToApplicationContext(context)
                .configureClient()
                .build();
    }


    //-----------------------------------------------------------------------//
    // Registration Form
    //-----------------------------------------------------------------------//


    @Test
    @DisplayName("Show Registration Form - WithAnonymousUser")
    @WithAnonymousUser
    void showRegistrationForm() throws Exception {

//        EntityExchangeResult result = client.get()
//                .uri("/registration/form")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody().returnResult()
//                ;
//
//        log.info("*** Result: [{}]", result);
//        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);
//        log.info("*** Result body: [{}] **************************************************", body);
//
//        assertAll(
//                () -> assertThat(jsonPathContent(body, "$['firstName']")).isNull(),
//                () -> assertThat(jsonPathContent(body, "$['lastName']")).isNull(),
//                () -> assertThat(jsonPathContent(body, "$['email']")).isNull(),
//                () -> assertThat(jsonPathContent(body, "$['password']")).isNull()
//        );
    }




    //-----------------------------------------------------------------------//


    /*@Test
    @DisplayName("View Registration Form / DTO")
    void test_viewRegistrationForm() {

        EntityExchangeResult result = client.get()
                .uri("/registration/form")
                .exchange()
                .expectStatus().isOk()
                .expectBody().returnResult()
                ;

        log.info("*** Result: [{}]", result);
        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);
        log.info("*** Result body: [{}] **************************************************", body);

        assertAll(
                () -> assertThat(jsonPathContent(body, "$['firstName']")).isNull(),
                () -> assertThat(jsonPathContent(body, "$['lastName']")).isNull(),
                () -> assertThat(jsonPathContent(body, "$['email']")).isNull(),
                () -> assertThat(jsonPathContent(body, "$['password']")).isNull()
        );
    }*/




    //-----------------------------------------------------------------------//

//    @Test
//    @DisplayName("Submit Registration Form - Success")
//    void test_submitRegistration_Form() {
//
//        RegistrationDto user = ReactiveTestUtils.createRegistrationDto();
//
//
//        EntityExchangeResult result = client.post()
//                .uri("/registration/new")
//                .body(Mono.just(user), RegistrationDto.class)
//                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
//                .header(ACCEPT, APPLICATION_JSON_VALUE)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody().returnResult()
//                ;
//
//        log.info("*** Result: [{}]", result);
//        String body = new String(result.getResponseBodyContent(), StandardCharsets.UTF_8);
//        log.info("*** Result body: [{}] **************************************************", body);
//
//        assertAll(
//                () -> assertThat(jsonPathContent(body, "$['firstName']")).isEqualTo(""),
//                () -> assertThat(jsonPathContent(body, "$['lastName']")).isEqualTo(""),
//                () -> assertThat(jsonPathContent(body, "$['email']")).isEqualTo(""),
//                () -> assertThat(jsonPathContent(body, "$['password']")).isEqualTo("")
//        );
//
//    }

    //-----------------------------------------------------------------------//

    /*@Test
    @DisplayName("Submit Registration Form - null first name")
    void registrationForm__null__first_name() throws Exception {
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

    }*/


    //-----------------------------------------------------------------------//


} // The End...
