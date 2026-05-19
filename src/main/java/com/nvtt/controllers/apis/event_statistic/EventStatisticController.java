/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.apis.event_statistic;

import com.nvtt.pojo.EventStatistic;
import com.nvtt.pojo.dtos.event_statistic.ResEventStatisticDTO;
import com.nvtt.services.EventStatisticService;
import com.nvtt.utils.EventStatisticUtils.EventStatisticUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lequa
 */
@RestController
@RequestMapping("/api")
public class EventStatisticController {
    
    @Autowired
    private EventStatisticService eventStatisticService;
    
    @Autowired
    private EventStatisticUtils eventStatisticUtils;
    
    @GetMapping("/secure/event-statistics")
    public ResponseEntity<List<ResEventStatisticDTO>> getEventStatistics(@RequestParam Map<String, String> params) {
        try {
            List<EventStatistic> eventStatistics = eventStatisticService.getEventStatistics(params);
            List<ResEventStatisticDTO> dtos = eventStatisticUtils.convertToResEventStatisticDTOList(eventStatistics);
            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @GetMapping("/secure/event-statistics/by-month")
    public ResponseEntity<List<ResEventStatisticDTO>> getEventStatisticsByMonth(
            @RequestParam String month,
            @RequestParam(required = false) String year,
            @RequestParam Map<String, String> params) {
        try {
            Map<String, String> filterParams = new HashMap<>(params);
            filterParams.put("month", month);
            filterParams.remove("quarter");
            if (year != null && !year.isEmpty()) {
                filterParams.put("year", year);
            }
            
            List<EventStatistic> eventStatistics = eventStatisticService.getEventStatistics(filterParams);
            List<ResEventStatisticDTO> dtos = eventStatisticUtils.convertToResEventStatisticDTOList(eventStatistics);
            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @GetMapping("/secure/event-statistics/by-quarter")
    public ResponseEntity<List<ResEventStatisticDTO>> getEventStatisticsByQuarter(
            @RequestParam String quarter,
            @RequestParam(required = false) String year,
            @RequestParam Map<String, String> params) {
        try {
            Map<String, String> filterParams = new HashMap<>(params);
            filterParams.put("quarter", quarter);
            filterParams.remove("month");
            if (year != null && !year.isEmpty()) {
                filterParams.put("year", year);
            }
            
            List<EventStatistic> eventStatistics = eventStatisticService.getEventStatistics(filterParams);
            List<ResEventStatisticDTO> dtos = eventStatisticUtils.convertToResEventStatisticDTOList(eventStatistics);
            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @GetMapping("/secure/event-statistics/by-year")
    public ResponseEntity<List<ResEventStatisticDTO>> getEventStatisticsByYear(
            @RequestParam String year,
            @RequestParam Map<String, String> params) {
        try {
            Map<String, String> filterParams = new HashMap<>(params);
            filterParams.put("year", year);
            filterParams.remove("month");
            filterParams.remove("quarter");
            
            List<EventStatistic> eventStatistics = eventStatisticService.getEventStatistics(filterParams);
            List<ResEventStatisticDTO> dtos = eventStatisticUtils.convertToResEventStatisticDTOList(eventStatistics);
            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @GetMapping("/secure/event-statistics/{eventId}")
    public ResponseEntity<ResEventStatisticDTO> getEventStatisticByEventId(@PathVariable Long eventId) {
        try {
            EventStatistic eventStatistic = eventStatisticService.getEventStatisticByEventId(eventId);
            ResEventStatisticDTO dto = eventStatisticUtils.convertToResEventStatisticDTO(eventStatistic);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
