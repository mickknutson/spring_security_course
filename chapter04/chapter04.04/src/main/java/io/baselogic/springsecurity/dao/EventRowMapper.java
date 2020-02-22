package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * An Event implementation of {@link RowMapper}.
 *
 * @since chapter01.00
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
    public EventRowMapper(final UserRowMapper ownerRowMapper, final UserRowMapper attendeeRowMapper) {
        this.ownerRowMapper = ownerRowMapper;
        this.attendeeRowMapper = attendeeRowMapper;
    }


    @Override
    public Event mapRow(final ResultSet rs, final int rowNum) throws SQLException {

        AppUser owner = ownerRowMapper.mapRow(rs, rowNum);
        AppUser attendee = attendeeRowMapper.mapRow(rs, rowNum);

        Calendar when = Calendar.getInstance();
        when.setTime(rs.getDate("events.event_date"));

        return Event.builder()
                .id(rs.getInt("events.id"))
                .summary(rs.getString("events.summary"))
                .description(rs.getString("events.description"))
                .when(when)
                .attendee(attendee)
                .owner(owner)
                .build();
    }

} // The End...
