/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import com.nvtt.pojo.Category;
import com.nvtt.pojo.Event;
import com.nvtt.pojo.EventStatus;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.admin.EventSearchCriteriaDTO;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vthan
 */
public interface EventRepository {

    List<Event> getEvents(Map<String, String> params, List<EventStatus> statuses);

    Event addEvent(Event event);

    Event getEventById(Long id);

    Event getEventById(Long id, List<EventStatus> statuses);

    Event getOwnEventById(Long id, Long organizerId);

    boolean deleteEvent(Event event);

    List<Event> getOrganizerEvents(Long organizerId, Map<String, String> params);

    // Truy vấn danh sách sự kiện kèm bộ lọc nâng cao
    List<Event> searchEvents(Integer statusId, Long categoryId, Date startDate, Date endDate, String organizerName);

    // Tìm nhanh danh sách danh mục và nhà tổ chức để nạp vào thẻ <select> bộ lọc
    List<Category> findAllCategories();

    List<User> findAllOrganizers();

    Event findById(Long id);

    void update(Event event);

    List<Event> searchEvents(EventSearchCriteriaDTO criteria);
    
    List<Event> findEventsWithDetailsByIds(List<Long> ids);
}
