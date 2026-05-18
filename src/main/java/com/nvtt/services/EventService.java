/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.User;

/**
 *
 * @author lequa
 */
public interface EventService {
    List<Event> getPublicEvents(Map<String, String> params);
    Event addEvent(Map<String, String> params, Optional<Set<MultipartFile>> images, Optional<Set<MultipartFile>> videos);
    Event getEventById(Long id);
    Event updateEvent(Long id, Map<String, String> params, Optional<Set<MultipartFile>> newImages, Optional<Set<MultipartFile>> newVideos, Optional<Set<String>> deletedMediaUrls);
    boolean deleteEvent(Long id);
    List<Event> getOrganizerEvents(Map<String, String> params);
    boolean launchEvent(Long id);
}
