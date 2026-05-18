package com.nvtt.repositories;

import java.util.List;
import java.util.Map;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.EventStatus;

/**
 *
 * @author lequa
 */
public interface EventRepository {
    List<Event> getPublicEvents(Map<String, String> params);
    Event addEvent(Event event);
    Event getEventById(Long eventId);
    boolean deleteEvent(Event event);
    List<Event> getOrganizerEvents(Long organizerId, Map<String, String> params);
}
