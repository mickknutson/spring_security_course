package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.User;
import io.baselogic.springsecurity.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.List;

/**
 * A jdbc implementation of {@link EventDao}.
 *
 * @author mickknutson
 *
 */
@Repository
@Validated
public class JdbcEventDao implements EventDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private EventRowMapper eventRowMapper;

    @Autowired
    private String EVENT_QUERY;


    //-------------------------------------------------------------------------

    public JdbcEventDao(@NotNull NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //-------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public Event findById(@NotNull Integer eventId) {
        final String sql = EVENT_QUERY + " AND e.id = :id";

        SqlParameterSource parameter = new MapSqlParameterSource().addValue("id", eventId);

        return jdbcTemplate.queryForObject(sql, parameter, eventRowMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findByUser(@NotNull Integer userId) {
        final String sql = EVENT_QUERY + " and (e.owner = :id OR e.attendee = :id) order by e.id";

        SqlParameterSource parameter = new MapSqlParameterSource().addValue("id", userId);

        return jdbcTemplate.query(sql, parameter, eventRowMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAll() {
        return jdbcTemplate.query(EVENT_QUERY + " order by e.id", eventRowMapper);
    }


    @Override
    public Integer save(@NotNull @Valid final Event event) {
        if (event.getId() != null) {
            throw new IllegalArgumentException("event.getId() must be null when creating a new Message");
        }
        final User owner = event.getOwner();
        final User attendee = event.getAttendee();
        final Calendar when = event.getWhen();


        final String sql = "insert into events (event_date, summary, description, owner, attendee) values(:event_date, :summary, :description, :owner, :attendee)";

        KeyHolder holder = new GeneratedKeyHolder();

        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("event_date", new java.sql.Date(when.getTimeInMillis()))
                .addValue("summary", event.getSummary())
                .addValue("description", event.getDescription())
                .addValue("owner", owner.getId())
                .addValue("attendee", attendee.getId());

        jdbcTemplate.update(sql, parameter, holder);

        return holder.getKey().intValue();
    }

} // The End...
