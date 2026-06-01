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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@RequestMapping("/api/v1")
public class EventStatisticController {
    
    private static final Logger logger = LogManager.getLogger(EventStatisticController.class);
    
    @Autowired
    private EventStatisticService eventStatisticService;
    
    @Autowired
    private EventStatisticUtils eventStatisticUtils;
    
    @GetMapping("/me/event-statistics")
    public ResponseEntity<List<ResEventStatisticDTO>> getEventStatistics(@RequestParam Map<String, String> params) {
        logger.info("start sql getEventStatistics");
        List<EventStatistic> eventStatistics = eventStatisticService.getEventStatistics(params);
        List<ResEventStatisticDTO> dtos = eventStatisticUtils.convertToResEventStatisticDTOList(eventStatistics);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
    
    @GetMapping("/me/event-statistics/by-month")
    public ResponseEntity<List<ResEventStatisticDTO>> getEventStatisticsByMonth(
            @RequestParam String month,
            @RequestParam(required = false) String year,
            @RequestParam Map<String, String> params) {
            logger.info("start sql getEventStaticticsByMonth");
            Map<String, String> filterParams = new HashMap<>(params);
            filterParams.put("month", month);
            filterParams.remove("quarter");
            if (year != null && !year.isEmpty()) {
                filterParams.put("year", year);
            }
            
            List<EventStatistic> eventStatistics = eventStatisticService.getEventStatistics(filterParams);
            List<ResEventStatisticDTO> dtos = eventStatisticUtils.convertToResEventStatisticDTOList(eventStatistics);
            logger.info("end sql");
            return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
    
    @GetMapping("/me/event-statistics/by-quarter")
    public ResponseEntity<List<ResEventStatisticDTO>> getEventStatisticsByQuarter(
            @RequestParam String quarter,
            @RequestParam(required = false) String year,
            @RequestParam Map<String, String> params) {
            logger.info("start sql getEventStatisticByQuarter");
            Map<String, String> filterParams = new HashMap<>(params);
            filterParams.put("quarter", quarter);
            filterParams.remove("month");
            if (year != null && !year.isEmpty()) {
                filterParams.put("year", year);
            }
            
            List<EventStatistic> eventStatistics = eventStatisticService.getEventStatistics(filterParams);
            List<ResEventStatisticDTO> dtos = eventStatisticUtils.convertToResEventStatisticDTOList(eventStatistics);
            logger.info("end sql");
            return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
    
    @GetMapping("/me/event-statistics/by-year")
    public ResponseEntity<List<ResEventStatisticDTO>> getEventStatisticsByYear(
            @RequestParam String year,
            @RequestParam Map<String, String> params) {
            Map<String, String> filterParams = new HashMap<>(params);
            filterParams.put("year", year);
            filterParams.remove("month");
            filterParams.remove("quarter");
            logger.info("start sql getEventStatisticsByYear");
            List<EventStatistic> eventStatistics = eventStatisticService.getEventStatistics(filterParams);
            List<ResEventStatisticDTO> dtos = eventStatisticUtils.convertToResEventStatisticDTOList(eventStatistics);
            logger.info("end sql");
            return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
    
    @GetMapping("/me/event-statistics/{eventId}")
    public ResponseEntity<ResEventStatisticDTO> getEventStatisticByEventId(@PathVariable Long eventId) {
            logger.info("start sql getEventStatisticByEventId");
            EventStatistic eventStatistic = eventStatisticService.getEventStatisticByEventId(eventId);
            ResEventStatisticDTO dto = eventStatisticUtils.convertToResEventStatisticDTO(eventStatistic);
            logger.info("end sql");
            return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
