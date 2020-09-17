package io.baselogic.springsecurity.web.controllers;

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
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Remember-Me Service tests to demonstrate testing with various users including:
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
 * @since chapter06.01 created
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class RememberMeTests {

    @Autowired
    private MockMvc mockMvc;

    private static final String USER = TestUtils.user1.getEmail();


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
    // Anonymous tests

    /**
     * Test Secured Page with {@link WithAnonymousUser} annotations mixed with {@link SecurityMockMvcResultMatchers}
     *
     * @throws Exception is the test fails unexpectedly.
     */
    @Test
    @DisplayName("Secured Page - anonymous - isUnauthorized")
    @WithAnonymousUser
    public void test_cookie_created() throws Exception {

        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
//                .andExpect(cookie().exists("overridden_remember_me"))
                .andExpect(unauthenticated())
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();

        log.info("***********************************************");
        log.info("*** {} Cookies ***", cookies.length);
        for(Cookie cookie: cookies){
            log.info("*** Cookie: {}", cookie);
        }
    }


    //-----------------------------------------------------------------------//
    // user1 Tests

    /**
     * Test Secured Page with {@link RequestPostProcessor} mixed with {@link SecurityMockMvcResultMatchers}
     *
     * @throws Exception is the test fails unexpectedly.
     */
    @Test
    @DisplayName("Form Login - authenticated - user1")
    public void test_cookie_created2() throws Exception {

        MvcResult result = mockMvc.perform(
                formLogin()
                        .user(TestUtils.user1UserDetails.getUsername())
                        .password(TestUtils.user1UserDetails.getPassword())
        )
                .andExpect(redirectedUrl("/default"))
                .andExpect(header().string("Location", endsWith("/default")))
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();

        log.info("***********************************************");
        log.info("*** {} Cookies ***", cookies.length);
        for(Cookie cookie: cookies){
            log.info("*** Cookie: {}", cookie);
        }

    }


} // The End...
