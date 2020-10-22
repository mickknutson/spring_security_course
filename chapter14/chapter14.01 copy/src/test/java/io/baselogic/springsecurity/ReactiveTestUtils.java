package io.baselogic.springsecurity;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.EventUserDetails;
import io.baselogic.springsecurity.web.model.CommandDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

/**
 * Test Utilities
 * @author mickknutson
 *
 * @since chapter01.00
 * @since chapter03.04 added User and EventUserDetails
 */
public interface ReactiveTestUtils {


    public static final CommandDto TEST_RESPONSE = CommandDto.builder()
            .message("Welcome to the EventManager!")
            .summary("This is the 'Reactive Spring Security' Chapter")
            .description("Each chapter will have a slightly different summary depending on what has been done.")
            .build();


} // The End...
