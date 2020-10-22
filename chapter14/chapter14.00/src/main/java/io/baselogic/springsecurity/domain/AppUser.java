package io.baselogic.springsecurity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link AppUser} is this applications notion of a user. It is good to use your own objects to interact with a
 * user especially in large applications. This ensures that as you evolve your security requirements (update Spring
 * Security, leverage new Spring Security modules, or even swap out security implementations) you can do so easily.
 */
// Document Annotations:
@Document(collection="app_users")

// Lombok Annotations:
@Data
//@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUser implements Persistable<Integer>, Serializable {

    @Id
    private Integer id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private Boolean persisted = Boolean.FALSE;


    @DBRef(lazy = false)
    private Set<Role> roles = new HashSet<>(5);

    @PersistenceConstructor
    public AppUser(Integer id, String email, String password, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean isNew() {
        return !persisted;
    }


    // --- convenience methods ----------------------------------------------//

    /**
     * Gets the full name in a formatted fashion. Note in a real application a formatter may be more appropriate, but in
     * this application simplicity is more important.
     *
     * @return AppUser email as their name.
     */
    @JsonIgnore
    @Transient
    public String getName() {
        return getEmail();
    }

    @JsonIgnore
    @Transient
    public void addRole(Role role){
        this.roles.add(role);
    }

    private static final long serialVersionUID = 8433999509932007961L;


} // The End...
