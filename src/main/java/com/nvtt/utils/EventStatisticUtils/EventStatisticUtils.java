/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.utils.EventStatisticUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.nvtt.pojo.EventStatistic;
import com.nvtt.pojo.dtos.event_statistic.ResEventStatisticDTO;
import com.nvtt.repositories.EventStatisticRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lequa
 */
@Component
public class EventStatisticUtils {

    @Autowired
    private EventStatisticRepository eventStatisticRepository;
   
    public EventStatistic convertParamsToEventStatisticObject(Map<String, String> params){
        try {
            EventStatistic es = new EventStatistic();
            if (params.get("eventId") != null){
                es.setEventId(Long.valueOf(params.get("eventId")));
            }
            if (params.get("totalViews") != null){
                es.setTotalViews(Integer.valueOf(params.get("totalViews")));
            }
            if (params.get("totalRevenue") != null){
                es.setTotalRevenue(new BigDecimal(params.get("totalRevenue")));
            }
            if (params.get("totalTicketsSold") != null){
                es.setTotalTicketsSold(Integer.valueOf(params.get("totalTicketsSold")));
            }
            return es;
        } catch (Exception e) {
            throw new RuntimeException("Error in convert params to Event Statistic Object" + e.getMessage());
        }
    }

    public EventStatistic addParamsToEventStatisticObject(EventStatistic es, Map<String, String> params){
        try {
            if (params.get("totalViews") != null){
                es.setTotalViews(Integer.valueOf(params.get("totalViews")));
            }
            if (params.get("totalRevenue") != null){
                es.setTotalRevenue(new BigDecimal(params.get("totalRevenue")));
            }
            if (params.get("totalTicketsSold") != null){
                es.setTotalTicketsSold(Integer.valueOf(params.get("totalTicketsSold")));
            }
            return es;
        } catch (Exception e) {
            throw new RuntimeException("Error in convert params to Event Statistic Object" + e.getMessage());
        }
    }
    
    public ResEventStatisticDTO convertToResEventStatisticDTO(EventStatistic eventStatistic) {
        ResEventStatisticDTO dto = new ResEventStatisticDTO();
        dto.setEventId(eventStatistic.getEventId());
        dto.setTotalTicketsSold(getIntValue(eventStatistic.getTotalTicketsSold()));
        dto.setTotalRevenue(eventStatistic.getTotalRevenue());
        dto.setTotalViews(getIntValue(eventStatistic.getTotalViews()));
        dto.setLastUpdated(eventStatistic.getLastUpdated());
        dto.setCreatedAt(eventStatistic.getCreatedAt());
        return dto;
    }
    
    public List<ResEventStatisticDTO> convertToResEventStatisticDTOList(List<EventStatistic> eventStatistics) {
        List<ResEventStatisticDTO> dtos = new ArrayList<>();
        for (EventStatistic eventStatistic : eventStatistics) {
            dtos.add(this.convertToResEventStatisticDTO(eventStatistic));
        }
        return dtos;
    }
    
    private int getIntValue(Integer value) {
        return value == null ? 0 : value;
    }
}
