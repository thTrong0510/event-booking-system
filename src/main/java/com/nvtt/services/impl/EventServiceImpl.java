/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.EventStatus;
import com.nvtt.pojo.dtos.admin.EventSearchCriteriaDTO;
import com.nvtt.pojo.dtos.response.EventResponseDTO;
import com.nvtt.repositories.EventRepository;
import com.nvtt.repositories.EventStatusRepository;
import com.nvtt.services.EventService;
import com.nvtt.utils.DateTimeUtil;
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
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventStatusRepository eventStatusRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDTO> getFilteredEvents(EventSearchCriteriaDTO criteria) {
        List<Event> events = eventRepository.searchEvents(criteria);

        return events.stream().map(e -> new EventResponseDTO(
                e.getId(),
                e.getName(),
                e.getCategory().getName(),
                e.getOrganizer().getFullName(),
                e.getStatus().getName(),
                DateTimeUtil.dateToString(e.getStartTime()),
                DateTimeUtil.dateToString(e.getEndTime()),
                e.getLocation(),
                e.getTotalTickets(),
                e.getAvailableTickets(),
                e.getTicketPrice()
        )).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventDetails(Long id) {
        Event event = eventRepository.findById(id);
        if (event == null) {
            throw new IllegalArgumentException("Không tìm thấy sự kiện với ID: " + id);
        }
        return event;
    }

    @Override
    public void approveEvent(Long id) {
        changeEventStatus(id, "PUBLISHED"); // Duyệt công khai hiển thị trên ReactJS Client
    }

    @Override
    public void rejectEvent(Long id) {
        changeEventStatus(id, "REJECTED"); // Từ chối duyệt nội dung xấu
    }

    @Override
    public void updateStatus(Long id, String statusName) {
        changeEventStatus(id, statusName); // ẨN (HIDDEN) hoặc XÓA (DELETED) vi phạm
    }

    private void changeEventStatus(Long id, String statusName) {
        Event event = eventRepository.findById(id);
        if (event != null) {
            // Tìm thực thể Trạng thái tương ứng từ database (HQL tuyển dụng proxy)
            // Giả lập lấy trạng thái cấu hình hệ thống từ DB
            EventStatus status = this.eventStatusRepository.findByName(statusName); // Hàm xử lý map tên cấu hình DB của bạn

            event.setStatus(status);
            eventRepository.update(event);
        }
    }
}
