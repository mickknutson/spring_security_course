package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.User;
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

    @Autowired
    public JdbcUserDao(final @NotNull NamedParameterJdbcTemplate jdbcTemplate,
                       final UserRowMapper userRowMapper,
                       final @Qualifier("userQuery") String userQuery) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
        this.userQuery = userQuery;
    }

    //-------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public User findById(final @NotNull Integer id) {
        final String sql = userQuery + " id = :id";

        SqlParameterSource parameter = new MapSqlParameterSource().addValue("id", id);

        return jdbcTemplate.queryForObject(sql, parameter, userRowMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(final @NotEmpty String email) {
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
    public List<User> findAllByEmail(final @NotEmpty String email) {
        final String sql = userQuery + " email LIKE '%"+ email +"%' ORDER BY id";

        return jdbcTemplate.query(sql, userRowMapper);
    }

    private static final String USER_INSERT_QUERY = "insert into users (email, password, first_name, last_name) values(:email, :psswd, :first_name, :last_name)";


    @Override
    public Integer save(final @NotNull User newUser) {
        if (newUser.getId() != null) {
            throw new IllegalArgumentException("newUser.getId() must be null when creating a "+ User.class.getName());
        }

        KeyHolder holder = new GeneratedKeyHolder();

        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("email", newUser.getEmail())
                .addValue("psswd", newUser.getPassword())
                .addValue("first_name", newUser.getFirstName())
                .addValue("last_name", newUser.getLastName());

        jdbcTemplate.update(USER_INSERT_QUERY, parameter, holder);

        return holder.getKey().intValue();
    }


} // The End...
