/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvtt.pojo.SystemStatisticsDaily;
import com.nvtt.pojo.dtos.admin.AdminDashboardDTO;
import com.nvtt.services.DashboardService;
import com.nvtt.services.SystemStatisticsService;
import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author vthan
 */
@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {
    
    private static final Logger logger = LogManager.getLogger(DashboardController.class);

    @Autowired
    private DashboardService dashboardService;
    
    @Autowired
    private SystemStatisticsService statisticsService;

    @GetMapping
    public String showDashboard(
            @RequestParam(value = "timeFilter", required = false, defaultValue = "MONTH") String timeFilter,
            @RequestParam(value = "selectedYear", required = false) Integer selectedYear,
            Model model) {
        
        if (selectedYear == null) {
            selectedYear = LocalDate.now().getYear();
        }
        logger.info("start sql showDashboard");
        SystemStatisticsDaily systemSummary = statisticsService.getSystemSummary(timeFilter, selectedYear);
        AdminDashboardDTO dashboardData = dashboardService.getDashboardData(timeFilter, selectedYear);
        logger.info("end sql");

        ObjectMapper mapper = new ObjectMapper();
        try {
            model.addAttribute("systemSummary", systemSummary);
            model.addAttribute("currentTimeFilter", timeFilter);
            model.addAttribute("currentYear", selectedYear);
            model.addAttribute("eventsJson", mapper.writeValueAsString(dashboardData.getEventsByTime()));
            model.addAttribute("salesJson", mapper.writeValueAsString(dashboardData.getTicketSalesTrend()));
            model.addAttribute("organizerJson", mapper.writeValueAsString(dashboardData.getRevenueByOrganizer()));
            model.addAttribute("categoryJson", mapper.writeValueAsString(dashboardData.getStatisticsByCategory()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("dashboardData", dashboardData);
        model.addAttribute("currentTimeFilter", timeFilter);
        model.addAttribute("currentYear", selectedYear);

        return "admin/dashboard";
    }
}
