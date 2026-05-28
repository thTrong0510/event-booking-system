/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.impl;

import com.nvtt.pojo.SystemStatisticsDaily;
import com.nvtt.repositories.SystemStatisticsRepository;
import com.nvtt.services.SystemStatisticsService;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vthan
 */
@Service
public class SystemStatisticsServiceImpl implements SystemStatisticsService {

    @Autowired
    private SystemStatisticsRepository statisticsRepository;

    @Override
    @Transactional
    public void calculateAndSaveDailyStatistics(Date targetDate) {

        Calendar cal = Calendar.getInstance();

        cal.setTime(targetDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfDay = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date endOfDay = cal.getTime();

        Map<String, Object> orderStats = statisticsRepository.getOrderStatsByPeriod(startOfDay, endOfDay);
        Integer totalOrders = (Integer) orderStats.get("totalOrders");
        BigDecimal totalRevenue = (BigDecimal) orderStats.get("totalRevenue");

        Long totalEvents = statisticsRepository.getCreatedEventsCountByPeriod(startOfDay, endOfDay);
        
        cal.setTime(targetDate);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date safeStatDate = cal.getTime();

        SystemStatisticsDaily dailyReport = new SystemStatisticsDaily();
        dailyReport.setStatDate(safeStatDate);
        dailyReport.setTotalOrders(totalOrders);
        dailyReport.setTotalRevenue(totalRevenue);
        dailyReport.setTotalEvents(totalEvents.intValue());
        statisticsRepository.save(dailyReport);
    }
}
