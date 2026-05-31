/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.utils.EventUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nvtt.pojo.Category;
import com.nvtt.pojo.Event;
import com.nvtt.pojo.EventMedia;
import com.nvtt.pojo.EventStatistic;
import com.nvtt.pojo.EventStatus;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.event.ResEventBasicInfoDTO;
import com.nvtt.pojo.dtos.event.ResEventInfoDTO;
import com.nvtt.pojo.dtos.event.ResEventMediaDTO;
import com.nvtt.repositories.CategoryRepository;
import com.nvtt.repositories.EventStatisticRepository;
import com.nvtt.repositories.EventStatusRepository;
import com.nvtt.utils.UserUtils.UserUtils;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author lequa
 */
@Component
public class EventUtils {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EventStatusRepository eventStatusRepository;

    @Autowired
    private EventStatisticRepository eventStatisticRepository;

    @Autowired
    private UserUtils userUtils;

    public ResEventInfoDTO convertToResEventInfoDTO(Event event, EventStatistic es) {
        Set<EventMedia> eventMedias = event.getEventMedias();
        Set<ResEventMediaDTO> resEventMediaDTOs = eventMedias.stream()
                .map(eventMedia -> new ResEventMediaDTO(eventMedia.getMediaType(), eventMedia.getMediaUrl()))
                .collect(Collectors.toSet());
        int eventViews;
        if (es != null) {
            if (es.getTotalViews() != null) {
                eventViews = es.getTotalViews();
            } else {
                eventViews = 0;
            }
        } else {
            eventViews = 0;
        }
        return new ResEventInfoDTO(event.getId(), event.getName(), event.getDescription(),
                resEventMediaDTOs, event.getStartTime(), event.getEndTime(), event.getLocation(),
                event.getTotalTickets(), event.getCreatedAt(), event.getUpdatedAt(), event.getTicketPrice(),
                event.getAvailableTickets(), event.getStatus().getName(), event.getCategory().getName(), eventViews);
    }

    public ResEventBasicInfoDTO convertToResEventBasicInfoDTO(Event event) {
        return new ResEventBasicInfoDTO(event.getId(), event.getName(), event.getDescription());
    }

    public boolean isOwner(Event event, Long userId) {
        return event.getOrganizer().getId().equals(userId);
    }

    public Event convertParamsToEventObject(Map<String, String> params) {
        User organizer = userUtils.getCurrentUser();
        Event event = new Event();
        event.setOrganizer(organizer);
        if (params.get("statusId") != null) {
            EventStatus status = eventStatusRepository.getStatusById(Long.valueOf(params.get("statusId")));
            event.setStatus(status);
        } else {
            EventStatus status = eventStatusRepository.getStatusByName("DRAFT");
            event.setStatus(status);
        }
        if (params.get("category") != null) {
            Category category = categoryRepository.getCategoryByName(params.get("category"));
            event.setCategory(category);
        }
        if (params.get("name") != null) {
            event.setName(params.get("name"));
        }
        if (params.get("description") != null) {
            event.setDescription(params.get("description"));
        }
        if (params.get("startTime") != null) {
            event.setStartTime(new Date(Long.parseLong(params.get("startTime"))));
        }
        if (params.get("endTime") != null) {
            event.setEndTime(new Date(Long.parseLong(params.get("endTime"))));
        }
        if (params.get("location") != null) {
            event.setLocation(params.get("location"));
        }
        if (params.get("totalTickets") != null) {
            event.setTotalTickets(Integer.parseInt(params.get("totalTickets")));
        }
        if (params.get("ticketPrice") != null) {
            event.setTicketPrice(BigDecimal.valueOf(Double.parseDouble(params.get("ticketPrice"))));
        }
        if (params.get("availableTickets") != null) {
            event.setAvailableTickets(Integer.parseInt(params.get("availableTickets")));
        }

        return event;
    }

    private EventStatus updateStatusForEventByTime(Date startTime, Date endTime) {
        Date now = new Date();
        EventStatus status = new EventStatus();
        if (now.before(startTime)) {
            status = eventStatusRepository.getStatusByName("UPCOMING");
        } else if (now.after(endTime)) {
            status = eventStatusRepository.getStatusByName("ENDED");
        } else if (now.after(startTime) && now.before(endTime)) {
            status = eventStatusRepository.getStatusByName("ONSALE");
        }
        return status;
    }

    public List<ResEventInfoDTO> convertEventsToDTOs(List<Event> events) {

        List<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());

        List<EventStatistic> stats = eventStatisticRepository.getEventStatisticsByEventIds(eventIds);

        Map<Long, EventStatistic> statMap = stats.stream()
                .collect(Collectors.toMap(EventStatistic::getEventId, s -> s));

        return events.stream().map(event -> {
            EventStatistic stat = statMap.get(event.getId());
            int views = (stat != null && stat.getTotalViews() != null) ? stat.getTotalViews() : 0;

            return convertToResEventInfoDTO(event, views);
        }).collect(Collectors.toList());
    }

    public ResEventInfoDTO convertToResEventInfoDTO(Event event, int eventViews) {

        Set<ResEventMediaDTO> resEventMediaDTOs = new HashSet<>();
        if (event.getEventMedias() != null) {
            resEventMediaDTOs = event.getEventMedias().stream()
                    .map(media -> new ResEventMediaDTO(media.getMediaType(), media.getMediaUrl()))
                    .collect(Collectors.toSet());
        }

        return new ResEventInfoDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                resEventMediaDTOs,
                event.getStartTime(),
                event.getEndTime(),
                event.getLocation(),
                event.getTotalTickets(),
                event.getCreatedAt(),
                event.getUpdatedAt(),
                event.getTicketPrice(),
                event.getAvailableTickets(),
                event.getStatus().getName(),
                event.getCategory().getName(),
                eventViews
        );
    }
}
