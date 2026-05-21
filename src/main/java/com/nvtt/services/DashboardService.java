/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services;

import com.nvtt.pojo.SystemStatisticsDaily;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vthan
 */
public interface DashboardService {
    Map<String, Object> getCardMetrics();
    List<SystemStatisticsDaily> getReportData(Date startDate, Date endDate);
}
