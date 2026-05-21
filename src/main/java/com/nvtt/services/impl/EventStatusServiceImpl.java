/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import com.nvtt.pojo.EventStatus;
import com.nvtt.repositories.EventStatusRepository;
import com.nvtt.services.EventStatusService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vthan
 */
@Service
@Transactional
public class EventStatusServiceImpl implements EventStatusService {

    @Autowired
    private EventStatusRepository eventStatusRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EventStatus> getAllStatuses() {
        return this.eventStatusRepository.findAll();
    }

    @Override
    public EventStatus getByName(String name) {
        return this.eventStatusRepository.findByName(name);
    }
}
