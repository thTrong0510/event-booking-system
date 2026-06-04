package com.nvtt.services;

import com.nvtt.pojo.dtos.admin.EventSearchCriteriaDTO;
import com.nvtt.pojo.dtos.event.EventCompareResponseDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.nvtt.pojo.Event;

public interface EventService {

    List<Event> getPublicEvents(Map<String, String> params);

    Event addEvent(Map<String, String> params, Optional<Set<MultipartFile>> images, Optional<Set<MultipartFile>> videos);

    Event getPublicEventById(Long id);

    Event getOwnEventById(Long id);

    Event updateEvent(Long id, Map<String, String> params, Optional<Set<MultipartFile>> newImages, Optional<Set<MultipartFile>> newVideos, Optional<Set<String>> deletedMediaUrls);

    boolean deleteEvent(Long id);

    List<Event> getOrganizerEvents(Map<String, String> params);

    boolean launchEvent(Long id);

    boolean endEvent(Long id);

    Map<String, Object> getFilteredEvents(EventSearchCriteriaDTO criteria);

    Event getEventDetails(Long id);

    void approveEvent(Long id);

    void rejectEvent(Long id);

    void updateStatus(Long id, String statusName);

    List<EventCompareResponseDTO> getEventsForComparison(List<Long> ids);
}
