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

    @Override
    @Transactional(readOnly = true)
    public SystemStatisticsDaily getSystemSummary(String timeFilter, Integer selectedYear) {
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        Date startDate = null;
        Date endDate = null;
        boolean includeToday = false;
        if ("YEAR".equals(timeFilter)) {
            includeToday = true;
            cal.add(Calendar.DAY_OF_MONTH, -1);
            setToEndOfDay(cal);
            endDate = cal.getTime();
        } else {
            if (selectedYear == null) {
                selectedYear = currentYear;
            }
            cal.set(selectedYear, Calendar.JANUARY, 1, 0, 0, 0);
            cal.set(Calendar.MILLISECOND, 0);
            startDate = cal.getTime();
            if (selectedYear == currentYear) {
                includeToday = true;
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_MONTH, -1);
                setToEndOfDay(cal);
                endDate = cal.getTime();
            } else {
                cal.set(selectedYear, Calendar.DECEMBER, 31, 23, 59, 59);
                cal.set(Calendar.MILLISECOND, 999);
                endDate = cal.getTime();
            }
        }

        SystemStatisticsDaily summary = statisticsRepository.getHistoricalSummary(startDate, endDate);

        if (includeToday) {
            cal.setTime(new Date());

            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date startToday = cal.getTime();

            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date endToday = cal.getTime();

            Map<String, Object> liveOrders = statisticsRepository.getLiveOrderSummaryToday(startToday, endToday);
            Long liveEventsCount = statisticsRepository.getLiveEventCountToday(startToday, endToday);

            int todayOrders = (Integer) liveOrders.get("totalOrders");
            BigDecimal todayRevenue = (BigDecimal) liveOrders.get("totalRevenue");
            int todayEvents = liveEventsCount.intValue();

            summary.setTotalOrders(summary.getTotalOrders() != null ? summary.getTotalOrders() + todayOrders : todayOrders);
            summary.setTotalRevenue(summary.getTotalRevenue() != null ? summary.getTotalRevenue().add(todayRevenue) : todayRevenue);
            summary.setTotalEvents(summary.getTotalEvents() != null ? summary.getTotalEvents() + todayEvents : todayEvents);
        }

        return summary;
    }

    private void setToEndOfDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
    }
}
