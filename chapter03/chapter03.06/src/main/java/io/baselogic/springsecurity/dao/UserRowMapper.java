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

        AppUser appUser = new AppUser();
        appUser.setId(rs.getInt(columnLabelPrefix + "id"));
        appUser.setEmail(rs.getString(columnLabelPrefix + "email"));
        appUser.setPassword(rs.getString(columnLabelPrefix + "password"));
        appUser.setFirstName(rs.getString(columnLabelPrefix + "first_name"));
        appUser.setLastName(rs.getString(columnLabelPrefix + "last_name"));
        return appUser;
    }

} // The End...
