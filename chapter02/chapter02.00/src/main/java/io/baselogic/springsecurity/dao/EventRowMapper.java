package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * An Event implementation of {@link RowMapper}.
 *
 * @author mickknutson
 */
public class EventRowMapper implements RowMapper<Event> {


    private UserRowMapper ownerRowMapper;

    private UserRowMapper attendeeRowMapper;

    /**
     * Creates a new instance that takes an owner and attendee {@link UserRowMapper}
     *
     * @param ownerRowMapper
     * @param attendeeRowMapper
     */
    public EventRowMapper(UserRowMapper ownerRowMapper, UserRowMapper attendeeRowMapper) {
        this.ownerRowMapper = ownerRowMapper;
        this.attendeeRowMapper = attendeeRowMapper;
    }


    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {

        User owner = ownerRowMapper.mapRow(rs, rowNum);
        User attendee = attendeeRowMapper.mapRow(rs, rowNum);

        Event event = new Event();
        event.setId(rs.getInt("events.id"));
        event.setSummary(rs.getString("events.summary"));
        event.setDescription(rs.getString("events.description"));

        Calendar when = Calendar.getInstance();
        when.setTime(rs.getDate("events.event_date"));
        event.setWhen(when);
        event.setAttendee(attendee);
        event.setOwner(owner);
        return event;
    }

} // The End...
