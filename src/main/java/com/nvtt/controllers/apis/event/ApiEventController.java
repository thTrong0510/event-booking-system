/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.apis.event;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.EventStatistic;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.event.EventCompareResponseDTO;
import com.nvtt.pojo.dtos.event.ResEventInfoDTO;
import com.nvtt.services.EventService;
import com.nvtt.services.EventStatisticService;
import com.nvtt.services.UserService;
import com.nvtt.utils.EventUtils.EventUtils;
import com.nvtt.utils.exceptions.IdInvalidException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author lequa
 */
@RestController
@RequestMapping("/api")
public class ApiEventController {

    private static final Logger logger = LogManager.getLogger(ApiEventController.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventUtils eventUtils;

    @Autowired
    private EventStatisticService eventStatisticService;

    @GetMapping("/events")
    public ResponseEntity<List<ResEventInfoDTO>> getEvents(@RequestParam Map<String, String> params) {
        logger.info("start sql getEvents");
        List<Event> events = eventService.getPublicEvents(params);
        List<ResEventInfoDTO> resEventInfoDTOs = this.eventUtils.convertEventsToDTOs(events);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.OK).body(resEventInfoDTOs);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<ResEventInfoDTO> getEventById(@PathVariable Long id) {
        logger.info("start sql getEventById");
        Event event = eventService.getPublicEventById(id);
        EventStatistic statistic = this.eventStatisticService.getStatisticByEventId(event.getId());
        ResEventInfoDTO dto = eventUtils.convertToResEventInfoDTO(event, statistic);
        eventStatisticService.increaseViews(event.getId(), 1, statistic);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("/secure/organizer/events")
    public ResponseEntity<List<ResEventInfoDTO>> getOrganizerEvents(@RequestParam Map<String, String> params) {
        logger.info("start sql getOrganizerEvents");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            List<Event> events = eventService.getOrganizerEvents(params);
            List<ResEventInfoDTO> resEventInfoDTOs = this.eventUtils.convertEventsToDTOs(events);
            logger.info("end sql");
            return ResponseEntity.status(HttpStatus.OK).body(resEventInfoDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    @GetMapping("/secure/organizer/events/{id}")
    public ResponseEntity<ResEventInfoDTO> getOwnEventById(@PathVariable Long id) {
        logger.info("start sql getOwnEventById");
        Event event = eventService.getOwnEventById(id);
        EventStatistic statistic = this.eventStatisticService.getEventStatisticByEventId(event.getId());
        ResEventInfoDTO dto = eventUtils.convertToResEventInfoDTO(event, statistic);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping("/secure/organizer/events")
    public ResponseEntity<ResEventInfoDTO> addEvent(@RequestParam Map<String, String> params, @RequestParam("images") Optional<Set<MultipartFile>> images,
            @RequestParam("videos") Optional<Set<MultipartFile>> videos) {
        logger.info("start sql addEvent");
        Event addedEvent = eventService.addEvent(params, images, videos);
        EventStatistic statistic = this.eventStatisticService.getEventStatisticByEventId(addedEvent.getId());
        ResEventInfoDTO dto = eventUtils.convertToResEventInfoDTO(addedEvent, statistic);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/secure/organizer/events/{id}")
    public ResponseEntity<ResEventInfoDTO> updateEvent(@PathVariable Long id, @RequestParam Map<String, String> params,
            @RequestParam("newImages") Optional<Set<MultipartFile>> newImages,
            @RequestParam("newVideos") Optional<Set<MultipartFile>> newVideos,
            @RequestParam("deletedMediaUrls") Optional<Set<String>> deletedMediaUrls) {
        logger.info("start sql update event");
        Event updatedEvent = eventService.updateEvent(id, params, newImages, newVideos, deletedMediaUrls);
        EventStatistic statistic = this.eventStatisticService.getEventStatisticByEventId(updatedEvent.getId());
        ResEventInfoDTO dto = eventUtils.convertToResEventInfoDTO(updatedEvent, statistic);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping("/secure/organizer/launch-event")
    public ResponseEntity<Void> launchEvent(@RequestParam Long eventId) {
        logger.info("start sql lauchEvent");
        eventService.launchEvent(eventId);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/secure/organizer/end-event")
    public ResponseEntity<Void> endEvent(@RequestParam Long eventId) {
        eventService.endEvent(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/secure/organizer/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        logger.info("start sql deleteEvent");
        eventService.deleteEvent(id);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/events/compare")
    public ResponseEntity<List<EventCompareResponseDTO>> compareEvents(@RequestParam("ids") List<Long> ids) throws IdInvalidException {
        logger.info("start sql compareEvent");
        if (ids == null || ids.size() < 2 || ids.size() > 3) {
            throw new IdInvalidException("Vui lòng cung cấp từ 2 đến 3 ID sự kiện để so sánh.");
        }

        List<EventCompareResponseDTO> compareResult = eventService.getEventsForComparison(ids);
        logger.info("end sql");
        return ResponseEntity.ok(compareResult);
    }
}
