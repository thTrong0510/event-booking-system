package com.nvtt.scheduler;

import com.nvtt.services.SystemStatisticsService;
import com.nvtt.utils.exceptions.IdInvalidException;
import java.util.Calendar;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SystemStatisticsScheduler {

    private static final Logger logger = LogManager.getLogger(SystemStatisticsScheduler.class);

    @Autowired
    private SystemStatisticsService statisticsService;

    @Scheduled(cron = "0 5 0 * * *")
    public void runDailySystemStatistics() throws IdInvalidException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = cal.getTime();

        logger.info("Cron job system statistics started for date: {}", yesterday);
        try {
            statisticsService.calculateAndSaveDailyStatistics(yesterday);
            logger.info("Cron job system statistics executed successfully.");
        } catch (Exception e) {
            logger.error("Error in daily system statistics job: {}", e.getMessage(), e);
            throw new IdInvalidException("error: cron job daily statistic system");
        }
    }
}
