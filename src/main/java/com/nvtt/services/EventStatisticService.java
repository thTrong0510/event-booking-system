package com.nvtt.services;

import com.nvtt.pojo.EventStatistic;
import java.util.List;
import java.util.Map;

public interface EventStatisticService {

    EventStatistic addEventStatistic(Map<String, String> params);

    List<EventStatistic> getEventStatistics(Map<String, String> params);

    EventStatistic getEventStatisticByEventId(Long eventId);

    EventStatistic updateEventStatistic(Long id, Map<String, String> params);

    void increaseViews(Long eventId, int views, EventStatistic es);

    public EventStatistic getStatisticByEventId(Long eventId);
}
