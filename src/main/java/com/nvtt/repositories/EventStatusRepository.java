package com.nvtt.repositories;

import com.nvtt.pojo.EventStatus;
import java.util.List;

public interface EventStatusRepository {

    EventStatus getStatusByName(String name);

    EventStatus addEventStatus(EventStatus eventStatus);

    EventStatus getStatusById(Long id);

    List<EventStatus> findAll();

    EventStatus findByName(String name);

    List<EventStatus> findByNameIn(List<String> names);
}
