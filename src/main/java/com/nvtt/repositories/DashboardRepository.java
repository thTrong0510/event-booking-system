/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import com.nvtt.pojo.SystemStatisticsDaily;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author vthan
 */
public interface DashboardRepository {
    // 4 Hàm lấy dữ liệu Real-time cho 4 cái Cards đầu trang
    Long countActiveEvents();
    BigDecimal sumTotalRevenue();
    Long countTotalTicketsSold();
    Long countPendingOrganizers();

    // Hàm lấy dữ liệu biểu đồ và bảng báo cáo chiến lược
    List<SystemStatisticsDaily> getDailyStatistics(Date startDate, Date endDate);
}
