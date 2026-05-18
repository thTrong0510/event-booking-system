/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import com.nvtt.pojo.EventStatistic;
import com.nvtt.repositories.EventStatisticRepository;
import com.nvtt.services.EventStatisticService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nvtt.utils.EventStatisticUtils.EventStatisticUtils;
import jdk.jshell.spi.ExecutionControl;

/**
 *
 * @author lequa
 */
@Service
@Transactional
public class EventStatisticServiceImpl implements EventStatisticService {

    @Autowired
    private EventStatisticRepository eventStatisticRepository;

    @Autowired
    private EventStatisticUtils eventStatisticUtils;

    @Override
    public EventStatistic addEventStatistic(Map<String, String> params) {
        try {
            EventStatistic eventStatistic = eventStatisticUtils.convertParamsToEventStatisticObject(params);
            return eventStatisticRepository.addEventStatistic(eventStatistic);
        } catch (Exception e) {
            throw new RuntimeException("Error in add event statistic: " + e.getMessage());
        }

    }

    @Override
    public List<EventStatistic> getEventStatistics(Map<String, String> params) {
        return eventStatisticRepository.getEventStatistics(params);
    }

    @Override
    public EventStatistic getEventStatisticByEventId(Long eventId) {
        return eventStatisticRepository.getEventStatisticByEventId(eventId);
    }

    @Override
    public EventStatistic updateEventStatistic(Long eventId, Map<String, String> params) {
        EventStatistic es = eventStatisticRepository.getEventStatisticByEventId(eventId);
        if (es == null) {
            throw new RuntimeException("Don't have any Event Statistic with this Event Id");
        } else {
            return eventStatisticRepository.addEventStatistic(es);
        }
    }

    @Override
    public void increaseViews(Long eventId, int views) {
        try {
            EventStatistic es = eventStatisticRepository.getEventStatisticByEventId(eventId);
            if (es == null) {
                throw new RuntimeException("Don't have any Event Statistic with this Event Id");
            } else {
                es.setTotalViews(es.getTotalViews() + views);
                eventStatisticRepository.updateEventStatistic(es);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in update views for Event Statistic: " + e.getMessage());
        }
    }
}
