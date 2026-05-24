/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;
import com.nvtt.pojo.EventStatistic;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lequa
 */
public interface EventStatisticService {
    EventStatistic addEventStatistic(Map<String, String> params);
    List<EventStatistic> getEventStatistics(Map<String, String> params);
    EventStatistic getEventStatisticByEventId(Long eventId); 
    EventStatistic updateEventStatistic(Long id, Map<String, String> params);
    void increaseViews(Long eventId, int views);
}
