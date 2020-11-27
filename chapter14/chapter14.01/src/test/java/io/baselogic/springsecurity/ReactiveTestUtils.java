package io.baselogic.springsecurity;

import com.jayway.jsonpath.JsonPath;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.web.model.RegistrationDto;
import org.hamcrest.Matcher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Test Utilities
 *
 * @author mickknutson
 *
 * @since chapter01.00
 * @since chapter03.04 added User and EventUserDetails
 */
public interface ReactiveTestUtils {

    //-----------------------------------------------------------------------//

    static RegistrationDto createRegistrationDto(){
        
        return RegistrationDto.builder()
                .firstName("Test")
                .lastName("User")
                .email("test@baselogic.com")
                .password("password")
                .build();
    }


    //-----------------------------------------------------------------------//
    // Events

    static Mono<Event> createEmptyEventMono(){
        return Mono.empty();
    }

    static Mono<Event> createMono(Event event){
        return Mono.just(event);
    }

    static Flux<Event> createEmptyEventFlux(){
        return Flux.empty();
    }

    static Flux<Event> createFlux(Event ...event){
        return Flux.fromArray(event);
    }



    //-----------------------------------------------------------------------//
    // AppUsers

    static Mono<AppUser> createEmptyAppUserMono(){
        return Mono.empty();
    }

    static Mono<AppUser> createMono(AppUser user){
        return Mono.just(user);
    }

    static Flux<AppUser> createEmptyAppUserFlux(){
        return Flux.empty();
    }

    static Flux<AppUser> createFlux(AppUser ...user){
        return Flux.fromArray(user);
    }

    //-----------------------------------------------------------------------//
    // AppUsers

    static Mono<UserDetails> createEmptyUserDetailsMono(){
        return Mono.empty();
    }

    static Mono<UserDetails> createUserDetailsMono(UserDetails user){
        return Mono.just(user);
    }




    //-----------------------------------------------------------------------//
    /**
     * Standard JsonPath expressions:
     */
    //String messagePath = "$['tool']['jsonpath']['creator']['location'][*]";
    public static final String MESSAGE_PATH = "$['message']";
    public static final String SUMMARY_PATH = "$['summary']";
    public static final String DESCRIPTION_PATH = "$['description']";
    public static final String TIMESTAMP_PATH = "$['timestamp']";

//    public static final String TIMESTAMP_PATH = "$['timestamp']";


    //-----------------------------------------------------------------------//

//    public static JsonPathResultMatchers jsonPath(String expression, Object... args) {
//        return new JsonPathResultMatchers(expression, args);
//    }
//
//    public static JsonPathResultMatchers(String expression, Object... args) {
//        this.jsonPathHelper = new JsonPathExpectationsHelper(expression, args);
//    }

    static <T> Boolean jsonPath(String content, String expression, Matcher<? super T> matcher){
        return true;
    }

    static String jsonPathContent(String content, String expression){
        return JsonPath.parse(content).read(expression);
    }

    static String jsonPathContent(byte[] bytes, String expression){
        return JsonPath
                .parse(new String(bytes, StandardCharsets.UTF_8))
                .read(expression);
    }

    static String jsonPathContent(EntityExchangeResult result, String expression){
        return jsonPathContent(result.getResponseBodyContent(), expression);

    }



} // The End...
