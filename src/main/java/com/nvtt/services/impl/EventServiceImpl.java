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
import com.nvtt.pojo.EventStatistic;
import com.nvtt.pojo.Role;
import com.nvtt.repositories.CategoryRepository;
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
import com.nvtt.utils.constants.EventUpdatePolicy;
import java.util.HashMap;

import com.nvtt.pojo.dtos.admin.EventSearchCriteriaDTO;
import com.nvtt.pojo.dtos.event.EventCompareResponseDTO;
import com.nvtt.pojo.dtos.response.EventResponseDTO;
import com.nvtt.repositories.EventStatusRepository;
import com.nvtt.utils.DateTimeUtil;
import com.nvtt.utils.exceptions.ServiceException;
import com.nvtt.utils.exceptions.StorageException;
import java.util.stream.Collectors;

/**
 *
 * @author vthan
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
    private CategoryRepository categoryRepository;

    @Autowired
    private EventStatusService eventStatusService;

    @Autowired
    private EventStatisticService eventStatisticService;

    @Autowired
    private EventStatusUtils eventStatusUtils;

    @Autowired
    private EventStatusRepository eventStatusRepository;

    @Override
    public List<Event> getPublicEvents(Map<String, String> params) {
        try {
            List<EventStatus> publicStatuses = eventStatusUtils.eventStatusPublic();
//            for (EventStatus p : publicStatuses) {
//                EventStatus checkedStatus = eventStatusService.getStatusByName(p.getName());
//                if (checkedStatus == null) {
//                    throw new ServiceException("Don't find any status in public statuses");
//                }
//            }
            List<Event> events = eventRepository.getEvents(params, publicStatuses);
            return events;
        } catch (Exception e) {
            throw new ServiceException("Failed to get events: " + e.getMessage());
        }

    }

    @Override
    public List<Event> getOrganizerEvents(Map<String, String> params) {
        try {
            User u = userUtils.getCurrentUser();
            return eventRepository.getOrganizerEvents(u.getId(), params);
        } catch (Exception e) {
            throw new ServiceException("Failed to get events: " + e.getMessage());
        }
    }

    @Override
    public Event getPublicEventById(Long id) {
        try {
            List<EventStatus> publicStatuses = eventStatusUtils.eventStatusPublic();
            Event event = eventRepository.getEventById(id, publicStatuses);
            if (event == null) {
                throw new ServiceException("Don't have any event with this id");
            }
            return event;
        } catch (Exception e) {
            throw new ServiceException("Failed to get event: " + e.getMessage());
        }
    }

    @Override
    public Event getOwnEventById(Long id) {
        try {
            User u = userUtils.getCurrentUser();
            Event e = eventRepository.getOwnEventById(id, u.getId());
            if (e == null) {
                throw new ServiceException("You dont have permission for this event or event doesnt exist");
            }
            return e;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

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
                        throw new StorageException("Failed to upload images");
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
                        throw new StorageException("Failed to upload videos");
                    }
                }
            }

            return eventRepository.addEvent(event);
        } catch (Exception e) {
            System.err.println("Error adding event: " + e.getMessage());
            throw new RuntimeException("Error adding event" + e.getMessage());
        }
    }

    @Override
    public Event updateEvent(Long id, Map<String, String> params, Optional<Set<MultipartFile>> newImages, Optional<Set<MultipartFile>> newVideos, Optional<Set<String>> deletedMediaUrls) {
        try {
            Event event = eventRepository.getEventById(id);
            if (event == null) {
                throw new RuntimeException("Event not found");
            }

            String statusName = event.getStatus() != null ? event.getStatus().getName() : "";
            Set<String> requestedFields = new HashSet<>(params.keySet());
            requestedFields.remove("id");
            if (newImages.isPresent() && !newImages.get().isEmpty()) {
                requestedFields.add("newImages");
            }
            if (newVideos.isPresent() && !newVideos.get().isEmpty()) {
                requestedFields.add("newVideos");
            }
            if (deletedMediaUrls.isPresent() && !deletedMediaUrls.get().isEmpty()) {
                requestedFields.add("deletedMediaUrls");
            }

            try {
                EventUpdatePolicy.validateEditableFields(statusName, requestedFields);
            } catch (IllegalArgumentException ex) {
                throw new ServiceException(ex.getMessage());
            }

            if (EventUpdatePolicy.isFieldEditable(statusName, "deletedMediaUrls") && deletedMediaUrls.isPresent()) {
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

            if (EventUpdatePolicy.isFieldEditable(statusName, "name") && params.get("name") != null) {
                event.setName(params.get("name"));
            }
            if (EventUpdatePolicy.isFieldEditable(statusName, "description") && params.get("description") != null) {
                event.setDescription(params.get("description"));
            }
            if (EventUpdatePolicy.isFieldEditable(statusName, "startTime") && params.get("startTime") != null) {
                event.setStartTime(new Date(Long.parseLong(params.get("startTime"))));
            }
            if (EventUpdatePolicy.isFieldEditable(statusName, "endTime") && params.get("endTime") != null) {
                event.setEndTime(new Date(Long.parseLong(params.get("endTime"))));
            }
            if (EventUpdatePolicy.isFieldEditable(statusName, "location") && params.get("location") != null) {
                event.setLocation(params.get("location"));
            }
            if (EventUpdatePolicy.isFieldEditable(statusName, "totalTickets") && params.get("totalTickets") != null) {
                event.setTotalTickets(Integer.parseInt(params.get("totalTickets")));
            }
            if (EventUpdatePolicy.isFieldEditable(statusName, "ticketPrice") && params.get("ticketPrice") != null) {
                event.setTicketPrice(BigDecimal.valueOf(Double.parseDouble(params.get("ticketPrice"))));
            }
            if (EventUpdatePolicy.isFieldEditable(statusName, "category") && params.get("category") != null) {
                event.setCategory(categoryRepository.getCategoryByName(params.get("category")));
            }

            if (EventUpdatePolicy.isFieldEditable(statusName, "newImages") && newImages.isPresent() && !newImages.get().isEmpty()) {
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
                        throw new StorageException("Failed to upload images");
                    }
                }
            }

            if (EventUpdatePolicy.isFieldEditable(statusName, "newVideos") && newVideos.isPresent() && !newVideos.get().isEmpty()) {
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
                        throw new StorageException("Failed to upload videos");
                    }
                }
            }

            return eventRepository.addEvent(event);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating event" + e.getMessage());
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
            throw new RuntimeException("Error deleting event: " + e.getMessage());
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
                if (savedEvent != null) {
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

    public boolean endEvent(Long id) {
        try {
            Event event = eventRepository.getEventById(id);
            User current = userUtils.getCurrentUser();
            if (event == null) {
                throw new RuntimeException("Event not found");
            } else {
                if (!eventUtils.isOwner(event, current.getId())) {
                    throw new RuntimeException("Don't have permission to launch event");
                }
                EventStatus status = eventStatusService.getStatusByName("ENDED");
                event.setStatus(status);
                Event savedEvent = eventRepository.addEvent(event);
                if (savedEvent != null) {
                    return true;
                } else {
                    throw new ServiceException("Event doesnt save successfully");
                }
            }
        } catch (Exception e) {
            throw new ServiceException("Failed to end event: " + e.getMessage());
        }
    }

    private boolean isOrganizer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            Role userRole = user.getRole();
            return userRole != null && userRole.getName().contains("ORGANIZER");
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getFilteredEvents(EventSearchCriteriaDTO criteria) {
        Map<String, Object> repoResult = eventRepository.searchEvents(criteria);
        List<Event> events = (List<Event>) repoResult.get("events");

        List<EventResponseDTO> dtos = (List<EventResponseDTO>) events.stream().map(e -> new EventResponseDTO(
                e.getId(),
                e.getName(),
                e.getCategory().getName(),
                e.getOrganizer().getFullName(),
                e.getStatus().getName(),
                DateTimeUtil.dateToString(e.getStartTime()),
                DateTimeUtil.dateToString(e.getEndTime()),
                e.getLocation(),
                e.getTotalTickets(),
                e.getAvailableTickets(),
                e.getTicketPrice()
        )).collect(Collectors.toList());
        repoResult.put("events", dtos);
        return repoResult;
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventDetails(Long id) {
        Event event = eventRepository.findById(id);
        if (event == null) {
            throw new IllegalArgumentException("Không tìm thấy sự kiện với ID: " + id);
        }
        return event;
    }

    @Override
    public void approveEvent(Long id) {
        changeEventStatus(id, "PUBLISHED");
    }

    @Override
    public void rejectEvent(Long id) {
        changeEventStatus(id, "REJECTED");
    }

    @Override
    public void updateStatus(Long id, String statusName) {
        changeEventStatus(id, statusName); // ẨN (HIDDEN) hoặc XÓA (DELETED) vi phạm
    }

    private void changeEventStatus(Long id, String statusName) {
        Event event = eventRepository.findById(id);
        if (event != null) {
            EventStatus status = this.eventStatusRepository.findByName(statusName);

            event.setStatus(status);
            eventRepository.update(event);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventCompareResponseDTO> getEventsForComparison(List<Long> ids) {
        List<Object[]> eventDataList = eventRepository.findEventsWithDetailsByIds(ids);

        return eventDataList.stream()
                .map(row -> {
                    Event event = (Event) row[0];
                    EventStatistic stat = (EventStatistic) row[1];
                    return convertToCompareDTO(event, stat);
                })
                .collect(Collectors.toList());
    }

    private EventCompareResponseDTO convertToCompareDTO(Event event, EventStatistic eventStatistic) {
        EventCompareResponseDTO dto = new EventCompareResponseDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setTicket_price(event.getTicketPrice());
        dto.setStart_time(DateTimeUtil.dateToString(event.getStartTime()));
        dto.setEnd_time(DateTimeUtil.dateToString(event.getEndTime()));
        dto.setLocation(event.getLocation());
        dto.setAvailable_tickets(event.getAvailableTickets());
        dto.setTotal_tickets(event.getTotalTickets());

        if (event.getEventMedias() != null) {
            event.getEventMedias().stream()
                    .filter(m -> "IMAGE".equals(m.getMediaType()))
                    .findFirst()
                    .ifPresent(media -> dto.setRepresentative_image(media.getMediaUrl()));

            event.getEventMedias().stream()
                    .filter(m -> "VIDEO".equals(m.getMediaType()))
                    .findFirst()
                    .ifPresent(media -> dto.setVideo_url(media.getMediaUrl()));
        }

        if (event.getCategory() != null) {
            EventCompareResponseDTO.CategoryDTO catDto = new EventCompareResponseDTO.CategoryDTO();
            catDto.setName(event.getCategory().getName());
            dto.setCategory(catDto);
        }

        if (event.getOrganizer() != null) {
            EventCompareResponseDTO.OrganizerDTO orgDto = new EventCompareResponseDTO.OrganizerDTO();
            orgDto.setFull_name(event.getOrganizer().getFullName());
            orgDto.setAvatar_url(event.getOrganizer().getAvatarUrl());
            dto.setOrganizer(orgDto);
        }

        if (eventStatistic != null) {
            EventCompareResponseDTO.StatisticsDTO statDto = new EventCompareResponseDTO.StatisticsDTO();
            statDto.setTotal_tickets_sold(eventStatistic.getTotalTicketsSold());
            statDto.setTotal_views(eventStatistic.getTotalViews());
            dto.setStatistics(statDto);
        }

        return dto;
    }
}
