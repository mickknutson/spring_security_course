package io.baselogic.springsecurity.web.authentication;

import io.baselogic.springsecurity.authentication.DomainUsernamePasswordAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


/**
 * An extension to the existing {@link UsernamePasswordAuthenticationFilter} that
 * obtains a domain parameter and then creates a {@link DomainUsernamePasswordAuthenticationToken}.
 *
 * @author mickknutson
 * @since chapter03.06 Created Class
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public final class DomainUsernamePasswordAuthenticationFilterTests {

    @Autowired
    private DomainUsernamePasswordAuthenticationFilter filter;

//    @Autowired
//    private AuthenticationManager authenticationManager;

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setup(WebApplicationContext context) {

        // https://docs.spring.io/spring-security/site/docs/current/reference/html5/#test-mockmvc-setup
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }



    @Test
    @DisplayName("attempt valid Authentication")
    public void attemptAuthentication_valid() throws ServletException, IOException{

        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.POST.name(), "/login");
        request.addParameter("username", "user1");
        request.addParameter("password", "user1");
        request.addParameter("domain", "baselogic.com");

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain filterChain = (filterRequest, filterResponse) -> {
            assertThat(response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)).isNull();
            assertThat(response.getHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS)).isNull();
        };

        log.info("*** Execute DomainUsernamePasswordAuthenticationFilter.doFilter(request, response, filterChain)");
        filter.doFilter(request, response, filterChain);

        response.getHeaderNames().forEach(h ->{
            log.info("* Header: [{}], value: [{}]", h, response.getHeader(h));
        });

        assertThat(response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)).isNull();
    }


    @Test
    @DisplayName("attempt Authentication with a non-POST HTTP method")
    public void attemptAuthentication_non_POST_method() throws ServletException, IOException{

        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), "/login");
        request.addParameter("username", "user1");
        request.addParameter("password", "user1");
        request.addParameter("domain", "baselogic.com");

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain filterChain = (filterRequest, filterResponse) -> {
            assertThat(response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)).isNull();
            assertThat(response.getHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS)).isNull();
        };

        log.info("*** Execute DomainUsernamePasswordAuthenticationFilter.doFilter(request, response, filterChain)");
        filter.doFilter(request, response, filterChain);

        response.getHeaderNames().forEach(h ->{
            log.info("* Header: [{}], value: [{}]", h, response.getHeader(h));
        });

//        assertThat(response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)).isNull();
    }

} // The End...