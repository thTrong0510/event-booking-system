/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import java.util.Map;

import com.nvtt.pojo.EventStatus;

/**
 *
 * @author lequa
 */
public interface EventStatusService {
    EventStatus getStatusByName(String name);
    EventStatus addEventStatus(Map<String, String> params);
    EventStatus updateEventStatus(Long id, Map<String, String> params);
    EventStatus getStatusById(Long id);
}
