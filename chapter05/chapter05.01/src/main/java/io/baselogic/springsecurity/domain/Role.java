package io.baselogic.springsecurity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * A {@link Role} is grouping of allowed Authorizations.
 *
 * @author mickknutson
 * @since chapter05.01 Created for JPA
 * @since chapter05.02 Updated for MongoDB
 */
// JPA Annotations:
@Entity
@Table(name = "roles")

// Lombok Annotations:
//@Data // Throws StackOverflowError
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    @JsonBackReference
    private Set<AppUser> users;

} // The End...