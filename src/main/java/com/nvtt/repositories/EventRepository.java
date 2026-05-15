package com.nvtt.repositories;

import java.util.List;
import java.util.Map;

import com.nvtt.pojo.Event;

/**
 *
 * @author lequa
 */
public interface EventRepository {
    List<Event> getEvent(Map<String, String> params);
    Event addEvent(Event event);
    Event getEventById(Long eventId);
    boolean deleteEvent(Event event);
    List<Event> getOrganizerEvent(Long organizerId, Map<String, String> params);
}
