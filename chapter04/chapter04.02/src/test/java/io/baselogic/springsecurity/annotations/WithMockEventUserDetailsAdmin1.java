package io.baselogic.springsecurity.annotations;

import io.baselogic.springsecurity.domain.EventUserDetails;
import org.springframework.security.core.context.SecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to set {@link EventUserDetails} to the {@link SecurityContext}
 */
@Retention(RetentionPolicy.RUNTIME)
@WithMockEventUserDetails(username = "admin1@baselogic.com", password = "admin1", name = "ADMIN1", roles = {"USER", "ADMIN"})
public @interface WithMockEventUserDetailsAdmin1 { }
