package io.baselogic.springsecurity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.security.Principal;

/**
 * {@link AppUser} is this applications notion of a user. It is good to use your own objects to interact with a
 * user especially in large applications. This ensures that as you evolve your security requirements (update Spring
 * Security, leverage new Spring Security modules, or even swap out security implementations) you can do so easily.
 */
@Data
public class AppUser implements Principal, Serializable {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private static final long serialVersionUID = 8433999509932007961L;

    /**
     * Gets the full name in a formatted fashion. Note in a real application a formatter may be more appropriate, but in
     * this application simplicity is more important.
     *
     * @return AppUser email as their name.
     */
    @JsonIgnore

    public String getName() {
        return getEmail();
    }

} // The End...
