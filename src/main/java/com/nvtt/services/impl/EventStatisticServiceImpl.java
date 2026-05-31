/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import com.nvtt.pojo.EventStatistic;
import com.nvtt.pojo.User;
import com.nvtt.repositories.EventStatisticRepository;
import com.nvtt.services.EventStatisticService;
import com.nvtt.utils.UserUtils.UserUtils;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nvtt.utils.EventStatisticUtils.EventStatisticUtils;

/**
 *
 * @author lequa
 */
@Service
@Transactional
public class EventStatisticServiceImpl implements EventStatisticService {

    @Autowired
    private EventStatisticRepository eventStatisticRepository;

    @Autowired
    private EventStatisticUtils eventStatisticUtils;
    
    @Autowired
    private UserUtils userUtils;

    @Override
    public EventStatistic addEventStatistic(Map<String, String> params) {
        try {
            EventStatistic eventStatistic = eventStatisticUtils.convertParamsToEventStatisticObject(params);
            return eventStatisticRepository.addEventStatistic(eventStatistic);
        } catch (Exception e) {
            throw new RuntimeException("Error in add event statistic: " + e.getMessage());
        }

    }

    @Override
    public List<EventStatistic> getEventStatistics(Map<String, String> params) {
        User currentUser = getCurrentOrganizerOrAdmin();
        DateRange createdAtRange = buildCreatedAtRange(params);
        if (isAdmin(currentUser)) {
            return eventStatisticRepository.getEventStatisticsByCreatedAtRange(params, createdAtRange.from(), createdAtRange.to());
        }
        return eventStatisticRepository.getEventStatisticsByOrganizerAndCreatedAtRange(currentUser.getId(), params, createdAtRange.from(), createdAtRange.to());
    }

    @Override
    public EventStatistic getEventStatisticByEventId(Long eventId) {
        User currentUser = getCurrentOrganizerOrAdmin();
        if (isAdmin(currentUser)) {
            return eventStatisticRepository.getEventStatisticByEventId(eventId);
        }
        return eventStatisticRepository.getEventStatisticByEventIdAndOrganizerId(eventId, currentUser.getId());
    }

    @Override
    public EventStatistic updateEventStatistic(Long eventId, Map<String, String> params) {
        User currentUser = getCurrentOrganizerOrAdmin();
        EventStatistic es = isAdmin(currentUser)
                ? eventStatisticRepository.getEventStatisticByEventId(eventId)
                : eventStatisticRepository.getEventStatisticByEventIdAndOrganizerId(eventId, currentUser.getId());
        if (es == null) {
            throw new RuntimeException("Don't have any Event Statistic with this Event Id");
        } else {
            es = eventStatisticUtils.addParamsToEventStatisticObject(es,params);
            return eventStatisticRepository.updateEventStatistic(es);
        }
    }

    @Override
    public void increaseViews(Long eventId, int views, EventStatistic es) {
        try {
            if (es == null) {
                throw new RuntimeException("Don't have any Event Statistic with this Event Id");
            } else {
                es.setTotalViews(es.getTotalViews() + views);
                eventStatisticRepository.updateEventStatistic(es);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in update views for Event Statistic: " + e.getMessage());
        }
    }
    
    private User getCurrentOrganizerOrAdmin() {
        User currentUser = userUtils.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("Unauthorized: User not authenticated");
        }
        
        if (currentUser.getRole() == null
                || (!currentUser.getRole().getName().contains("ORGANIZER")
                && !currentUser.getRole().getName().contains("ADMIN"))) {
            throw new RuntimeException("Unauthorized: User does not have ORGANIZER or ADMIN role");
        }
        
        return currentUser;
    }
    
    private boolean isAdmin(User user) {
        return user.getRole() != null && user.getRole().getName().contains("ADMIN");
    }
    
    private DateRange buildCreatedAtRange(Map<String, String> params) {
        if (params == null) {
            return new DateRange(null, null);
        }
        
        String monthParam = params.get("month");
        String quarterParam = params.get("quarter");
        String yearParam = params.get("year");
        
        boolean hasMonth = hasText(monthParam);
        boolean hasQuarter = hasText(quarterParam);
        boolean hasYear = hasText(yearParam);
        
        if (!hasMonth && !hasQuarter && !hasYear) {
            return new DateRange(null, null);
        }
        
        if (hasMonth && hasQuarter) {
            throw new RuntimeException("Cannot filter by both month and quarter");
        }
        
        int year = hasYear ? parseInt(yearParam, "year") : LocalDate.now().getYear();
        LocalDate from;
        LocalDate to;
        
        if (hasMonth) {
            int month = parseInt(monthParam, "month");
            if (month < 1 || month > 12) {
                throw new RuntimeException("month must be between 1 and 12");
            }
            from = LocalDate.of(year, month, 1);
            to = from.plusMonths(1);
        } else if (hasQuarter) {
            int quarter = parseInt(quarterParam, "quarter");
            if (quarter < 1 || quarter > 4) {
                throw new RuntimeException("quarter must be between 1 and 4");
            }
            from = LocalDate.of(year, (quarter - 1) * 3 + 1, 1);
            to = from.plusMonths(3);
        } else {
            from = LocalDate.of(year, 1, 1);
            to = from.plusYears(1);
        }
        
        ZoneId zoneId = ZoneId.systemDefault();
        return new DateRange(
                Date.from(from.atStartOfDay(zoneId).toInstant()),
                Date.from(to.atStartOfDay(zoneId).toInstant()));
    }
    
    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    private int parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(fieldName + " must be a number");
        }
    }
    
    private record DateRange(Date from, Date to) {
    }

    @Override
    public EventStatistic getStatisticByEventId(Long eventId) {
        return eventStatisticRepository.getEventStatisticByEventId(eventId);
    }
}
