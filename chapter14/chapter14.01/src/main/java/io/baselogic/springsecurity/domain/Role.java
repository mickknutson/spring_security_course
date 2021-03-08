package io.baselogic.springsecurity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
 * @author mickknutson
 * @since chapter05.01 Created for JPA
 * @since chapter05.02 Updated for MongoDB
 */
// Document Annotations:
@Document(collection="role")

// Lombok Annotations:
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Persistable<Integer>, Serializable {

    @Id
    private Integer id;
    private String name;

    private boolean persisted = false;


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