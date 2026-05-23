/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.EventStatus;
import com.nvtt.pojo.dtos.admin.EventSearchCriteriaDTO;
import com.nvtt.pojo.dtos.event.EventCompareResponseDTO;
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
    
    @Override
    @Transactional(readOnly = true)
    public List<EventCompareResponseDTO> getEventsForComparison(List<Long> ids) {
        // Gọi xuống tầng Repository để truy vấn dữ liệu tối ưu
        List<Event> events = eventRepository.findEventsWithDetailsByIds(ids);

        // Chuyển đổi dữ liệu Entity sang DTO để trả ra ngoài Client
        return events.stream().map(this::convertToCompareDTO).collect(Collectors.toList());
    }

    private EventCompareResponseDTO convertToCompareDTO(Event event) {
        EventCompareResponseDTO dto = new EventCompareResponseDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setTicket_price(event.getTicketPrice());
        dto.setStart_time(DateTimeUtil.dateToString(event.getStartTime()));
        dto.setEnd_time(DateTimeUtil.dateToString(event.getEndTime()));
        dto.setLocation(event.getLocation());
        dto.setAvailable_tickets(event.getAvailableTickets());
        dto.setTotal_tickets(event.getTotalTickets());

        // Lấy 1 ảnh đại diện đầu tiên (media_type = 'IMAGE') từ danh sách Media đã được Fetch Join sẵn
        if (event.getEventMedias()!= null) {
            event.getEventMedias().stream()
                    .filter(m -> "IMAGE".equals(m.getMediaType()))
                    .findFirst()
                    .ifPresent(media -> dto.setRepresentative_image(media.getMediaUrl()));
                    
            // Lấy video đầu tiên nếu có phục vụ logic so sánh có/không video
            event.getEventMedias().stream()
                    .filter(m -> "VIDEO".equals(m.getMediaType()))
                    .findFirst()
                    .ifPresent(media -> dto.setVideo_url(media.getMediaUrl()));
        }

        // Map thông tin Category
        if (event.getCategory() != null) {
            EventCompareResponseDTO.CategoryDTO catDto = new EventCompareResponseDTO.CategoryDTO();
            catDto.setName(event.getCategory().getName());
            dto.setCategory(catDto);
        }

        // Map thông tin Nhà tổ chức (Organizer)
        if (event.getOrganizer() != null) {
            EventCompareResponseDTO.OrganizerDTO orgDto = new EventCompareResponseDTO.OrganizerDTO();
            orgDto.setFull_name(event.getOrganizer().getFullName());
            orgDto.setAvatar_url(event.getOrganizer().getAvatarUrl());
            dto.setOrganizer(orgDto);
        }

        // Map thông tin Thống kê (Statistics)
        if (event.getEventStatistic()!= null) {
            EventCompareResponseDTO.StatisticsDTO statDto = new EventCompareResponseDTO.StatisticsDTO();
            statDto.setTotal_tickets_sold(event.getEventStatistic().getTotalTicketsSold());
            statDto.setTotal_views(event.getEventStatistic().getTotalViews());
            dto.setStatistics(statDto);
        }

        return dto;
    }
}
