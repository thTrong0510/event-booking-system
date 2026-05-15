/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nvtt.pojo.EventStatus;
import com.nvtt.pojo.User;
import com.nvtt.repositories.EventStatusRepository;
import com.nvtt.services.EventStatusService;
import com.nvtt.utils.EventStatusUtils.EventStatusUtils;
import com.nvtt.utils.UserUtils.UserUtils;

/**
 *
 * @author lequa
 */
@Service
@Transactional
public class EventStatusServiceImpl implements EventStatusService {
    
    @Autowired
    private EventStatusRepository eventStatusRepository;

    @Autowired
    private EventStatusUtils eventStatusUtils;

    @Autowired
    private UserUtils userUtils;

    @Override
    public EventStatus getStatusByName(String name) {
        return eventStatusRepository.getStatusByName(name);
    }

    @Override
    public EventStatus addEventStatus(Map<String, String> params){
        try {
            User current = userUtils.getCurrentUser();
            if(current.getRole().getName() == "ADMIN"){
                EventStatus status = new EventStatus();
                status = eventStatusUtils.convertParamsToEventStatusObject(params);
                return eventStatusRepository.addEventStatus(status);
            } else {
                throw new RuntimeException("You don't have permission to add event status");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error in add event status");
        }
    }

}
