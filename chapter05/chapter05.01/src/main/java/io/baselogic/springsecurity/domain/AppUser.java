package io.baselogic.springsecurity.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.security.Principal;
import java.util.Set;

/**
 * {@link AppUser} is this applications notion of a user. It is good to use your own objects to interact with a
 * user especially in large applications. This ensures that as you evolve your security requirements (update Spring
 * Security, leverage new Spring Security modules, or even swap out security implementations) you can do so easily.
 */
// JPA Annotations:
@Entity
@Table(name = "app_users")

// Lombok Annotations:
@Data // Throws StackOverflowError
@Builder // NOTE: This does not work with JPA
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode

@AllArgsConstructor
@NoArgsConstructor
public class AppUser implements Principal, Serializable {

//    public AppUser() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    private static final long serialVersionUID = 8433999509932007961L;

    /**
     * Gets the full name in a formatted fashion. Note in a real application a formatter may be more appropriate, but in
     * this application simplicity is more important.
     *
     * @return AppUser email as their name.
     */
//    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getName() {
        return getEmail();
    }

} // The End...
