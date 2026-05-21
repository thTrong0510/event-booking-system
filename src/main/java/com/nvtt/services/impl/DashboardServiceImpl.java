/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import com.nvtt.pojo.SystemStatisticsDaily;
import com.nvtt.pojo.dtos.admin.AdminDashboardDTO;
import com.nvtt.repositories.DashboardRepository;
import com.nvtt.services.DashboardService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vthan
 */
@Service
@Transactional(readOnly = true) // Tối ưu hiệu năng Hibernate chỉ đọc dữ liệu
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    @Override
    public AdminDashboardDTO getDashboardData(String timeFilter, int year) {
        String filter = (timeFilter == null || timeFilter.isEmpty()) ? "MONTH" : timeFilter.toUpperCase();

        AdminDashboardDTO dto = new AdminDashboardDTO();

        // Truyền thêm biến year vào hàm lấy số lượng sự kiện
        dto.setEventsByTime(dashboardRepository.getEventsCountByTime(filter, year));

        dto.setTicketSalesTrend(dashboardRepository.getTicketSalesOverTime());
        dto.setRevenueByOrganizer(dashboardRepository.getRevenueByOrganizer());
        dto.setStatisticsByCategory(dashboardRepository.getStatisticsByCategory());

        return dto;
    }
}
