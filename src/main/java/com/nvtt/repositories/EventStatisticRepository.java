/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import com.nvtt.pojo.EventStatistic;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lequa
 */
public interface EventStatisticRepository {
    EventStatistic addEventStatistic(EventStatistic eventStatistic);
    List<EventStatistic> getEventStatistics(Map<String, String> params);
    EventStatistic getEventStatisticByEventId(Long eventId);
    EventStatistic updateEventStatistic(EventStatistic eventStatistic);
}
