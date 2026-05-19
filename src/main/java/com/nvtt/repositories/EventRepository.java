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
    List<Event> getEvents(Map<String, String> params, List<EventStatus> statuses);
    Event addEvent(Event event);
    Event getEventById(Long id);
    Event getEventById(Long id, List<EventStatus> statuses);
    Event getOwnEventById(Long id, Long organizerId);
    boolean deleteEvent(Event event);
    List<Event> getOrganizerEvents(Long organizerId, Map<String, String> params);
}
