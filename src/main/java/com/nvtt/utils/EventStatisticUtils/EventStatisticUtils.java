/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.utils.EventStatisticUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.nvtt.pojo.EventStatistic;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author lequa
 */
@Component
public class EventStatisticUtils {
   
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
    
}
