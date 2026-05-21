/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import com.nvtt.pojo.SystemStatisticsDaily;
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
    public Map<String, Object> getCardMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("activeEvents", dashboardRepository.countActiveEvents());
        metrics.put("totalRevenue", dashboardRepository.sumTotalRevenue());
        metrics.put("totalTicketsSold", dashboardRepository.countTotalTicketsSold());
        metrics.put("pendingOrganizers", dashboardRepository.countPendingOrganizers());
        return metrics;
    }

    @Override
    public List<SystemStatisticsDaily> getReportData(Date startDate, Date endDate) {
        return dashboardRepository.getDailyStatistics(startDate, endDate);
    }
}