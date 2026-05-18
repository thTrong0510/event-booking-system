/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nvtt.pojo.Event;
import com.nvtt.pojo.EventMedia;
import com.nvtt.pojo.Role;
import com.nvtt.repositories.EventRepository;
import com.nvtt.services.EventService;
import com.nvtt.services.UserService;
import com.nvtt.services.EventStatisticService;
import com.nvtt.services.EventStatusService;
import com.nvtt.utils.EventUtils.EventUtils;
import com.nvtt.utils.UserUtils.UserUtils;
import com.nvtt.pojo.User;
import com.nvtt.pojo.EventStatus;
import com.nvtt.utils.EventStatusUtils.EventStatusUtils;
import java.util.HashMap;

/**
 *
 * @author lequa
 */
@Service
@Transactional
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserService userService;

    @Autowired
    private EventUtils eventUtils;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private EventStatusService eventStatusService;
    
    @Autowired
    private EventStatisticService eventStatisticService;
    
    @Autowired
    private EventStatusUtils eventStatusUtils;

    @Override
    public List<Event> getPublicEvents(Map<String, String> params) {
        List<EventStatus> publicStatuses = eventStatusUtils.eventStatusPublic();
        return eventRepository.getPublicEvents(params);
    }

    @Override
    public List<Event> getOrganizerEvents(Map<String, String> params) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                User u = userService.getUserByEmail(authentication.getName());
                return eventRepository.getOrganizerEvents(u.getId(), params);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.getEventById(id);
    }

    @Override
    public Event addEvent(Map<String, String> params, Optional<Set<MultipartFile>> images, Optional<Set<MultipartFile>> videos) {
        try {
            User organizer = userUtils.getCurrentUser();
            if (organizer == null) {
                throw new RuntimeException("Unauthorized: User not authenticated");
            }
            if (!isOrganizer()) {
                throw new RuntimeException("Unauthorized: User does not have ORGANIZER role");
            }
            Event event = eventUtils.convertParamsToEventObject(params);
            event.setAvailableTickets(event.getTotalTickets());
            event.setEventMedias(new HashSet<>());

            if (!images.isEmpty()) {
                for (MultipartFile image : images.get()) {
                    try {
                        System.err.println("Uploading image: " + image.getOriginalFilename());
                        Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                        String imageUrl = uploadResult.get("secure_url").toString();
                        System.err.println("Uploaded image URL: " + imageUrl);
                        EventMedia media = new EventMedia();
                        media.setEvent(event);
                        media.setMediaType("IMAGE");
                        media.setMediaUrl(imageUrl);
                        event.getEventMedias().add(media);
                        for (EventMedia a : event.getEventMedias()) {
                            System.err.println("-----Media type: " + a.getMediaType() + ", URL: " + a.getMediaUrl());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!videos.isEmpty()) {
                for (MultipartFile video : videos.get()) {
                    try {
                        Map uploadResult = cloudinary.uploader().upload(video.getBytes(), ObjectUtils.emptyMap());
                        String videoUrl = uploadResult.get("secure_url").toString();
                        EventMedia media = new EventMedia();
                        media.setEvent(event);
                        media.setMediaType("VIDEO");
                        media.setMediaUrl(videoUrl);
                        event.getEventMedias().add(media);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return eventRepository.addEvent(event);
        } catch (Exception e) {
            System.err.println("Error adding event: " + e.getMessage());
            throw new RuntimeException("Error adding event", e);
        }
    }

    @Override
    public Event updateEvent(Long id, Map<String, String> params, Optional<Set<MultipartFile>> newImages, Optional<Set<MultipartFile>> newVideos, Optional<Set<String>> deletedMediaUrls) {
        try {
            Event event = eventRepository.getEventById(id);
            if (event == null) {
                throw new RuntimeException("Event not found");
            }

            if (deletedMediaUrls.isPresent()) {
                for (String url : deletedMediaUrls.get()) {
                    EventMedia mediaToDelete = event.getEventMedias().stream()
                            .filter(media -> media.getMediaUrl().equals(url))
                            .findFirst()
                            .orElse(null);
                    if (mediaToDelete != null) {
                        event.getEventMedias().remove(mediaToDelete);
                    }
                }
            }

            event.setName(params.get("name"));
            event.setDescription(params.get("description"));
            event.setStartTime(new Date(Long.parseLong(params.get("startTime"))));
            event.setEndTime(new Date(Long.parseLong(params.get("endTime"))));
            event.setLocation(params.get("location"));
            event.setTotalTickets(Integer.parseInt(params.get("totalTickets")));
            event.setTicketPrice(BigDecimal.valueOf(Double.parseDouble(params.get("ticketPrice"))));

            if (!newImages.isEmpty()) {
                for (MultipartFile image : newImages.get()) {
                    try {
                        Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                        String imageUrl = uploadResult.get("secure_url").toString();
                        EventMedia media = new EventMedia();
                        media.setEvent(event);
                        media.setMediaType("IMAGE");
                        media.setMediaUrl(imageUrl);
                        event.getEventMedias().add(media);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!newVideos.isEmpty()) {
                for (MultipartFile video : newVideos.get()) {
                    try {
                        Map uploadResult = cloudinary.uploader().upload(video.getBytes(), ObjectUtils.emptyMap());
                        String videoUrl = uploadResult.get("secure_url").toString();
                        EventMedia media = new EventMedia();
                        media.setEvent(event);
                        media.setMediaType("VIDEO");
                        media.setMediaUrl(videoUrl);
                        event.getEventMedias().add(media);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return eventRepository.addEvent(event);
        } catch (Exception e) {
            throw new RuntimeException("Error updating event", e);
        }

    }

    @Override
    public boolean deleteEvent(Long id) {
        try {
            Event event = eventRepository.getEventById(id);
            if (event == null) {
                throw new RuntimeException("Event not found");
            }
            return eventRepository.deleteEvent(event);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting event", e);
        }
    }

    @Override
    public boolean launchEvent(Long id) {
        try {
            Event event = eventRepository.getEventById(id);
            User current = userUtils.getCurrentUser();
            if (event == null) {
                throw new RuntimeException("Event not found");
            } else {
                if (!eventUtils.isOwner(event, current.getId())) {
                    throw new RuntimeException("Don't have permission to launch event");
                }
                EventStatus status = eventStatusService.getStatusByName("ONSALE");
                event.setStatus(status);
                Event savedEvent = eventRepository.addEvent(event);
                if (savedEvent != null){
                    Map<String, String> params = new HashMap<>();
                    params.put("eventId", event.getId().toString());
                    params.put("totalViews", "0");
                    params.put("totalTicketsSold", "0");
                    params.put("totalRevenue", "0");
                    eventStatisticService.addEventStatistic(params);
                } else {
                    throw new RuntimeException("Error in add Event Statistic");
                }
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in lauch event: " + e.getMessage());
        }
    }

    private boolean isOrganizer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            Role userRole = user.getRole();
            return userRole != null && userRole.getName().equals("ORGANIZER");
        }
        return false;
    }

}
