package com.nvtt.repositories;

import com.nvtt.pojo.Category;
import java.util.List;
import java.util.Map;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.EventStatus;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.admin.EventSearchCriteriaDTO;
import java.util.Date;

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

    List<Event> searchEvents(Integer statusId, Long categoryId, Date startDate, Date endDate, String organizerName);

    List<Category> findAllCategories();

    List<User> findAllOrganizers();

    Event findById(Long id);

    void update(Event event);

    List<Event> searchEvents(EventSearchCriteriaDTO criteria);

    List<Event> findEventsWithDetailsByIds(List<Long> ids);
}
