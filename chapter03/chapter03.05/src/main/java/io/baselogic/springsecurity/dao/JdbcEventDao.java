package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import org.springframework.beans.factory.annotation.Qualifier;
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
 */
@Repository
@Validated
public class JdbcEventDao implements EventDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final EventRowMapper eventRowMapper;

    private final String eventQuery;

    //-----------------------------------------------------------------------//

    public JdbcEventDao(final @NotNull NamedParameterJdbcTemplate jdbcTemplate,
                        final EventRowMapper eventRowMapper,
                        final @Qualifier("eventQuery") String eventQuery) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventRowMapper = eventRowMapper;
        this.eventQuery = eventQuery;
    }

    //-----------------------------------------------------------------------//

    @Override
    @Transactional(readOnly = true)
    public Event findById(final @NotNull Integer eventId) {
        final String sql = eventQuery + " AND e.id = :id";

        SqlParameterSource parameter = new MapSqlParameterSource().addValue("id", eventId);

        return jdbcTemplate.queryForObject(sql, parameter, eventRowMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findByUser(final @NotNull Integer userId) {
        final String sql = eventQuery + " and (e.owner = :id OR e.attendee = :id) order by e.id";

        SqlParameterSource parameter = new MapSqlParameterSource().addValue("id", userId);

        return jdbcTemplate.query(sql, parameter, eventRowMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAll() {
        return jdbcTemplate.query(eventQuery + " order by e.id", eventRowMapper);
    }

    private static final String EVENT_INSERT_QUERY = "insert into events (event_date, summary, description, owner, attendee) values(:event_date, :summary, :description, :owner, :attendee)";

    @Override
    public Integer save(final @NotNull @Valid Event event) {
        if (event.getId() != null) {
            throw new IllegalArgumentException("event.getId() must be null when creating a new Message");
        }
        final AppUser owner = event.getOwner();
        final AppUser attendee = event.getAttendee();
        final Calendar when = event.getWhen();


        KeyHolder holder = new GeneratedKeyHolder();

        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("event_date", new java.sql.Date(when.getTimeInMillis()))
                .addValue("summary", event.getSummary())
                .addValue("description", event.getDescription())
                .addValue("owner", owner.getId())
                .addValue("attendee", attendee.getId());

        jdbcTemplate.update(EVENT_INSERT_QUERY, parameter, holder);

        return holder.getKey().intValue();
    }

} // The End...
