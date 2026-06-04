package com.nvtt.services;

import com.nvtt.pojo.SystemStatisticsDaily;
import java.util.Date;

public interface SystemStatisticsService {

    void calculateAndSaveDailyStatistics(Date targetDate);

    SystemStatisticsDaily getSystemSummary(String timeFilter, Integer selectedYear);
}
