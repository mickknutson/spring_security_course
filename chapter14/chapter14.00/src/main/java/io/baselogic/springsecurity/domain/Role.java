package io.baselogic.springsecurity.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * A {@link Role} is grouping of allowed Authorizations.
 *
 * @author Mick Knutson
 */
// Document Annotations:
@Document(collection="role")

// Lombok Annotations:
@Data
@NoArgsConstructor
public class Role implements Persistable<Integer>, Serializable {

    @Id
    private Integer id;
    private String name;

    private Boolean persisted = Boolean.FALSE;


    @PersistenceConstructor
    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean isNew() {
        return !persisted;
    }



} // The End...