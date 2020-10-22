package io.baselogic.springsecurity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.Set;

/**
 * A {@link Role} is grouping of allowed Authorizations.
 *
 * @author Mick Knutson
 */
// JPA Annotations:
@Table(value = "roles")

// Lombok Annotations:
@Data // Throws StackOverflowError
//@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

    @Id
    private Integer id;
    private String name;

//    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    @JsonBackReference
    private Set<AppUser> users;

} // The End...