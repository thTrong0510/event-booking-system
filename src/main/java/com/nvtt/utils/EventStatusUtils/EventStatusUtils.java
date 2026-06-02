/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.utils.EventStatusUtils;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.nvtt.pojo.EventStatus;
import com.nvtt.pojo.dtos.event_status.ResEventStatusDTO;
import com.nvtt.repositories.EventStatusRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author lequa
 */
@Component
public class EventStatusUtils {
    
    @Autowired
    private EventStatusRepository eventStatusRepository;
    
    public EventStatus convertParamsToEventStatusObject(Map<String, String> params){
        try {
            EventStatus status = new EventStatus();
            if(params.get("description") != null){
                status.setDescription(params.get("description"));
            }
            if(params.get("name") != null){
                status.setName(params.get("name"));
            }
            return status;
        } catch (Exception e) {
            throw new RuntimeException("Error params", e);
        }
    }
    
    public List<EventStatus> eventStatusPublic(){
        return eventStatusRepository.findByNameIn(
                List.of("PUBLISHED", "ONSALE", "SOLDOUT", "ENDED", "COMPLETED")
        );
    }
    
    public ResEventStatusDTO convertToEventStatusDTO(EventStatus eventStatus){
        ResEventStatusDTO dto = new ResEventStatusDTO(eventStatus.getId(), eventStatus.getName());
        return dto;
    }
    
    public List<ResEventStatusDTO> convertToListResEventStatusDTO(List<EventStatus> eventStatuses){
        List<ResEventStatusDTO> dtos = new ArrayList<>();
        for (EventStatus e : eventStatuses) {
            dtos.add(this.convertToEventStatusDTO(e));
        }
        return dtos;
    } 
}
