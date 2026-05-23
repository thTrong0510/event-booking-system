/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.dtos.admin.EventSearchCriteriaDTO;
import com.nvtt.pojo.dtos.event.EventCompareResponseDTO;
import com.nvtt.pojo.dtos.response.EventResponseDTO;
import java.util.List;

/**
 *
 * @author vthan
 */
public interface EventService {

    List<EventResponseDTO> getFilteredEvents(EventSearchCriteriaDTO criteria);

    Event getEventDetails(Long id); // Trả về POJO có kiểm soát cho chi tiết đa phương tiện

    void approveEvent(Long id);

    void rejectEvent(Long id);

    void updateStatus(Long id, String statusName);
    
    List<EventCompareResponseDTO> getEventsForComparison(List<Long> ids);
}
