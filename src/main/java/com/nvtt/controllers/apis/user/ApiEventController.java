/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.apis.user;

import com.nvtt.pojo.dtos.event.EventCompareResponseDTO;
import com.nvtt.services.EventService;
import com.nvtt.utils.exceptions.IdInvalidException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vthan
 */
@RestController
@RequestMapping("/api/events")
public class ApiEventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/compare")
    public ResponseEntity<List<EventCompareResponseDTO>> compareEvents(@RequestParam("ids") List<Long> ids) throws IdInvalidException{
        if (ids == null || ids.size() < 2 || ids.size() > 3) {
            throw new IdInvalidException("Vui lòng cung cấp từ 2 đến 3 ID sự kiện để so sánh.");
        }
        
        List<EventCompareResponseDTO> compareResult = eventService.getEventsForComparison(ids);
        return ResponseEntity.ok(compareResult);
    }
}
