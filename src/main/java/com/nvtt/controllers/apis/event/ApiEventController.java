/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.apis.event;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.nvtt.pojo.EventMedia;
import com.nvtt.pojo.Role;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.event.ResEventInfoDTO;
import com.nvtt.pojo.dtos.event.ResEventMediaDTO;
import com.nvtt.services.EventService;
import com.nvtt.services.UserService;
import com.nvtt.utils.EventUtils.EventUtils;

import jakarta.data.repository.Delete;

/**
 *
 * @author lequa
 */
@RestController
@RequestMapping("/api")
public class ApiEventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private EventUtils eventUtils;

    @GetMapping("/events")
    public ResponseEntity<Set<ResEventInfoDTO>> getEvents(@RequestParam Map<String, String> params) {
        try {
            List<Event> events = eventService.getEvent(params);
            Set<ResEventInfoDTO> resEventInfoDTOs = events.stream()
                    .map(event -> eventUtils.convertToResEventInfoDTO(event))
                    .collect(Collectors.toSet());
            return ResponseEntity.status(HttpStatus.OK).body(resEventInfoDTOs);
        } catch (Exception e) {
            System.err.println("Error fetching all events: " + e.getMessage());
            throw new RuntimeException("Error fetching all events", e);
        }
    }
    
    @GetMapping("/events/{id}")
    public ResponseEntity<ResEventInfoDTO> getEventById(@PathVariable Long id) {
        try {
            Event event = eventService.getEventById(id);
            ResEventInfoDTO dto = eventUtils.convertToResEventInfoDTO(event);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (Exception e) {
            System.err.println("Error fetching events: " + e.getMessage());
            throw new RuntimeException("Error fetching events", e);
        }
    }
    
    @GetMapping("/secure/organizer/events")
    public ResponseEntity<Set<ResEventInfoDTO>> getOrganizerEvents(@RequestParam Map<String, String> params) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                User u = userService.getUserByEmail(authentication.getName());
                Map<String, Long> organizerId = Map.of("organizerId", u.getId());
                List<Event> events = eventService.getOrganizerEvent(params);
                Set<ResEventInfoDTO> resEventInfoDTOs = events.stream()
                    .map(event -> eventUtils.convertToResEventInfoDTO(event))
                    .collect(Collectors.toSet());
                return ResponseEntity.status(HttpStatus.OK).body(resEventInfoDTOs);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }
        } catch (Exception e) {
            System.err.println("Error fetching all events: " + e.getMessage());
            throw new RuntimeException("Error fetching all events", e);
        }
    }

    @PostMapping("/secure/organizer/events")
    public ResponseEntity<ResEventInfoDTO> addEvent(@RequestParam Map<String, String> params,@RequestParam("images") Optional<Set<MultipartFile>> images, 
        @RequestParam("videos") Optional<Set<MultipartFile>> videos) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                String email = authentication.getName();
                User user = userService.getUserByEmail(email);
                Role userRole = user.getRole();
                if (userRole == null || !userRole.getName().equals("ORGANIZER")) {
                    throw new RuntimeException("Unauthorized: User does not have ORGANIZER role");
                }
            }
            Event addedEvent = eventService.addEvent(params, images, videos);
            return ResponseEntity.status(HttpStatus.CREATED).body(eventUtils.convertToResEventInfoDTO(addedEvent));
        } catch (Exception e) {
            System.err.println("Error adding event: " + e.getMessage());
            throw new RuntimeException(e.getMessage().toString(), e);
        }
    }

    @PutMapping("/secure/organizer/events/{id}")
    public ResponseEntity<ResEventInfoDTO> updateEvent(@PathVariable Long id, @RequestParam Map<String, String> params,
            @RequestParam("newImages") Optional<Set<MultipartFile>> newImages, 
            @RequestParam("newVideos") Optional<Set<MultipartFile>> newVideos,
            @RequestParam("deletedMediaUrls") Optional<Set<String>> deletedMediaUrls) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                String email = authentication.getName();
                User user = userService.getUserByEmail(email);
                Event event = eventService.getEventById(id);
                if (user.getId() != event.getOrganizer().getId() || !user.getRole().getName().equals("ORGANIZER")) {
                    throw new RuntimeException("Unauthorized: User is not the organizer of this event");
                }
            }
            Event updatedEvent = eventService.updateEvent(id, params, newImages, newVideos, deletedMediaUrls);
            return ResponseEntity.status(HttpStatus.OK).body(eventUtils.convertToResEventInfoDTO(updatedEvent));
        } catch (Exception e) {
            System.err.println("Error updating event: " + e.getMessage());
            throw new RuntimeException(e.getMessage().toString(), e);
        }
    }

    @DeleteMapping("/secure/organizer/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                String email = authentication.getName();
                User user = userService.getUserByEmail(email);
                Event event = eventService.getEventById(id);
                if (user.getId() != event.getOrganizer().getId() || !user.getRole().getName().equals("ORGANIZER")) {
                    throw new RuntimeException("Unauthorized: User is not the organizer of this event");
                }
            }
            eventService.deleteEvent(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            System.err.println("Error deleting event: " + e.getMessage());
            throw new RuntimeException(e.getMessage().toString(), e);
        }
    }
}
