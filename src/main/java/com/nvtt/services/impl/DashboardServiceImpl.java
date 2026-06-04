package com.nvtt.services.impl;

import com.nvtt.pojo.dtos.admin.AdminDashboardDTO;
import com.nvtt.repositories.DashboardRepository;
import com.nvtt.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    @Override
    public AdminDashboardDTO getDashboardData(String timeFilter, int year) {
        String filter = (timeFilter == null || timeFilter.isEmpty()) ? "MONTH" : timeFilter.toUpperCase();

        AdminDashboardDTO dto = new AdminDashboardDTO();

        dto.setEventsByTime(dashboardRepository.getEventsCountByTime(filter, year));
        dto.setTicketSalesTrend(dashboardRepository.getTicketSalesOverTime(filter, year));
        dto.setStatisticsByCategory(dashboardRepository.getStatisticsByCategory(filter, year));
        dto.setRevenueByOrganizer(dashboardRepository.getRevenueByOrganizer(filter, year));

        return dto;
    }
}
