/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvtt.pojo.dtos.admin.AdminDashboardDTO;
import com.nvtt.services.DashboardService;
import java.time.LocalDate;
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
public class AdminDashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public String showDashboard(
            @RequestParam(value = "timeFilter", required = false, defaultValue = "MONTH") String timeFilter,
            @RequestParam(value = "selectedYear", required = false) Integer selectedYear,
            Model model) {

        // Nếu không chọn năm, hệ thống tự động lấy năm hiện tại làm mốc mặc định
        if (selectedYear == null) {
            selectedYear = LocalDate.now().getYear();
        }

        AdminDashboardDTO dashboardData = dashboardService.getDashboardData(timeFilter, selectedYear);

        ObjectMapper mapper = new ObjectMapper();
        try {
            model.addAttribute("eventsJson", mapper.writeValueAsString(dashboardData.getEventsByTime()));
            model.addAttribute("salesJson", mapper.writeValueAsString(dashboardData.getTicketSalesTrend()));
            model.addAttribute("organizerJson", mapper.writeValueAsString(dashboardData.getRevenueByOrganizer()));
            model.addAttribute("categoryJson", mapper.writeValueAsString(dashboardData.getStatisticsByCategory()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("dashboardData", dashboardData);
        model.addAttribute("currentTimeFilter", timeFilter);
        model.addAttribute("currentYear", selectedYear); // Đẩy năm hiện tại ra lại giao diện để hiển thị

        return "admin/dashboard";
    }
}
