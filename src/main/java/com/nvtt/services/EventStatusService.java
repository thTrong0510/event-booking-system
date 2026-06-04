package com.nvtt.services;

import java.util.Map;

import com.nvtt.pojo.EventStatus;
import java.util.List;

public interface EventStatusService {

    EventStatus getStatusByName(String name);

    EventStatus addEventStatus(Map<String, String> params);

    EventStatus updateEventStatus(Long id, Map<String, String> params);

    EventStatus getStatusById(Long id);

    List<EventStatus> getAllStatuses();

    EventStatus getByName(String name);
}
