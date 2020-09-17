package io.baselogic.springsecurity.web.model;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * RegistrationDto
 *
 * @since chapter03.00
 * @author mickknutson
 */
@Data
public class RegistrationDto {

    @NotEmpty(message="First Name is required")
    private String firstName;
    @NotEmpty(message="Last Name is required")
    private String lastName;
    @Email(message="Please provide a valid email address")
    @NotEmpty(message="Email is required")
    private String email;
    @NotEmpty(message="Password is required")
    private String password;

} // The End...
