package io.baselogic.springsecurity.web.controllers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsAdmin1;
import io.baselogic.springsecurity.annotations.WithMockEventUserDetailsUser1;
import io.baselogic.springsecurity.dao.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URL;
import java.util.List;
import java.util.Set;

import com.gargoylesoftware.htmlunit.javascript.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.ModelAndViewAssert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AdvancedAuthorizationTests
 *
 * @author mickknutson
 * @since chapter12.01 Created
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class SessionManagementTests {

    @Autowired
    private MockMvc mockMvc;

    // HtmlUnit uses the Rhino Engine
    private WebClient webClient;

    @LocalServerPort
    private int serverPort;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

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
                .addFilters(springSecurityFilterChain)
                .apply(springSecurity())
//                .apply(sharedHttpSession()) // use this session across requests
                .build();

        webClient = MockMvcWebClientBuilder
                .webAppContextSetup(context)
                .build();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.setJavaScriptErrorListener(new SilentJavaScriptErrorListener());
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(true);
    }


    //-----------------------------------------------------------------------//



    //-----------------------------------------------------------------------//

//        @Test
        @DisplayName("HTML Unit Welcome Home Page")
        public void testHomePage_htmlUnit() throws Exception {

            HtmlPage page = webClient.getPage("http://localhost:"+serverPort+"/login/form");

            log.info("*** Page URI: {}", page.getBaseURI());
            log.info("*** serverPort: {}", serverPort);
            if(log.isTraceEnabled()){
                log.info("***: {}", page.asXml());
            }

            final HtmlForm form = page.getFormByName("login");
            log.info("*** getActionAttribute: {}",form.getActionAttribute());

            HtmlInput username = page.getHtmlElementById("username");
            username.setValueAttribute("user1");

            HtmlInput password = page.getHtmlElementById("password");
            password.setValueAttribute("user1");

            HtmlButton submit =  page.getHtmlElementById("submit");
//            HtmlPage pageAfterClick = submit.click();
//
//            Set<Cookie> cookies = webClient.getCookies(new URL("http://localhost:"+serverPort));
//            log.info("*** cookies: {}", cookies);

//            String titleText = pageAfterClick.getTitleText();
//            assertThat(titleText).contains("Create Event");

        }

//    @Test
    @DisplayName("All Events Page - authenticated - admin1")
    @WithMockEventUserDetailsAdmin1
    public void test_AllEventsPage_admin1_authenticated() throws Exception {

        MvcResult result = mockMvc.perform(get("/events/"))

                .andExpect(status().isOk())
                .andExpect(authenticated().withRoles("USER","ADMIN"))
                .andExpect(view().name("events/list"))
                .andExpect(cookie().exists("JSESSIONID"))
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

    @Test
    public void login_With_CorrectCredentials() throws Exception {
        /*MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "user1@baselogic.com")
                .param("password", "user1")
        )
//                .andExpect(status().isOk())
                .andExpect(authenticated().withRoles("USER"))
//                .andExpect(cookie().exists("JSESSIONID"))
                .andReturn()
        ;*/

//        RequestBuilder requestBuilder = formLogin().user("user1@baselogic.com").password("user1");
//        MvcResult result = mockMvc.perform(requestBuilder)
//                .andDo(print())
//                .andExpect(cookie().exists("JSESSIONID"))
//                .andReturn()
//                ;
//
//        log.info("*** cookies: {}", result.getResponse().getCookies());
    }



} // The End...
