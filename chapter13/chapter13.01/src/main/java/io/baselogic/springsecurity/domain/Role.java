package io.baselogic.springsecurity.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

/**
 * A {@link Role} is grouping of allowed Authorizations.
 *
 * @author Mick Knutson
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

//    public Role() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    private Set<AppUser> users;

} // The End...