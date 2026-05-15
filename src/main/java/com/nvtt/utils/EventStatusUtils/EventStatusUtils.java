/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.utils.EventStatusUtils;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.nvtt.pojo.EventStatus;

/**
 *
 * @author lequa
 */
@Component
public class EventStatusUtils {
    
    public EventStatus convertParamsToEventStatusObject(Map<String, String> params){
        try {
            EventStatus status = new EventStatus();
            status.setDescription(params.get("description"));
            status.setName(params.get("name"));
            return status;
        } catch (Exception e) {
            throw new RuntimeException("Error params", e);
        }
    }
}
