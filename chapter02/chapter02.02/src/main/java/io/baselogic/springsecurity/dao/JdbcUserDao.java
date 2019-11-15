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
 *
 */
@Repository
@Validated
public class JdbcUserDao implements UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private UserRowMapper userRowMapper;

    private String USER_QUERY;

    @Autowired
    public JdbcUserDao(final @NotNull NamedParameterJdbcTemplate jdbcTemplate,
                       final UserRowMapper userRowMapper,
                       final @Qualifier("userQuery") String userQuery) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
        this.USER_QUERY = userQuery;
    }

    //-------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public User findById(final @NotNull Integer id) {
        final String sql = USER_QUERY + " id = :id";

        SqlParameterSource parameter = new MapSqlParameterSource().addValue("id", id);

        return jdbcTemplate.queryForObject(sql, parameter, userRowMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(final @NotEmpty String email) {
        try {

            final String sql = USER_QUERY + " email = :email";

            SqlParameterSource parameter = new MapSqlParameterSource().addValue("email", email);

            return jdbcTemplate.queryForObject(sql, parameter, userRowMapper);
        } catch (EmptyResultDataAccessException notFound) {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllByEmail(final @NotEmpty String email) {
        final String sql = USER_QUERY + " email LIKE '%"+ email +"%' ORDER BY id";

        return jdbcTemplate.query(sql, userRowMapper);
    }



    @Override
    public Integer save(final @NotNull User newUser) {
        if (newUser.getId() != null) {
            throw new IllegalArgumentException("newUser.getId() must be null when creating a "+ User.class.getName());
        }

        final String sql = "insert into users (email, password, first_name, last_name) values(:email, :psswd, :first_name, :last_name)";

        KeyHolder holder = new GeneratedKeyHolder();

        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("email", newUser.getEmail())
                .addValue("psswd", newUser.getPassword())
                .addValue("first_name", newUser.getFirstName())
                .addValue("last_name", newUser.getLastName());

        jdbcTemplate.update(sql, parameter, holder);

        return holder.getKey().intValue();
    }


} // The End...
