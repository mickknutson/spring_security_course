package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A jdbc implementation of {@link UserDao}.
 *
 * @author mickknutson
 * @since chapter01.00
 *
 */
@Repository
@Validated
public class JdbcUserDao implements UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final UserRowMapper userRowMapper;

    private final String userQuery;
    private final String userInsertQuery;

    @Autowired
    public JdbcUserDao(final @NotNull NamedParameterJdbcTemplate jdbcTemplate,
                       final UserRowMapper userRowMapper,
                       final @Qualifier("userQuery") String userQuery,
                       final @Qualifier("userInsertQuery") String userInsertQuery) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
        this.userQuery = userQuery;
        this.userInsertQuery = userInsertQuery;
    }

    //-----------------------------------------------------------------------//

    @Override
    @Transactional(readOnly = true)
    public AppUser findById(final @NotNull Integer id) {
        final String sql = userQuery + " id = :id";

        SqlParameterSource parameter = new MapSqlParameterSource().addValue("id", id);

        return jdbcTemplate.queryForObject(sql, parameter, userRowMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public AppUser findByEmail(final @NotEmpty String email) {
        try {

            final String sql = userQuery + " email = :email";

            SqlParameterSource parameter = new MapSqlParameterSource().addValue("email", email);

            return jdbcTemplate.queryForObject(sql, parameter, userRowMapper);
        } catch (EmptyResultDataAccessException notFound) {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppUser> findAllByEmail(final @NotEmpty String email) {
        final String sql = userQuery + " email LIKE '%"+ email +"%' ORDER BY id";

        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public Integer save(final @NotNull AppUser newAppUser) {
        if (newAppUser.getId() != null) {
            throw new IllegalArgumentException("newUser.getId() must be null when creating a "+ AppUser.class.getName());
        }

        KeyHolder holder = new GeneratedKeyHolder();

        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("email", newAppUser.getEmail())
                .addValue("psswd", newAppUser.getPassword())
                .addValue("first_name", newAppUser.getFirstName())
                .addValue("last_name", newAppUser.getLastName());

        jdbcTemplate.update(userInsertQuery, parameter, holder);

        return holder.getKey().intValue();
    }


} // The End...
