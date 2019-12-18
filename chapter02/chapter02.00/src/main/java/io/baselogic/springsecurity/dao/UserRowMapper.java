package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A User implementation of {@link RowMapper}.
 *
 * @author mickknutson
 *
 */
public class UserRowMapper implements RowMapper<AppUser> {

    private final String columnLabelPrefix;

    /**
     * Creates a new instance that allows for a custom prefix for the columnLabel.
     *
     * @param columnLabelPrefix
     */
    public UserRowMapper(String columnLabelPrefix) {
        this.columnLabelPrefix = columnLabelPrefix;
    }


    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {

        AppUser user = new AppUser();
        user.setId(rs.getInt(columnLabelPrefix + "id"));
        user.setEmail(rs.getString(columnLabelPrefix + "email"));
        user.setPassword(rs.getString(columnLabelPrefix + "password"));
        user.setFirstName(rs.getString(columnLabelPrefix + "first_name"));
        user.setLastName(rs.getString(columnLabelPrefix + "last_name"));
        return user;
    }

} // The End...
