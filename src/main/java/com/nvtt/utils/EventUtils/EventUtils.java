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
import com.nvtt.pojo.EventStatus;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.event.ResEventInfoDTO;
import com.nvtt.pojo.dtos.event.ResEventMediaDTO;
import com.nvtt.repositories.CategoryRepository;
import com.nvtt.repositories.EventStatusRepository;
import com.nvtt.utils.UserUtils.UserUtils;

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
    private UserUtils userUtils;

    public ResEventInfoDTO convertToResEventInfoDTO(Event event) {
        Set<EventMedia> eventMedias = event.getEventMedias();
        Set<ResEventMediaDTO> resEventMediaDTOs = eventMedias.stream()
                .map(eventMedia -> new ResEventMediaDTO(eventMedia.getMediaType(), eventMedia.getMediaUrl()))
                .collect(Collectors.toSet());
        return new ResEventInfoDTO(event.getId(), event.getName(), event.getDescription(),
                resEventMediaDTOs, event.getStartTime(), event.getEndTime(), event.getLocation(),
                event.getTotalTickets(), event.getCreatedAt(), event.getUpdatedAt(), event.getTicketPrice(), 
                event.getAvailableTickets(), event.getStatus().getName(), event.getCategory().getName());
    }

    public boolean isOwner(Event event, Long userId) {
        return event.getOrganizer().getId().equals(userId);
    }

    public Event convertParamsToEventObject(Map<String, String> params){
        User organizer = userUtils.getCurrentUser();
        Event event = new Event();
        event.setStatus(updateStatusForEventByTime(new Date(Long.parseLong(params.get("startTime"))), new Date(Long.parseLong(params.get("endTime")))));
        Category category = categoryRepository.getCategoryByName(params.get("category"));
        event.setCategory(category);
        event.setOrganizer(organizer);
        event.setName(params.get("name"));
        event.setDescription(params.get("description"));
        event.setStartTime(new Date(Long.parseLong(params.get("startTime"))));
        event.setEndTime(new Date(Long.parseLong(params.get("endTime"))));
        event.setLocation(params.get("location"));
        event.setTotalTickets(Integer.parseInt(params.get("totalTickets")));
        event.setTicketPrice(BigDecimal.valueOf(Double.parseDouble(params.get("ticketPrice"))));
        event.setAvailableTickets(Integer.parseInt(params.get("totalTickets")));
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
}
