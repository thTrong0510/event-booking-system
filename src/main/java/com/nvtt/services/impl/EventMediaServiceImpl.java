/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nvtt.pojo.EventMedia;
import com.nvtt.repositories.EventMediaRepository;
import com.nvtt.services.EventMediaService;

/**
 *
 * @author lequa
 */
@Service
@Transactional
public class EventMediaServiceImpl implements EventMediaService {

    private EventMediaRepository eventMediaRepository;
    
    @Override
    public void deleteMediaByUrl(String url) {
        EventMedia media = eventMediaRepository.getEventMedia(Map.of("url", url));
        eventMediaRepository.deleteEventMedia(media);
    }
}
